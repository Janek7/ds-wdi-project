package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new;

import java.io.Serializable;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class GameMode extends AbstractRecord<Attribute> implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final Attribute GAME_MODE = new Attribute("GameMode");

    private String gameMode;

    public GameMode(String identifier, String provenance) {
        super(identifier, provenance);
    }

    // METHODS

    @Override
    public boolean hasValue(Attribute attribute) {
        if (attribute == GAME_MODE)
            return getGameMode() != null && !getGameMode().isEmpty();
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((gameMode == null) ? 0 : gameMode.hashCode());
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
        GameMode other = (GameMode) obj;
        if (gameMode == null) {
            if (other.gameMode != null)
                return false;
        } else if (!gameMode.equals(other.gameMode))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GameMode [gameMode=" + gameMode + "]";
    }

    // GETTER AND SETTER

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

}
