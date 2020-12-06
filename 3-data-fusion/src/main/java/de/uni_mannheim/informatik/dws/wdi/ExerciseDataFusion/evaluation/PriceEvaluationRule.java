package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.VideoGame;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.PercentageSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

public class PriceEvaluationRule extends EvaluationRule<VideoGame, Attribute>{

	SimilarityMeasure<Double> sim = new PercentageSimilarity(0.25);

	@Override
	public boolean isEqual(VideoGame record1, VideoGame record2, Attribute schemaElement) {
		// the title is correct if all tokens are there, but the order does not
		// matter
		if (!record1.getPrice().equals("") && !record2.getPrice().equals("")) {
			return sim.calculate(Double.valueOf(record1.getPrice()), Double.valueOf(record2.getPrice())) >= 0.7;
		} else {
			return false;
		}
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

