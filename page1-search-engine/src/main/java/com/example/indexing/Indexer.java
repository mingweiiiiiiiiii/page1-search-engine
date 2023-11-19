package com.example.indexing;

import com.example.fbisparser.Fbis95Structure;
import com.example.fbisparser.Fbis96Parser;
import com.example.fr.FederalRegisterDocument;
import com.example.fr.FederalRegisterReader;
import com.example.ft.FinancialTimesDocument;
import com.example.ft.FinancialTimesReader;
import com.example.latimes.LosAngelesTimesDocument;
import com.example.latimes.LosAngelesTimesReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {
  public static final String FT_PATH = "./data/ft";
  public static final String LATIMES_PATH = "./data/latimes";
  public static final String FR_PATH = "./data/fr94";
  public static final String DIRTORY_ADDRESS = "./dir";

  private final String DOC_NO = "docno";
  private final String TITLE = "title";
  private final String CONTENT = "content";

  private ArrayList<Document> allLuceneDocuments;

  public Indexer() throws IOException {
    this.allLuceneDocuments = new ArrayList<>();
    this.createDocumentSameFieldForAllCorpus();
    this.constructIndex();
  }

  public void createDocumentSameFieldForAllCorpus() throws IOException {
    // FBIS.
    Fbis96Parser myFbisParser = new Fbis96Parser();
    myFbisParser.loadFiles();
    ArrayList<Fbis95Structure> fbisDocuments = myFbisParser.getMyFbisContainer();
    for (Fbis95Structure d : fbisDocuments) {
      Document doc = new Document();
      doc.add(new StringField(DOC_NO, d.getDocno(), Field.Store.YES));
      doc.add(new TextField(TITLE, d.getTitle(), Field.Store.YES));
      doc.add(new TextField(CONTENT, d.getText(), Field.Store.YES));
      this.allLuceneDocuments.add(doc);
    }
    // Financial Times.
    ArrayList<FinancialTimesDocument> ftDocuments = FinancialTimesReader.readDocuments(FT_PATH);
    for (FinancialTimesDocument d : ftDocuments) {
      Document doc = new Document();
      doc.add(new StringField(DOC_NO, d.getNumber(), Field.Store.YES));
      doc.add(new TextField(TITLE, d.getHeadline(), Field.Store.YES));
      doc.add(new TextField(CONTENT, d.getText(), Field.Store.YES));
      this.allLuceneDocuments.add(doc);
    }
    // Federal Register.
    ArrayList<FederalRegisterDocument> frDocuments = FederalRegisterReader.readDocuments(FR_PATH);
    for (FederalRegisterDocument d : frDocuments) {
      Document doc = new Document();
      doc.add(new StringField(DOC_NO, d.getNumber(), Field.Store.YES));
      doc.add(new TextField(TITLE, d.getTitle(), Field.Store.YES));
      doc.add(new TextField(CONTENT, d.getText(), Field.Store.YES));
      this.allLuceneDocuments.add(doc);
    }
    // // LA Times.
    ArrayList<LosAngelesTimesDocument> laDocuments = LosAngelesTimesReader.readDocuments(LATIMES_PATH);
    for (LosAngelesTimesDocument d : laDocuments) {
      Document doc = new Document();
      doc.add(new StringField(DOC_NO, d.getNumber(), Field.Store.YES));
      doc.add(new TextField(TITLE, d.getHeadline(), Field.Store.YES));
      doc.add(new TextField(CONTENT, d.getText(), Field.Store.YES));
      this.allLuceneDocuments.add(doc);
    }
  }

  public void constructIndex() throws IOException {
    System.out.println("Starting indexing ");
    Analyzer mAnalyzer = new StandardAnalyzer();
    IndexWriterConfig myWriterconfig = new IndexWriterConfig(mAnalyzer);
    myWriterconfig.setMaxBufferedDocs(100000);
    myWriterconfig.setSimilarity(new BM25Similarity());

    Path myindexPath = Paths.get(DIRTORY_ADDRESS);
    Directory indexDir = FSDirectory.open(myindexPath);
    // Overwrite seveearl times
    myWriterconfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    IndexWriter indexWriter = new IndexWriter(indexDir, myWriterconfig);
    // add document to the index
    indexWriter.forceMerge(100000);
    indexWriter.addDocuments(this.allLuceneDocuments);
    indexWriter.close();
    indexDir.close();
    System.out.println("Finishing indexing.");
  }

  public ArrayList<Document> getAllLuceneDocuments() {
    return allLuceneDocuments;
  }
}
