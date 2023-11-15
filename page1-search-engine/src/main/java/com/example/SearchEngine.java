package com.example;

import com.example.ft.FinancialTimesReader;
import com.example.latimes.LosAngelesTimesReader;

/**
 * SearchEngine indexes and searches documents.
 */
public class SearchEngine {
  public static final String FT_PATH = "./data/ft";
  public static final String LATIMES_PATH = "./data/latimes";

  /**
   * Main method for SearchEngine.
   */
  public static void main(String[] args) throws Exception {
    FinancialTimesReader.readDocuments(FT_PATH);
    LosAngelesTimesReader.readDocuments(LATIMES_PATH);
  }
}
