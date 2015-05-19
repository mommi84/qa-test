package org.aksw.tsoru.qatest2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * @author Tommaso Soru <>
 *
 */
public class TheIndex {
	
	private Version version;
	private Analyzer analyzer;
	private Directory directory;

	private DirectoryReader ireader;
	private IndexSearcher isearcher;
	private QueryParser parser;
	
	private static final String STORED_FIELD = "label";
	private static final String[] NOT_STORED_FIELDS = {"uri"};
	
	private static final double SCORE_THRESHOLD = 0.6;
	private static final int TOP_DOCS = 1;
	
	public TheIndex() {
		version = Version.LUCENE_4_9;
	    // Store the index in memory:
//	    directory = new RAMDirectory();
	    // To store an index on disk, use this instead:
		try {
			directory = FSDirectory.open(new File("lucene"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		analyzer = new StandardAnalyzer(version);
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
//		indexToDB();
		
//		ArtistIndex index = new ArtistIndex();
//		index.build(new LastFM());
//		index.initSearch();
//		index.search("INDUKTI live at Alcatraz, Milan");
//		index.search("infected mushroom - free entry");
//		System.out.println("Last.fm index size: " + index.isearcher.collectionStatistics("name").docCount());
//		index.listValidTags(new LastFM());
//		index.close();
		
	}
	
//	/**
//	 * Method for generating an SQL script.
//	 * @throws Exception 
//	 */
//	public static void indexToDB() throws Exception {
//		
//		ArtistIndex index = new ArtistIndex();
//		index.initSearch();
//		
//		MySQL mysql = new MySQL("fb-crawler.cnfmmltl5d4p.us-east-1.rds.amazonaws.com", "evaluation", "root", "groop2014");
//		
//		IndexReader ir = index.ireader;
//
//		int l = ir.maxDoc();
//		for(int i=0; i<l; i++) {
//			int compl = (i / (l / 100));
//			Document d = ir.document(i);
//			String name = d.get("name");
//			String tags = d.get("tags");
////			System.out.println(name);
//			int record = 0;
//			Genre genre = null;
//			for(String tagstring : tags.split(",")) {
//				String[] tag = tagstring.split("=");
//				Genre g = MetaFeatures.align(tag[0], false);
//				if(g != null) {
//					int count = Integer.parseInt(tag[1]);
//					if(count > record) {
//						record = count;
//						genre = g;
//					}
//				}
//			}
//			if(genre != null) {
////				System.out.println("\t"+genre.getName());
//				try {
//					mysql.insert(name, genre.getName());
//				} catch (Exception e) {
//					System.out.println(e.getMessage());
//				}
//			}
//			if(i % (l / 100) == 0)
//				System.out.println(compl+"% completed.");
//		}
//		
//		System.out.println();
//		mysql.close();
//		index.close();
//	}
	
	/**
	 * Build the index from files AA-99.map located in "etc/lastfm/".
	 * Remember to delete the directory "etc/lastfm/lucene/" (if exists) before calling this.
	 * 
	 * @param fm
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClassNotFoundException
	 */
//	public void build(LastFM fm) throws IOException, ParseException, ClassNotFoundException {
//		System.out.println("Lucene: index build started.");
//		
//	    IndexWriterConfig config = new IndexWriterConfig(version, analyzer);
//	    IndexWriter iwriter = new IndexWriter(directory, config);
//	    
//	    char[] chars = fm.characters();
//	    for(char c1 : chars) {
//	    	System.out.print("\n"+c1);
//	    	for(char c2 : chars) {
//	    		HashMap<String, Artist> map = fm.readIndex(c1+""+c2);
//	    		for(String key : map.keySet()) {
//	    		    Document doc = new Document();
//	    		    String tagstring = "";
//	    		    for(Tag tag : map.get(key).getTags())
//	    		    	tagstring += tag.getName() + "=" + tag.getCount() + ",";
//	    		    tagstring = cutLast(tagstring);
//	    		    doc.add(new Field(STORED_FIELD, key, TextField.TYPE_STORED));
//	    		    doc.add(new Field(NOT_STORED_FIELDS[0], tagstring, TextField.TYPE_STORED));
////	    		    System.out.println(tagstring);
//	    		    iwriter.addDocument(doc);
//	    		}
//		    	System.out.print(".");
//	    	}
//	    }
//		System.out.println();
//		
//	    iwriter.close();
//	    
//	}
	
	public void write(HashMap<String, String> map) throws IOException {
	    IndexWriterConfig config = new IndexWriterConfig(version, analyzer);
	    IndexWriter iwriter = new IndexWriter(directory, config);
	    
	    for(String key : map.keySet()) {
	    	Document doc = new Document();
	    	doc.add(new Field(STORED_FIELD, key, TextField.TYPE_STORED));
	    	doc.add(new Field(NOT_STORED_FIELDS[0], map.get(key), TextField.TYPE_NOT_STORED));
	    	iwriter.addDocument(doc);
	    }
	    
	    iwriter.close();
	}

//	public void listValidTags(LastFM fm) throws FileNotFoundException, ClassNotFoundException, IOException {
//		System.out.println("=== Begin tag list ===");
//		
//	    TreeSet<String> genres = new TreeSet<String>();
////	    char[] chars = fm.characters();
////	    for(char c1 : chars) {
////	    	System.out.print("\n"+c1);
////	    	for(char c2 : chars) {
////	    		System.out.print(".");
////	    		HashMap<String, Artist> map = fm.readIndex(c1+""+c2);
////	    		for(String key : map.keySet()) {
////	    		    for(Tag tag : map.get(key).getTags()) {
////	    		    	String gen = LastFMFeatures.align(tag.getName());
////	    		    	if(gen != null)
////	    		    		genres.add(gen);
////	    		    }
////	    		}
////	    	}
////	    }
//	    // TODO scroll Lucene index to collect tags...
//		System.out.println();
//		
//	    for(String genre : genres) {
//	    	System.out.println(genre);
//	    }
//	    	
//	}
//	
//	private String cutLast(String tagstring) {
//		if(tagstring.length() == 0)
//			return tagstring;
//		return tagstring.substring(0, tagstring.length() - 1);
//	}
//
	public void initSearch() throws IOException {
	    // Prepare for search
	    ireader = DirectoryReader.open(directory);
	    isearcher = new IndexSearcher(ireader);
	    // Parse a simple query that searches for "text":
	    parser = new QueryParser(version, STORED_FIELD, analyzer);
	}
//	
//	public ArrayList<Document> search(String text) throws IOException, ParseException {
//		
//		ArrayList<Document> results = new ArrayList<>();
//	    Query query = parser.parse(text.toLowerCase());
//	    ScoreDoc[] hits = isearcher.search(query, null, TOP_DOCS).scoreDocs;
////	    assertEquals(1, hits.length);
////	    System.out.println(hits.length);
//	    // Iterate through the results:
//	    for (int i = 0; i < hits.length; i++) {
//			Document hitDoc = isearcher.doc(hits[i].doc);
//			if(hits[i].score > SCORE_THRESHOLD) {
////				System.out.println(text+"\n\t"+hits[i].score + " : " + hitDoc.get(STORED_FIELD) + " => "+hitDoc.get(NOT_STORED_FIELDS[0]));
//				results.add(hitDoc);
//			} else
//				break;
////	      assertEquals("This is the text to be indexed.", hitDoc.get("fieldname"));
//	    }
//	    return results;
//	}
//	
//	public void close() throws IOException {
//	    ireader.close();
//	    directory.close();
//	}
	
	public ArrayList<Document> search(String text) throws IOException, ParseException {
	
		ArrayList<Document> results = new ArrayList<Document>();
	    Query query = parser.parse(text.toLowerCase());
	    ScoreDoc[] hits = isearcher.search(query, null, TOP_DOCS).scoreDocs;
	//    assertEquals(1, hits.length);
	//    System.out.println(hits.length);
	    // Iterate through the results:
	    for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			if(hits[i].score > SCORE_THRESHOLD) {
	//			System.out.println(text+"\n\t"+hits[i].score + " : " + hitDoc.get(STORED_FIELD) + " => "+hitDoc.get(NOT_STORED_FIELDS[0]));
				results.add(hitDoc);
			} else
				break;
	//      assertEquals("This is the text to be indexed.", hitDoc.get("fieldname"));
    }
    return results;
}


	
}
