package com.example.fr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Federal Register (FR) reader.
 */
public class FederalRegisterReader {
  // HTML tags.
  private static final String DOC = "DOC";
  private static final String DOCNO = "DOCNO";
  private static final String PARENT = "PARENT";
  private static final String TEXT = "TEXT";
  private static final String USDEPT = "USDEPT";
  private static final String AGENCY = "AGENCY";
  private static final String USBUREAU = "USBUREAU";
  private static final String TITLE = "DOCTITLE";
  private static final String ADDRESS = "ADDRESS";
  private static final String FURTHER = "FURTHER";
  private static final String SUMMARY = "SUMMARY";
  private static final String ACTION = "ACTION";
  private static final String SIGNER = "SIGNER";
  private static final String SIGNJOB = "SIGNJOB";
  private static final String SUPPLEM = "SUPPLEM";
  private static final String BILLING = "BILLING";
  private static final String FRFILING = "FRFILING";
  private static final String DATE = "DATE";
  private static final String CFRNO = "CFRNO";
  private static final String RINDOCK = "RINDOCK";
  private static final String TABLE = "TABLE";
  private static final String FOOTNOTE = "FOOTNOTE";
  private static final String FOOTCITE = "FOOTCITE";
  private static final String FOOTNAME = "FOOTNAME";

  /**
   * Reads all the Federal Register (FR) documents.
   *
   * @param topLevelDirName Top level FR directory name.
   * @return ArrayList of FR documents.
   */
  public static ArrayList<FederalRegisterDocument> readDocuments(String topLevelDirName) {
    System.out.println("Reading federal register documents in directory " 
        + topLevelDirName + "...");
    ArrayList<FederalRegisterDocument> documents = new ArrayList<FederalRegisterDocument>();

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
   * Reads all the FR documents in a directory.
   *
   * @param directoryName Directory name.
   * @return ArrayList of FR documents.
   */
  private static ArrayList<FederalRegisterDocument> readDocumentsInDirectory(String directoryName) {
    ArrayList<FederalRegisterDocument> documents = new ArrayList<FederalRegisterDocument>();

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
   * Reads all the FR documents in a file.
   *
   * @param filename File name.
   * @return ArrayList of FR documents.
   */
  private static ArrayList<FederalRegisterDocument> readDocumentsInFile(String filename) {
    ArrayList<FederalRegisterDocument> documents = new ArrayList<FederalRegisterDocument>();

    try {
      File file = new File(filename);
      Document htmlDoc = Jsoup.parse(file);
      Elements htmlDocs = htmlDoc.select(DOC);
      for (Element doc : htmlDocs) {
        // Extract the document information into FTDocument.
        Elements e = doc.getElementsByTag(DOCNO);
        final String number = e.text().trim();
        e = doc.getElementsByTag(PARENT);
        final String parent = e.text().trim();
        e = doc.getElementsByTag(TEXT);
        final String text = e.text().trim();
        e = doc.getElementsByTag(USDEPT);
        final String usDept = e.text().trim();
        e = doc.getElementsByTag(AGENCY);
        final String agency = e.text().trim();
        e = doc.getElementsByTag(USBUREAU);
        final String usBureau = e.text().trim();
        e = doc.getElementsByTag(TITLE);
        final String title = e.text().trim();
        e = doc.getElementsByTag(ADDRESS);
        final String address = e.text().trim();
        e = doc.getElementsByTag(FURTHER);
        final String further = e.text().trim();
        e = doc.getElementsByTag(SUMMARY);
        final String summary = e.text().trim();
        e = doc.getElementsByTag(ACTION);
        final String action = e.text().trim();
        e = doc.getElementsByTag(SIGNER);
        final String signer = e.text().trim();
        e = doc.getElementsByTag(SIGNJOB);
        final String signJob = e.text().trim();
        e = doc.getElementsByTag(SUPPLEM);
        final String supplem = e.text().trim();
        e = doc.getElementsByTag(BILLING);
        final String billing = e.text().trim();
        e = doc.getElementsByTag(FRFILING);
        final String frFiling = e.text().trim();
        e = doc.getElementsByTag(DATE);
        final String date = e.text().trim();
        e = doc.getElementsByTag(CFRNO);
        final String cfrNo = e.text().trim();
        e = doc.getElementsByTag(RINDOCK);
        final String rindock = e.text().trim();
        e = doc.getElementsByTag(TABLE);
        final String table = e.text().trim();
        e = doc.getElementsByTag(FOOTNOTE);
        final String footNote = e.text().trim();
        e = doc.getElementsByTag(FOOTCITE);
        final String footCite = e.text().trim();
        e = doc.getElementsByTag(FOOTNAME);
        final String footName = e.text().trim();
        documents.add(new FederalRegisterDocument(number, parent, text, usDept, agency, usBureau,
            title, address, further, summary, action, signer, signJob, supplem, billing, frFiling,
            date, cfrNo, rindock, table, footNote, footCite, footName));
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