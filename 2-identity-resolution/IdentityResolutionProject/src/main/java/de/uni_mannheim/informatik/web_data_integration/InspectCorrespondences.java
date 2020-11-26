package de.uni_mannheim.informatik.web_data_integration;

import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGame;

import java.io.File;

public class InspectCorrespondences {

    public static void main(String[] args) {

        // load correspondences
        System.out.println("*\n*\tLoading correspondences\n*");
        CorrespondenceSet<VideoGame, Attribute> correspondences = new CorrespondenceSet<>();
        correspondences.loadCorrespondences(new File("data/correspondences/sales_steam_correspondences.csv"), dsSteam, dsSales);
        correspondences.loadCorrespondences(new File("data/correspondences/steam_wikidata_correspondences.csv"), dsSteam, dsWikidata);
        correspondences.loadCorrespondences(new File("data/correspondences/wikidata_sales_correspondences.csv"), dsWikidata, dsSales);

        // write group size distribution
        correspondences.printGroupSizeDistribution();

    }

}
