package com.example;

import com.example.fbisparser.Fbis95Structure;
import com.example.fbisparser.Fbis96Parser;
import com.example.fr.FederalRegisterDocument;
import com.example.fr.FederalRegisterReader;
import com.example.ft.FinancialTimesDocument;
import com.example.ft.FinancialTimesReader;
import com.example.latimes.LosAngelesTimesDocument;
import com.example.latimes.LosAngelesTimesReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.FSDirectory;

public class Indexer {
  public static final String FT_PATH = "./data/ft";
  public static final String LATIMES_PATH = "./data/latimes";
  public static final String FR_PATH = "./data/fr94";

  private final String DOC_NO = "docno";
  private final String TITLE = "title";
  private final String CONTENT = "content";

  private ArrayList<Document> allLuceneDocuments;

  public Indexer() throws IOException {
    this.allLuceneDocuments = new ArrayList<>();
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

  public void indexDocuments(String indexDirectory, Analyzer analyzer, Similarity scorer) throws IOException {
    System.out.println("Indexing documents...");
    IndexWriter indexWriter = createWriter(indexDirectory, analyzer, scorer);
    indexWriter.forceMerge(100000);
    indexWriter.addDocuments(this.allLuceneDocuments);
    indexWriter.close();

    String analyzerName = analyzer.getClass().getName()
            .substring(analyzer.getClass().getName().lastIndexOf('.') + 1);
    String scorerName = scorer.getClass().getName()
            .substring(scorer.getClass().getName().lastIndexOf('.') + 1);
    System.out.println("Indexed documents with " + analyzerName + " and " + scorerName
    + " and stored to directory " + indexDirectory + ".");
  }

   /**
   * Creates an IndexWriter with the given analyzer and scorer.
   *
   * @param analyzer Analyzer to use for indexing.
   * @param scorer Similarity to use for indexing.
   * @return IndexWriter object.
   */
  private static IndexWriter createWriter(String indexDirectory, Analyzer analyzer, 
      Similarity similarity) throws IOException {
    IndexWriterConfig config = new IndexWriterConfig(analyzer);
    config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    config.setMaxBufferedDocs(100000);
    config.setSimilarity(similarity);
    FSDirectory dir = FSDirectory.open(Paths.get(indexDirectory));
    IndexWriter writer = new IndexWriter(dir, config);
    return writer;
  }
}