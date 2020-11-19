package de.uni_mannheim.informatik.web_data_integration.comparator;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.date.YearSimilarity;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGame;

public class VideoGamePubDateComparator3Years implements Comparator<VideoGame, Attribute> {

	private static final long serialVersionUID = 1L;
    private YearSimilarity sim = new YearSimilarity(3);
	
	private ComparatorLogger comparisonLog;

	@Override
	public double compare(
			VideoGame record1,
			VideoGame record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
	  
	  double similarity = 0.0;
      
      if (record1.getPublishingDate() != null && record2.getPublishingDate() != null ) {
        similarity = sim.calculate(record1.getPublishingDate(), record2.getPublishingDate());

      }
      
     
     if(this.comparisonLog != null){
         this.comparisonLog.setComparatorName(getClass().getName());
         
         if (record1.getPublishingDate() != null) {
            this.comparisonLog.setRecord1Value(record1.getPublishingDate().toString());

         } else {
              this.comparisonLog.setRecord1Value("");
         }
         
         if (record2.getPublishingDate() != null) {
           this.comparisonLog.setRecord2Value(record2.getPublishingDate().toString());

        } else {
            this.comparisonLog.setRecord2Value("");
        }
         
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