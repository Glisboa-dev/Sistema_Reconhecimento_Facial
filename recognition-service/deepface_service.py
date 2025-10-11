from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

from deepface import DeepFace
import pickle
import os

from minio_service import download_image

GALLERY_FILE = "deepface_gallery.pkl"
BUCKET_NAME = "faces"

app = FastAPI()

# ---------------- gallery helpers ----------------
def load_gallery():
    if os.path.exists(GALLERY_FILE):
        with open(GALLERY_FILE, "rb") as f:
            return pickle.load(f)
    return {}

def save_gallery(gallery):
    with open(GALLERY_FILE, "wb") as f:
        pickle.dump(gallery, f)

# ---------------- request schema ----------------
class AddReferenceRequest(BaseModel):
    record_id: int
    object_name: str

# ---------------- API endpoint ----------------
@app.post("/add_reference")
def add_reference_api(request: AddReferenceRequest):
    try:
        # download image from MinIO
        img = download_image(BUCKET_NAME, request.object_name)

        # extract embedding
        embedding = DeepFace.represent(img, model_name="Facenet")[0]["embedding"]

        # update gallery
        gallery = load_gallery()
        gallery.setdefault(request.record_id, []).append(embedding)
        save_gallery(gallery)

        return {"message": f"Added reference embedding for record_id {request.record_id}"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
