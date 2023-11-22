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
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.example.queryCreation.QueryCreator;

public class Querier {
  public static final int TOP_DOCS_LIMIT = 1000;

  private static final String DOC_NO = "docno";
  private static final String TITLE  = "title";
  private static final String CONTENT  = "content";

  private final ArrayList<String> queries;

  private static IndexReader myDirectorReader;
  private final String DOC_NO = "docno";
  private final String TITLE = "title";
  private final String CONTENT = "content";


  /*
   * Code from this link:
   * https://github.com/taaanmay/CS7IS3-Assignment-2/blob/main/src/main/java/app/
   * QueryResolverWithExp.java
   */
  private static Query expandQuery(IndexSearcher searcher, Analyzer analyzer, Query queryContents, ScoreDoc[] hits,
                                   IndexReader reader) throws Exception {
    BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
    queryBuilder.add(queryContents, BooleanClause.Occur.SHOULD);
    TopDocs topDocs = searcher.search(queryContents, 4);

    for (ScoreDoc score : topDocs.scoreDocs) {
      Document hitDoc = reader.document(score.doc);
      String fieldText = hitDoc.getField("content").stringValue();
      String[] moreLikeThisField = {"content"};
      MoreLikeThisQuery
          expandedQueryMoreLikeThis = new MoreLikeThisQuery(fieldText, moreLikeThisField, analyzer,
          "content");
      Query expandedQuery = expandedQueryMoreLikeThis.rewrite(reader);
      queryBuilder.add(expandedQuery, BooleanClause.Occur.SHOULD);
    }
    return queryBuilder.build();
  }



  /**
   * Constructor for Querier.
   * @param pathToQueries Path to the queries to search.
   */
  public Querier(String pathToQueries) {
    // Read queries from topics.
    QueryCreator qc = new QueryCreator();
    this.queries = qc.createQueries();
  }

  /**
   * Queries the documents with the given analyzer and scorer.
   * @param indexDirectory Path to the index directory.
   * @param analyzer Analyzer to use for querying.
   * @param scorer Similarity to use for querying.
   * @throws Exception exception.
   */
  public void queryIndex(String indexDirectory, Analyzer analyzer, Similarity scorer) throws Exception {
    System.out.println("Querying index at '" + indexDirectory + "'...");
    String analyzerName = analyzer.getClass().getName()
            .substring(analyzer.getClass().getName().lastIndexOf('.') + 1);
    String scorerName = scorer.getClass().getName()
            .substring(scorer.getClass().getName().lastIndexOf('.') + 1);
    File fout = new File("./results/" + analyzerName + "-" + scorerName);
    FileOutputStream fos = new FileOutputStream(fout);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

    IndexSearcher searcher = createIndexSearcher(indexDirectory, scorer);

    // Search index with all queries and write results to file.
    int queryID = 401;
    for (String q : queries) {
      q = q.trim();

      // Adding query expansion here :proximity search
      ScoreDoc [] results = scoreDocumentsearch(q, searcher, analyzer);
      String queryResults = "";
      int rank = 1;

      for (int j = 0; j < results.length; j++) {
        ScoreDoc sd = results[j];
        // Output in trec_eval format - see http://www.rafaelglater.com/en/post/learn-how-to-use-trec_eval-to-evaluate-your-information-retrieval-system.
        // Format: query-id Q0 document-id rank score STANDARD
        queryResults += queryID + " Q0 " + (searcher.doc(sd.doc).get(DOC_NO)) + " "
            + rank + " " + sd.score + " " + analyzerName + "-" + scorerName + "\n";
        rank++;
      }
      bw.write(queryResults);
      queryID++;
    }
    bw.close();
    System.out.println("Queried index with " + analyzerName + " and " + scorerName + " results saved to '"  + fout.getPath() + "'.");
  }

  /**
   * Creates an IndexSearcher with the given scorer.
   * @param scorer Similarity to use for querying.
   * @throws IOException input-otput exception.
   */
  private static IndexSearcher createIndexSearcher(String indexDirectory, Similarity scorer) throws IOException {
    // Use file system directory to retrieve index.
    Directory dir = FSDirectory.open(Paths.get(indexDirectory));
    IndexReader reader = DirectoryReader.open(dir);
    myDirectorReader = reader;
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
  private static ScoreDoc[] scoreDocumentsearch(String term, IndexSearcher searcher,
                                                Analyzer analyzer) throws Exception {
    // Use specified analyzer to parse query.
    QueryParser qp = createQueryParser(analyzer);
    qp.setAllowLeadingWildcard(true);
    // Search both title and content fields for query term.
    // https://lucene.apache.org/core/2_9_4/queryparsersyntax.html.
    String queryTerm = "title:" + term + " OR content:" + term;
    // Search index for top results for query.
    Query query = qp.parse(queryTerm);

        /*
      Query expansion here
       */
    Query myqry = qp.parse(term);
    ScoreDoc [] myScoreDocs = {};
    Query expandedQuery = expandQuery(searcher, analyzer, myqry, myScoreDocs, myDirectorReader);

    myScoreDocs = searcher.search(expandedQuery, TOP_DOCS_LIMIT).scoreDocs;




    return myScoreDocs;
  }
}