from pyserini.search import SimpleSearcher, querybuilder
import json
import sys
from BaselineSearcher import BaselineSearcher
from DistilRobertaSearcher import DistilRobertaSearcher
from USESearcher import USESearcher
from RobertaSearcher import RobertaSearcher
from RM3Searcher import RM3Searcher
from USEPremiseSearcher import USEPremiseSearcher

index_path = '../anserini/indexes/concat_arguments/'

searchers = {
    'baseline': BaselineSearcher,
    'distil-roberta': DistilRobertaSearcher,
    'roberta': RobertaSearcher,
    'universal-sentence-encoder': USESearcher,
    'universal-sentence-encoder-only-premise': USEPremiseSearcher,
    'rm3': RM3Searcher,
}

def print_results(searcher, hits, max_amount = 100):
    for i, hit in enumerate(hits):
        arg = searcher.get_argument(hit.docid)
        print(str(i + 1) + ". (Score: " + str(hit.score) + ")")
        print("Conclusion: " + arg["conclusion"])
        print("Premise: " + arg["premise"])
        print()
        if (i + 1) >= max_amount:
            return

if __name__ == "__main__":
    if len(sys.argv) == 3:
        searcher = searchers[sys.argv[1]](index_path)
        hits = searcher.search(sys.argv[2])
        print("Query: " + sys.argv[2])
        print("Seacher: " + searcher.get_name())
        print()
        print_results(searcher, hits)
    else:
        print("Please specify searcher and query")
