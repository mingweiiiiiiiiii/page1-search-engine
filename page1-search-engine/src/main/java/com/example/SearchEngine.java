package com.example;

import com.example.indexing.indexingConstrution;
import com.example.queryCreation.ProcessingFromTopicParser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * SearchEngine indexes and searches documents.
 */
public class SearchEngine {
  private static String MYOUTPUT_FILE_DIRECTORY = "./output";
  private static final int NUMBER_DOCUMENT_QUERY = 1000;
  private final String DOC_NUMBE_MARCO = "docno";
  private static final String TITLE_MARCO  = "title";
  private static final String  CONTENT_MARCO  = "content";
  public static final String FT_PATH = "./data/ft";
  public static final String LATIMES_PATH = "./data/latimes";
  public static final String FR_PATH = "./data/fr94";
  public static final  String DIRTORY_ADDRESS = "./dir";

  /**
   * Main method for SearchEngine.
   */
  public static void main(String[] args) throws IOException, ParseException {
    //indexing StandaradAnalyzer and BM25SIMIALRITY
    System.out.println("Reading corpora and indexing with StandardAnalyzer and " 
        + "BM25 scoring method...");
    indexingConstrution myIndexCreation =  new indexingConstrution();
    System.out.println("Finishing creating index.");

    // DELETE write lock file.
    String directoryPath = "./dir";
    String fileName = "write.lock";
    File lockFile = new File(directoryPath, fileName);
    if (lockFile.exists()) {
      boolean isDeleted = lockFile.delete();
      if (isDeleted) {
        System.out.println(fileName + " has been successfully deleted.");
      } else {
        System.out.println("Could not delete " + fileName + ". Please check file permissions.");
      }
    } else {
      System.out.println(fileName + " does not exist in the directory " + directoryPath);
    }

    System.out.println("Start searching...");
    Directory dirr = FSDirectory.open(Paths.get(DIRTORY_ADDRESS));
    DirectoryReader dirReader = DirectoryReader.open(dirr);
    IndexSearcher searcher = new IndexSearcher(dirReader);
    searcher.setSimilarity(new BM25Similarity());

    // Read queries from topics.
    ProcessingFromTopicParser myqueryGeneration = new ProcessingFromTopicParser();
    myqueryGeneration.run();
    ArrayList<String> myQueryList = myqueryGeneration.getQueryList();

    // Create query parser.
    float weightForTitle = 0.2f;
    float weightForDesc =1.1f;
    Map<String, Float> boosts = new HashMap<>();
    boosts.put(TITLE_MARCO,weightForTitle);
    boosts.put(CONTENT_MARCO,weightForDesc);
    String []Feature3_CONSTANT_TITLE_DESC_FEATURE =new String []{TITLE_MARCO,CONTENT_MARCO};
    Analyzer  standardAnalyzer =  new StandardAnalyzer();
    QueryParser parser_single_mutiple = new MultiFieldQueryParser(Feature3_CONSTANT_TITLE_DESC_FEATURE, standardAnalyzer,boosts);
    parser_single_mutiple.setAllowLeadingWildcard(true);

    int initlisedTopicNumber = 401;

    String analyzerTag = "st";
    String similarityMatchingAlgorithmTag = "bm25";
    String featureTag = "title_text";
    String saveFileAddress = MYOUTPUT_FILE_DIRECTORY + analyzerTag
    + "-" + similarityMatchingAlgorithmTag + "-" + featureTag + ".txt";
    FileWriter fileWriter = new FileWriter(saveFileAddress);
    for (int i = 0; i < myQueryList.size(); i++) {
      String querytempDescription = myQueryList.get(i).trim();
      Query myqry = parser_single_mutiple.parse(querytempDescription);
      TopDocs topDocs = searcher.search(myqry, NUMBER_DOCUMENT_QUERY);
      ScoreDoc[] mydocHit =topDocs.scoreDocs;
      final String DOC_NUMBE_MARCO = "docno";
      int RankLable = 1;
      if(RankLable % 100 == 0) {
        RankLable = 1;
      }
      for (int j = 0; j < mydocHit.length; j ++ ) {
        ScoreDoc hit = mydocHit[j];
        String searchDOCNO = searcher.doc(hit.doc).get(DOC_NUMBE_MARCO);        
        // query-id Q0 document-id rank score STANDARD
        String formatOutput = initlisedTopicNumber + " Q0 " +searchDOCNO + " " + RankLable + " " + hit.score + " STANDARD";
        System.out.println(formatOutput);
        RankLable++;
        fileWriter.write(formatOutput + System.lineSeparator());
      }
      initlisedTopicNumber++;
    }
    fileWriter.close();
  }
}
