package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation;

import java.util.HashSet;
import java.util.Set;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.GameMode;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.Genre;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.VideoGame;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class GameModeEvaluationRule extends EvaluationRule<VideoGame, Attribute> {

	@Override
	public boolean isEqual(VideoGame record1, VideoGame record2, Attribute schemaElement) {
		Set<String> games1 = new HashSet<>();

		for (GameMode a : record1.getGameModes()) {
			// note: evaluating using the actor's name only suffices for simple
			// lists
			// in your project, you should have actor ids which you use here
			// (and in the identity resolution)
			games1.add(a.getGameMode());
		}

		Set<String> games2 = new HashSet<>();
		for (GameMode a : record2.getGameModes()) {
			games2.add(a.getGameMode());
		}

		return games1.containsAll(games2) && games2.containsAll(games1);
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
