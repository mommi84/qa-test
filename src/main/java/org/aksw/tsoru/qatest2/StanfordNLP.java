package org.aksw.tsoru.qatest2;

import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.nndep.Configuration;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class StanfordNLP {

	private static LexicalizedParser parser = LexicalizedParser
			.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
	// private TreeUtil treeUtil;
//	private static TokenizerFactory<Word> tokenizerFactory = PTBTokenizer.factory();
	
	public static void main(String[] args) {
		String query = QATest2.QUERY[0];
//		getBestParse(query);
		parseToJSON(query);
	}

	public static String parseToJSON(String query) {

		MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
		
		DependencyParser pars = DependencyParser
				.loadFromModelFile(DependencyParser.DEFAULT_MODEL);
		DocumentPreprocessor tokenizer = new DocumentPreprocessor(
				new StringReader(query));
		String s = "";
		for (List<HasWord> sentence : tokenizer) {
			List<TaggedWord> tagged = tagger.tagSentence(sentence);
			GrammaticalStructure gs = pars.predict(tagged);
			// Print typed dependencies
			s += gs + "\n";
			System.out.println(gs.typedDependenciesCollapsedTree());
		}

		// TODO implement
		// String s =
		// "{\n \"sentences\": [\n {\n \"index\": \"0\",\n \"parse\": \"(ROOT\n (NP\n (NP (NNP Barack) (NNP Obama))\n (NP (NN birth) (NN place))\n (. .)))\",\n \"basic-dependencies\": [\n {\n \"dep\": \"ROOT\",\n \"governor\": \"0\",\n \"governorGloss\": \"ROOT\",\n \"dependent\": \"2\",\n \"dependentGloss\": \"Obama\"\n },\n {\n \"dep\": \"compound\",\n \"governor\": \"2\",\n \"governorGloss\": \"Obama\",\n \"dependent\": \"1\",\n \"dependentGloss\": \"Barack\"\n },\n {\n \"dep\": \"compound\",\n \"governor\": \"4\",\n \"governorGloss\": \"place\",\n \"dependent\": \"3\",\n \"dependentGloss\": \"birth\"\n },\n {\n \"dep\": \"dep\",\n \"governor\": \"2\",\n \"governorGloss\": \"Obama\",\n \"dependent\": \"4\",\n \"dependentGloss\": \"place\"\n }\n ],\n \"collapsed-dependencies\": [\n {\n \"dep\": \"ROOT\",\n \"governor\": \"0\",\n \"governorGloss\": \"ROOT\",\n \"dependent\": \"2\",\n \"dependentGloss\": \"Obama\"\n },\n {\n \"dep\": \"compound\",\n \"governor\": \"2\",\n \"governorGloss\": \"Obama\",\n \"dependent\": \"1\",\n \"dependentGloss\": \"Barack\"\n },\n {\n \"dep\": \"compound\",\n \"governor\": \"4\",\n \"governorGloss\": \"place\",\n \"dependent\": \"3\",\n \"dependentGloss\": \"birth\"\n },\n {\n \"dep\": \"dep\",\n \"governor\": \"2\",\n \"governorGloss\": \"Obama\",\n \"dependent\": \"4\",\n \"dependentGloss\": \"place\"\n }\n ],\n \"collapsed-ccprocessed-dependencies\": [\n {\n \"dep\": \"ROOT\",\n \"governor\": \"0\",\n \"governorGloss\": \"ROOT\",\n \"dependent\": \"2\",\n \"dependentGloss\": \"Obama\"\n },\n {\n \"dep\": \"compound\",\n \"governor\": \"2\",\n \"governorGloss\": \"Obama\",\n \"dependent\": \"1\",\n \"dependentGloss\": \"Barack\"\n },\n {\n \"dep\": \"compound\",\n \"governor\": \"4\",\n \"governorGloss\": \"place\",\n \"dependent\": \"3\",\n \"dependentGloss\": \"birth\"\n },\n {\n \"dep\": \"dep\",\n \"governor\": \"2\",\n \"governorGloss\": \"Obama\",\n \"dependent\": \"4\",\n \"dependentGloss\": \"place\"\n }\n ],\n \"tokens\": [\n {\n \"index\": \"1\",\n \"word\": \"Barack\",\n \"lemma\": \"Barack\",\n \"characterOffsetBegin\": \"0\",\n \"characterOffsetEnd\": \"6\",\n \"pos\": \"NNP\",\n \"ner\": \"PERSON\",\n \"speaker\": \"PER0\"\n },\n {\n \"index\": \"2\",\n \"word\": \"Obama\",\n \"lemma\": \"Obama\",\n \"characterOffsetBegin\": \"7\",\n \"characterOffsetEnd\": \"12\",\n \"pos\": \"NNP\",\n \"ner\": \"PERSON\",\n \"speaker\": \"PER0\"\n },\n {\n \"index\": \"3\",\n \"word\": \"birth\",\n \"lemma\": \"birth\",\n \"characterOffsetBegin\": \"13\",\n \"characterOffsetEnd\": \"18\",\n \"pos\": \"NN\",\n \"ner\": \"O\",\n \"speaker\": \"PER0\"\n },\n {\n \"index\": \"4\",\n \"word\": \"place\",\n \"lemma\": \"place\",\n \"characterOffsetBegin\": \"19\",\n \"characterOffsetEnd\": \"24\",\n \"pos\": \"NN\",\n \"ner\": \"O\",\n \"speaker\": \"PER0\"\n },\n {\n \"index\": \"5\",\n \"word\": \".\",\n \"lemma\": \".\",\n \"characterOffsetBegin\": \"24\",\n \"characterOffsetEnd\": \"25\",\n \"pos\": \".\",\n \"ner\": \"O\",\n \"speaker\": \"PER0\"\n }\n ]\n }\n ]\n}";
		System.out.println("===============\n"+s);
		return s;
	}

	public static Tree getBestParse(String sentence) {
//		List<Word> tokens = tokenize(sentence);
//		List<Word> tokens = tokenizerFactory.getTokenizer(new StringReader(sentence)).tokenize();
//		LexicalizedParser parser = getParser();
//		Tree tree = parser.parse(tokens);
		Tree tree = parser.parse(sentence);
//		System.out.println(tree.pennString());
		return tree;
		// Tree stringLabeledTree = getTreeUtil().treeToStringLabeledTree(tree);
		// return stringLabeledTree;
	}

//	private static List<Word> tokenize(String sentence) {
//		return buildTokenizer(sentence).tokenize();
//	}
//
//	private static Tokenizer<Word> buildTokenizer(String sentence) {
//		return getTokenizerFactory().getTokenizer(new StringReader(sentence));
//	}
//
//	private static LexicalizedParser getParser() {
//		return parser;
//	}

	// private TreeUtil getTreeUtil() {
	// return treeUtil;
	// }

//	private static TokenizerFactory<Word> getTokenizerFactory() {
//		if (tokenizerFactory == null) {
//			tokenizerFactory = PTBTokenizer.factory();
//		}
//		return tokenizerFactory;
//	}

}
