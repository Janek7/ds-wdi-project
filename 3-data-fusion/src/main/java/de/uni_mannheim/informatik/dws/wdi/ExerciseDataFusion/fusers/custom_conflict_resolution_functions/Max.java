package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.custom_conflict_resolution_functions;

import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

import java.util.Collection;
import java.util.Iterator;

public class Max<RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends ConflictResolutionFunction<Double, RecordType, SchemaElementType> {

    public Max() {
    }

    public FusedValue<Double, RecordType, SchemaElementType> resolveConflict(Collection<FusibleValue<Double, RecordType, SchemaElementType>> values) {

        FusibleValue<Double, RecordType, SchemaElementType> max = null;
        Iterator var4 = values.iterator();

        while(true) {
            FusibleValue<Double, RecordType, SchemaElementType> value;
            do {
                if (!var4.hasNext()) {
                    return new FusedValue(max);
                }

                value = (FusibleValue)var4.next();
            } while(max != null && Double.valueOf(String.valueOf(value.getValue())) <= max.getValue());

            max = value;
        }

    }
}
