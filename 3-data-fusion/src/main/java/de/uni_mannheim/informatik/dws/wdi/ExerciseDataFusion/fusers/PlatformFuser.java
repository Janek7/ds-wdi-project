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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PlatformFuser extends AttributeValueFuser<String, VideoGame, Attribute> {

    private static List<PlatformBlockingKeyPair> platformBlockingKeyPairList = loadPlatformBlockingKeyList();
    private static List<BlockingKeyTargetValuePair> blockingKeyTargetValuePairList = loadBlockingKeyTargetValueList();

	public PlatformFuser(ConflictResolutionFunction resolutionFunction) {
		super(resolutionFunction);
	}

	@Override
	public void fuse(RecordGroup<VideoGame, Attribute> group, VideoGame fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {

		// get the fused value
		FusedValue<String, VideoGame, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		
		// set the value for the fused record
		fusedRecord.setPlatform(fused.getValue());

		// add provenance info
		fusedRecord.setAttributeProvenance(VideoGame.PLATFORM, fused.getOriginalIds());
	}

	@Override
	public boolean hasValue(VideoGame record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(VideoGame.PLATFORM);
	}

	@Override
	public String getValue(VideoGame record, Correspondence<Attribute, Matchable> correspondence) {
		return getPlatformTargetValue(record.getPlatform());
	}

	// helper stuff

    /**
     * looks if there is a custom blocking key for the platform values, if yes retrieve the associated target value for
     * fusion
     * @param platformSourceValue platform source value
     * @return target value
     */
    private static String getPlatformTargetValue(String platformSourceValue) {

	    // lookup blocking key
	    String blockingKey = null;
	    for (PlatformBlockingKeyPair platformBlockingKeyPair : platformBlockingKeyPairList) {
	        if (platformBlockingKeyPair.platform.equals(platformSourceValue)) {
	            blockingKey = platformBlockingKeyPair.blockingKey;
	            break;
            }
        }

        if (blockingKey != null) {

            //lookup target value
            String targetValue = null;
            for (BlockingKeyTargetValuePair blockingKeyTargetValuePair : blockingKeyTargetValuePairList) {
                if (blockingKeyTargetValuePair.blockingKey.equals(blockingKey)) {
                    targetValue = blockingKeyTargetValuePair.targetValue;
                }
            }

            if (targetValue != null) {
                return targetValue;
            } else {
                return platformSourceValue;
            }


        } else {
            return platformSourceValue;
        }

    }

    // helper classes

	private static class PlatformBlockingKeyPair {

		private String platform;
		private String blockingKey;

		private PlatformBlockingKeyPair(String platform, String blockingKey) {
			this.platform = platform;
			this.blockingKey = blockingKey;
		}

	}

	private static List<PlatformBlockingKeyPair> loadPlatformBlockingKeyList() {

		List<PlatformBlockingKeyPair> platformBlockingKeyPairList = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader("data/platform/platform_blocking_keys.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] elements = line.split(";");
				platformBlockingKeyPairList.add(new PlatformBlockingKeyPair(elements[0], elements[1]));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return platformBlockingKeyPairList;

	}

	private static class BlockingKeyTargetValuePair {

		private String blockingKey;
		private String targetValue;

		private BlockingKeyTargetValuePair(String blockingKey, String targetValue) {
			this.blockingKey = blockingKey;
			this.targetValue = targetValue;
		}

	}

	private static List<BlockingKeyTargetValuePair> loadBlockingKeyTargetValueList() {

		List<BlockingKeyTargetValuePair> blockingKeyTargetValuePairList = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader("data/platform/platform_blocking_keys.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] elements = line.split(";");
				blockingKeyTargetValuePairList.add(new BlockingKeyTargetValuePair(elements[0], elements[1]));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return blockingKeyTargetValuePairList;

	}

}
