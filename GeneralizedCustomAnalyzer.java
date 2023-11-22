package com.example;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.CharArraySet;

import java.io.IOException;
import java.util.Arrays;

public class GeneralizedCustomAnalyzer extends Analyzer {

    private static final String[] COMMON_STOP_WORDS = {
        "a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it",
        "no", "not", "of", "on", "or", "such", "that", "the", "their", "then", "there", "these",
        "they", "this", "to", "was", "will", "with", "etc"
    };

    private final CharArraySet stopWordsSet;

    public GeneralizedCustomAnalyzer() {
        this.stopWordsSet = new CharArraySet(Arrays.asList(COMMON_STOP_WORDS), true);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer source = new StandardTokenizer();
        LowerCaseFilter lowerCaseFilter = new LowerCaseFilter(source);
        StopFilter stopFilter = new StopFilter(lowerCaseFilter, stopWordsSet);
        PorterStemFilter stemFilter = new PorterStemFilter(stopFilter);

        return new TokenStreamComponents(source, stemFilter);
    }
}
