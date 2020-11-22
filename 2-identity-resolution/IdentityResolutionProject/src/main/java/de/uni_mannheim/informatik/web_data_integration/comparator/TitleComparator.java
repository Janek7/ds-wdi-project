package de.uni_mannheim.informatik.web_data_integration.comparator;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGame;

public class TitleComparator implements Comparator<VideoGame, Attribute> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private SimilarityMeasure<String> stringSimilarityMeasure;

    private ComparatorLogger comparisonLog;

    public TitleComparator(SimilarityMeasure<String> stringSimilarityMeasure) {
        this.stringSimilarityMeasure = stringSimilarityMeasure;
    }

    @Override
    public double compare(VideoGame record1, VideoGame record2,
            Correspondence<Attribute, Matchable> schemaCorrespondence) {
        String s1 = record1.getTitle();
        String s2 = record2.getTitle();

        double similarity = stringSimilarityMeasure.calculate(s1, s2);

        if (this.comparisonLog != null) {
            this.comparisonLog.setComparatorName(getClass().getName());
            this.comparisonLog.setRecord1Value(s1);
            this.comparisonLog.setRecord2Value(s2);
            this.comparisonLog.setSimilarity(Double.toString(similarity));
        }
        return similarity;
    }

    @Override
    public ComparatorLogger getComparisonLog() {
        return this.comparisonLog;
    }

    @Override
    public void setComparisonLog(ComparatorLogger comparatorLog) {
        this.comparisonLog = comparatorLog;
    }

}
