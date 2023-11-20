#!/bin/sh

# Run search engine.
echo "Running search engine..."
(cd ./page1-search-engine && mvn clean install && java -jar ./target/page1-search-engine-1.0.jar)

# Run trec_eval and save results to file.
for file in ./page1-search-engine/results/*; do
  	echo "Running trec_eval for $(basename $file) and storing results to ./page1-search-engine/evaluation/$(basename $file).txt..."
    (./trec_eval-9.0.7/trec_eval -m official ./page1-search-engine/evaluation/qrels $file) > ./page1-search-engine/evaluation/$(basename $file).txt
done
