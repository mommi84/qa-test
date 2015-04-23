package org.aksw.tsoru.qatest;

import org.apache.jena.riot.RiotException;
import org.apache.log4j.Logger;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class Sparql {
	
	private final static Logger logger = Logger.getLogger("qa-test");
	
	private String endpoint, graph;

	public Sparql(String endpoint, String graph) {
		super();
		this.endpoint = endpoint;
		this.graph = graph;
	}

	public StmtIterator describe(String uri) {
		String query = "DESCRIBE <"+uri+">";
		Query sparqlQuery = QueryFactory.create(query, Syntax.syntaxARQ);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, sparqlQuery, graph);
		StmtIterator it = null;
		try {
			it = qexec.execDescribe().listStatements();
		} catch (RiotException e) {
			logger.warn(e.getMessage());
		}
		return it;
	}
	
}
