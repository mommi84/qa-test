package org.aksw.tsoru.qatest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class Settings {

	private final static Logger logger = Logger.getLogger("qa-test");

	public static final TreeSet<String> META_PROPERTIES = new TreeSet<String>();
	
	public static final HashMap<String, String> PREDEFINED_LABELS = new HashMap<String, String>();

	static {
		Scanner in = null;
		try {
			in = new Scanner(new File("src/main/resources/meta_properties.txt"));
		} catch (FileNotFoundException e) {
			logger.error("Could not load meta properties!");
		}
		while(in.hasNextLine())
			META_PROPERTIES.add(in.nextLine());
		in.close();
		
		PREDEFINED_LABELS.put("http://www.w3.org/1999/02/22-rdf-syntax-ns#type@en", "is a");
		PREDEFINED_LABELS.put("http://purl.org/dc/terms/subject@en", "belongs to");
		PREDEFINED_LABELS.put("http://www.w3.org/2000/01/rdf-schema#label@en", "'s full name is");
	}

}
