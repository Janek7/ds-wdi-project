package de.uni_mannheim.informatik.web_data_integration.model;

import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class GameModeXMLReader extends XMLMatchableReader<GameMode, Attribute> {

    @Override
    public GameMode createModelFromElement(Node node, String provenanceInfo) {
        String id = getValueFromChildElement(node, "id");

        GameMode gameMode = new GameMode(id, provenanceInfo);

        gameMode.setGameMode(node.getTextContent());
        return gameMode;
    }

}
