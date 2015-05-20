package org.aksw.tsoru.qatest2;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class QATest2 {
	
	public static final String QUERY = "Barack Obama birth place";
	
	private static final Logger LOGGER = Logger.getLogger(QATest2.class);
	
	private static final boolean ENABLE_INDEXING = false;

	public static void main(String[] args) throws ParseException {
		
		if(ENABLE_INDEXING) {
			Corpus.extractOntologyLabels();
			Corpus.extractLabels();
			LabelIndex.index();
		}
		
		LabelIndex.initSearch();
		
		LOGGER.info("Query: '"+QUERY+"'");
		
		Something res = NLPStuff.subjects(QUERY);
		
		String p = res.get();
		LOGGER.info(p + " => " + LabelIndex.search(p));
		
		LOGGER.info("birth place => " + LabelIndex.search("birth place"));
	}

}
