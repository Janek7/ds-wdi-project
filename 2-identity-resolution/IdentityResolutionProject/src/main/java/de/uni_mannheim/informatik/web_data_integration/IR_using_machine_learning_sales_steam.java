package de.uni_mannheim.informatik.web_data_integration;

import java.io.File;

import org.slf4j.Logger;


import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import de.uni_mannheim.informatik.web_data_integration.blocking.VideoGameBlockingKeyByTitleGenerator;
import de.uni_mannheim.informatik.web_data_integration.comparator.VideoGamePlatformComparator;
import de.uni_mannheim.informatik.web_data_integration.comparator.VideoGamePubDateComparator;
import de.uni_mannheim.informatik.web_data_integration.comparator.VideoGamePublisherComparatorEqual;
import de.uni_mannheim.informatik.web_data_integration.comparator.VideoGamePublisherComparatorJaccard;
import de.uni_mannheim.informatik.web_data_integration.comparator.VideoGamePublisherComparatorLevenshtein;
import de.uni_mannheim.informatik.web_data_integration.comparator.VideoGameTitleComparatorEqual;
import de.uni_mannheim.informatik.web_data_integration.comparator.VideoGameTitleComparatorJaccard;
import de.uni_mannheim.informatik.web_data_integration.comparator.VideoGameTitleComparatorLevenshtein;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGame;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGameXMLReader;

public class IR_using_machine_learning_sales_steam {

private static final Logger logger = WinterLogManager.activateLogger("default");
	
    public static void main( String[] args ) throws Exception
    {
    	// loading data 
		System.out.println("*\n*\tLoading datasets\n*");
        HashedDataSet<VideoGame, Attribute> dataVideoGameSteam = new HashedDataSet<>();
        new VideoGameXMLReader().loadFromXML(new File("data/input/steam_mapping_output.xml"),
                         "/videogames/videogame", dataVideoGameSteam);
        HashedDataSet<VideoGame, Attribute> dataVideoGameSales = new HashedDataSet<>();
        new VideoGameXMLReader().loadFromXML(new File("data/input/sales_mapping_output.xml"),
                         "/videogames/videogame", dataVideoGameSales);
		
		// load the training set
		MatchingGoldStandard gsTraining = new MatchingGoldStandard();
	    gsTraining.loadFromCSVFile(new File("data/goldstandard/sales_steam/gold-standard_sales_steam_training.csv"));


		// create a matching rule
		String options[] = new String[] { "-S" };
		String modelType = "SimpleLogistic"; // use a logistic regression
		WekaMatchingRule<VideoGame, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
		matchingRule.activateDebugReport("data/output/sales_steam/1debugResultsMatchingRule.csv", 1000, gsTraining);
		
		// add comparators
		// Title
  //	matchingRule.addComparator(new VideoGameTitleComparatorEqual());
		matchingRule.addComparator(new VideoGameTitleComparatorLevenshtein());
		matchingRule.addComparator(new VideoGameTitleComparatorJaccard());
//		
		// Platform
  	    matchingRule.addComparator(new VideoGamePlatformComparator(new TokenizingJaccardSimilarity()));
        matchingRule.addComparator(new VideoGamePlatformComparator(new LevenshteinSimilarity()));
//     
		// Publisher
//      matchingRule.addComparator(new VideoGamePublisherComparatorJaccard());
//      matchingRule.addComparator(new VideoGamePublisherComparatorLevenshtein());
//      matchingRule.addComparator(new VideoGamePublisherComparatorEqual());

        // Year
//        matchingRule.addComparator(new VideoGamePubDateComparator(1));
//        matchingRule.addComparator(new VideoGamePubDateComparator(2));
//        matchingRule.addComparator(new VideoGamePubDateComparator(3));

		
		// train the matching rule's model
		System.out.println("*\n*\tLearning matching rule\n*");
		RuleLearner<VideoGame, Attribute> learner = new RuleLearner<>();
		learner.learnMatchingRule(dataVideoGameSteam, dataVideoGameSales, null, matchingRule, gsTraining);
		System.out.println(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));
		
		// create a blocker (blocking strategy)
        SortedNeighbourhoodBlocker<VideoGame, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new VideoGameBlockingKeyByTitleGenerator(), 75);


//		StandardRecordBlocker<VideoGame, Attribute> blocker = new StandardRecordBlocker<VideoGame, Attribute>(new VideoGameBlockingKeyByTitleGenerator());
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByDecadeGenerator(), 1);
		
        blocker.collectBlockSizeData("data/output/sales_steam/1debugResultsBlocking.csv", 100);
		
		// Initialize Matching Engine
		MatchingEngine<VideoGame, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		System.out.println("*\n*\tRunning identity resolution\n*");
		Processable<Correspondence<VideoGame, Attribute>> correspondences = engine.runIdentityResolution(
				dataVideoGameSteam, dataVideoGameSales, null, matchingRule,
				blocker);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/sales_steam/1sales_steam_correspondences.csv"), correspondences);	
		
		
	
		// load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
 		MatchingGoldStandard gsTest = new MatchingGoldStandard();
 		gsTest.loadFromCSVFile(new File(
            "data/goldstandard/sales_steam/gold-standard_sales_steam_test.csv"));
 		
		
		// evaluate your result
		System.out.println("*\n*\tEvaluating result\n*");
		MatchingEvaluator<VideoGame, Attribute> evaluator = new MatchingEvaluator<VideoGame, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences,
				gsTest);
		
		// print the evaluation result
		System.out.println("Steam <-> Sales");
		System.out.println(String.format(
				"Precision: %.4f",perfTest.getPrecision()));
		System.out.println(String.format(
				"Recall: %.4f",	perfTest.getRecall()));
		System.out.println(String.format(
				"F1: %.4f",perfTest.getF1()));
    }
}
