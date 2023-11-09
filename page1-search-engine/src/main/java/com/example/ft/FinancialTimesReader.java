package com.example.ft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Financial Times (FT) reader.
 */
public class FinancialTimesReader {
  // HTML tags.
  private static final String DOC = "DOC";
  private static final String DOCNO = "DOCNO";
  private static final String PROFILE = "PROFILE";
  private static final String DATE = "DATE";
  private static final String HEADLINE = "HEADLINE";
  private static final String BYLINE = "BYLINE";
  private static final String TEXT = "TEXT";
  private static final String PUBLISHER = "PUB";
  private static final String PAGE = "PAGE";

  /**
   * Reads all the FT documents.
   *
   * @param topLevelDirName Top level ft directory name.
   * @return ArrayList of FT documents.
   */
  public static ArrayList<FinancialTimesDocument> readDocuments(String topLevelDirName) {
    System.out.println("Reading finanical times documents in directory " + topLevelDirName + "...");
    ArrayList<FinancialTimesDocument> documents = new ArrayList<FinancialTimesDocument>();

    // Read files in all the directories in the top level directory.
    File folder = new File(topLevelDirName);
    File[] listOfDirs = folder.listFiles();
    for (File dir : listOfDirs) {
      if (dir.isDirectory()) {
        documents.addAll(readDocumentsInDirectory(dir.getAbsolutePath()));
      }
    }
    System.out.println("Read " + documents.size() + " documents in directory " 
        + folder.getName() + ".");
    return documents;
  }

  /**
   * Reads all the FT documents in a directory.
   *
   * @param directoryName Directory name.
   * @return ArrayList of FT documents.
   */
  private static ArrayList<FinancialTimesDocument> readDocumentsInDirectory(String directoryName) {
    ArrayList<FinancialTimesDocument> documents = new ArrayList<FinancialTimesDocument>();

    // Read files in the directory.
    File folder = new File(directoryName);
    File[] listOfFiles = folder.listFiles();
    for (File file : listOfFiles) {
      if (file.isFile()) {
        documents.addAll(readDocumentsInFile(file.getAbsolutePath()));
      }
    }
    System.out.println("Read " + documents.size() + " documents from directory " 
        + folder.getName() + ".");
    return documents;
  }

  /**
   * Reads all the FT documents in a file.
   *
   * @param filename File name.
   * @return ArrayList of FT documents.
   */
  private static ArrayList<FinancialTimesDocument> readDocumentsInFile(String filename) {
    ArrayList<FinancialTimesDocument> documents = new ArrayList<FinancialTimesDocument>();

    try {
      File file = new File(filename);
      Document htmlDoc = Jsoup.parse(file);
      Elements htmlDocs = htmlDoc.select(DOC);
      for (Element doc : htmlDocs) {
        // Extract the document information into FTDocument.
        Elements e = doc.getElementsByTag(DOCNO);
        final String docNo = e.text().trim();
        e = doc.getElementsByTag(PROFILE);
        final String profile = e.text().trim();
        e = doc.getElementsByTag(DATE);
        final String date = e.text().trim();
        e = doc.getElementsByTag(HEADLINE);
        final String headline = e.text().trim();
        e = doc.getElementsByTag(BYLINE);
        final String byline = e.text().trim();
        e = doc.getElementsByTag(TEXT);
        final String text = e.text().trim();
        e = doc.getElementsByTag(PUBLISHER);
        final String publisher = e.text().trim();
        e = doc.getElementsByTag(PAGE);
        final String page = e.text().trim();
        documents.add(new FinancialTimesDocument(docNo, profile, date, headline,
            byline, text, publisher, page));
      }
    } catch (FileNotFoundException e) {
      System.out.println("Could not find file " + filename);
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Could not parse file " + filename);
      e.printStackTrace();
    }
    return documents;
  }
}