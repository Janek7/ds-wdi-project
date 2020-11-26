package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new;

import java.io.Serializable;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class Genre extends AbstractRecord<Attribute> implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final Attribute GENRE = new Attribute("Genre");

    private String genre;

    public Genre(String identifier, String provenance) {
        super(identifier, provenance);
    }

    // METHODS

    @Override
    public boolean hasValue(Attribute attribute) {
        if (attribute == GENRE)
            return getGenre() != null && !getGenre().isEmpty();
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((genre == null) ? 0 : genre.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Genre other = (Genre) obj;
        if (genre == null) {
            if (other.genre != null)
                return false;
        } else if (!genre.equals(other.genre))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Genre [genre=" + genre + "]";
    }

    // GETTER AND SETTER

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

}
