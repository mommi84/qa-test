package org.aksw.tsoru.qatest;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * This is just to test a micro-model based on few data.
 *  
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class QATest {

	public static void main(String[] args) {
		
		DatasetBuilder dsb = new DatasetBuilder();
		dsb.run();
		
		TreeSet<Fact> facts = dsb.getFacts();
		HashMap<String, String> labels = dsb.getLabels();
		
		for(Fact fact : facts) {
			System.out.println(fact.getSubject().getURI()+"\t"+labels.get(fact.getSubject().getURI()+"@en"));
			System.out.println(fact.getPredicate().getURI()+"\t"+labels.get(fact.getPredicate().getURI()+"@en"));
			System.out.println(fact.getObject().toString()+"\t"+labels.get(fact.getObject().toString()+"@en"));
			System.out.println("--");
		}
		
	}

}
