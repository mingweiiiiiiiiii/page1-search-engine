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
Below are condensed versions of the markdown tables generated in `compare.md` when running the search engine with the current configuration (`TF-IDF query creation`) as well as the baseline `topic title query creation` method (which can be found in this [branch](https://github.com/briantwhelan/page1-search-engine/tree/results-title)).

TLDR; out of the different implementations that were tested, the `CustomAnalyzer_Syn_stp-BM25Similarity` combination performed best for `TF-IDF query creation` with MAP 0.291 while the `GeneralizedCustomAnalyzer-BM25Similarity+LMDirichletSimilarity` performed best for `topic title query creation` with MAP 0.2933 (however there were many similarly performing combinations).

### Topic Title Query Creation Results
| runid                                                             | map    | set_recall |
|-------------------------------------------------------------------|--------|------------|
| EnglishAnalyzer-ClassicSimilarity+LMDirichletSimilarity           | 0.2779 | 0.7815     |
| GeneralizedCustomAnalyzer-LMJelinekMercerSimilarity               | 0.2788 | 0.7539     |
| SimpleAnalyzer-ClassicSimilarity                                  | 0.1539 | 0.5395     |
| WhitespaceAnalyzer-BM25Similarity+LMDirichletSimilarity           | 0.2324 | 0.6012     |
| SimpleAnalyzer-ClassicSimilarity+LMDirichletSimilarity            | 0.2579 | 0.6658     |
| SimpleAnalyzer-LMJelinekMercerSimilarity                          | 0.2426 | 0.6506     |
| EnglishAnalyzer-ClassicSimilarity                                 | 0.1883 | 0.6747     |
| CustomAnalyzer_Syn_stp-BM25Similarity                             | 0.2862 | 0.7555     |
| GeneralizedCustomAnalyzer-ClassicSimilarity+BM25Similarity        | 0.2617 | 0.7299     |
| EnglishAnalyzer-BM25Similarity+LMDirichletSimilarity              | 0.2856 | 0.7653     |
| CustomAnalyzer_Syn_stp-LMJelinekMercerSimilarity                  | 0.2803 | 0.762      |
| SimpleAnalyzer-ClassicSimilarity+BM25Similarity                   | 0.2413 | 0.6454     |
| CustomAnalyzer_Syn_stp-ClassicSimilarity+BM25Similarity           | 0.2635 | 0.745      |
| WhitespaceAnalyzer-BM25Similarity                                 | 0.1976 | 0.5802     |
| EnglishAnalyzer-LMDirichletSimilarity                             | 0.2826 | 0.7708     |
| StandardAnalyzer-ClassicSimilarity                                | 0.1559 | 0.5577     |
| EnglishAnalyzer-BM25Similarity                                    | 0.2909 | 0.7522     |
| WhitespaceAnalyzer-LMJelinekMercerSimilarity                      | 0.19   | 0.5762     |
| GeneralizedCustomAnalyzer-LMDirichletSimilarity                   | 0.2807 | 0.7645     |
| StandardAnalyzer-LMDirichletSimilarity                            | 0.2645 | 0.6819     |
| WhitespaceAnalyzer-ClassicSimilarity+BM25Similarity               | 0.1968 | 0.5775     |
| WhitespaceAnalyzer-LMDirichletSimilarity                          | 0.201  | 0.5665     |
| EnglishAnalyzer-ClassicSimilarity+BM25Similarity                  | 0.2653 | 0.736      |
| EnglishAnalyzer-LMJelinekMercerSimilarity                         | 0.2803 | 0.7604     |
| GeneralizedCustomAnalyzer-BM25Similarity+LMDirichletSimilarity    | 0.2933 | 0.771      |
| GeneralizedCustomAnalyzer-BM25Similarity                          | 0.2917 | 0.7481     |
| WhitespaceAnalyzer-ClassicSimilarity                              | 0.1183 | 0.4674     |
| StandardAnalyzer-BM25Similarity+LMDirichletSimilarity             | 0.2736 | 0.683      |
| StandardAnalyzer-ClassicSimilarity+BM25Similarity                 | 0.248  | 0.6624     |
| SimpleAnalyzer-LMDirichletSimilarity                              | 0.2627 | 0.6769     |
| CustomAnalyzer_Syn_stp-BM25Similarity+LMDirichletSimilarity       | 0.2907 | 0.7795     |
| CustomAnalyzer_Syn_stp-LMDirichletSimilarity                      | 0.2835 | 0.7634     |
| WhitespaceAnalyzer-ClassicSimilarity+LMDirichletSimilarity        | 0.2109 | 0.5836     |
| CustomAnalyzer_Syn_stp-ClassicSimilarity                          | 0.192  | 0.678      |
| GeneralizedCustomAnalyzer-ClassicSimilarity                       | 0.1842 | 0.6619     |
| SimpleAnalyzer-BM25Similarity                                     | 0.2641 | 0.6651     |
| StandardAnalyzer-ClassicSimilarity+LMDirichletSimilarity          | 0.2604 | 0.6679     |
| CustomAnalyzer_Syn_stp-ClassicSimilarity+LMDirichletSimilarity    | 0.2724 | 0.7685     |
| StandardAnalyzer-BM25Similarity                                   | 0.2723 | 0.6747     |
| GeneralizedCustomAnalyzer-ClassicSimilarity+LMDirichletSimilarity | 0.2753 | 0.7658     |
| StandardAnalyzer-LMJelinekMercerSimilarity                        | 0.2564 | 0.677      |
| SimpleAnalyzer-BM25Similarity+LMDirichletSimilarity               | 0.2774 | 0.6831     |

### Topic Title Query Creation Results
| runid                                                             | map    | set_recall |
|-------------------------------------------------------------------|--------|------------|
| EnglishAnalyzer-ClassicSimilarity+LMDirichletSimilarity           | 0.2243 | 0.6773     |
| GeneralizedCustomAnalyzer-LMJelinekMercerSimilarity               | 0.2639 | 0.7199     |
| SimpleAnalyzer-ClassicSimilarity                                  | 0.1624 | 0.6098     |
| WhitespaceAnalyzer-BM25Similarity+LMDirichletSimilarity           | 0.1106 | 0.4018     |
| SimpleAnalyzer-ClassicSimilarity+LMDirichletSimilarity            | 0.2114 | 0.6441     |
| SimpleAnalyzer-LMJelinekMercerSimilarity                          | 0.2223 | 0.6492     |
| EnglishAnalyzer-ClassicSimilarity                                 | 0.1845 | 0.6474     |
| CustomAnalyzer_Syn_stp-BM25Similarity                             | 0.291  | 0.7333     |
| GeneralizedCustomAnalyzer-ClassicSimilarity+BM25Similarity        | 0.2579 | 0.7047     |
| EnglishAnalyzer-BM25Similarity+LMDirichletSimilarity              | 0.246  | 0.6787     |
| CustomAnalyzer_Syn_stp-LMJelinekMercerSimilarity                  | 0.277  | 0.7338     |
| SimpleAnalyzer-ClassicSimilarity+BM25Similarity                   | 0.2395 | 0.6849     |
| CustomAnalyzer_Syn_stp-ClassicSimilarity+BM25Similarity           | 0.2807 | 0.7302     |
| WhitespaceAnalyzer-BM25Similarity                                 | 0.1215 | 0.4298     |
| EnglishAnalyzer-LMDirichletSimilarity                             | 0.2111 | 0.6665     |
| StandardAnalyzer-ClassicSimilarity                                | 0.1692 | 0.5881     |
| EnglishAnalyzer-BM25Similarity                                    | 0.2741 | 0.7157     |
| WhitespaceAnalyzer-LMJelinekMercerSimilarity                      | 0.1246 | 0.4485     |
| GeneralizedCustomAnalyzer-LMDirichletSimilarity                   | 0.2135 | 0.6616     |
| StandardAnalyzer-LMDirichletSimilarity                            | 0.1965 | 0.6002     |
| WhitespaceAnalyzer-ClassicSimilarity+BM25Similarity               | 0.1271 | 0.4495     |
| WhitespaceAnalyzer-LMDirichletSimilarity                          | 0.0994 | 0.4105     |
| EnglishAnalyzer-ClassicSimilarity+BM25Similarity                  | 0.2681 | 0.7192     |
| EnglishAnalyzer-LMJelinekMercerSimilarity                         | 0.2816 | 0.7436     |
| GeneralizedCustomAnalyzer-BM25Similarity+LMDirichletSimilarity    | 0.2436 | 0.6731     |
| GeneralizedCustomAnalyzer-BM25Similarity                          | 0.2678 | 0.712      |
| WhitespaceAnalyzer-ClassicSimilarity                              | 0.0757 | 0.4023     |
| StandardAnalyzer-BM25Similarity+LMDirichletSimilarity             | 0.2233 | 0.6547     |
| StandardAnalyzer-ClassicSimilarity+BM25Similarity                 | 0.2377 | 0.6802     |
| SimpleAnalyzer-LMDirichletSimilarity                              | 0.1926 | 0.6004     |
| CustomAnalyzer_Syn_stp-BM25Similarity+LMDirichletSimilarity       | 0.2586 | 0.7085     |
| CustomAnalyzer_Syn_stp-LMDirichletSimilarity                      | 0.2038 | 0.6697     |
| WhitespaceAnalyzer-ClassicSimilarity+LMDirichletSimilarity        | 0.1091 | 0.4064     |
| CustomAnalyzer_Syn_stp-ClassicSimilarity                          | 0.1894 | 0.6559     |
| GeneralizedCustomAnalyzer-ClassicSimilarity                       | 0.1866 | 0.6192     |
| SimpleAnalyzer-BM25Similarity                                     | 0.2308 | 0.6635     |
| StandardAnalyzer-ClassicSimilarity+LMDirichletSimilarity          | 0.2012 | 0.6408     |
| CustomAnalyzer_Syn_stp-ClassicSimilarity+LMDirichletSimilarity    | 0.2379 | 0.6967     |
| StandardAnalyzer-BM25Similarity                                   | 0.2338 | 0.6733     |
| GeneralizedCustomAnalyzer-ClassicSimilarity+LMDirichletSimilarity | 0.2161 | 0.6675     |
| StandardAnalyzer-LMJelinekMercerSimilarity                        | 0.2261 | 0.6586     |
| SimpleAnalyzer-BM25Similarity+LMDirichletSimilarity               | 0.2169 | 0.6575     |

### Query Creation
Queries were created from [topics](./page1-search-engine/data/queries/topics.txt). The `QueryCreator` created these queries by using the combining the `title`, `description` and `narrative`, removing stop words and punctuation and then using TF-IDF to keep the top 10 keywords for each topic/query. This process results in these [queries](./page1-search-engine/data/queries/queries.txt) being generated when run.


### [Analyzers](https://lucene.apache.org/core/8_1_0/core/org/apache/lucene/analysis/Analyzer.html)

The [WhitespaceAnalyzer](https://lucene.apache.org/core/8_1_0/analyzers-common/org/apache/lucene/analysis/core/WhitespaceAnalyzer.html), [SimpleAnalyzer](https://lucene.apache.org/core/8_1_0/analyzers-common/org/apache/lucene/analysis/core/SimpleAnalyzer.html), [StandardAnalyzer](https://lucene.apache.org/core/8_0_0/core/org/apache/lucene/analysis/standard/StandardAnalyzer.html) and [EnglishAnalyser](https://lucene.apache.org/core/8_10_1/analyzers-common/org/apache/lucene/analysis/en/EnglishAnalyzer.html) were each used as well as two custom analyzers.

Disregarding the query creation method and scorer used, the EnglishAnalyzer and two custom analyzers were the only well-performing analyzers.

<TODO>

### [Scorers](https://lucenenet.apache.org/docs/4.8.0-beta00007/api/Lucene.Net/Lucene.Net.Search.Similarities.html)

The [Vector Space Model](https://lucene.apache.org/core/8_1_0/core/org/apache/lucene/search/similarities/ClassicSimilarity.html), [BM25 Model](https://lucene.apache.org/core/8_1_0/core/org/apache/lucene/search/similarities/BM25Similarity.html), [Dirichlet Model](https://lucenenet.apache.org/docs/4.8.0-beta00007/api/Lucene.Net/Lucene.Net.Search.Similarities.LMDirichletSimilarity.html) and [Jelinek-Mercer Model](https://lucenenet.apache.org/docs/4.8.0-beta00007/api/Lucene.Net/Lucene.Net.Search.Similarities.LMJelinekMercerSimilarity.html) as well as various combinations of these scorers (using [MultiSimilarity](https://lucenenet.apache.org/docs/4.8.0-beta00007/api/Lucene.Net/Lucene.Net.Search.Similarities.MultiSimilarity.html)) were tested in combination with the various analyzers.

Disregarding the Analyzer used, the <TODO> model performed best.
