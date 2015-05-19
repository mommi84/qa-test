package org.aksw.tsoru.qatest2;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.log4j.Logger;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.core.Quad;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class Corpus {
	
	private static final String DATASET = "../../keydiscovery/rocker/data/persondata_en.nt";
	
	private static final Logger LOGGER = Logger.getLogger(Corpus.class);

	public static void extractLabels() {
		
		StreamRDF dest = new StreamRDF() {

			public void base(String arg0) {}

			public void finish() {
				LOGGER.info("Done.");
			}

			public void prefix(String arg0, String arg1) {}

			public void quad(Quad arg0) {}

			public void start() {
				LOGGER.info("Started.");
			}

			public void triple(Triple arg0) {
				if(arg0.getPredicate().getURI().equals(URLs.RDFS_LABEL) || 
						arg0.getPredicate().getURI().equals(URLs.FOAF_NAME))
					if(arg0.getObject().getLiteralLanguage().matches("en"))
						LabelIndex.add(arg0);
			}
		};
		
		RDFDataMgr.parse(dest, DATASET);

		LOGGER.info(LabelIndex.print());
		
	}

}
