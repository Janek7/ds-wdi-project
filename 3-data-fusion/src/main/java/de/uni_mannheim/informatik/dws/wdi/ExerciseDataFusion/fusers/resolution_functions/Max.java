package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.resolution_functions;

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
        if (values.size() == 0) {
            return new FusedValue((Object)null);
        } else {
            double max = 0.0D;
            double count = 0.0D;

            for(Iterator var7 = values.iterator(); var7.hasNext(); ++count) {
                FusibleValue<Double, RecordType, SchemaElementType> value = (FusibleValue)var7.next();
                double doubleValue = (Double)value.getValue();
                max = doubleValue > max ? doubleValue : max;
            }

            return new FusedValue(max);
        }
    }
}
