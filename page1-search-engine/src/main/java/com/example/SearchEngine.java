package com.example;

import com.example.fr.FederalRegisterReader;
import com.example.ft.FinancialTimesReader;
import com.example.latimes.LosAngelesTimesReader;

/**
 * SearchEngine indexes and searches documents.
 */
public class SearchEngine {
  public static final String FT_PATH = "./data/ft";
  public static final String LATIMES_PATH = "./data/latimes";
  public static final String FR_PATH = "./data/fr94";

  /**
   * Main method for SearchEngine.
   */
  public static void main(String[] args) throws Exception {
    FinancialTimesReader.readDocuments(FT_PATH);
    LosAngelesTimesReader.readDocuments(LATIMES_PATH);
    FederalRegisterReader.readDocuments(FR_PATH);
  }
}
