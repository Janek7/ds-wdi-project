package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class VideoGameXMLFormatter extends XMLFormatter<VideoGame> {

    GenreXMLFormatter genreFormatter = new GenreXMLFormatter();
    GameModeXMLFormatter gameModeFormatter = new GameModeXMLFormatter();

    @Override
    public Element createRootElement(Document doc) {
        return doc.createElement("videogames");
    }

    @Override
    public Element createElementFromRecord(VideoGame record, Document doc) {
        Element videoGame = doc.createElement("videogame");

        videoGame.appendChild(createTextElement("id", record.getIdentifier(), doc));
        videoGame.appendChild(createTextElement("title", record.getTitle(), doc));
        videoGame.appendChild(createTextElement("platform", record.getPlatform(), doc));
        videoGame.appendChild(createTextElement("publisher", record.getPublisher(), doc));
        videoGame.appendChild(createTextElement("publishingdate", record.getPublishingDate()!=null ? record.getPublishingDate().toString() : "", doc));
        videoGame.appendChild(createTextElement("developer", record.getDeveloper(), doc));
        videoGame.appendChild(createTextElement("usk_rating", record.getUskRating(), doc));
        videoGame.appendChild(createTextElement("pegi_rating", record.getPegiRating(), doc));
        videoGame.appendChild(createGenresElement(record, doc));
        videoGame.appendChild(createGameModesElement(record, doc));
        // videoGame.appendChild(createTextElement(name, value, doc));
        return videoGame;
    }

    protected Element createTextElementWithProvenance(String name, String value, String provenance, Document doc) {
        Element elem = createTextElement(name, value, doc);
        elem.setAttribute("provenance", provenance);
        return elem;
    }

    protected Element createGenresElement(VideoGame record, Document doc) {
        Element genreRoot = genreFormatter.createRootElement(doc);

        for (Genre g : record.getGenres()) {
            genreRoot.appendChild(genreFormatter.createElementFromRecord(g, doc));
        }
        return genreRoot;
    }

    protected Element createGameModesElement(VideoGame record, Document doc) {
        Element gameModeRoot = gameModeFormatter.createRootElement(doc);

        for (GameMode g : record.getGameModes()) {
            gameModeRoot.appendChild(gameModeFormatter.createElementFromRecord(g, doc));
        }
        return gameModeRoot;
    }

}
