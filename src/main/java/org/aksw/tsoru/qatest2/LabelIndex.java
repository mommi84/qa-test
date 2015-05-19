package org.aksw.tsoru.qatest2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import com.hp.hpl.jena.graph.Triple;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class LabelIndex {
	
	private static HashMap<String, String> map = new HashMap<String, String>();
	
	private static TheIndex index = new TheIndex();

	public static void add(Triple arg0) {
		map.put(arg0.getObject().toString(), arg0.getSubject().getURI());
	}

	public static String print() {
		return "Map size = " + map.size();
	}

	public static String getURI(String label) {
		return map.get(label);
	}
	
	public static void index() {
		try {
			index.write(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void initSearch() {
		try {
			index.initSearch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Document search(String q) {
		try {
			ArrayList<Document> arr = index.search(q);
			if(arr.isEmpty())
				return null;
			return arr.get(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
