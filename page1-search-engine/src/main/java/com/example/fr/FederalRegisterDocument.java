package com.example.fr;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 * Financial Register (FR) document.
 */
public class FederalRegisterDocument {
  public String getNumber() {
    return number;
  }

  public String getParent() {
    return parent;
  }

  public String getText() {
    return text;
  }

  public String getUsDept() {
    return usDept;
  }

  public String getAgency() {
    return agency;
  }

  public String getUsBureau() {
    return usBureau;
  }

  public String getTitle() {
    return title;
  }

  public String getAddress() {
    return address;
  }

  public String getFurther() {
    return further;
  }

  public String getSummary() {
    return summary;
  }

  public String getAction() {
    return action;
  }

  public String getSigner() {
    return signer;
  }

  public String getSignJob() {
    return signJob;
  }

  public String getSupplem() {
    return supplem;
  }

  public String getBilling() {
    return billing;
  }

  public String getFrFiling() {
    return frFiling;
  }

  public String getDate() {
    return date;
  }

  public String getCfrNo() {
    return cfrNo;
  }

  public String getRindock() {
    return rindock;
  }

  public String getTable() {
    return table;
  }

  public String getFootNote() {
    return footNote;
  }

  public String getFootCite() {
    return footCite;
  }

  public String getFootName() {
    return footName;
  }

  private final String number;
  private final String parent;
  private final String text;

  // Useful for retreival.
  private final String usDept;
  private final String agency;
  private final String usBureau;
  private final String title;
  private final String address;
  private final String further;
  private final String summary;
  private final String action;
  private final String signer;
  private final String signJob;
  private final String supplem;
  private final String billing;
  private final String frFiling;
  private final String date;
  private final String cfrNo;
  private final String rindock;

  // Optionally useful for retreival.
  private final String table;
  private final String footNote;
  private final String footCite;
  private final String footName;

  /** 
   * Constructor for Federal Register (FR) document.
   *
   * @param number document number.
   * @param parent document parent.
   * @param text document text.
   * @param usDept document usDept.
   * @param agency document agency.
   * @param usBureau document usBureau.
   * @param title document title.
   * @param address document address.
   * @param further document further.
   * @param summary document summary.
   * @param action document action.
   * @param signer document signer.
   * @param signJob document signJob.
   * @param supplem document supplem.
   * @param billing document billing.
   * @param frFiling document frFiling.
   * @param date document date.
   * @param cfrNo document cfrNo.
   * @param rindock document rindock.
   * @param table document table.
   * @param footNote document footNote.
   * @param footCite document footCite.
   * @param footName document footName.
   */
  public FederalRegisterDocument(String number, String parent, String text, String usDept,
      String agency, String usBureau, String title, String address, String further,
      String summary, String action, String signer, String signJob, String supplem,
      String billing, String frFiling, String date, String cfrNo, String rindock, String table,
      String footNote, String footCite, String footName) {
    this.number = number;
    this.parent = parent;
    this.text = text;
    this.usDept = usDept;
    this.agency = agency;
    this.usBureau = usBureau;
    this.title = title;
    this.address = address;
    this.further = further;
    this.summary = summary;
    this.action = action;
    this.signer = signer;
    this.signJob = signJob;
    this.supplem = supplem;
    this.billing = billing;
    this.frFiling = frFiling;
    this.date = date;
    this.cfrNo = cfrNo;
    this.rindock = rindock;
    this.table = table;
    this.footNote = footNote;
    this.footCite = footCite;
    this.footName = footName;
  }

  /**
   * Create a Lucene document from the FederalRegisterDocument.
   *
   * @return a Lucene document.
   */
  public Document createLuceneDocument() {
    Document doc = new Document();
    doc.add(new StringField("number", number, Field.Store.YES));
    doc.add(new StringField("parent", parent, Field.Store.YES));
    doc.add(new TextField("text", text, Field.Store.YES));
    doc.add(new StringField("usDept", usDept, Field.Store.YES));
    doc.add(new StringField("agency", agency, Field.Store.YES));
    doc.add(new StringField("usBureau", usBureau, Field.Store.YES));
    doc.add(new StringField("title", title, Field.Store.YES));
    doc.add(new StringField("address", address, Field.Store.YES));
    doc.add(new StringField("further", further, Field.Store.YES));
    doc.add(new StringField("summary", summary, Field.Store.YES));
    doc.add(new StringField("action", action, Field.Store.YES));
    doc.add(new StringField("signer", signer, Field.Store.YES));
    doc.add(new StringField("signJob", signJob, Field.Store.YES));
    doc.add(new StringField("supplem", supplem, Field.Store.YES));
    doc.add(new StringField("billing", billing, Field.Store.YES));
    doc.add(new StringField("frFiling", frFiling, Field.Store.YES));
    doc.add(new StringField("date", date, Field.Store.YES));
    doc.add(new StringField("cfrNo", cfrNo, Field.Store.YES));
    doc.add(new StringField("rindock", rindock, Field.Store.YES));
    doc.add(new StringField("table", table, Field.Store.YES));
    doc.add(new StringField("footNote", footNote, Field.Store.YES));
    doc.add(new StringField("footCite", footCite, Field.Store.YES));
    doc.add(new StringField("footName", footName, Field.Store.YES));
    return doc;
  }

  /**
   * Returns a string representation of the FederalRegisterDocument.
   *
   * @return a string representation of the FederalRegisterDocument.
   */
  public String toString() {
    return "Number:" + number + "\nParent:" + parent + "\nText:" + text + "\nUsDept:" + usDept
        + "\nAgency:" + agency + "\nUsBureau:" + usBureau + "\nTitle:" + title + "\nAddress:"
        + address + "\nFurther:" + further + "\nSummary:" + summary + "\nAction:" + action
        + "\nSigner:" + signer + "\nSignJob:" + signJob + "\nSupplem:" + supplem + "\nBilling:"
        + billing + "\nFrFiling:" + frFiling + "\nDate:" + date + "\nCfrNo:" + cfrNo + "\nRindock:"
        + rindock + "\nTable:" + table + "\nFootNote:" + footNote + "\nFootCite:" + footCite
        + "\nFootName:" + footName;
  }
}
