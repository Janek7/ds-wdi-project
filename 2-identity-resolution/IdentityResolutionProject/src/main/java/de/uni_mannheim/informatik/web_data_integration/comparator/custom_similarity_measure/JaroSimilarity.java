package de.uni_mannheim.informatik.web_data_integration.comparator.custom_similarity_measure;

import com.wcohen.ss.Jaro;

import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;

public class JaroSimilarity extends SimilarityMeasure<String> {

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
            Jaro j = new Jaro();
            double score = j.score(first, second);
            return score;
        }
    }

}
