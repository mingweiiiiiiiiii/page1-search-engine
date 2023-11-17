package com.example;

import com.example.indexing.indexingConstrution;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;



import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.nio.file.Paths;



import com.example.queryCreation.ProcessingFromTopicParser;
import java.nio.file.Path;
import java.util.ArrayList;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.similarities.BM25Similarity;

/**
 * SearchEngine indexes and searches documents.
 */
public class SearchEngine {

  private static String MYOUTPUT_FILE_DIRECTORY = "./output";
  private static final int NUMBER_DOCUMENT_QUERY = 1000;
  private final String DOC_NUMBE_MARCO = "docno";
  private static final String TITLE_MARCO  = "title";

  private   static final String  CONTENT_MARCO  = "content";
  public static final String FT_PATH = "./data/ft";
  public static final String LATIMES_PATH = "./data/latimes";
  public static final String FR_PATH = "./data/fr94";
  public static final  String DIRTORY_ADDRESS = "./dir";

  public SearchEngine() {
  }

  /**
   * Main method for SearchEngine.
   */
  public static <Document> void main(String[] args) throws IOException, ParseException {

    //indexing StandaradAnalyzer and BM25SIMIALRITY
    indexingConstrution myIndexCreation =  new indexingConstrution();
   // FinancialTimesReader.readDocuments(FT_PATH);
   // LosAngelesTimesReader.readDocuments(LATIMES_PATH);
   // FederalRegisterReader.readDocuments(FR_PATH);
    // NEXT step create query
    System.out.println("Finishing indexing ");


    Path myindexPath = Paths.get(DIRTORY_ADDRESS);
    Directory indexDir = FSDirectory.open(myindexPath);
    Directory mydircotry = FSDirectory.open(myindexPath);
    DirectoryReader mydirReader = DirectoryReader.open(mydircotry);
    IndexSearcher searcher = new IndexSearcher(mydirReader);
    //  FINISHING SEARCH CONFIG


    searcher.setSimilarity(new BM25Similarity());
    System.out.println("Current simialrity  is BM25");

    // Read query
    ProcessingFromTopicParser myqueryGeneration = new ProcessingFromTopicParser();
    myqueryGeneration.run();
    ArrayList<String> myQueryList = myqueryGeneration.getQueryList();


    QueryParser parser_single_mutiple = null;
    float weightForTittle = 0.2f;

    float weightForDescr =1.1f;
    Map<String, Float> bootssetting3 = new HashMap<>();
    bootssetting3.put(TITLE_MARCO,weightForTittle);
    bootssetting3.put(CONTENT_MARCO,weightForDescr);
    String []Feature3_CONSTANT_TITLE_DESC_FEATURE =new String []{TITLE_MARCO,CONTENT_MARCO};
    // String []Feature6_CONSTANT_BIBI_DESCI_FEATURE =new String []{constant_bibi,constant_descir};
    // String []Feature7_CONSTANT_ALL_THREE_FEATURE =new String []{constant_title,constant_bibi,constant_descir};
    Analyzer  myAnalyzer =  new StandardAnalyzer();
    parser_single_mutiple = new MultiFieldQueryParser(Feature3_CONSTANT_TITLE_DESC_FEATURE, myAnalyzer,bootssetting3);

    parser_single_mutiple.setAllowLeadingWildcard(true);

    List<String> results = new ArrayList<>();
    int initlisedTopicNumber = 400;

    String analyzerTag = "st";
    String simialirtyMatchingAlgorithmTag = "bm25";
    String featureTag = "title_text";
    String hyphenS = "-";
    String saveFileAddress = MYOUTPUT_FILE_DIRECTORY+analyzerTag+hyphenS+simialirtyMatchingAlgorithmTag+hyphenS+featureTag+".txt";
    FileWriter fileWriter = new FileWriter(saveFileAddress);
    for (int i = 0; i < myQueryList.size(); i++) {
      String querytempDescription = myQueryList.get(i).trim();
      Query myqry = parser_single_mutiple.parse(querytempDescription);
      TopDocs topDocs = searcher.search(myqry, NUMBER_DOCUMENT_QUERY);

      ScoreDoc[] mydocHit =topDocs.scoreDocs;

      final String DOC_NUMBE_MARCO = "docno";

      for (int j = 0; j < mydocHit.length; j ++ ) {
        ScoreDoc hit = mydocHit[j];

        String searchDOCNO = searcher.doc(hit.doc).get(DOC_NUMBE_MARCO);
        // query-id Q0 document-id rank score STANDARD
        initlisedTopicNumber++;
        int rank = j + 1;
       // query-id Q0 document-id rank score STANDARD
        String formatOutput = initlisedTopicNumber + " Q0 " +searchDOCNO + " " + rank + " " + hit.score + " STANDARD";
        System.out.println(formatOutput);
        fileWriter.write(formatOutput + System.lineSeparator());
      }


    }

    fileWriter.close();

  }

}

