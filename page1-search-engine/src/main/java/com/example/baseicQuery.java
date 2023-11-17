package com.example;


import com.example.queryCreation.ProcessingFromTopicParser;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.queryparser.classic.QueryParser;


import org.apache.lucene.queryparser.classic.ParseException;

import org.apache.lucene.search.Query;

import java.io.IOException;
import java.util.ArrayList;



public class baseicQuery {
  public static void main(String[] args) throws IOException, ParseException {
    ProcessingFromTopicParser myqueryGeneration = new ProcessingFromTopicParser();
    myqueryGeneration.run();
    ArrayList<String> myQueryList = myqueryGeneration.getQueryList();


    QueryParser qp = new QueryParser("content", new StandardAnalyzer());



    for (int i = 0; i < myQueryList.size(); i++) {
      String querytempDescription = myQueryList.get(i).trim();

      try {

        qp.setAllowLeadingWildcard(true);
        Query qry = qp.parse(querytempDescription);

        System.out.println("Query parsed: " + qry.toString());

      } catch (Exception e) {
        e.printStackTrace();

      }





    }
  }
}

