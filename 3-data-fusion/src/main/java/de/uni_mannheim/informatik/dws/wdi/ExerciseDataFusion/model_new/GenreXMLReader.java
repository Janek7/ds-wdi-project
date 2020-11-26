package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new;

import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class GenreXMLReader extends XMLMatchableReader<Genre, Attribute> {

    @Override
    public Genre createModelFromElement(Node node, String provenanceInfo) {
        String id = getValueFromChildElement(node, "id");

        Genre genre = new Genre(id, provenanceInfo);

        genre.setGenre(node.getTextContent());
        return genre;
    }

}
