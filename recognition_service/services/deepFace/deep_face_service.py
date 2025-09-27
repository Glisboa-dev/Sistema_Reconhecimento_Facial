from deepface import DeepFace
import numpy as np
import cv2
from config.deepFace import deep_face_config as dfc
from services.minIO.minIo_service import MinioService

class DeepFaceService:
    def __init__(self, model_name=dfc.MODEL_NAME,
                 detector_backend=dfc.DETECTOR_BACKEND,
                 enforce_detection=dfc.ENFORCE_DETECTION,
                 augment_angles=dfc.AUGMENT_ANGLES,
                 flip_horizontal=dfc.FLIP_HORIZONTAL,
                 minio_service: MinioService = MinioService()):
        self.model_name = model_name
        self.detector_backend = detector_backend
        self.enforce_detection = enforce_detection
        self.augment_angles = augment_angles
        self.flip_horizontal = flip_horizontal
        self.minio_service = minio_service

    def augment_face(self, img):
        """Perform rotation and optional horizontal flip for augmentation."""
        h, w = img.shape[:2]
        augmented = []

        for angle in self.augment_angles:
            M = cv2.getRotationMatrix2D((w//2, h//2), angle, 1)
            rotated = cv2.warpAffine(img, M, (w, h))
            augmented.append(rotated)
            if self.flip_horizontal:
                augmented.append(cv2.flip(rotated, 1))

        return augmented

    def get_embeddings_from_image(self, img):
        """Get embeddings from a single OpenCV image (BGR)."""
        augmented_imgs = self.augment_face(img)
        embeddings = []

        for aug_img in augmented_imgs:
            img_rgb = cv2.cvtColor(aug_img, cv2.COLOR_BGR2RGB)
            embedding = DeepFace.represent(
                img_rgb,
                model_name=self.model_name,
                detector_backend=self.detector_backend,
                enforce_detection=self.enforce_detection
            )[0]["embedding"]
            embeddings.append(np.array(embedding, dtype=np.float32))

        return embeddings

    def get_embeddings_from_minio(self, bucket, object_name):
        """Retrieve image from MinIO and return embeddings."""
        img = self.minio_service.load_image(bucket, object_name)
        return self.get_embeddings_from_image(img)
