import cv2
import numpy as np
from deepface import DeepFace
from deepface_service import load_gallery

THRESHOLD = 0.4  # Facenet distance threshold

def recognize_camera():
    # Load gallery embeddings
    gallery = load_gallery()

    # Open webcam (0 = default camera)
    cap = cv2.VideoCapture(0)

    if not cap.isOpened():
        print("Cannot open camera")
        return

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        img_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

        try:
            # Detect faces
            faces = DeepFace.extract_faces(img_path=img_rgb, detector_backend="opencv", align=False, enforce_detection=False)

            for face in faces:
                x, y, w, h = face["facial_area"]["x"], face["facial_area"]["y"], face["facial_area"]["w"], face["facial_area"]["h"]
                face_img = face["face"]

                # Embedding
                embedding = DeepFace.represent(face_img, model_name="Facenet")[0]["embedding"]

                # Compare with gallery
                recognized = False
                name = "Unknown"
                best_dist = float("inf")

                for person_id, embeddings_list in gallery.items():
                    for ref_emb in embeddings_list:
                        dist = np.linalg.norm(np.array(ref_emb) - np.array(embedding))
                        if dist < best_dist:
                            best_dist = dist
                            if dist < THRESHOLD:
                                recognized = True
                                name = person_id

                # Draw box
                color = (0, 255, 0) if recognized else (0, 0, 255)
                cv2.rectangle(frame, (x, y), (x + w, y + h), color, 2)
                cv2.putText(frame, f"{name} ({best_dist:.2f})", (x, y - 10),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.5, color, 2)

        except Exception as e:
            # No faces detected or other error
            pass

        # Show frame
        cv2.imshow("Face Recognition", frame)

        # Press 'q' to exit
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()
