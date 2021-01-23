import json

directory = "C:/Users/Song/Desktop/InformationRetrieval/data/"
files = ["debatepedia.json", "debatewise.json", "idebate.json", "parliamentary.json", "debateorg.json"]

result = ""

for f in files:
    with open(directory + f) as json_file:
        result += f + ":\n"
        data = json.load(json_file)
        result += json.dumps(data["arguments"][0], indent=4, sort_keys=True)
        result += "\n\n"

f = open("../results/json_format.txt", "w")
f.write(result)
f.close()
