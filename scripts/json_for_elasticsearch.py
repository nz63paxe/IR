import json
import sys

max_num = 500

data_directory = "../data/"
files = ["debatepedia.json", "debatewise.json", "idebate.json", "parliamentary.json", "debateorg.json"]
# files = ["parliamentary.json", "idebate.json"]

for f in files:
    with open(data_directory + f) as json_file:
        data = json.load(json_file)
        arg_num = 0
        iteration = 0
        result = ""
        for arg in data["arguments"]:
            arg_num += 1
            result += "{\"index\": {}}\n"
            result += json.dumps(arg)
            result += "\n"
            if arg_num > max_num:
                f_write = open(data_directory + "splits/" + f.split(".")[0] + "_" + str(iteration) + ".json", "w")
                f_write.write(result)
                f_write.close()
                iteration += 1
                arg_num = 0
                result = ""
        if result != "":
            f_write = open(data_directory + "splits/" + f.split(".")[0] + "_" + str(iteration) + ".json", "w")
            f_write.write(result)
            f_write.close()

