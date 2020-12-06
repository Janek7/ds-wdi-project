package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.VideoGame;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.DeviationSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.PercentageSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

public class AgeEvaluationRule extends EvaluationRule<VideoGame, Attribute>{


	@Override
	public boolean isEqual(VideoGame record1, VideoGame record2, Attribute schemaElement) {
		if(record1.getAge()==null && record2.getAge()==null)
			return true;
		else if(record1.getAge()==null ^ record2.getAge()==null)
			return false;
		else
			return record1.getAge().equals(record2.getAge());
	}

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.datafusion.EvaluationRule#isEqual(java.lang.Object, java.lang.Object, de.uni_mannheim.informatik.wdi.model.Correspondence)
	 */
	@Override
	public boolean isEqual(VideoGame record1, VideoGame record2,
			Correspondence<Attribute, Matchable> schemaCorrespondence) {
		return isEqual(record1, record2, (Attribute)null);
	}
	
}