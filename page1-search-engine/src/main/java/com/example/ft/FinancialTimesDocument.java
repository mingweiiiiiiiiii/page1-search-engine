package com.example.ft;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 * Financial Times (FT) document.
 */
public class FinancialTimesDocument {
  private final String number;
  private final String profile;
  private final String date;
  private final String headline;
  private final String byline;
  private final String text;
  private final String publisher;
  private final String page;

  /**
   * Constructor for Financial Times (FT) document.
   *
   * @param number document number.
   * @param profile document profile.
   * @param date document date.
   * @param headline document headline.
   * @param byline document byline.
   * @param text document text.
   * @param publisher document publisher.
   * @param page document page.
   */
  public FinancialTimesDocument(String number, String profile, String date, String headline,
      String byline, String text, String publisher, String page) {
    this.number = number;
    this.profile = profile;
    this.date = date;
    this.headline = headline;
    this.byline = byline;
    this.text = text;
    this.publisher = publisher;
    this.page = page;
  }

  /**
   * Create a Lucene document from the FTDocument.
   *
   * @return a Lucene document.
   */
  public Document createLuceneDocument() {
    Document doc = new Document();
    doc.add(new StringField("number", number, Field.Store.YES));
    doc.add(new StringField("profile", profile, Field.Store.YES));
    doc.add(new StringField("date", date, Field.Store.YES));
    doc.add(new TextField("headline", headline, Field.Store.YES));
    doc.add(new TextField("byline", byline, Field.Store.YES));
    doc.add(new TextField("text", text, Field.Store.YES));
    doc.add(new StringField("publisher", publisher, Field.Store.YES));
    doc.add(new StringField("page", page, Field.Store.YES));
    return doc;
  }

  /**
   * Returns a string representation of the FTDocument.
   *
   * @return a string representation of the FTDocument.
   */
  public String toString() {
    return "Number: " + number + "\nProfile " + profile + "\nDate: " + date
        + "\nHeadline: " + headline + "\nByline: " + byline + "\nText: " + text
        + "\nPublisher: " + publisher + "\nPage: " + page;
  }
}
