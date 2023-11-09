package com.example;

import com.example.ft.FinancialTimesReader;

/**
 * SearchEngine indexes and searches documents.
 */
public class SearchEngine {
  public static final String FT_PATH = "./data/ft";

  /**
   * Main method for SearchEngine.
   */
  public static void main(String[] args) throws Exception {
    FinancialTimesReader.readDocuments(FT_PATH);
  }
}
