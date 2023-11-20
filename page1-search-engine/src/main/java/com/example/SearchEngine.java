package com.example;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;

/**
 * SearchEngine indexes and searches documents.
 */
public class SearchEngine {
  private static final String INDEX_DIRECTORY = "./index";
  /**
   * Main method for SearchEngine.
   */
  public static void main(String[] args) throws Exception {
    Analyzer analyzer = new StandardAnalyzer();
    Similarity scorer = new BM25Similarity();
    Indexer indexer = new Indexer();
    indexer.indexDocuments(INDEX_DIRECTORY, analyzer, scorer);

    // DELETE write lock file.
    String fileName = "write.lock";
    File lockFile = new File(INDEX_DIRECTORY, fileName);
    if (lockFile.exists()) {
      boolean isDeleted = lockFile.delete();
      if (isDeleted) {
        System.out.println(fileName + " has been successfully deleted.");
      } else {
        System.out.println("Could not delete " + fileName + ". Please check file permissions.");
      }
    } else {
      System.out.println(fileName + " does not exist in the directory " + INDEX_DIRECTORY);
    }

    Querier querier = new Querier("./topics/topics.txt");
    querier.queryIndex(INDEX_DIRECTORY, analyzer, scorer);
  }
}
