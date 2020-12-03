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
		Set<String> gameModes1 = new HashSet<>();
		Set<String> gameModes2 = new HashSet<>();

		for (GameMode g : record1.getGameModes()) {
			gameModes1.add(g.getGameMode());
		}

		for (GameMode g : record2.getGameModes()) {
			gameModes2.add(g.getGameMode());
		}

		// return gameModes1.containsAll(gameModes2) &&
		// gameModes2.containsAll(gameModes1);
		if (gameModes1.size() < gameModes2.size()) {
			return gameModes2.containsAll(gameModes1);
		} else if (gameModes1.size() > gameModes2.size()) {
			return gameModes1.containsAll(gameModes2);
		}
		return gameModes1.containsAll(gameModes2) && gameModes2.containsAll(gameModes1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uni_mannheim.informatik.wdi.datafusion.EvaluationRule#isEqual(java.lang.
	 * Object, java.lang.Object,
	 * de.uni_mannheim.informatik.wdi.model.Correspondence)
	 */
	@Override
	public boolean isEqual(VideoGame record1, VideoGame record2,
			Correspondence<Attribute, Matchable> schemaCorrespondence) {
		return isEqual(record1, record2, (Attribute) null);
	}

}
