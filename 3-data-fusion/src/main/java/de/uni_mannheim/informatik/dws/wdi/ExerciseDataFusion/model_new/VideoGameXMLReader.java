package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class VideoGameXMLReader extends XMLMatchableReader<VideoGame, Attribute> implements FusibleFactory<VideoGame, Attribute> {

    @Override
    protected void initialiseDataset(DataSet<VideoGame, Attribute> dataset) {
        super.initialiseDataset(dataset);
    dataset.addAttribute(VideoGame.TITLE);
    dataset.addAttribute(VideoGame.PLATFORM);
    dataset.addAttribute(VideoGame.PUBLISHER);
    dataset.addAttribute(VideoGame.PUBLISHING_DATE);
    dataset.addAttribute(VideoGame.DEVELOPER);
    dataset.addAttribute(VideoGame.AGE);
    dataset.addAttribute(VideoGame.USK_RATING);
    dataset.addAttribute(VideoGame.PEGI_RATING);
    dataset.addAttribute(VideoGame.GENRES);
    dataset.addAttribute(VideoGame.GAME_MODES);
    dataset.addAttribute(VideoGame.PRICE);
    }

    @Override
    public VideoGame createModelFromElement(Node node, String provenanceInfo) {
        String id = getValueFromChildElement(node, "id");

        VideoGame videoGame = new VideoGame(id, provenanceInfo);

        videoGame.setTitle(getValueFromChildElement(node, "title"));
        videoGame.setPlatform(getValueFromChildElement(node, "platform"));
        videoGame.setPublisher(getValueFromChildElement(node, "publisher"));
        videoGame.setDeveloper(getValueFromChildElement(node, "developer"));
        videoGame.setPrice(getValueFromChildElement(node, "price"));
        videoGame.setAge(getValueFromChildElement(node, "age"));
        videoGame.setUskRating(getValueFromChildElement(node, "usk_rating"));
        videoGame.setPegiRating(getValueFromChildElement(node, "pegi_rating"));

        List<Genre> genres = getObjectListFromChildElement(node, "genres", "genre", new GenreXMLReader(),
                provenanceInfo);
        List<GameMode> gameModes = getObjectListFromChildElement(node, "gamemodes", "gamemode", new GameModeXMLReader(),
                provenanceInfo);

        videoGame.setGenres(genres);
        videoGame.setGameModes(gameModes);

        try {
            String year = getValueFromChildElement(node, "publishingdate");
            if (year != null && !year.isEmpty()) {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyy")
                        .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1).parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
                        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                        .toFormatter(Locale.ENGLISH);
                LocalDateTime y = LocalDateTime.parse(year, formatter);
                videoGame.setPublishingDate(y.toLocalDate());
            }
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println(String.format("%s - %s - %s", getValueFromChildElement(node, "id"),
                    getValueFromChildElement(node, "title"), getValueFromChildElement(node, "publishingdate")));
        }

        return videoGame;
    }


    @Override
    public VideoGame createInstanceForFusion(RecordGroup<VideoGame, Attribute> recordGroup) {

        List<String> ids = new LinkedList<>();
        for (VideoGame videoGame : recordGroup.getRecords()) {
            ids.add(videoGame.getIdentifier());
        }
        Collections.sort(ids);
        String mergedId = StringUtils.join(ids, '+');
        return new VideoGame(mergedId, "fused");

    }

}
