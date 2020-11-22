package de.uni_mannheim.informatik.web_data_integration.blocking;

import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGame;

public class VideoGameBlockingKeyByTitlePlatformGenerator extends RecordBlockingKeyGenerator<VideoGame, Attribute> {
	
	private static final long serialVersionUID = 1L;


	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.matching.blocking.generators.BlockingKeyGenerator#generateBlockingKeys(de.uni_mannheim.informatik.wdi.model.Matchable, de.uni_mannheim.informatik.wdi.model.Result, de.uni_mannheim.informatik.wdi.processing.DatasetIterator)
	 */
	@Override
	public void generateBlockingKeys(VideoGame record, Processable<Correspondence<Attribute, Matchable>> correspondences,
			DataIterator<Pair<String, VideoGame>> resultCollector) {
		
		String title = record.getTitle();

		char[] titleAsChars = new char[title.length()]; 
        for (int i = 0; i < title.length(); i++) { 
            titleAsChars[i] = title.charAt(i); 
        }
        String titleShort="";
		
		if (Character.compare(titleAsChars[0],'T') == 0 && Character.compare(titleAsChars[1],'h') ==0 ) {
			StringBuilder s = new StringBuilder(titleAsChars.length-3);
			for (int i=4; i<titleAsChars.length; i++) {
			   s.append(titleAsChars[i]);
			}
			  titleShort = s.toString();
		}
		else {
			titleShort=record.getTitle();
		}
		
		
		String[] tokens  = titleShort.split(" ");

		String blockingKeyValue = "";

		for(int i = 0; i <= 2 && i < tokens.length; i++) {
			blockingKeyValue += tokens[i].substring(0, Math.min(2,tokens[i].length())).toUpperCase();
		}

		if (record.getPlatform() != null) {
            blockingKeyValue += record.getPlatform().charAt(0);
        }

		resultCollector.next(new Pair<>(blockingKeyValue, record));
	}
	

}
