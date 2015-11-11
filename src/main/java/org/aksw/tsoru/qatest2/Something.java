package org.aksw.tsoru.qatest2;

import java.util.ArrayList;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class Something {
	
	private ArrayList<String> compounds = new ArrayList<String>();
	
	public Something() {
		super();
	}

	public void addCompound(String el) {
		compounds.add(el);
	}
	
	public String toString() {
		String str = "";
		if(compounds.isEmpty())
			return str;
		for(String s : compounds)
			str += s + " ";
		return str.trim();
	}

}
