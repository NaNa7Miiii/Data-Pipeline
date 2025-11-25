config = {
    "topic_modelling": {
        "umap": {
            "n_neighbors": 15,
            "n_components": 5,
            "min_dist": 0.0,
            "metric": "cosine",
            "random_state": 42
        },
        "hdbscan": {
            "min_cluster_size": 10,
            "min_samples": 5,
            "metric": "euclidean",
            "cluster_selection_method": "eom"
        },
        "ctfidf": {
            "reduce_frequent_words": True
        },
        "outlier_reduction": {
            "embedding_thresh": 0.1
        }
    }
}
