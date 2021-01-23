from pyserini.search import SimpleSearcher, querybuilder
import json
import sys
from BaselineSearcher import BaselineSearcher
from sentence_transformers import SentenceTransformer, util
import torch

class DistilRobertaSearcher:

    def __init__(self, index_path):
        self.baseline_searcher = BaselineSearcher(index_path)
        self.name = "DistilRobertaSearcher"
        self.model = SentenceTransformer('paraphrase-distilroberta-base-v1')

    def get_name(self):
        return self.name

    def search(self, query, max_amount = 10):
        before_rerank_amount = 100
        hits = self.baseline_searcher.search(query)[:before_rerank_amount]

        arg_corpus = []
        for i, hit in enumerate(hits):
            arg = self.get_argument(hit.docid)
            text = arg['conclusion'] + ' ' + arg['premise']
            arg_corpus.append(text)

        arg_corpus_embeddings = self.model.encode(arg_corpus, convert_to_tensor=True)
        query_embedding = self.model.encode(query, convert_to_tensor=True)

        cos_scores = util.pytorch_cos_sim(query_embedding, arg_corpus_embeddings)[0]
        cos_scores = cos_scores.cpu()

        for i, hit in enumerate(hits):
            hit.score = cos_scores[i] * hit.score

        hits.sort(key=lambda x: x.score, reverse=True)
        return hits[:max_amount]

    def get_argument(self, id):
        return self.baseline_searcher.get_argument(id)
