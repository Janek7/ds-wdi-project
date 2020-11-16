package de.uni_mannheim.informatik.web_data_integration;

import java.io.File;

import org.slf4j.Logger;

import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGame;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGameXMLReader;

public class IR_using_linear_combination {

        private static final Logger logger = WinterLogManager.activateLogger("default");

        public static void main(String[] args) throws Exception {
                /*
                 * Einlesen der Mappings
                 * 
                 * Einige Einträge von "sales_mapping" haben fehlerhafte "publishingdates".
                 * Diese werden nochmal extra ausgegeben (siehe try/catch in
                 * VideoGameXMLReader.java). Ansonsten werden die 3 Mappings eingelesen und 5
                 * beispielhafte Einträge ausgegeben (siehe logging).
                 */
                System.out.println("*\n*\tLoading datasets\n*");

                HashedDataSet<VideoGame, Attribute> dataVideoGameSales = new HashedDataSet<>();
                new VideoGameXMLReader().loadFromXML(new File("data/input/sales_mapping_output.xml"),
                                "/videogames/videogame", dataVideoGameSales);

                System.out.println(dataVideoGameSales.take(5).get());

                HashedDataSet<VideoGame, Attribute> dataVideoGameSteam = new HashedDataSet<>();
                new VideoGameXMLReader().loadFromXML(new File("data/input/steam_mapping_output.xml"),
                                "/videogames/videogame", dataVideoGameSteam);

                System.out.println(dataVideoGameSteam.take(5).get());
                System.out.println("");

                HashedDataSet<VideoGame, Attribute> dataVideoGameWikidata = new HashedDataSet<>();
                new VideoGameXMLReader().loadFromXML(new File("data/input/wikidata_mapping_output.xml"),
                                "/videogames/videogame", dataVideoGameWikidata);

                System.out.println(dataVideoGameWikidata.take(5).get());
        }

}
