from minio import Minio
from PIL import Image
import io
import numpy as np

# MinIO client
minio_client = Minio(
    "localhost:9000",
    access_key="minioadmin",
    secret_key="minioadmin",
    secure=False
)

def download_image(bucket: str, object_name: str) -> np.ndarray:
    response = minio_client.get_object(bucket, object_name)
    data = response.read()
    response.close()
    response.release_conn()
    img = Image.open(io.BytesIO(data)).convert("RGB")
    return np.array(img)
