package org.aksw.tsoru.qatest2;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class QATest2 {
	
	private static final String QUERY = "Barack Obama birth place";
	
	private static final Logger LOGGER = Logger.getLogger(QATest2.class);

	public static void main(String[] args) throws ParseException {
		
		Corpus.extractLabels();
		
		LabelIndex.index();
		
		LOGGER.info("Query: '"+QUERY+"'");
		
		Something res = NLPStuff.subjects(QUERY);
		
		String p = res.get();
		LOGGER.info(p + " => " + LabelIndex.search(p));
	}

}
