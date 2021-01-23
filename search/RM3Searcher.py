from pyserini.search import SimpleSearcher, querybuilder
import json
import sys

class RM3Searcher:

    def __init__(self, index_path):
        self.searcher = SimpleSearcher(index_path)
        self.searcher.set_qld() # use Dirichlet
        self.searcher.set_rm3(10, 10, 0.5, True)
        self.name = "RM3"

    def get_name(self):
        return self.name

    def search(self, query, max_amount = 10):
        hits = self.searcher.search(query)[:max_amount]
        return hits

    def get_argument(self, id):
        arg = json.loads(self.searcher.doc(id).raw())
        return arg
