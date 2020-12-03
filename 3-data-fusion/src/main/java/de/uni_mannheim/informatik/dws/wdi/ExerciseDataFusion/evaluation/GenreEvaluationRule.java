package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation;

import java.util.HashSet;
import java.util.Set;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.Genre;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new.VideoGame;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class GenreEvaluationRule extends EvaluationRule<VideoGame, Attribute> {

	@Override
	public boolean isEqual(VideoGame record1, VideoGame record2, Attribute schemaElement) {
		Set<String> genre1 = new HashSet<>();
		Set<String> genre2 = new HashSet<>();

		for (Genre g : record1.getGenres()) {
			genre1.add(g.getGenre());
		}

		for (Genre g : record2.getGenres()) {
			genre2.add(g.getGenre());
		}

		// return gameModes1.containsAll(gameModes2) &&
		// gameModes2.containsAll(gameModes1);
		if (genre1.size() < genre2.size()) {
			return genre2.containsAll(genre1);
		} else if (genre1.size() > genre2.size()) {
			return genre1.containsAll(genre2);
		}
		return genre1.containsAll(genre2) && genre2.containsAll(genre1);
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
