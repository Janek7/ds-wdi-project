package de.uni_mannheim.informatik.web_data_integration.matching_rules;

import java.io.File;

import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.MaximumOfTokenContainment;
import de.uni_mannheim.informatik.web_data_integration.blocking.VideoGameBlockingKeyByTitleAndPlatformGenerator;
import de.uni_mannheim.informatik.web_data_integration.comparator.*;
import de.uni_mannheim.informatik.web_data_integration.comparator.PlatformComparatorAdvanced;
import de.uni_mannheim.informatik.web_data_integration.comparator.custom_similarity_measure.JaroSimilarity;
import de.uni_mannheim.informatik.web_data_integration.comparator.custom_similarity_measure.JaroWinklerSimilarity;

import org.slf4j.Logger;

import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import de.uni_mannheim.informatik.web_data_integration.blocking.VideoGameBlockingKeyByTitleGenerator;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGame;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGameXMLReader;

public class IR_using_linear_combination_nadine {

	private static final Logger logger = WinterLogManager.activateLogger("default");

	public static void main(String[] args) throws Exception {
		// loading data
		System.out.println("*\n*\tLoading datasets\n*");
		HashedDataSet<VideoGame, Attribute> dataVideoGameSteam = new HashedDataSet<>();
		new VideoGameXMLReader().loadFromXML(new File("data/input/steam_mapping_output.xml"), "/videogames/videogame",
				dataVideoGameSteam);
		HashedDataSet<VideoGame, Attribute> dataVideoGameWikidata = new HashedDataSet<>();
		new VideoGameXMLReader().loadFromXML(new File("data/input/wikidata_mapping_output.xml"),
				"/videogames/videogame", dataVideoGameWikidata);

		
		// load the gold standard (test set)
		System.out.println("*\n*\tLoading gold standard\n*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File("data/goldstandard/steam_wikidata/gold-standard_steam_wikidata.csv"));

		// create a matching rule
		LinearCombinationMatchingRule<VideoGame, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.76);
		matchingRule.activateDebugReport("data/output/steam_wikidata_linear/debugResultsMatchingRule.csv", 1000, gsTest);

		// add comparators
//        matchingRule.addComparator(new TitleComparator(new JaroSimilarity()), 0.4);
//		matchingRule.addComparator(new PlatformComparatorAdvanced(new LevenshteinSimilarity()), 0.4);
//		matchingRule.addComparator(new DeveloperComparator(new JaroWinklerSimilarity()), 0.1);
//		matchingRule.addComparator(new PubDateComparator(1), 0.1);
		matchingRule.addComparator(new TitleComparator(new MaximumOfTokenContainment()), 0.25);
		matchingRule.addComparator(new PlatformComparatorAdvanced(new MaximumOfTokenContainment()), 0.25);
		matchingRule.addComparator(new PublisherComparator(new JaroWinklerSimilarity()), 0.1);
		matchingRule.addComparator(new PubDateComparator(1), 0.4);

		// creating a blocker
		StandardRecordBlocker<VideoGame, Attribute> blocker = new StandardRecordBlocker<VideoGame, Attribute>(
				new VideoGameBlockingKeyByTitleAndPlatformGenerator());
		blocker.setMeasureBlockSizes(true);
		blocker.collectBlockSizeData("data/output/steam_wikidata_linear/debugResultsBlocking.csv", 100);
//		 SortedNeighbourhoodBlocker<VideoGame, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new VideoGameBlockingKeyByTitleGenerator(), 100);
//	        blocker.setMeasureBlockSizes(true);
//	        blocker.collectBlockSizeData("data/output/steam_wikidata_linear/debugResultsBlocking.csv", 100);

		// Initialize Matching Engine
		MatchingEngine<VideoGame, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		System.out.println("*\n*\tRunning identity resolution\n*");
		Processable<Correspondence<VideoGame, Attribute>> correspondences = engine
				.runIdentityResolution(dataVideoGameSteam, dataVideoGameWikidata, null, matchingRule, blocker);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/steam_wikidata_linear/steam_wikidata_correspondences.csv"),
				correspondences);

		System.out.println("*\n*\tEvaluating result\n*");
		// evaluate your result
		MatchingEvaluator<VideoGame, Attribute> evaluator = new MatchingEvaluator<VideoGame, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences, gsTest);

		// print the evaluation result
		System.out.println("Steam <-> Wikidata");
		System.out.println(String.format("Precision: %.4f", perfTest.getPrecision()));
		System.out.println(String.format("Recall: %.4f", perfTest.getRecall()));
		System.out.println(String.format("F1: %.4f", perfTest.getF1()));

	}

}
