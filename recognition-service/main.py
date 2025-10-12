import threading
import cv2
import numpy as np
import pickle
import os
import io
from mtcnn import MTCNN
from deepface import DeepFace
from minio import Minio
from PIL import Image
from flask import Flask, request, jsonify
from collections import deque, defaultdict
from concurrent.futures import ThreadPoolExecutor
import time
import traceback
import requests
from requests.adapters import HTTPAdapter
from urllib3.util.retry import Retry

GALLERY_FILE = "deepface_gallery.pkl"
BUCKET_NAME = "faces"
THRESHOLD = 0.50
HOST = "127.0.0.1"
PORT = 5001
RECOGNITION_CONFIRMATION_TIME = 0.7
RECOGNITION_COOLDOWN = 300
TEST_IMMEDIATE = True
DEQUE_LEN = 8
REQUIRED_FRAMES_BELOW = 3

minio_client = Minio("localhost:9000", access_key="minioadmin", secret_key="minioadmin", secure=False)

def download_image_from_minio(bucket, object_name):
    obj = minio_client.get_object(bucket, object_name)
    data = obj.read()
    obj.close()
    obj.release_conn()
    img = Image.open(io.BytesIO(data)).convert("RGB")
    return np.asarray(img)

gallery_lock = threading.Lock()
distance_history = {}
id_history = defaultdict(lambda: deque(maxlen=DEQUE_LEN))
last_recognized_time = {}

CPU_COUNT = os.cpu_count() or 4
executor = ThreadPoolExecutor(max_workers=max(4, CPU_COUNT * 2))

session = requests.Session()
retries = Retry(total=3, backoff_factor=0.5, status_forcelist=(500,502,503,504))
adapter = HTTPAdapter(max_retries=retries)
session.mount("http://", adapter)
session.mount("https://", adapter)

def l2_normalize(v):
    v = np.asarray(v, dtype=np.float32)
    norm = np.linalg.norm(v)
    if norm == 0:
        return v
    return v / norm

def load_gallery():
    if os.path.exists(GALLERY_FILE):
        try:
            with open(GALLERY_FILE, "rb") as f:
                raw = pickle.load(f)
            normalized = {k: [l2_normalize(np.asarray(e, dtype=np.float32)) for e in v] for k, v in raw.items()}
            print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] Galeria carregada com {len(normalized)} entradas", flush=True)
            return normalized
        except Exception:
            traceback.print_exc()
    return {}

def save_gallery(g):
    tmp = {k: [np.asarray(e, dtype=np.float32) for e in v] for k, v in g.items()}
    with open(GALLERY_FILE, "wb") as f:
        pickle.dump(tmp, f)

gallery = load_gallery()

print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] Carregando modelo Facenet...", flush=True)
facenet = DeepFace.build_model("Facenet")
print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] Modelo Facenet carregado", flush=True)
detector = MTCNN()
print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] Detector MTCNN inicializado", flush=True)

def compute_embedding(img_rgb_160):
    rep = DeepFace.represent(img_rgb_160, model_name="Facenet", enforce_detection=False)
    if isinstance(rep, list):
        emb = np.asarray(rep[0]["embedding"], dtype=np.float32)
    elif isinstance(rep, dict) and "embedding" in rep:
        emb = np.asarray(rep["embedding"], dtype=np.float32)
    else:
        emb = np.asarray(rep, dtype=np.float32).reshape(-1)
    return l2_normalize(emb)

def augment_face_for_enroll(face):
    faces = [face, cv2.flip(face, 1)]
    for angle in (-15, 15):
        M = cv2.getRotationMatrix2D((face.shape[1]//2, face.shape[0]//2), angle, 1)
        rotated = cv2.warpAffine(face, M, (face.shape[1], face.shape[0]))
        faces.extend([rotated, cv2.flip(rotated, 1)])
    return faces

def augment_face_for_probe(face):
    return [face, cv2.flip(face, 1)]

def add_to_gallery(record_id, img_array):
    try:
        face_resized = cv2.resize(img_array, (160,160))
        augmented_faces = augment_face_for_enroll(face_resized)
        embeddings = [compute_embedding(f) for f in augmented_faces]
        with gallery_lock:
            gallery[str(record_id)] = embeddings
            save_gallery(gallery)
        print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] Registro {record_id} adicionado com {len(embeddings)} embeddings", flush=True)
    except Exception as e:
        print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] Erro ao adicionar {record_id}: {e}", flush=True)
        traceback.print_exc()

