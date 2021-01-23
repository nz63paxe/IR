import xml.etree.ElementTree as ET

from RM3Searcher import RM3Searcher

runs_folder = 'C:/Users/Song/Desktop/InformationRetrieval/advanced-information-retrieval/evaluation/runs/'
topics = 'C:/Users/Song/Desktop/InformationRetrieval/advanced-information-retrieval/evaluation/default_topics.xml'
data_directory = "C:/Users/Song/Desktop/InformationRetrieval/data/"
files = ["debatepedia.json", "debatewise.json", "idebate.json", "parliamentary.json", "debateorg.json"]
hits_per_search = 10


def evaluate_searcher(searcher):
    results = ''
    root = ET.parse(topics).getroot()
    for topic_tag in root.findall('topic'):
        query = topic_tag.find('title').text
        for index, hit in enumerate(searcher.search(query, 10)):
            arg = searcher.get_argument(hit.docid)
            arg = searcher.get_argument(hit.docid)
            arg = searcher.get_argument(hit.docid)
            results += topic_tag.find('number').text + '\t'
            results += 'Q0' + '\t'
            results += '"' + arg['id'] + '"' + '\t'
            results += str(index + 1) + '\t'
            results += str(hit.score) + '\t'
            results += '"' + arg['id'] + '"' + '\t'
            results += '"' + arg['id'] + '"' + '\t'
            results += searcher.get_name() + '\t'
            results += '\n'

    with open(runs_folder + searcher.get_name(), 'w') as out_file:
        out_file.write(results)


if __name__ == "__main__":
    index_path = '../anserini/indexes/concat_arguments/'
    evaluate_searcher(RM3Searcher(index_path))