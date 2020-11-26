package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class GenreXMLFormatter extends XMLFormatter<Genre> {

    @Override
    public Element createRootElement(Document doc) {
        return doc.createElement("genres");
    }

    @Override
    public Element createElementFromRecord(Genre record, Document doc) {
        Element genre = createTextElement("genre", record.getGenre(), doc);
        return genre;
    }

}
