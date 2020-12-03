package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.custom_conflict_resolution_functions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.GameMode;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

public class GameModeResolution<ValueType, RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable>
        extends ConflictResolutionFunction<List<ValueType>, RecordType, SchemaElementType> {

    private static boolean gameModeCorrespondencesLoaded = false;
    private static final List<GameModeCorrespondences> gameModeCorrespondences = new ArrayList<>();

    public GameModeResolution() {
        if (!gameModeCorrespondencesLoaded) {
            readFileIntoList("data/input/gamemode_correspondences.csv", gameModeCorrespondences);
        }

    }

    @Override
    public FusedValue<List<ValueType>, RecordType, SchemaElementType> resolveConflict(
            Collection<FusibleValue<List<ValueType>, RecordType, SchemaElementType>> values) {
        HashSet<ValueType> union = new HashSet<>();
        HashSet<ValueType> cleansedUnion = new HashSet<>();

        for (FusibleValue<List<ValueType>, RecordType, SchemaElementType> value : values) {
            union.addAll(value.getValue());
        }

        for (ValueType element : union) {
            ValueType gameMode = (ValueType) checkGameMode(gameModeCorrespondences, (GameMode) element);
            cleansedUnion.add((ValueType) gameMode);
        }

        List<ValueType> list = new LinkedList<>(cleansedUnion);
        FusedValue<List<ValueType>, RecordType, SchemaElementType> fused = new FusedValue<>(list);

        for (FusibleValue<List<ValueType>, RecordType, SchemaElementType> value : values) {
            fused.addOriginalRecord(value);
        }
        return fused;
    }

    private static void readFileIntoList(String filePath, List<GameModeCorrespondences> targetList) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(";");
                targetList.add(new GameModeCorrespondences(elements[0], elements[1]));
            }

        } catch (FileNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    private static class GameModeCorrespondences {
        String gameMode1;
        String gameMode2;

        private GameModeCorrespondences(String gameMode1, String gameMode2) {
            this.gameMode1 = gameMode1;
            this.gameMode2 = gameMode2;
        }
    }

    private static GameMode checkGameMode(List<GameModeCorrespondences> correspondenceList, GameMode gameMode) {
        for (GameModeCorrespondences gameModeCorrespondence : correspondenceList) {
            if (gameModeCorrespondence.gameMode1.equals(gameMode.getGameMode())) {
                return gameMode;
            } else if (gameModeCorrespondence.gameMode2.equals(gameMode.getGameMode())) {
                gameMode.setGameMode(gameModeCorrespondence.gameMode1);
                return gameMode;

            }
        }
        return gameMode;
    }

}
