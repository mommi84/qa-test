package org.aksw.tsoru.qatest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceRequiredException;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class DatasetBuilder implements Runnable {
	
	private final static Logger logger = Logger.getLogger("qa-test");

	private Sparql sparql = new Sparql("http://dbpedia.org/sparql", "http://dbpedia.org");
	
	private TreeSet<Fact> facts = new TreeSet<Fact>();
	private HashMap<String, String> labels = new HashMap<String, String>();
	
	public DatasetBuilder() {
		super();
		labels.putAll(Settings.PREDEFINED_LABELS);
	}
	
	public TreeSet<Fact> getFacts() {
		return facts;
	}
	
	public HashMap<String, String> getLabels() {
		return labels;
	}

	private TreeSet<String> visited = new TreeSet<String>();

	public void run() {
		
		String argRes = "http://dbpedia.org/resource/Aristotle";
		
		StmtIterator it = callDescribe(argRes);
		
		Set<Statement> stSet = it.toSet();
		int n = stSet.size();
		
		Iterator<Statement> stIt = stSet.iterator();
		
		logger.info("Total of "+n+" triples.");
		
		for(int i=0; stIt.hasNext(); i++) {
			Statement triple = stIt.next();
			Resource s = triple.getSubject();
			Property p = triple.getPredicate();
			RDFNode o = triple.getObject();
			
			if(Settings.META_PROPERTIES.contains(p.getURI()))
				continue;
			
			// TODO if (p=type and o=rdf:Property) ...
			
			saveLabel(s, p, o);
			
			facts.addAll(getConstellation(p.getURI()));
			
			Resource oRes = null;
			Resource toGet = null;
			try {
				oRes = o.asResource();
				if(s.getURI().equals(argRes))
					toGet = oRes;
				else 
					toGet = s;
			} catch (ResourceRequiredException e) {
				// argument resource is in subject
			}
			if(toGet != null)
				facts.addAll(getConstellation(toGet.getURI()));
			
			if(i % 10 == 0)
				logger.info("====== Processed: " + i + " / " + n + " ======");
			
			// TODO remove me
			if(i == 10)
				break;
		}
		
		logger.info("Total facts collected: "+facts.size());
	}

	private void saveLabel(Resource s, Property p, RDFNode o) {
		
		if(p.getURI().equals("http://www.w3.org/2000/01/rdf-schema#label"))
			labels.put(s.getURI()+"@"+o.asLiteral().getLanguage(), o.asLiteral().getString());
		
	}

	private TreeSet<Fact> getConstellation(String uriToGet) {
		
		TreeSet<Fact> fs = new TreeSet<Fact>();
		
		StmtIterator it = callDescribe(uriToGet);
		if(it != null) {
			logger.info("Getting <" + uriToGet + ">...");
			while(it.hasNext()) {
				Statement triple = it.next();
				Resource s = triple.getSubject();
				Property p = triple.getPredicate();
				RDFNode o = triple.getObject();
				
				if(Settings.META_PROPERTIES.contains(p.getURI()))
					continue;
				
				saveLabel(s, p, o);
				
				fs.add(new Fact(triple));
			}
			logger.info(fs.size()+" facts in temp list: "+fs);
		}
		
		return fs;
	}
	
	private StmtIterator callDescribe(String uri) {
		
		if(visited.contains(uri)) {
			logger.info("URI already visited: <" + uri + ">");
			return null;
		} else {
			visited.add(uri);
			return sparql.describe(uri);
		}
		
	}

}
