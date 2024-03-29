package org.aksw.tsoru.qatest2;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class QATest2 {
	
	public static final String[] QUERY = {
		"How did serfdom develop in and then leave Russia ?",
//		"Baggio jersey number",
//		"Barack Obama birth place",
//		"Currencies of all G8 countries",
//		"In which city was the assassin of Martin Luther King born?",
//		"How many Golden Globe awards did the husband of Katie Holmes win?",
//		"Which recipients of the Victoria Cross died in the Battle of Arnhem?",
//		"Where did the first man in space die?",
//		"How old was Steve Job's sister when she first met him?",
//		"Which members of the Wu-Tang Clan took their stage name from a movie?",
//		"Which writers had influenced the philosopher that refused a Nobel Prize?",
//		"Under which king did the British prime minister that signed the Munich agreement serve?",
//		"Who composed the music for the film that depicts the early life of Jane Austin?"
	};
	
	private static final Logger LOGGER = Logger.getLogger(QATest2.class);
	
	private static final boolean ENABLE_INDEXING = false;

	public static void main(String[] args) throws ParseException {
		
		if(ENABLE_INDEXING) {
			Corpus.extractOntologyLabels();
			Corpus.extractLabels();
			LabelIndex.index();
		}
		
		LabelIndex.initSearch();
		
		
		for(String q : QUERY) {
			LOGGER.info("Query: '"+q+"'");
			
//			Something res = NLPStuff.subjectsJSON(q);
			Something res = NLPStuff.subjects(q);
			
			String str = res.toString();
			
			LOGGER.info(str + " => " + LabelIndex.search(str));
			
		}
	}

}
