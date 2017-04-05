package org.aksw.tsoru.qatest2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.aksw.tsoru.qatest2.model.NLPNode;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.stanford.nlp.trees.Tree;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class NLPStuff {

	private static final Logger LOGGER = Logger.getLogger(NLPStuff.class);
	
	public static Something subjects(String query) throws ParseException {
		Something res = new Something();
		res.addCompound("nothing");
		
		Tree tree = StanfordNLP.getBestParse(query);
		
		HashMap<String, ArrayList<String>> sparseMatrix = toSparseMatrix(tree);
		System.out.println(sparseMatrix);
		
		TreeSet<NLPNode> nodes = new TreeSet<>();
		toNodes(null, tree, nodes);
		System.out.println(nodes);
		
		
//		System.out.println(tree.constituents());
		return res;
	}

	private static void toNodes(NLPNode parent, Tree tree, TreeSet<NLPNode> nodes) {
		
		String treeName = tree.value();
		
		NLPNode node = new NLPNode(parent, treeName);
		for(NLPNode n : nodes)
			if(n.getName().equals(treeName))
				node.incr();
		nodes.add(node);
		
		for(Tree subtree : tree.children()) {
			if(!subtree.isLeaf())
				toNodes(node, subtree, nodes);
			else
				node.setValue(subtree.value());
		}
	}

	private static HashMap<String, ArrayList<String>> toSparseMatrix(Tree tree) {
		HashMap<String, ArrayList<String>> matrix = new HashMap<String, ArrayList<String>>();
		
		recursiveVisit(tree, matrix);
		
		return matrix;
	}

	private static void recursiveVisit(Tree tree, HashMap<String, ArrayList<String>> matrix) {
		String treeName = tree.value();
//		System.out.println(treeName+" | "+tree.isLeaf());
		for(Tree subtree : tree.children()) {
			if(!subtree.isLeaf()) {
				ArrayList<String> arr;
				if(matrix.containsKey(treeName))
					arr = matrix.get(treeName);
				else {
					arr = new ArrayList<String>();
					matrix.put(treeName, arr);
				}
				arr.add(subtree.value());
				recursiveVisit(subtree, matrix);
			}
		}
	}

	public static Something subjectsJSON(String query) throws ParseException {
		
		String jsonString = StanfordNLP.parseToJSON(query);
		JSONParser jsonP = new JSONParser();
		JSONObject root = (JSONObject) jsonP.parse(jsonString);

		Something res = new Something();

		HashMap<String, ArrayList<String>> subjects = new HashMap<String, ArrayList<String>>();
		JSONArray basicDep = (JSONArray) ((JSONObject) (((JSONArray) root
				.get("sentences")).get(0))).get("basic-dependencies");
		for (Object obj : basicDep) {
			JSONObject dep = (JSONObject) obj;
			
			// assumes ROOT is first
			if(dep.get("dep").equals("ROOT")) {
				ArrayList<String> arr = subjects.get("ROOT") == null ?
						new ArrayList<String>() :
							subjects.get("ROOT");
				subjects.put("ROOT", arr);
				arr.add((String) dep.get("dependentGloss"));
			}
			// search for compounds
			if(dep.get("dep").equals("compound")) {
				String gov = (String) dep.get("governorGloss");
				ArrayList<String> arr = subjects.get(gov) == null ?
						new ArrayList<String>() :
							subjects.get(gov);
				subjects.put(gov, arr);
				arr.add((String) dep.get("dependentGloss"));
			}
		}

		recursiveCompounds("ROOT", subjects, res);
		
		LOGGER.info("Subject: "+res);

		return res;
	}

	private static void recursiveCompounds(String key,
			HashMap<String, ArrayList<String>> subjects, Something res) {
		
		ArrayList<String> sub = subjects.get(key);
		if(sub == null)
			return;
		
		for(String dep : sub) {
			res.addCompound(dep);
			recursiveCompounds(dep, subjects, res);
		}
		
	}

}
