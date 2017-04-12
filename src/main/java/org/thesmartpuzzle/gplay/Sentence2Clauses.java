package org.thesmartpuzzle.gplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


import org.aksw.tsoru.qatest2.StanfordNLP;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;


import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.Tree;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class Sentence2Clauses {
	
	private static final Logger logger = Logger.getLogger(Sentence2Clauses.class);
	
	private static final String[] TARGETS = {"ROOT", "SBAR"}; 

	
	public static void main(String[] args) throws FileNotFoundException {
		
		String prefix = args[1];
		
		final Integer EVERY = Integer.parseInt(args[2]);
		
		HashSet<String> reviews = new HashSet<>();
		
		logger.info("Loading input file in-memory...");
		Scanner in = new Scanner(new File(args[0]));
		while(in.hasNextLine()) {
			String input = in.nextLine();
			reviews.add(input);
		}
		in.close();
		
		final Integer SIZE = reviews.size();

		long start = System.currentTimeMillis();
		
		logger.info("Opening file "+prefix+"...");
		SyncWriter writer = SyncWriter.getInstance();
		writer.open(new File(prefix));
		
		Bean b = new Bean();
		
		logger.info("Started NLP processing...");
		reviews.parallelStream().forEach(input -> {
			
			StringBuffer out = new StringBuffer(input + "\n");

			HashSet<String> clauses = run(input);

			out.append(clauses.size() + "\n");
			for(String clause : clauses) {
				out.append(clause + "\n");
			}

			try {
				synchronized (writer) {					
					writer.println(out.toString().trim());
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			
			synchronized (b) {
				b.count ++;
				if(b.count % EVERY == 0)
					logger.info(b.count + "/" + SIZE + " rows processed.");
			}
			
		});
		
		writer.close();
		
		logger.info("Done in "+(System.currentTimeMillis()-start)+" ms.");

	}
	
	public static HashSet<String> run(String input) {
		
//		input = input.replaceAll("\n", " ").replaceAll("\t", " ");
		//System.out.println("=================\ninput: " + input);
		
		HashSet<String> allClauses = new HashSet<>();
		
//		long start = System.currentTimeMillis();
		List<String> sentences = toSentences(input);
//		System.out.println("--> toSentences: "+(System.currentTimeMillis()-start));
		for(String sentence : sentences) {

			LinkedList<String> sub = new LinkedList<>();
//			long st = System.currentTimeMillis();
			Tree tree = StanfordNLP.getBestParse(sentence);
//			System.out.println("----> getBestParse: "+(System.currentTimeMillis()-st));
			downTree(tree, sub);
			//System.out.println("sub:");
//			for(String s : sub)
				//System.out.println("- "+s);
			ArrayList<String> clauses = new ArrayList<>();

			while(!sub.isEmpty()) {
				String up = sub.removeLast();
//				System.out.println("up: " + up);
//				System.out.println("sub: ("+sub.size()+")");
//				for(String s : sub)
//					System.out.println("- "+s);
				boolean found = false;
				for(String c : clauses) {
					//System.out.print("* contains " + c + "? ");
					if(up.contains(c)) {
						
						int p = up.indexOf(c);
						String before = clean(up.substring(0, p));
						if(!before.isEmpty())
							sub.add(before);
						String after = clean(up.substring(p + c.length()));
						if(!after.isEmpty())
							sub.add(after);
						
						found = true;
						//System.out.println("yes");
						break;
					} else {
						//System.out.println("no");
					}
				}
				if(!found) {
					//System.out.println("added: " + up);
					clauses.add(clean(up));
				}
			}
			allClauses.addAll(clauses);
		}
		
		//System.out.println();
//		for(String clause : allClauses)
			//System.out.println("- " + clause);

		return allClauses;
		
	}
	
	
	private static String clean(String str) {
		
		if(str.isEmpty())
			return str;
		
		str = str.trim();
		
		if(str.startsWith(".") || str.startsWith(","))
			return clean(str.substring(1).trim());
		
		if(str.endsWith(".") || str.endsWith(","))
			return clean(str.substring(0, str.length()-1).trim());
		
		return str;
		
	}
	
	private static List<String> toSentences(String paragraph) {
		Reader reader = new StringReader(paragraph);
		DocumentPreprocessor dp = new DocumentPreprocessor(reader);
		List<String> sentenceList = new ArrayList<String>();

		for (List<HasWord> sentence : dp) {
		   String sentenceString = "";
		   for(HasWord hw : sentence)
			   sentenceString += hw.word() + " ";
		   sentenceList.add(sentenceString.trim());
		}

		return sentenceList;
	}
	
	private static void downTree(Tree tree, LinkedList<String> clauses) {

		if(ArrayUtils.contains(TARGETS, tree.value()))
			clauses.add(join(tree.yieldWords()));
		
		for(Tree subtree : tree.children())
			downTree(subtree, clauses);

	}

	private static String join(ArrayList<Word> yieldWords) {
		String s = "";
		for(Word w : yieldWords)
			s += w.word() + " ";
		return s.trim();
	}

}

class Bean {
	Integer count = 0;
}
