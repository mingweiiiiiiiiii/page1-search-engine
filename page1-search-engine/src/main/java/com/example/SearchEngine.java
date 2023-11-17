package com.example;

import com.example.fr.FederalRegisterReader;
import com.example.ft.FinancialTimesReader;
import com.example.indexing.indexingConstrution;
import com.example.latimes.LosAngelesTimesReader;
import java.io.IOException;

import com.example.queryCreation.ProcessingFromTopicParser;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * SearchEngine indexes and searches documents.
 */
public class SearchEngine {
  private static final int NUMBER_DOCUMENT_QUERY = 1000;
  private final String DOC_NUMBE_MARCO = "docno";
  private final String TITLE_MARCO  = "title";

  private final String CONTENT_MARCO  = "content";
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
    QueryParser qp = new QueryParser("content", new StandardAnalyzer());


    for (int i = 0; i < myQueryList.size(); i++) {
      String querytempDescription = myQueryList.get(i).trim();
      Query myqry = parser_single_mutiple.parse(querytempDescription);
      TopDocs topDocs = searcher.search(myqry, NUMBER_DOCUMENT_QUERY);

      ScoreDoc[] mydocHit =topDocs.scoreDocs;

      for (ScoreDoc scoreDoc : mydocHit) {

        int docID = scoreDoc.doc;
        Document doc = (Document) searcher.doc(docID);
       //

      }





    }

  }

}

