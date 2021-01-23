from pyserini.search import SimpleSearcher, querybuilder
import json
import sys
from BaselineSearcher import BaselineSearcher
import tensorflow_hub as hub
import numpy as np
import matplotlib.pyplot as plt
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3' 

class USEPremiseSearcher:

    def __init__(self, index_path):
        self.baseline_searcher = BaselineSearcher(index_path)
        self.name = "USEPremiseSearcher"
        self.model = hub.load("https://tfhub.dev/google/universal-sentence-encoder/4") # https://tfhub.dev/google/universal-sentence-encoder-large/5

    def get_name(self):
        return self.name

    def search(self, query, max_amount = 10):
        before_rerank_amount = 100
        hits = self.baseline_searcher.search(query)[:before_rerank_amount]

        arg_corpus = []
        for i, hit in enumerate(hits):
            arg = self.get_argument(hit.docid)
            text = arg['premise']
            arg_corpus.append(text)

        embeddings = self.model(arg_corpus) 
        query_embedding = self.model([query])[0]

        cos_scores = []
        for i, emb in enumerate(embeddings):
            similarity = np.inner(query_embedding, emb)
            cos_scores.append(similarity)

        for i, hit in enumerate(hits):
            hit.score = cos_scores[i]

        hits.sort(key=lambda x: x.score, reverse=True)
        return hits[:max_amount]

    def get_argument(self, id):
        return self.baseline_searcher.get_argument(id)
