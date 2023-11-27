# Page1 Search Engine
This group project aims to acheive the best search engine performance for a collection of news articles from various sources.

## Overview

[Apache Lucene](https://lucene.apache.org/) is an open-source search engine library which provides methods for indexing and querying collections of documents.

In this project, a number of different implementations of Lucene are used to index and search newspaper article datasets from four different sources:
1. Financial Times Limited (1991, 1992, 1993, 1994),
2. Federal Register (1994),
3. Foreign Broadcast Information Service (1996)
4. Los Angeles Times (1989, 1990).

Multiple implementations are used to evaluate their respective performances using [trec_eval](https://trec.nist.gov/trec_eval/).

![Overview](./page1-search-engine/overview.png)

## Running the Search Engine

Before running, trec_eval must be compiled and the relevant python packages must be installed. To do this, run the `install` script.

```sh
$ ./install.sh
```

To run the search engine, run the `run` script which does the following:

- Runs the search engine, outputting result files in `/results`
- Runs trec_eval against all of these results using the `qrels` as the groud truth, producing evaluation files in `/evaluation`
- Creates a markdown table, `compare.md`, containing the results for each of the implementations, enabling comparison

```sh
$ ./run.sh
```

## Results

Below is a condensed version of the markdown table produced in `compare.md` when running the search engine with the current configuration.

TLDR; out of the different implementations that were tested, the <TODO> combination performed best.
<TODO>

### Query Creation
Queries were created from [topics](./page1-search-engine/data/queries/topics.txt). The `QueryCreator` created these queries by using the combining the `title`, `description` and `narrative`, removing stop words and punctuation and then using TF-IDF to keep the top 10 keywords for each topic/query. This process results in these [queries](./page1-search-engine/data/queries/queries.txt) being generated when run.


### [Analyzers](https://lucene.apache.org/core/8_1_0/core/org/apache/lucene/analysis/Analyzer.html)

The [WhitespaceAnalyzer](https://lucene.apache.org/core/8_1_0/analyzers-common/org/apache/lucene/analysis/core/WhitespaceAnalyzer.html), [SimpleAnalyzer](https://lucene.apache.org/core/8_1_0/analyzers-common/org/apache/lucene/analysis/core/SimpleAnalyzer.html), [StandardAnalyzer](https://lucene.apache.org/core/8_0_0/core/org/apache/lucene/analysis/standard/StandardAnalyzer.html) and [EnglishAnalyser](https://lucene.apache.org/core/8_10_1/analyzers-common/org/apache/lucene/analysis/en/EnglishAnalyzer.html) were each used as well as two custom analyzers.

Disregarding the Scorer used, the <TODO> analyzer performed best.

### [Scorers](https://lucenenet.apache.org/docs/4.8.0-beta00007/api/Lucene.Net/Lucene.Net.Search.Similarities.html)

The [Vector Space Model](https://lucene.apache.org/core/8_1_0/core/org/apache/lucene/search/similarities/ClassicSimilarity.html), [BM25 Model](https://lucene.apache.org/core/8_1_0/core/org/apache/lucene/search/similarities/BM25Similarity.html), [Dirichlet Model](https://lucenenet.apache.org/docs/4.8.0-beta00007/api/Lucene.Net/Lucene.Net.Search.Similarities.LMDirichletSimilarity.html) and [Jelinek-Mercer Model](https://lucenenet.apache.org/docs/4.8.0-beta00007/api/Lucene.Net/Lucene.Net.Search.Similarities.LMJelinekMercerSimilarity.html) as well as various combinations of these scorers (using [MultiSimilarity](https://lucenenet.apache.org/docs/4.8.0-beta00007/api/Lucene.Net/Lucene.Net.Search.Similarities.MultiSimilarity.html)) were tested in combination with the various analyzers.

Disregarding the Analyzer used, the <TODO> model performed best.
