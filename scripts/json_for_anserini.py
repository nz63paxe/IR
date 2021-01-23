import json
import sys

max_num = 500

data_directory = "C:/Users/Song/Desktop/InformationRetrieval/data/"
output_directory = "C:/Users/Song/Desktop/InformationRetrieval/output/anserini/data/"
files = ["debatepedia.json", "debatewise.json", "idebate.json", "parliamentary.json", "debateorg.json"]


# files = ["parliamentary.json", "idebate.json"]


def prepare_data():
    id_num = 0
    for f in files:
        with open(data_directory + f, encoding='utf-8') as json_file:
            data = json.load(json_file)
            args = data["arguments"]
            result_list = []

            for arg in args:
                obj = {}
                obj["id"] = arg["id"]
                id_num += 1
                conclusion = arg["conclusion"]
                premise = arg["premises"][0]["text"]
                obj["contents"] = conclusion + " " + premise
                obj["premise"] = premise
                obj["conclusion"] = conclusion
                obj["stance"] = arg["premises"][0]["stance"]
                result_list.append(obj)
            f_write = open(output_directory + f.split(".")[0] + ".json", "w")
            f_write.write(json.dumps(result_list, indent=4))
            f_write.close()
            print("Finished", f)


if __name__ == "__main__":
    prepare_data()
