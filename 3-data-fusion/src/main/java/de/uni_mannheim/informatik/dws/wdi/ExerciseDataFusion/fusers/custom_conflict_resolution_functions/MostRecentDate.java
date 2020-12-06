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

        if (values.size() == 0) {
            return new FusedValue((Object)null);
        } else {
            LocalDate max = null;
            double count = 0.0D;

            for(Iterator var7 = values.iterator(); var7.hasNext(); ++count) {
                FusibleValue<LocalDate, RecordType, SchemaElementType> value = (FusibleValue)var7.next();
                LocalDate dateValue = value.getValue();
                if (max == null) {
                    max = dateValue;
                } else {
                    max = dateValue.compareTo(max) > 0 ? dateValue : max;
                }
            }

            return new FusedValue(max);
        }

    }
}
