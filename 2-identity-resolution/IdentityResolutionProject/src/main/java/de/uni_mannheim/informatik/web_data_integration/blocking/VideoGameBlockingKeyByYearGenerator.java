package de.uni_mannheim.informatik.web_data_integration.blocking;


import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.web_data_integration.model.VideoGame;

public class VideoGameBlockingKeyByYearGenerator extends
RecordBlockingKeyGenerator<VideoGame, Attribute> {

private static final long serialVersionUID = 1L;


/* (non-Javadoc)
* @see de.uni_mannheim.informatik.wdi.matching.blocking.generators.BlockingKeyGenerator#generateBlockingKeys(de.uni_mannheim.informatik.wdi.model.Matchable, de.uni_mannheim.informatik.wdi.model.Result, de.uni_mannheim.informatik.wdi.processing.DatasetIterator)
*/
@Override
public void generateBlockingKeys(VideoGame record, Processable<Correspondence<Attribute, Matchable>> correspondences,
	DataIterator<Pair<String, VideoGame>> resultCollector) {
resultCollector.next(new Pair<>(Integer.toString(record.getPublishingDate().getYear()), record));
}

}
