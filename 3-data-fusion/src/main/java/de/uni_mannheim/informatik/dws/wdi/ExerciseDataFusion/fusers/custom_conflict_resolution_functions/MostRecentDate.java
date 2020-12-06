package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.custom_conflict_resolution_functions;

import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;

public class MostRecentDate<RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends ConflictResolutionFunction<LocalDate, RecordType, SchemaElementType> {

    public MostRecentDate() {
    }

    public FusedValue<LocalDate, RecordType, SchemaElementType> resolveConflict(Collection<FusibleValue<LocalDate, RecordType, SchemaElementType>> values) {

        FusibleValue<LocalDate, RecordType, SchemaElementType> mostRecentDate = null;
        Iterator var4 = values.iterator();

        while(true) {
            FusibleValue<LocalDate, RecordType, SchemaElementType> value;
            do {
                if (!var4.hasNext()) {
                    return new FusedValue(mostRecentDate);
                }

                value = (FusibleValue)var4.next();
            } while(mostRecentDate != null && !value.getValue().isAfter(mostRecentDate.getValue()));

            mostRecentDate = value;
        }

    }
}
