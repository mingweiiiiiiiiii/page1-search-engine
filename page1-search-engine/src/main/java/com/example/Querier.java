package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.example.queryCreation.ProcessingFromTopicParser;

public class Querier {
  public static final String INDEX_DIR = "./dir";
  public static final int TOP_DOCS_LIMIT = 1000;

  private static final String TITLE  = "title";
  private static final String CONTENT  = "content";

  private final ArrayList<String> queries;

  /**
   * Constructor for Querier.
   * @param pathToQueries Path to the queries to search.
   */
  public Querier(String pathToQueries) {
    // Read queries from topics.
    ProcessingFromTopicParser myqueryGeneration = new ProcessingFromTopicParser();
    myqueryGeneration.run();
    this.queries = myqueryGeneration.getQueryList();
  }

  /**
   * Queries the documents with the given analyzer and scorer.
   * @param analyzer Analyzer to use for querying.
   * @param scorer Similarity to use for querying.
   * @throws Exception exception.
   */
  public void queryDocuments(Analyzer analyzer, Similarity scorer) throws Exception {
    String analyzerName = analyzer.getClass().getName()
            .substring(analyzer.getClass().getName().lastIndexOf('.') + 1);
    String scorerName = scorer.getClass().getName()
            .substring(scorer.getClass().getName().lastIndexOf('.') + 1);
    File fout = new File("./results/" + analyzerName + "-" + scorerName);
    FileOutputStream fos = new FileOutputStream(fout);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

    IndexSearcher searcher = createIndexSearcher(scorer);

    // Search index with all queries and write results to file.
    int queryID = 401;
    for (String q : queries) {
      q = q.trim();
      TopDocs results = search(q, searcher, analyzer);
      String queryResults = "";
      int rank = 1;
      for (ScoreDoc sd : results.scoreDocs) {
        // Output in trec_eval format - see http://www.rafaelglater.com/en/post/learn-how-to-use-trec_eval-to-evaluate-your-information-retrieval-system.
        // Format: query-id Q0 document-id rank score STANDARD
        queryResults += queryID + " Q0 " + (sd.doc + 1) + " " 
          + rank + " " + sd.score + " " + analyzerName + "-" + scorerName + "\n";
        rank++;
      }
      bw.write(queryResults);
      queryID++;
    }
    bw.close();
    System.out.println("Queried documents with " + analyzerName + " and " + scorerName + ".");
  }

  /**
   * Creates an IndexSearcher with the given scorer.
   * @param scorer Similarity to use for querying.
   * @throws IOException input-otput exception.
   */
  private static IndexSearcher createIndexSearcher(Similarity scorer) throws IOException {
    // Use file system directory to retrieve index.
    Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
    IndexReader reader = DirectoryReader.open(dir);
    IndexSearcher searcher = new IndexSearcher(reader);
    // Configure index searcher with scorer.
    searcher.setSimilarity(scorer);
    return searcher;
  }

  private static QueryParser createQueryParser(Analyzer analyzer) {
    // Create query parser.
    float weightForTitle = 0.2f;
    float weightForDesc =1.1f;
    Map<String, Float> boosts = new HashMap<>();
    boosts.put(TITLE, weightForTitle);
    boosts.put(CONTENT, weightForDesc);
    String []fields = new String []{TITLE,CONTENT};
    QueryParser parser_single_mutiple = new MultiFieldQueryParser(fields, analyzer, boosts);
    parser_single_mutiple.setAllowLeadingWildcard(true);
    return parser_single_mutiple;
  }

  /**
   * Searches the documents for the given term.
   * @param term Term to search for.
   * @param searcher IndexSearcher to use for searching.
   * @param analyzer Analyzer to use for searching.
   * @throws Exception exception.
   */
  private static TopDocs search(String term, IndexSearcher searcher, 
      Analyzer analyzer) throws Exception {
    // Use specified analyzer to parse query.
    QueryParser qp = createQueryParser(analyzer);
    qp.setAllowLeadingWildcard(true);
    // Search both title and content fields for query term.
    // https://lucene.apache.org/core/2_9_4/queryparsersyntax.html.
    String queryTerm = "title:" + term + " OR content:" + term;
    // Search index for top results for query.
    Query query = qp.parse(queryTerm);
    TopDocs topDocs = searcher.search(query, TOP_DOCS_LIMIT);
    return topDocs;
  }
}