# -^- coding : utf-8 -^-
import glob
import json
import math

jsonArr = []

for file in glob.glob("*.json"):
	data = json.load(open(file, "r")).get("data")
	if data is not None:
		jsonArr.append(data)

original = jsonArr[0]
for i in jsonArr:
	sumOfSquares = 0.0
	for x in range(len(original)):
		sumOfSquares += (original[x] - i[x]) ** 2
	print(math.sqrt(sumOfSquares))
print("length =  " + str(len(jsonArr)))
