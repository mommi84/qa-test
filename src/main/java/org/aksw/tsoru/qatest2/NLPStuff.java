package org.aksw.tsoru.qatest2;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class NLPStuff {

	private static final Logger LOGGER = Logger.getLogger(NLPStuff.class);

	public static Something subjects(final String QUERY) throws ParseException {
		String jsonString = StanfordNLP.parse(QUERY);
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