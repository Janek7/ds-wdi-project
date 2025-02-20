/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.VideoGame;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;


public class AgeFuser extends AttributeValueFuser<Double, VideoGame, Attribute> {

	public AgeFuser(ConflictResolutionFunction resolutionFunction) {
		super(resolutionFunction);
	}

	@Override
	public void fuse(RecordGroup<VideoGame, Attribute> group,
                     VideoGame fusedRecord,
                     Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences,
                     Attribute schemaElement) {

		// get the fused value
		FusedValue<Double, VideoGame, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		try {
			// set the value for the fused record
            String ageFormatted = String.valueOf((int) Math.round(fused.getValue()));
			fusedRecord.setAge(ageFormatted);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		

		// add provenance info
		fusedRecord.setAttributeProvenance(VideoGame.AGE, fused.getOriginalIds());
	}

	@Override
	public boolean hasValue(VideoGame record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(VideoGame.AGE);
	}

	@Override
	public Double getValue(VideoGame record, Correspondence<Attribute, Matchable> correspondence) {
		return Double.valueOf(record.getAge());
	}

}