app = Flask(__name__)

@app.route("/embed", methods=["POST"])
def embed_endpoint():
    try:
        data = request.get_json(force=True)
        record_id = data.get("record_id")
        object_name = data.get("object_name")
        bucket = data.get("bucket", BUCKET_NAME)
        if not record_id or not object_name:
            return jsonify({"status":"error","msg":"record_id ou object_name ausente"}),400
        print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] /embed record_id={record_id} object_name={object_name}", flush=True)
        img = download_image_from_minio(bucket, object_name)
        executor.submit(add_to_gallery, record_id, img)
        return jsonify({"status":"ok"}),200
    except Exception as e:
        print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] /embed erro: {e}", flush=True)
        traceback.print_exc()
        return jsonify({"status":"error","msg":str(e)}),500

def start_flask():
    print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] Iniciando Flask em {HOST}:{PORT}", flush=True)
    app.run(host=HOST, port=PORT, threaded=True, use_reloader=False)

def prepare_gallery_for_matching(raw_gallery):
    prepared = {}
    emb_dim = None
    for k, v in raw_gallery.items():
        if not v: continue
        arr = np.vstack(v).astype(np.float32)
        emb_dim = arr.shape[1]
        arr /= (np.linalg.norm(arr, axis=1, keepdims=True) + 1e-8)
        prepared[k] = arr
    for k in raw_gallery.keys():
        if k not in prepared:
            prepared[k] = np.empty((0, emb_dim or 128), dtype=np.float32)
    return prepared

def best_match_for_probe_embeddings(probe_embeddings, gallery_prepared):
    best_id, best_score = None, float("inf")
    if not probe_embeddings:
        return None, best_score
    if not any(v.size for v in gallery_prepared.values()):
        return None, float("inf")
    # ensure all probe embeddings have same dimension as gallery
    valid_probe = []
    for p in probe_embeddings:
        for g_arr in gallery_prepared.values():
            if g_arr.size > 0 and p.shape[0] != g_arr.shape[1]:
                p = p[:g_arr.shape[1]]
        valid_probe.append(l2_normalize(p))
    P = np.vstack(valid_probe).astype(np.float32)
    for rid, emb_array in gallery_prepared.items():
        if emb_array.size == 0: continue
        try:
            dots = np.dot(P, emb_array.T)
            per_probe_min_dist = 1.0 - np.max(dots, axis=1)
            record_score = float(np.min(per_probe_min_dist))
            if record_score < best_score:
                best_id, best_score = rid, record_score
        except Exception:
            traceback.print_exc()
    return best_id, best_score

def send_register(record_id):
    try:
        if record_id is None:
            return
        url = f"http://127.0.0.1:8080/api/presences/register/{record_id}"
        payload = {"id": int(record_id)} if str(record_id).isdigit() else {"id": record_id}
        print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] -> send_register START record_id={record_id}", flush=True)
        r = session.post(url, json=payload, timeout=8)
        status = getattr(r, "status_code", None)
        text = getattr(r, "text", "")
        print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] -> send_register DONE record_id={record_id} status={status} text={text[:400]}", flush=True)
        last_recognized_time[str(record_id)] = time.time()
    except Exception as e:
        print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] Erro ao enviar register {record_id}: {e}", flush=True)
        traceback.print_exc()

def sendAlert():
    print("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
    try:
        url = "http://127.0.0.1:3000/alert"
        session.post(url, timeout=5)
        print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] ALERT sent", flush=True)
    except Exception as e:
        print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] Erro ao enviar ALERT: {e}", flush=True)

