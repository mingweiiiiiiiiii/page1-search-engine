package com.example.latimes;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 * Los Angeles (LA) Times document.
 */
public class LosAngelesTimesDocument {
  private final String number;
  private final String id;
  private final String date;
  private final String section;
  private final String length;
  private final String headline;
  private final String byline;
  private final String text;
  private final String graphic;
  private final String type;

  /**
   * Constructor for Los Angeles (LA) Times Document.
   *
   * @param number document number.
   * @param id document id.
   * @param date document date.
   * @param section document section.
   * @param length document length.
   * @param headline document headline.
   * @param byline document byline.
   * @param text document text.
   * @param graphic document graphic.
   * @param type document type.
   */
  public LosAngelesTimesDocument(String number, String id, String date,
      String section, String length, String headline, String byline,
      String text, String graphic, String type) {
    this.number = number;
    this.id = id;
    this.date = date;
    this.section = section;
    this.length = length;
    this.headline = headline;
    this.byline = byline;
    this.text = text;
    this.graphic = graphic;
    this.type = type;
  }

  /**
   * Create a Lucene document from the LosAngelesTimesDocument.
   *
   * @return a Lucene document.
   */
  public Document createLuceneDocument() {
    Document doc = new Document();
    doc.add(new StringField("number", number, Field.Store.YES));
    doc.add(new StringField("id", id, Field.Store.YES));
    doc.add(new StringField("date", date, Field.Store.YES));
    doc.add(new StringField("section", section, Field.Store.YES));
    doc.add(new StringField("length", length, Field.Store.YES));
    doc.add(new TextField("headline", headline, Field.Store.YES));
    doc.add(new TextField("byline", byline, Field.Store.YES));
    doc.add(new TextField("text", text, Field.Store.YES));
    doc.add(new StringField("graphic", graphic, Field.Store.YES));
    doc.add(new StringField("type", type, Field.Store.YES));
    return doc;
  }

  /**
   * Returns a string representation of the LosAngelesTimesDocument.
   *
   * @return a string representation of the LosAngelesTimesDocument.
   */
  public String toString() {
    return "Number: " + number + "\n" + "ID: " + id + "\n" + "Date: " + date
        + "\n" + "Section: " + section + "\n" + "Length: " + length + "\n"
        + "Headline: " + headline + "\n" + "Byline: " + byline + "\n"
        + "Text: " + text + "\n" + "Graphic: " + graphic + "\n" + "Type: "
        + type + "\n";
  }
}
