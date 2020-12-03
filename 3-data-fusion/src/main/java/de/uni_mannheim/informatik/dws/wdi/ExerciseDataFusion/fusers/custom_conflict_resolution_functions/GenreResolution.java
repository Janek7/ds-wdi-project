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

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.Genre;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

public class GenreResolution<ValueType, RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable>
        extends ConflictResolutionFunction<List<ValueType>, RecordType, SchemaElementType> {

    private static boolean genreCorrespondencesLoaded = false;
    private static final List<GenreCorrespondences> genreCorrespondences = new ArrayList<>();

    public GenreResolution() {
        if (!genreCorrespondencesLoaded) {
            readFileIntoList("data/input/genre_correspondences.csv", genreCorrespondences);
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
            ValueType genre = (ValueType) checkGenre(genreCorrespondences, (Genre) element);
            cleansedUnion.add((ValueType) genre);
        }

        List<ValueType> list = new LinkedList<>(cleansedUnion);
        FusedValue<List<ValueType>, RecordType, SchemaElementType> fused = new FusedValue<>(list);

        for (FusibleValue<List<ValueType>, RecordType, SchemaElementType> value : values) {
            fused.addOriginalRecord(value);
        }
        return fused;
    }

    private static class GenreCorrespondences {
        String genre1;
        String genre2;

        private GenreCorrespondences(String genre1, String genre2) {
            this.genre1 = genre1;
            this.genre2 = genre2;
        }
    }

    private static void readFileIntoList(String filePath, List<GenreCorrespondences> targetList) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(";");
                targetList.add(new GenreCorrespondences(elements[0], elements[1]));
            }

        } catch (FileNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    private static Genre checkGenre(List<GenreCorrespondences> correspondenceList, Genre genre) {
        for (GenreCorrespondences genreCorrespondence : correspondenceList) {
            if (genreCorrespondence.genre1.equals(genre.getGenre())) {
                return genre;
            } else if (genreCorrespondence.genre2.equals(genre.getGenre())) {
                genre.setGenre(genreCorrespondence.genre1);
                return genre;

            }
        }
        return genre;
    }

}
