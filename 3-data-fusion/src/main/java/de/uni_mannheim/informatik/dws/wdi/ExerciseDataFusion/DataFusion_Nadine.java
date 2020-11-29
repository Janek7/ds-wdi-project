package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import org.apache.logging.log4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.TitleEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.TitleFuser;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.VideoGame;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.VideoGameXMLFormatter;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.VideoGameXMLReader;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.string.ShortestString;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroupFactory;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class DataFusion_Nadine {
    /*
     * Logging Options:
     * 		default: 	level INFO	- console
     * 		trace:		level TRACE     - console
     * 		infoFile:	level INFO	- console/file
     * 		traceFile:	level TRACE	- console/file
     *
     * To set the log level to trace and write the log to winter.log and console,
     * activate the "traceFile" logger as follows:
     *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
     *
     */

    private static final Logger logger = WinterLogManager.activateLogger("traceFile");
    public static void main(String[] args) throws Exception {
        // Load the Data into FusibleDataSet
        System.out.println("*\n*\tLoading datasets\n*");
        FusibleDataSet<VideoGame, Attribute> dsWikidata = new FusibleHashedDataSet<>();
        new VideoGameXMLReader().loadFromXML(new File("data/input/wikidata_mapping_output.xml"), "/videogames/videogame", dsWikidata);
        dsWikidata.printDataSetDensityReport();

        FusibleDataSet<VideoGame, Attribute> dsSteam = new FusibleHashedDataSet<>();
        new VideoGameXMLReader().loadFromXML(new File("data/input/steam_mapping_output.xml"), "/videogames/videogame", dsSteam);
        dsSteam.printDataSetDensityReport();

        FusibleDataSet<VideoGame, Attribute> dsSales = new FusibleHashedDataSet<>();
        new VideoGameXMLReader().loadFromXML(new File("data/input/sales_mapping_output.xml"), "/videogames/videogame", dsSales);
        dsSales.printDataSetDensityReport();

        // Maintain Provenance
        // Scores (e.g. from rating)
        dsWikidata.setScore(3.0);
        dsSteam.setScore(1.0);
        dsSales.setScore(2.0);

        // Date (e.g. last update)
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd")
                .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter(Locale.ENGLISH);

        // was macht das?
        dsWikidata.setDate(LocalDateTime.parse("2012-01-01", formatter));
        dsSteam.setDate(LocalDateTime.parse("2010-01-01", formatter));
        dsSales.setDate(LocalDateTime.parse("2008-01-01", formatter));

        // load correspondences
        System.out.println("*\n*\tLoading correspondences\n*");
        CorrespondenceSet<VideoGame, Attribute> correspondences = new CorrespondenceSet<>();
        correspondences.loadCorrespondences(new File("data/correspondences/sales_steam_correspondences.csv"), dsSteam, dsSales);
        correspondences.loadCorrespondences(new File("data/correspondences/steam_wikidata_correspondences.csv"), dsSteam, dsWikidata);
        correspondences.loadCorrespondences(new File("data/correspondences/wikidata_sales_correspondences.csv"), dsWikidata, dsSales);

        // write group size distribution
        correspondences.printGroupSizeDistribution();

        // load the gold standard
        System.out.println("*\n*\tEvaluating results\n*");
        DataSet<VideoGame, Attribute> gs = new FusibleHashedDataSet<>();
        new VideoGameXMLReader().loadFromXML(new File("data/input/goldstandard_fusion.xml"), "/videogames/videogame", gs);

        // define the fusion strategy
        DataFusionStrategy<VideoGame, Attribute> strategy = new DataFusionStrategy<>(new VideoGameXMLReader());

        // write debug results to file
        strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);

        // add attribute fusers
        strategy.addAttributeFuser(VideoGame.TITLE, new TitleFuser(new ShortestString()), new TitleEvaluationRule());
//        strategy.addAttributeFuser(VideoGame.PLATFORM, new PlatformFuser(new LongestString()), null);
//        strategy.addAttributeFuser(VideoGame.PUBLISHER, new PublisherFuser(new LongestString()), null);
//        strategy.addAttributeFuser(VideoGame.PUBLISHING_DATE, new PublishingDateFuser(new MostRecent()), null);
//        strategy.addAttributeFuser(VideoGame.DEVELOPER, new DeveloperFuser(new LongestString()), null);
//        strategy.addAttributeFuser(VideoGame.GENRES, new GenreFuser(new Intersection()), null);
//        strategy.addAttributeFuser(VideoGame.GAME_MODES, new GameModeFuser(new Intersection()), null);
//        strategy.addAttributeFuser(VideoGame.PRICE, new PriceFuser(new Average()), null);
//        strategy.addAttributeFuser(VideoGame.AGE, new AgeFuser(new Average()), null);
//        strategy.addAttributeFuser(VideoGame.USK_RATING, new UskRatingFuser(), null);
//        strategy.addAttributeFuser(VideoGame.PEGI_RATING, new PegiRatingFuser(), null);

        // create the fusion engine
        DataFusionEngine<VideoGame, Attribute> engine = new DataFusionEngine<>(strategy);

        // print consistency report
        engine.printClusterConsistencyReport(correspondences, null);

        // print record groups sorted by consistency
        engine.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies.csv"), correspondences, null);

        // run the fusion
        System.out.println("*\n*\tRunning data fusion\n*");
        FusibleDataSet<VideoGame, Attribute> fusedDataSet = engine.run(correspondences, null);

        // write the result
        new VideoGameXMLFormatter().writeXML(new File("data/output/fused.xml"), fusedDataSet);

        // evaluate
        DataFusionEvaluator<VideoGame, Attribute> evaluator = new DataFusionEvaluator<>(strategy, new RecordGroupFactory<>());

        double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

        System.out.println(String.format("Accuracy: %.2f", accuracy));
    }
}
