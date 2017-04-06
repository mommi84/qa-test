package org.thesmartpuzzle.gplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import org.aksw.tsoru.qatest2.StanfordNLP;
import org.apache.commons.lang3.ArrayUtils;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.Tree;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class Sentence2Clauses {
	
	private static final String[] TARGETS = {"ROOT", "SBAR"}; 

	public static void main(String[] args) throws FileNotFoundException {
		
		String prefix = args[1];
		
		HashSet<String> reviews = new HashSet<>();
		
		Scanner in = new Scanner(new File(args[0]));
		while(in.hasNextLine()) {
			String input = in.nextLine();
			reviews.add(input);
		}
		in.close();
		
		final int N = Runtime.getRuntime().availableProcessors();
		final PrintWriter pw[] = new PrintWriter[N];
		
		IntStream.range(0, N).parallel().forEach(i -> {
			String name = Thread.currentThread().getName();
			if(name.equals("main"))
				name = "0";
			else
				name = name.substring(name.lastIndexOf('-') + 1);
			String filename = prefix + "." + name;
			System.out.println("Opening " + filename);
			
			try {
				pw[i] = new PrintWriter(new File(filename));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		});
		
		long start = System.currentTimeMillis();
		
		reviews.parallelStream().forEach(input -> {
			
			String out = input + "\n";
//			long st = System.currentTimeMillis();
			HashSet<String> clauses = run(input);
//			System.out.println("review: "+(System.currentTimeMillis()-st));
			out += clauses.size() + "\n";
			for(String clause : clauses) {
				out += clause + "\n";
			}
//			System.out.println("\n" + Thread.currentThread().getName());
			String name = Thread.currentThread().getName();
			int n = name.equals("main") ? 0 : Integer.parseInt(name.substring(name.length() - 1));
			
			pw[n].println(out.trim());
			
		});
		
		System.out.println("Done in "+(System.currentTimeMillis()-start)+" ms.");
		
		IntStream.range(0, N).parallel().forEach(i -> {
			String name = Thread.currentThread().getName();
			if(name.equals("main"))
				name = "0";
			else
				name = name.substring(name.lastIndexOf('-') + 1);
			String filename = prefix + "." + name;
			System.out.println("Closing " + filename);
			
			try {
				pw[i].close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		});


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

		//System.out.println("Sentences: ("+sentenceList.size()+")");
//		for (String sentence : sentenceList)
//		   System.out.println("- " + sentence);
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
