package org.aksw.tsoru.qatest2.arq;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class SparqlAlgebra {
	
	private static Pattern p = Pattern.compile("\\s+");

	public static void main(String[] args) {

		String q = "SELECT ?a WHERE { "
				+ "<http://dbpedia.org/resource/Serfdom> <http://dbpedia.org/ontology/abstract> ?a . "
				+ "FILTER(LANG(?a) = \"en\") }";
		System.out.println(q);
		System.out.println(algebraExpression(q));

	}

	public static String algebraExpression(String queryString) {

		Query query = QueryFactory.create(queryString);
		Op op = Algebra.compile(query);
		Matcher m = p.matcher(op.toString()); 
		return m.replaceAll(" ");

	}

}
