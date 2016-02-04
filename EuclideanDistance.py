# -^- coding : utf-8 -^-
import glob
import json
import math

f = open("2016_01_20__17_26_53.json", "r")
jsonData = json.load(f)
Criteria = jsonData.get("data")

jsonArr = []
for file in glob.glob("*.json"):
	data = json.load(open(file, "r")).get("data")
	if data is not None:
		jsonArr.append(data)

for i in jsonArr:
	sumOfSquares = 0.0
	for x in range(len(Criteria)):
		sumOfSquares += (Criteria[x] - i[x]) ** 2
	print(math.sqrt(sumOfSquares))
print("length =  " + str(len(jsonArr)))