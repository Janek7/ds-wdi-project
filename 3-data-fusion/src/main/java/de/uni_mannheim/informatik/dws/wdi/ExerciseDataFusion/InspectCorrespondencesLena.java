package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.VideoGame;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.VideoGameXMLReader;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class InspectCorrespondencesLena {

    public static void main(String[] args) throws Exception {

        FusibleDataSet<VideoGame, Attribute> dsWikidata = new FusibleHashedDataSet<>();
        new VideoGameXMLReader().loadFromXML(new File("data/input/wikidata_mapping_output.xml"), "/videogames/videogame", dsWikidata);
        //dsWikidata.printDataSetDensityReport();

        FusibleDataSet<VideoGame, Attribute> dsSteam = new FusibleHashedDataSet<>();
        new VideoGameXMLReader().loadFromXML(new File("data/input/steam_mapping_output.xml"), "/videogames/videogame", dsSteam);
//        dsSteam.printDataSetDensityReport();

        FusibleDataSet<VideoGame, Attribute> dsSales = new FusibleHashedDataSet<>();
        new VideoGameXMLReader().loadFromXML(new File("data/input/sales_mapping_output.xml"), "/videogames/videogame", dsSales);
//        dsSales.printDataSetDensityReport();

        // load correspondences
        System.out.println("*\n*\tLoading correspondences\n*");
        CorrespondenceSet<VideoGame, Attribute> correspondences = new CorrespondenceSet<>();
        correspondences.loadCorrespondences(new File("data/correspondences/sales_steam_correspondences.csv"), dsSteam, dsSales);
        correspondences.loadCorrespondences(new File("data/correspondences/steam_wikidata_correspondences.csv"), dsSteam, dsWikidata);
	    correspondences.loadCorrespondences(new File("data/correspondences/wikidata_sales_correspondences.csv"), dsWikidata, dsSales);
	     
        Collection<RecordGroup<VideoGame, Attribute>> recordGroups = correspondences.getRecordGroups();
        for (RecordGroup<VideoGame,Attribute> recordGroup : recordGroups) {
                try {
                        if (recordGroup.getRecords().size() > 4) {
                                System.out.println(recordGroup.getRecords());
                                System.out.println("");
                                
                        }
                } catch (Exception e) {
                        //TODO: handle exception
                }
        }


        // write group size distribution
        correspondences.printGroupSizeDistribution();

    }

}