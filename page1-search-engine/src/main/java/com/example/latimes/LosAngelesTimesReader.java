package com.example.latimes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Los Angeles (LA) Times reader.
 */
public class LosAngelesTimesReader {
  private static final HashSet<String> FILES_TO_EXCLUDE = new HashSet<String>(
      Arrays.asList("readchg.txt", "readmela.txt"));
  // HTML tags.
  private static final String DOC = "DOC";
  private static final String DOCNO = "DOCNO";
  private static final String DOCID = "DOCID";
  private static final String DATE = "DATE";
  private static final String SECTION = "SECTION";
  private static final String LENGTH = "LENGTH";
  private static final String HEADLINE = "HEADLINE";
  private static final String BYLINE = "BYLINE";
  private static final String TEXT = "TEXT";
  private static final String GRAPHIC = "GRAPHIC";
  private static final String TYPE = "TYPE";

  /**
   * Read all the LA Times documents.
   *
   * @param directory latimes directory name.
   * @return ArrayList of LA Times documents.
   */
  public static ArrayList<LosAngelesTimesDocument> readDocuments(String directory) {
    System.out.println("Reading la times documents in directory " + directory + "...");
    ArrayList<LosAngelesTimesDocument> documents = new ArrayList<LosAngelesTimesDocument>();

    // Read files in all the directories in the top level directory.
    File dir = new File(directory);
    File[] files = dir.listFiles();
    for (File file : files) {
      if (!FILES_TO_EXCLUDE.contains(file.getName())) {
        documents.addAll(readDocumentsInFile(file.getAbsolutePath()));
      }
    }
    System.out.println("Read " + documents.size() + " documents in directory " 
        + dir.getName() + ".");
    return documents;
  }

  /**
   * Read all the LA Times documents in a file.
   *
   * @param filename File name.
   * @return ArrayList of LA Times documents.
   */
  private static ArrayList<LosAngelesTimesDocument> readDocumentsInFile(String filename) {
    ArrayList<LosAngelesTimesDocument> documents = new ArrayList<LosAngelesTimesDocument>();

    try {
      File file = new File(filename);
      Document htmlDoc = Jsoup.parse(file);
      Elements htmlDocs = htmlDoc.select(DOC);
      for (Element doc : htmlDocs) {
        // Extract the document information into LosAngelesTimesDocument.
        Elements e = doc.getElementsByTag(DOCNO);
        final String docNo = e.text().trim();
        e = doc.getElementsByTag(DOCID);
        final String docId = e.text().trim();
        e = doc.getElementsByTag(DATE);
        final String date = e.text().trim();
        e = doc.getElementsByTag(SECTION);
        final String section = e.text().trim();
        e = doc.getElementsByTag(LENGTH);
        final String length = e.text().trim();
        e = doc.getElementsByTag(HEADLINE);
        final String headline = e.text().trim();
        e = doc.getElementsByTag(BYLINE);
        final String byline = e.text().trim();
        e = doc.getElementsByTag(TEXT);
        final String text = e.text().trim();
        e = doc.getElementsByTag(GRAPHIC);
        final String graphic = e.text().trim();
        e = doc.getElementsByTag(TYPE);
        final String type = e.text().trim();
        documents.add(new LosAngelesTimesDocument(docNo, docId, date, section, length, headline,
            byline, text, graphic, type));
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