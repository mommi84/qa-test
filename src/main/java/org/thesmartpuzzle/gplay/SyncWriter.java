package org.thesmartpuzzle.gplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class SyncWriter {
	
	private PrintWriter pw;
	
	private static SyncWriter sw;

	private SyncWriter() {
		super();
	}
	
	public void open(File f) throws FileNotFoundException {
		pw = new PrintWriter(f);
	}

	public void close() throws FileNotFoundException {
		pw.close();
	}

	public void println(String s) throws FileNotFoundException {
		pw.println(s);
	}
	
	public static SyncWriter getInstance() {
		if(sw == null)
			sw = new SyncWriter();
		return sw;
	}
		
}
