package de.uni_mannheim.informatik.web_data_integration.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class GameModeXMLFormatter extends XMLFormatter<GameMode> {

    @Override
    public Element createRootElement(Document doc) {
        return doc.createElement("gamemodes");
    }

    @Override
    public Element createElementFromRecord(GameMode record, Document doc) {
        Element gameMode = createTextElement("gamemode", record.getGameMode(), doc);
        return gameMode;
    }

}
