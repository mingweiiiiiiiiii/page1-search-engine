package com.example.queryCreation;

import com.example.Topic;
import com.example.TopicParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
/*
@Author: Mingwei Shi

This class is to receive the content from the topic parser and then remove the stop words and punctuation, followed by tokenizing the
word

Then, perform the TF-IDF to each term and select the TOP 10 TF-IDF SCORE.

Then, use the set operation to make the value unique.

Each tf-idf score might contain several terms; select all of them.

TOP 15 TF-IDF SCORE
Then, repeat the set operation to select unique keyword terms for each query's final keyword collection.

You could generate the query online or offline
Offline: check out the folder: "./data/queryfile/query.txt";
ProcessingFromTopicParser myProcess  = new ProcessingFromTopicParser()
Online receive the input ArrayList<String> my = myProcess.getQueryList();
 */
public class QueryCreator {
	private static final String STOPWORDS_PATH = "./data/queries/173Stopwords.txt";
	private final List<Topic> topics;

	public QueryCreator() {
		TopicParser tp = new TopicParser();
		this.topics = tp.getTopics();
	}

	// https://gist.github.com/larsyencken/1440509
	public String[] stringRemovalNoiseAndToken(String text) {
		// Read stopwords into ArrayList.
		ArrayList<String> stopwords = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(STOPWORDS_PATH))) {
			String line;
			while ((line = reader.readLine()) != null) {
				stopwords.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Remove stopwords from text.
		String[] words = text.split("\\s+");
		StringBuilder wordsExcludingStopwords = new StringBuilder();
		for (String word : words) {
			if (!stopwords.contains(word.toLowerCase())) {
				wordsExcludingStopwords.append(word);
				wordsExcludingStopwords.append(' ');
			}
		}

		// Remove punctuation from text, but if there is a period within a string,
		// e.g. "U.S", then the period is maintained.
		// U.S and don't matain the original case
		String[] tokenizedKeywordsPunction = wordsExcludingStopwords.toString().trim().split("\\s+");
		String[] processedKeywordsRemovalPunction = new String[tokenizedKeywordsPunction.length];
		for (int i = 0; i < tokenizedKeywordsPunction.length; i++) {
			//<![a-zA-Z])\p{Punct} to make sure the punctuation is not part of Abbreviation.
			//\p{Punct}(?![a-zA-Z]) to remove the last punctuation.
			processedKeywordsRemovalPunction[i] = tokenizedKeywordsPunction[i].replaceAll("(?<![a-zA-Z])\\p{Punct}|\\p{Punct}(?![a-zA-Z])", "");
		}

		// Remove other words considered irrelevant by observation.
		List<String> filteredRelevantkeywords = new ArrayList<>();
		String[] removelist ={"relevant","i.e",
				"must","also","contain","am","is","will","due","as","it"
				,"taken","takes","done","even","may","either","claims",
				"itself","e.g","used"};
		for (String keyword : processedKeywordsRemovalPunction) {
			String currentKeywords= keyword;
			boolean judgeForAdd = true;

			for(int idexR= 0 ;idexR <removelist.length;idexR++)
			{
				String loopKeywords = removelist[idexR];
				if(loopKeywords.equalsIgnoreCase(currentKeywords))
				{
					judgeForAdd = false;
				}
			}
			if(judgeForAdd) {
				filteredRelevantkeywords.add(keyword);
			}

		}
		String[] removeRelevantList =  filteredRelevantkeywords.toArray(new String[0]);

		// remove digit number such as 103 ; as these are not keywords
		List<String> filteredNumericList = new ArrayList<>();
		for (String keyword : removeRelevantList) {
			if (!keyword.matches("\\d+")) {
				filteredNumericList.add(keyword.toLowerCase());
			}
		}
		String[] finalStringArrayTemp =  filteredNumericList.toArray(new String[0]);
		// for each string if contain "/",such as "damage/casualties" into two string into a arraylist
		ArrayList<String> splitKeywordsByAntiSlash = new ArrayList<>();
		for (String keyword : finalStringArrayTemp) {
			if (keyword.contains("/")) {
				String[] parts = keyword.split("/");
				splitKeywordsByAntiSlash.addAll(Arrays.asList(parts));
			} else {
				splitKeywordsByAntiSlash.add(keyword);
			}
		}
	/*
 		Based on Brain's suggestion ,in the corpora there are no words like "ultraviolet"
   		Instead it use its' original acronym. Hence I decide to comment this code out.
 	*/	
		// Change UV to ultraviolet for query 27.
		
		ArrayList<String> finalList = new ArrayList<>();
		for(int i = 0; i < splitKeywordsByAntiSlash.size(); i++) {
			String currentWords = splitKeywordsByAntiSlash.get(i);
			finalList.add(currentWords);
			// Comment out the filter
			/*
			if(currentWords.equalsIgnoreCase("UV")) {
				finalList.add("ultraviolet");
			} else {
				finalList.add(currentWords);
			}
   			*/
		}
  		
		
		return finalList.toArray(new String[0]);
	}


	public ArrayList<String> createQueries() {
		ArrayList<String> queries = new ArrayList<>();
		for (Topic topic : this.topics) {
			// Merge the string from title, narrative and description to
			// form a full document
			// in order to perform TF-IDF score
			String finalString = topic.getTitle() + topic.getNarrative() + topic.getDescription();

			String[] tokenizedKeywords = stringRemovalNoiseAndToken(finalString);
			List<String> tokenizedKeywordsList = Arrays.asList(tokenizedKeywords);
			TFIDFCalculator calculator = new TFIDFCalculator();
			// compute  the each one TFIDF score
			// we have score and associated  term
			ArrayList<Double> scoreForTFIDF = new ArrayList<Double>();
			ArrayList<StructureForTDIDFStorage> myStorageStucture = new ArrayList<>();

			for (int indexIDF = 0; indexIDF < tokenizedKeywordsList.size(); indexIDF++) {
				String currentTerm = tokenizedKeywordsList.get(indexIDF);
				double currentTFIDFScire = calculator.tfIdf(tokenizedKeywordsList, currentTerm);
				scoreForTFIDF.add(currentTFIDFScire);
				StructureForTDIDFStorage tempS = new StructureForTDIDFStorage(currentTFIDFScire, currentTerm);
				myStorageStucture.add(tempS);

			}
			// Sorting in descending order
			Collections.sort(scoreForTFIDF);
			Collections.reverse(scoreForTFIDF);

			ArrayList<Double> firstFiveScore = new ArrayList<>();
			// top 10
			int topKeywords = 13;
			for (int getIndex = 0; getIndex < topKeywords; getIndex++) {
				firstFiveScore.add(scoreForTFIDF.get(getIndex));
			}
			//
			/*
			 * Remove unique value
			 * https://stackoverflow.com/questions/13429119/get-unique-values-from-arraylist
			 * -in-java
			 */
			ArrayList<Double> uniqueList = (ArrayList) firstFiveScore.stream().distinct().collect(Collectors.toList());
			// Comparere
			// myStorageStucture
			StringBuilder currentKeywordsBulder = new StringBuilder();
			ArrayList<String> finalKeyword = new ArrayList<>();
			for (int indexUnique = 0; indexUnique < uniqueList.size(); indexUnique++) {
				double valueT = uniqueList.get(indexUnique);
				for (int indexForStruoew = 0; indexForStruoew < myStorageStucture.size(); indexForStruoew++) {

					double doubelForStore = myStorageStucture.get(indexForStruoew).getTdidfScore();
					Double d1 = valueT;
					Double d2 = doubelForStore;
					if (Double.compare(d1, d2) == 0) {

						String currentKeywodsToAdd = myStorageStucture.get(indexForStruoew).getKeywordTerms();

						finalKeyword.add(currentKeywodsToAdd);

					}
				}
			}
			// unique
			/*
			 * Remove unique value
			 * https://stackoverflow.com/questions/13429119/get-unique-values-from-arraylist
			 * -in-java
			 */
			ArrayList<String> uniqueListKeywords = (ArrayList) finalKeyword.stream().distinct()
					.collect(Collectors.toList());
			// StringBuilder currentKeywordsBulder = new StringBuilder();
			for (int indexKeyowrdL = 0; indexKeyowrdL < uniqueListKeywords.size(); indexKeyowrdL++) {
				String keyworT = uniqueListKeywords.get(indexKeyowrdL);
				currentKeywordsBulder.append(keyworT);
				currentKeywordsBulder.append(" ");
			}
			String finalStringCurrent = currentKeywordsBulder.toString();

			queries.add(finalStringCurrent.trim());



		}
		String filePath = "./data/queries/queries.txt";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (String line : queries) {
				writer.write(line);
				writer.newLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return queries;
	}
}
