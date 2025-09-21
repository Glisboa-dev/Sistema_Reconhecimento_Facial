# DeepFace configuration
MODEL_NAME = "ArcFace"        # Options: ArcFace, Facenet, VGG-Face, etc.
ENFORCE_DETECTION = True      # If False, DeepFace will return embedding even if no face is detected
DETECTOR_BACKEND = "retinaface"  # Other options: opencv, mtcnn, dlib, ssd, mediapipe, etc.

# Augmentation (optional, for robustness against angle/flip variations)
AUGMENT_ANGLES = [-15, 0, 15]   # Rotation in degrees
FLIP_HORIZONTAL = True
