package de.uni_mannheim.informatik.web_data_integration.blocking;

import de.uni_mannheim.informatik.web_data_integration.matching_rules.IR_using_linear_combination_janek;

import java.io.FileWriter;

public class BlockingTest {

    public static void main(String[] args) throws Exception {

        blockingTestWikidataSales();

    }

    private static void blockingTestWikidataSales() throws Exception {

        int neighborHoodSize = 100;
        while (neighborHoodSize <= 160) {
            long startTime = System.currentTimeMillis();
            ExecutionResult executionResult = IR_using_linear_combination_janek.identityResolution(neighborHoodSize);
            long stopTime = System.currentTimeMillis();
            int executionSeconds = (int) ((stopTime - startTime) / 1000);
            FileWriter csv = new FileWriter("data\\output\\blocking_test\\neighborhood_wikidata_sales.csv",true);
            csv.write(String.valueOf(neighborHoodSize) + ","
                    + String.valueOf(executionResult.getReductionRatio() ) + ","
                    + String.valueOf(executionSeconds) + ","
                    + String.valueOf(executionResult.getF1())+ System.getProperty( "line.separator" ));
            csv.flush();
            csv.close();
            neighborHoodSize += 10;
            System.out.println("TEST WITH NEIGHBORHOODSIZE " + neighborHoodSize + " FINISHED");
        }

    }

    private static void blockingTestSteamSales() throws Exception {

        int neighborHoodSize = 100;
        while (neighborHoodSize <= 160) {
            long startTime = System.currentTimeMillis();
            ExecutionResult executionResult = IR_using_linear_combination_janek.identityResolution(neighborHoodSize);
            long stopTime = System.currentTimeMillis();
            int executionSeconds = (int) ((stopTime - startTime) / 1000);
            FileWriter csv = new FileWriter("data\\output\\blocking_test\\neighborhood_steam_sales.csv",true);
            csv.write(String.valueOf(neighborHoodSize) + ","
                    + String.valueOf(executionResult.getReductionRatio() ) + ","
                    + String.valueOf(executionSeconds) + ","
                    + String.valueOf(executionResult.getF1())+ System.getProperty( "line.separator" ));
            csv.flush();
            csv.close();
            neighborHoodSize += 10;
            System.out.println("TEST WITH NEIGHBORHOODSIZE " + neighborHoodSize + " FINISHED");
        }

    }

}
