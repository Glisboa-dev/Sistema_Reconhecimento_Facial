import numpy as np
import faiss
from config.faiss import faiss_config as fc

class FaissService:
    def __init__(self):
        self.index = fc.create_index()
        self.student_ids = []  # Mapping between embeddings and student IDs

    def add_student(self, student_id: str, embeddings: list[np.ndarray]):
        """
        Add student embeddings to the FAISS index.
        Each embedding must be a numpy array of shape (512,)
        """
        embeddings_np = np.vstack(embeddings).astype(np.float32)
        self.index.add(embeddings_np)
        self.student_ids.extend([student_id] * len(embeddings))

    def recognize(self, query_embedding: np.ndarray, k: int = fc.TOP_K):
        """
        Recognize a face by comparing query embedding with the FAISS index.
        query_embedding must be shape (512,)
        """
        query_embedding = np.array([query_embedding], dtype=np.float32)
        D, I = self.index.search(query_embedding, k)
        matched_ids = [self.student_ids[i] for i in I[0]]
        distances = D[0]
        return matched_ids, distances

    def save_index(self, path: str):
        """Persist FAISS index to disk."""
        faiss.write_index(self.index, path)

    def load_index(self, path: str):
        """Load FAISS index from disk."""
        self.index = faiss.read_index(path)
