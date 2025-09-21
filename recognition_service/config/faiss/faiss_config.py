import faiss

# ArcFace embeddings are 512-dimensional
EMBEDDING_DIM = 512

# Index type: for small datasets, L2 is good
INDEX_TYPE = "IndexFlatL2"

# Number of nearest neighbors to return
TOP_K = 1

# Number of CPU threads for FAISS
NUM_THREADS = 4


def create_index():
    """Create a FAISS index based on the config."""
    faiss.omp_set_num_threads(NUM_THREADS)
    if INDEX_TYPE == "IndexFlatL2":
        return faiss.IndexFlatL2(EMBEDDING_DIM)
    else:
        raise NotImplementedError(f"Index type {INDEX_TYPE} not implemented")