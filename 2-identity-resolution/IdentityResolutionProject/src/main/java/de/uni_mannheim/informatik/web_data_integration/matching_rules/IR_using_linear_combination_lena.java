package de.uni_mannheim.informatik.web_data_integration.matching_rules;

import java.io.File;

import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;
import de.uni_mannheim.informatik.web_data_integration.comparator.PubDateComparator;
//import de.uni_mannheim.informatik.web_data_integration.comparator.PubDateComparator;
import de.uni_mannheim.informatik.web_data_integration.comparator.TitleComparator;
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

public class IR_using_linear_combination_lena {

	private static final Logger logger = WinterLogManager.activateLogger("trace");

	public static void main(String[] args) throws Exception {
		// loading data
		System.out.println("*\n*\tLoading datasets\n*");
		HashedDataSet<VideoGame, Attribute> dataVideoGameSteam = new HashedDataSet<>();
		new VideoGameXMLReader().loadFromXML(new File("data/input/steam_mapping_output.xml"), "/videogames/videogame",
				dataVideoGameSteam);
		HashedDataSet<VideoGame, Attribute> dataVideoGameSales = new HashedDataSet<>();
		new VideoGameXMLReader().loadFromXML(new File("data/input/sales_mapping_output.xml"),
            "/videogames/videogame", dataVideoGameSales);

		// load the gold standard (test set)
		System.out.println("*\n*\tLoading gold standard\n*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File("data/goldstandard/gold-standard_sales_steam.csv"));

		// create a matching rule
		LinearCombinationMatchingRule<VideoGame, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.7);
		matchingRule.activateDebugReport("data/output/sales_steam_linear/debugResultsMatchingRule.csv", 1000, gsTest);

		// add comparators
		// -- Title --
	    matchingRule.addComparator(new TitleComparator(new TokenizingJaccardSimilarity()), 0.7);

        // -- Year --
        matchingRule.addComparator(new PubDateComparator(3), 0.3);


		// creating a blocker
        SortedNeighbourhoodBlocker<VideoGame, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new VideoGameBlockingKeyByTitleGenerator(), 100);

//		StandardRecordBlocker<VideoGame, Attribute> blocker = new StandardRecordBlocker<VideoGame, Attribute>(
//				new VideoGameBlockingKeyByTitleGenerator());
		//NoBlocker<VideoGame, Attribute> blocker = new NoBlocker<>();

		blocker.setMeasureBlockSizes(true);
		blocker.collectBlockSizeData("data/output/sales_steam_linear/debugResultsBlocking.csv", 100);

		// Initialize Matching Engine
		MatchingEngine<VideoGame, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		System.out.println("*\n*\tRunning identity resolution\n*");
		Processable<Correspondence<VideoGame, Attribute>> correspondences = engine
				.runIdentityResolution(dataVideoGameSteam, dataVideoGameSales, null, matchingRule, blocker);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/sales_steam_linear/sales_steam_correspondences.csv"),
				correspondences);

		System.out.println("*\n*\tEvaluating result\n*");
		// evaluate your result
		MatchingEvaluator<VideoGame, Attribute> evaluator = new MatchingEvaluator<VideoGame, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences, gsTest);

		// print the evaluation result
		System.out.println("Steam <-> Sales");
		System.out.println(String.format("Precision: %.4f", perfTest.getPrecision()));
		System.out.println(String.format("Recall: %.4f", perfTest.getRecall()));
		System.out.println(String.format("F1: %.4f", perfTest.getF1()));

	}

}
