from config.minIO.minIO_config import client
import cv2
import numpy as np

class MinioService:
    def __init__(self, client_instance=client):
        self.client = client_instance

    def load_image(self, bucket: str, object_name: str):
        """
        Load an image from MinIO and return it as an OpenCV image (BGR).
        """
        response = self.client.get_object(bucket, object_name)
        try:
            data = response.read()
            img_array = np.frombuffer(data, np.uint8)
            img = cv2.imdecode(img_array, cv2.IMREAD_COLOR)
            if img is None:
                raise ValueError(
                    f"Failed to decode image {object_name} from bucket {bucket}"
                )
            return img
        finally:
            response.close()
            response.release_conn()
