package org.aksw.tsoru.qatest;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class Fact implements Comparable<Fact> {
	
	private Statement statement;
	
	private Resource subject;
	private Property predicate;
	private RDFNode object;
	
	/**
	 * Natural language expression (test only: will be replaced by synsets).
	 */
	private String nlexp;

	public Fact(Statement s) {
		super();
		this.statement = s;
		this.subject = s.getSubject();
		this.predicate = s.getPredicate();
		this.object = s.getObject();
	}

	public Resource getSubject() {
		return subject;
	}

	public Property getPredicate() {
		return predicate;
	}

	public RDFNode getObject() {
		return object;
	}

	public String getNlexp() {
		return nlexp;
	}

	public int compareTo(Fact o) {
		return this.toString().compareTo(o.toString());
	}

	public String toString() {
		return "<" + this.subject + "> <" + this.predicate + "> <" + this.object +">";
	}

	public Statement getStatement() {
		return statement;
	}
	
}
