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
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class indexingConstrution {


  ArrayList<Document> LuenceAllDocuemnt;

  public indexingConstrution() throws IOException {
    this.LuenceAllDocuemnt = new ArrayList<>();

    this.createDoucmentSameFieldForAllCorpus();
    // construcntion indexing
    this.constructIndex();

  }

  private final String DOC_NUMBE_MARCO = "docno";
  private final String TITLE_MARCO = "title";
  public static final String FT_PATH = "./data/ft";
  public static final String LATIMES_PATH = "./data/latimes";
  public static final String FR_PATH = "./data/fr94";
  public static final String DIRTORY_ADDRESS = "./dir";

  private final String CONTENT_MARCO = "content";

  public void createDoucmentSameFieldForAllCorpus() throws IOException {

// 1 FBIS
    Fbis96Parser myFbisParser = new Fbis96Parser();
    myFbisParser.loadFiles();
    ArrayList<Fbis95Structure> myFbisCollection = myFbisParser.getMyFbisContainer();

    for (int indexi = 0; indexi < myFbisCollection.size(); indexi++) {
      Fbis95Structure currentDocument = myFbisCollection.get(indexi);

      String tempDOC = currentDocument.getDocno();
      String tempTitle = currentDocument.getTitle();
      String tempContent = currentDocument.getTitle();
      //
      Document temPDocuement = new Document();
      // DOCNO not cut
      temPDocuement.add(new StringField(DOC_NUMBE_MARCO, tempDOC, Field.Store.YES));
      temPDocuement.add(new TextField(TITLE_MARCO, tempTitle, Field.Store.YES));
      temPDocuement.add(new TextField(CONTENT_MARCO, tempContent, Field.Store.YES));

      this.LuenceAllDocuemnt.add(temPDocuement);
    }
    // 2 FINAL time
    ArrayList<FinancialTimesDocument> myfinalTime = FinancialTimesReader.readDocuments(FT_PATH);
    for (int indexi = 0; indexi < myfinalTime.size(); indexi++) {

      FinancialTimesDocument currentDocument = myfinalTime.get(indexi);


      String tempDOC = currentDocument.getNumber();
      String tempTitle = currentDocument.getHeadline();
      String tempContent = currentDocument.getText();
      Document temPDocuement = new Document();
      temPDocuement.add(new StringField(DOC_NUMBE_MARCO, tempDOC, Field.Store.YES));
      temPDocuement.add(new TextField(TITLE_MARCO, tempTitle, Field.Store.YES));
      temPDocuement.add(new TextField(CONTENT_MARCO, tempContent, Field.Store.YES));
      this.LuenceAllDocuemnt.add(temPDocuement);

    }
    // 3 FR
    ArrayList<FederalRegisterDocument> myFR = FederalRegisterReader.readDocuments(FR_PATH);

    for (int indexi = 0; indexi < myFR.size(); indexi++) {

      FederalRegisterDocument currentDocument = myFR.get(indexi);


      String tempDOC = currentDocument.getNumber();
      String tempTitle = currentDocument.getTitle();
      String tempContent = currentDocument.getText();

      Document temPDocuement = new Document();
      temPDocuement.add(new StringField(DOC_NUMBE_MARCO, tempDOC, Field.Store.YES));
      temPDocuement.add(new TextField(TITLE_MARCO, tempTitle, Field.Store.YES));
      temPDocuement.add(new TextField(CONTENT_MARCO, tempContent, Field.Store.YES));
      this.LuenceAllDocuemnt.add(temPDocuement);

    }
    // 4    LosAngelesTimesReader.readDocuments(LATIMES_PATH);

    ArrayList<LosAngelesTimesDocument> myLST = LosAngelesTimesReader.readDocuments(LATIMES_PATH);
    for (int indexi = 0; indexi < myLST.size(); indexi++) {

      LosAngelesTimesDocument currentDocument = myLST.get(indexi);


      String tempDOC = currentDocument.getNumber();
      String tempTitle = currentDocument.getHeadline();
      String tempContent = currentDocument.getText();

      Document temPDocuement = new Document();
      temPDocuement.add(new StringField(DOC_NUMBE_MARCO, tempDOC, Field.Store.YES));
      temPDocuement.add(new TextField(TITLE_MARCO, tempTitle, Field.Store.YES));
      temPDocuement.add(new TextField(CONTENT_MARCO, tempContent, Field.Store.YES));
      this.LuenceAllDocuemnt.add(temPDocuement);

    }
    System.out.println("FInishing LOADING title");

  }

  public void constructIndex() throws IOException {
    System.out.println("Starting indexing ");
    Analyzer mAnalyzer = new StandardAnalyzer();
    IndexWriterConfig myWriterconfig = new IndexWriterConfig(mAnalyzer);
    myWriterconfig.setSimilarity(new BM25Similarity());

    Path myindexPath = Paths.get(DIRTORY_ADDRESS);
    Directory indexDir = FSDirectory.open(myindexPath);
    // Overwrite seveearl times
    myWriterconfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    IndexWriter indexWriter = new IndexWriter(indexDir, myWriterconfig);
    // add document to the index
    indexWriter.addDocuments(this.LuenceAllDocuemnt);

    System.out.println("Finishing indexing ");

    float weightForTittle = 0.2f;

    float weightForDescr = 1.1f;
    QueryParser parser_single_mutiple = null;
    Map<String, Float> bootssetting3 = new HashMap<>();
    bootssetting3.put(TITLE_MARCO, weightForTittle);
    bootssetting3.put(CONTENT_MARCO, weightForDescr);
    String[] Feature3_CONSTANT_TITLE_DESC_FEATURE = new String[] {TITLE_MARCO, CONTENT_MARCO};
    parser_single_mutiple =
        new MultiFieldQueryParser(Feature3_CONSTANT_TITLE_DESC_FEATURE, mAnalyzer, bootssetting3);

    parser_single_mutiple.setAllowLeadingWildcard(true);
    System.out.println("We select  all two title TITLE  conetne");

  }
}
