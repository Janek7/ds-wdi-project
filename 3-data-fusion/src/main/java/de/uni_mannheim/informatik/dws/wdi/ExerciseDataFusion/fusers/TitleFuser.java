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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TitleFuser extends AttributeValueFuser<String, VideoGame, Attribute> {

	public TitleFuser(ConflictResolutionFunction resolutionFunction) {
		super(resolutionFunction);
	}

	@Override
	public void fuse(RecordGroup<VideoGame, Attribute> group, VideoGame fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {

		// get the fused value
		FusedValue<String, VideoGame, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);

		// set the value for the fused record
		fusedRecord.setTitle(fused.getValue());

		// add provenance info
		fusedRecord.setAttributeProvenance(VideoGame.TITLE, fused.getOriginalIds());
	}

	@Override
	public boolean hasValue(VideoGame record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(VideoGame.TITLE);
	}

	@Override
	public String getValue(VideoGame record, Correspondence<Attribute, Matchable> correspondence) {
		return removeLastBracketToken(record.getTitle());
	}

	private static String removeLastBracketToken(String title) {

		Pattern bracketsPattern = Pattern.compile(".*(\\((.+)\\))$");
		Matcher bracketsMatcher = bracketsPattern.matcher(title);

		if (bracketsMatcher.find()) {

			String matchWithBrackets = bracketsMatcher.group(1);
			String matchInsideBrackets = bracketsMatcher.group(2);

			// check if content of the brackets is not only numbers -> then we keep it (sometimes its the year in the title)
			if (!matchInsideBrackets.matches("[-+]?\\d*\\.?\\d+")) {
				return title.replace(matchWithBrackets, "").trim();
			}

		}
		return title.trim();
	}

	// only for test

	public static void main(String[] args) {

	    String title1 = "Need for Speed: Most Wanted (videojoc del 2005)";
        System.out.println(title1);
		System.out.println(removeLastBracketToken(title1));

        String title2 = "Need for Speed: Most Wanted (2005)";
        System.out.println(title2);
		System.out.println(removeLastBracketToken(title2));
	}

}
