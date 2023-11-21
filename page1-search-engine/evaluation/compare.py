import glob
import pandas

data = {}
for filename in glob.glob("./page1-search-engine/evaluation/*.txt"):
  file = open(filename, 'r')
  for line in file:
    fields = line.split()
    if data.get(fields[0]) == None:
      data[fields[0]] = [fields[2]]
    else:
      data[fields[0]].append(fields[2])

df = pandas.DataFrame.from_dict(data)
path = "./page1-search-engine/evaluation/compare.md"
with open(path, 'w') as f:
  f.write(df.to_markdown())
print("Saved to '" + path + "'.")
  