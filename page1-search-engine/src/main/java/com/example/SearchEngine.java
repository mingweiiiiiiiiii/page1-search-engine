package com.example;

import com.example.Indexer;
import java.io.File;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.BM25Similarity;

/**
 * SearchEngine indexes and searches documents.
 */
public class SearchEngine {
  /**
   * Main method for SearchEngine.
   */
  public static void main(String[] args) throws Exception {
    System.out.println("Reading corpora and indexing with StandardAnalyzer and " 
        + "BM25 scoring method...");
    Indexer myIndexCreation =  new Indexer();
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
    Querier querier = new Querier("./topics/topics.txt");
    querier.queryDocuments(new StandardAnalyzer(), new BM25Similarity());
  }
}
