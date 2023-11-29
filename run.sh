#!/bin/sh

# Run search engine.
# If you are running into problems with JVM heap memory, try to increase the heap size.
# From testing, ~4GB heap size is needed to run the search engine on the entire dataset.
# To see the heap size, run the following command:
#  $ java -XX:+PrintFlagsFinal -version | grep HeapSize
# To increase the heap size, add the following flag to the java command below:
#  -Xmx4g (this increases the max heap size to 4GB)
echo "Running search engine..."
(cd ./page1-search-engine && mvn clean install && java -Xmx4g -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath="." -jar ./target/page1-search-engine-1.0.jar)

# Run trec_eval and save results to file.
for file in ./page1-search-engine/results/*; do
  if [ "$(basename "$file")" = "README.md" ]; then
    echo "Skipping README.md..."
    continue
  fi
  echo "Running trec_eval for $(basename "$file") and storing results to './page1-search-engine/evaluation/$(basename "$file").txt'..."
  (./trec_eval-9.0.7/trec_eval -m official ./page1-search-engine/evaluation/qrels "$file") > ./page1-search-engine/evaluation/"$(basename "$file")".txt
done

# Compile results into markdown table.
echo "Creating markdown table of results..."
python3 ./page1-search-engine/evaluation/compare.py
