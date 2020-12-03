package de.uni_mannheim.informatik.web_data_integration.blocking;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.web_data_integration.comparator.custom_similarity_measure.VideoGamePlatformSimilarity;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGame;

public class VideoGameBlockingKeyByTitleAndPlatformGenerator extends RecordBlockingKeyGenerator<VideoGame, Attribute> {

    private static final long serialVersionUID = 1L;
    private static List<PlatformKey> platformKeyList = readFileIntoList();


    /* (non-Javadoc)
     * @see de.uni_mannheim.informatik.wdi.matching.blocking.generators.BlockingKeyGenerator#generateBlockingKeys(de.uni_mannheim.informatik.wdi.model.Matchable, de.uni_mannheim.informatik.wdi.model.Result, de.uni_mannheim.informatik.wdi.processing.DatasetIterator)
     */
    @Override
    public void generateBlockingKeys(VideoGame record, Processable<Correspondence<Attribute, Matchable>> correspondences,
                                     DataIterator<Pair<String, VideoGame>> resultCollector) {

        String title = record.getTitle();

        // "The" remove
        char[] titleAsChars = new char[title.length()];
        for (int i = 0; i < title.length(); i++) {
            titleAsChars[i] = title.charAt(i);
        }
        String titleShort = "";
        if (Character.compare(titleAsChars[0], 'T') == 0 && Character.compare(titleAsChars[1], 'h') == 0) {
            StringBuilder s = new StringBuilder(titleAsChars.length - 3);
            for (int i = 4; i < titleAsChars.length; i++) {
                s.append(titleAsChars[i]);
            }
            titleShort = s.toString();
        } else {
            titleShort = record.getTitle();
        }

        // first 4 tokens
        String[] tokens = titleShort.split(" ");
        String blockingKeyValue = "";
        for (int i = 0; i < tokens.length; i++) {
            blockingKeyValue += tokens[i].substring(0, Math.min(2, tokens[i].length())).toUpperCase();
        }


        // platform key
        boolean found = false;
        for (PlatformKey platformKey : platformKeyList) {
            if (record.getPlatform().equals(platformKey.platformValue)) {
                blockingKeyValue += platformKey.platformKey;
                found = true;
                break;
            }
        }
        if (!found) {
            blockingKeyValue += record.getPlatform().substring(0, 2);
        }

        // append last number token
        String titleShortWithoutBracketsToken = removeLastBracketToken(titleShort);
        String[] titleShortWithoutBracketsTokenTokens = titleShortWithoutBracketsToken.split(" ");
        String lastToken = titleShortWithoutBracketsTokenTokens[titleShortWithoutBracketsTokenTokens.length - 1];
        if (lastToken != null) {
            Pattern lastNumberPattern = Pattern.compile("\\d+|(?=[MDCLXVI])M*(C[MD]|D?C*)(X[CL]|L?X*)(I[XV]|V?I*)$");
            Matcher lastNumberMatch = lastNumberPattern.matcher(lastToken);

            if (lastNumberMatch.find()) {
                blockingKeyValue += lastToken;
            }
        }


        // save result
        resultCollector.next(new Pair<>(blockingKeyValue, record));
    }

    // read platform lists

    private static String removeLastBracketToken(String title) {

        Pattern bracketsPattern = Pattern.compile(".*(\\((.+)\\))$");
        Matcher bracketsMatcher = bracketsPattern.matcher(title);

        if (bracketsMatcher.find()) {

            String matchWithBrackets = bracketsMatcher.group(1);
            String matchInsideBrackets = bracketsMatcher.group(2);

            // check if content of the brackets is not only numbers -> then we keep it (sometimes its the year in the title)
            if (!matchInsideBrackets.matches("[-+]?\\d*\\.?\\d+")) {
                return title.replace(matchWithBrackets, "").trim();
            }

        }
        return title.trim();
    }

    private static List<PlatformKey> readFileIntoList() {

        List<PlatformKey> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data/platform_correspondences/platform_blocking_keys.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(";");
                list.add(new PlatformKey(elements[1], elements[0]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;

    }

    static class PlatformKey {
        String platformKey;
        String platformValue;

        PlatformKey(String platformKey, String platformValue) {
            this.platformKey = platformKey;
            this.platformValue = platformValue;
        }
    }


}
