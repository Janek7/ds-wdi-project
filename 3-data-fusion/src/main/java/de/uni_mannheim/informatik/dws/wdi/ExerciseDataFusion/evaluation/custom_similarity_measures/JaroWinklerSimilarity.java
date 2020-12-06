package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.custom_similarity_measures;

import com.wcohen.ss.JaroWinkler;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;

public class JaroWinklerSimilarity extends SimilarityMeasure<String> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public double calculate(String first, String second) {
        // TODO Auto-generated method stub
        if (first == null || second == null) {
            return 0.0;
        } else {
            JaroWinkler jw = new JaroWinkler();
            double score = jw.score(first, second);
            return score;

        }
    }

}