def process_frame(frame_rgb, current_gallery_prepared):
    h_img, w_img = frame_rgb.shape[:2]
    small_frame = cv2.resize(frame_rgb, (0,0), fx=0.5, fy=0.5)
    try:
        detections = detector.detect_faces(small_frame)
    except Exception:
        traceback.print_exc()
        detections = []

    results = []

    if not detections:
        print("No faces detected in frame")
        return results

    for det in detections:
        x, y, w, h = det.get("box", (0,0,0,0))
        x, y, w, h = int(x*2), int(y*2), int(w*2), int(h*2)
        x = max(0, x)
        y = max(0, y)
        w = max(0, min(w, w_img - x))
        h = max(0, min(h, h_img - y))
        if w < 30 or h < 30:
            continue

        face_rgb = frame_rgb[y:y+h, x:x+w]
        scale = max(200/w, 200/h, 1.0)
        try:
            scaled = cv2.resize(face_rgb, (int(face_rgb.shape[1]*scale), int(face_rgb.shape[0]*scale)))
        except Exception:
            scaled = cv2.resize(face_rgb, (face_rgb.shape[1], face_rgb.shape[0]))
        face_rgb_resized = cv2.resize(scaled, (160,160))

        try:
            probe_faces = augment_face_for_probe(face_rgb_resized)
            probe_embeddings = [executor.submit(compute_embedding, pf).result(timeout=8) for pf in probe_faces]
            best_id, best_score = best_match_for_probe_embeddings(probe_embeddings, current_gallery_prepared)

            print(f"Detected: best_id={best_id} best_score={best_score}")

            if best_score is None or not np.isfinite(best_score):
                best_score = float("inf")

            # ALERT if unknown face or high distance
            if best_id is None or best_score > THRESHOLD:
                sendAlert()

            cx = (x + x + w) // 2
            cy = (y + y + h) // 2
            key = (int(cx // 20), int(cy // 20))
            if key not in distance_history:
                distance_history[key] = deque(maxlen=DEQUE_LEN)
            distance_history[key].append(float(best_score))
            id_history[str(best_id)].append(float(best_score))

            vals = [v for v in id_history[str(best_id)] if np.isfinite(v)]
            avg_dist = float(np.mean(vals)) if vals else float("inf")
            now = time.time()
            rec_id_str = str(best_id)
            last_sent = last_recognized_time.get(rec_id_str, 0)
            cooldown_ok = (now - last_sent) > RECOGNITION_COOLDOWN
            should_send = False
            frames_below = sum(1 for v in vals if v <= THRESHOLD)
            if TEST_IMMEDIATE and best_score <= THRESHOLD and cooldown_ok:
                should_send = True
            elif frames_below >= REQUIRED_FRAMES_BELOW and avg_dist <= THRESHOLD and cooldown_ok:
                should_send = True
            if should_send and best_id is not None:
                executor.submit(send_register, best_id)

        except Exception:
            traceback.print_exc()

        results.append((x, y, w, h, best_score, best_id, key))

    return results


frame_queue = deque(maxlen=1)
processed_result = {"results": []}
processing_future = None

def processing_loop():
    global processing_future
    local_gallery_prepared = {}
    last_gallery_version = None
    while True:
        with gallery_lock:
            gallery_snapshot = dict(gallery)
        version = tuple(sorted([(k, len(v)) for k, v in gallery_snapshot.items()]))
        if version != last_gallery_version:
            local_gallery_prepared = prepare_gallery_for_matching(gallery_snapshot)
            last_gallery_version = version
            print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] Galeria preparada ({len(local_gallery_prepared)} entradas)", flush=True)

        if frame_queue:
            latest = frame_queue[-1]
            if processing_future is None or processing_future.done():
                processing_future = executor.submit(process_frame, latest, local_gallery_prepared)

        if processing_future is not None and processing_future.done():
            try:
                results = processing_future.result(timeout=0.1)
                processed_result["results"] = results
            except Exception as e:
                processed_result["results"] = []
                print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] Erro na thread de processamento: {e}", flush=True)
            processing_future = None

        time.sleep(0.005)

def camera_loop():
    cap = cv2.VideoCapture(0)
    try:
        cap.set(cv2.CAP_PROP_BUFFERSIZE, 1)
        cap.set(cv2.CAP_PROP_FPS, 30)
    except Exception:
        pass

    t = threading.Thread(target=processing_loop, daemon=True)
    t.start()

    while True:
        ret, frame = cap.read()
        if not ret:
            break
        frame = cv2.flip(frame, 1)
        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        frame_queue.append(frame_rgb)
        cv2.imshow("Reconhecimento Facial", frame)
        if cv2.waitKey(1) & 0xFF == ord("q"):
            break

    cap.release()
    cv2.destroyAllWindows()

if __name__ == "__main__":
    print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] THRESHOLD={THRESHOLD} TEST_IMMEDIATE={TEST_IMMEDIATE}", flush=True)
    flask_thread = threading.Thread(target=start_flask, daemon=True)
    flask_thread.start()
    camera_loop()