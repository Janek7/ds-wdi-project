package de.uni_mannheim.informatik.web_data_integration.comparator.custom_similarity_measure;

import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGame;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VideoGamePlatformSimilarity extends SimilarityMeasure<VideoGame> {

    private static final long serialVersionUID = 1L;
    private static boolean platformCorrespondencesLoaded = false;
    private static final List<PlatformCorrespondence> wikidataSalesCorrespondences = new ArrayList<>();
    private static final List<PlatformCorrespondence> wikidataSteamCorrespondences = new ArrayList<>();
    private static final List<PlatformCorrespondence> salesSteamCorrespondences = new ArrayList<>();

    /**
     * Load of files is only one time necessary
     */
    public VideoGamePlatformSimilarity() {

        if (!platformCorrespondencesLoaded) {
            readFileIntoList("data/platform_correspondences/correspondences_platform_wikidata_sales.csv",
                    "Wikidata;Sales", wikidataSalesCorrespondences);
            readFileIntoList("data/platform_correspondences/correspondences_platform_wikidata_steam.csv",
                    "Wikidata;Steam", wikidataSteamCorrespondences);
            readFileIntoList("data/platform_correspondences/correspondences_platform_sales_steam.csv",
                    "Sales;Steam", salesSteamCorrespondences);
            platformCorrespondencesLoaded = true;
        }

    }

    @Override
    public double calculate(VideoGame first, VideoGame second) {
        switch (first.getSource()) {

            case WIKIDATA:

                switch (second.getSource()) {
                    case SALES:
                        return checkPair(wikidataSalesCorrespondences, first, second);
                    case STEAM:
                        return checkPair(wikidataSteamCorrespondences, first, second);
                    default:
                        return 0;
                }

            case SALES:

                switch (second.getSource()) {
                    case WIKIDATA:
                        return checkPair(wikidataSalesCorrespondences, second, first);
                    case STEAM:
                        return checkPair(salesSteamCorrespondences, first, second);
                    default:
                        return 0;
                }

            case STEAM:

                switch (second.getSource()) {
                    case WIKIDATA:
                        return checkPair(wikidataSteamCorrespondences, second, first);
                    case SALES:
                        return checkPair(salesSteamCorrespondences, second, first);
                    default:
                        return 0;
                }

            default:
                return 0;
        }
    }

    /**
     * checks if the given pair is in the given correspondences list
     *
     * @param correspondenceList correspondences list to check
     * @param first              VideoGame 1
     * @param second             VideoGame 2
     * @return search result
     */
    private static double checkPair(List<PlatformCorrespondence> correspondenceList, VideoGame first, VideoGame second) {

        for (PlatformCorrespondence platformCorrespondence : correspondenceList) {
            if (platformCorrespondence.platform1.equals(first.getPlatform())
                    && platformCorrespondence.platform2.equals(second.getPlatform())) {
                return 1;
            }
        }
        return 0;

    }

    /**
     * wrapper to store to platform values
     */
    private static class PlatformCorrespondence {

        String platform1;
        String platform2;

        private PlatformCorrespondence(String platform1, String platform2) {
            this.platform1 = platform1;
            this.platform2 = platform2;
        }

    }

    /**
     * reads correspondence pairs from a csv into a given list
     *
     * @param csvPath    csv path
     * @param header     csv header
     * @param targetList target list
     */
    private static void readFileIntoList(String csvPath, String header, List<PlatformCorrespondence> targetList) {

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.equals(header)) {
                    String[] elements = line.split(";");
                    targetList.add(new PlatformCorrespondence(elements[0], elements[1]));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
