package de.uni_mannheim.informatik.web_data_integration.matching_rules;

import java.io.File;

import de.uni_mannheim.informatik.dws.winter.similarity.string.JaccardOnNGramsSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import de.uni_mannheim.informatik.web_data_integration.comparator.PlatformComparator;
import de.uni_mannheim.informatik.web_data_integration.comparator.PubDateComparator;
import de.uni_mannheim.informatik.web_data_integration.comparator.PublisherComparator;
import de.uni_mannheim.informatik.web_data_integration.comparator.TitleComparator;
import de.uni_mannheim.informatik.web_data_integration.comparator.custom_similarity_measure.JaroSimilarity;
import org.slf4j.Logger;

import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import de.uni_mannheim.informatik.web_data_integration.blocking.VideoGameBlockingKeyByTitleGenerator;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGame;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGameXMLReader;

public class IR_using_machine_learning_wikidata_sales_janek {
	
	private static final Logger logger = WinterLogManager.activateLogger("trace");
	
	public static void main(String[] args) throws Exception {
		
		// loading data 
				System.out.println("*\n*\tLoading datasets\n*");
		        HashedDataSet<VideoGame, Attribute> dataVideoGameSales = new HashedDataSet<>();
		        new VideoGameXMLReader().loadFromXML(new File("data/input/sales_mapping_output.xml"),
		                         "/videogames/videogame", dataVideoGameSales);
		        HashedDataSet<VideoGame, Attribute> dataVideoGameWikidata = new HashedDataSet<>();
		        new VideoGameXMLReader().loadFromXML(new File("data/input/wikidata_mapping_output.xml"),
		                         "/videogames/videogame", dataVideoGameWikidata);
				
				// load the training set
				MatchingGoldStandard gsTraining = new MatchingGoldStandard();
				gsTraining.loadFromCSVFile(new File("data/goldstandard/gold-standard_wikidata_sales.csv"));

				// create a matching rule
				String options[] = new String[] { "-S" };
				String modelType = "SimpleLogistic"; // use a logistic regression
				WekaMatchingRule<VideoGame, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
				matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTraining);
				
				
				// add comparators
				matchingRule.addComparator(new TitleComparator(new JaccardOnNGramsSimilarity(3)));
				matchingRule.addComparator(new PlatformComparator(new LevenshteinSimilarity()));
				matchingRule.addComparator(new PublisherComparator(new JaroSimilarity()));
				matchingRule.addComparator(new PubDateComparator(7));
				
				
				
				// train the matching rule's model
				System.out.println("*\n*\tLearning matching rule\n*");
				RuleLearner<VideoGame, Attribute> learner = new RuleLearner<>();
				learner.learnMatchingRule(dataVideoGameSales, dataVideoGameWikidata, null, matchingRule, gsTraining);
				System.out.println(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));
				
				// create a blocker (blocking strategy)
				//StandardRecordBlocker<VideoGame, Attribute> blocker = new StandardRecordBlocker<VideoGame, Attribute>(new VideoGameBlockingKeyByTitleGenerator());
				//sorted doesn't work? only 0 scores
				//SortedNeighbourhoodBlocker<VideoGame, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new VideoGameBlockingKeyByPlatformGenerator(), 1);
				SortedNeighbourhoodBlocker<VideoGame, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new VideoGameBlockingKeyByTitleGenerator(), 75);
				blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);
				
				// Initialize Matching Engine
				MatchingEngine<VideoGame, Attribute> engine = new MatchingEngine<>();

				// Execute the matching
				System.out.println("*\n*\tRunning identity resolution\n*");
				Processable<Correspondence<VideoGame, Attribute>> correspondences = engine.runIdentityResolution(
						dataVideoGameSales, dataVideoGameWikidata, null, matchingRule,
						blocker);

				// write the correspondences to the output file
				new CSVCorrespondenceFormatter().writeCSV(new File("data/output/sales_wikidata_correspondences.csv"), correspondences);	
				
				
			
				// load the gold standard (test set)
				//hier momentan noch selber goldstandard wie oben ???
		        System.out.println("*\n*\tLoading gold standard\n*");
		 		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		 		gsTest.loadFromCSVFile(new File(
		 				"data/goldstandard/gold-standard_wikidata_sales.csv"));
		 		
				
				// evaluate your result
				System.out.println("*\n*\tEvaluating result\n*");
				MatchingEvaluator<VideoGame, Attribute> evaluator = new MatchingEvaluator<VideoGame, Attribute>();
				Performance perfTest = evaluator.evaluateMatching(correspondences,
						gsTest);
				
				// print the evaluation result
				System.out.println("Steam <-> Wikidata");
				System.out.println(String.format(
						"Precision: %.4f",perfTest.getPrecision()));
				System.out.println(String.format(
						"Recall: %.4f",	perfTest.getRecall()));
				System.out.println(String.format(
						"F1: %.4f",perfTest.getF1()));

	}

}