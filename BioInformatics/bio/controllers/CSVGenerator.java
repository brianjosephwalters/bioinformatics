package bio.controllers;

import java.io.PrintWriter;
import java.util.List;

/**
 * Creates csv files.
 * @author Brian J. Walters
 */
public class CSVGenerator {
	private PrintWriter pw;
	
	/**
	 * Creates a new CSVGenerator. 
	 * @param filename		The filename 
	 */
	public CSVGenerator(String filename) {
		try {
			pw = new PrintWriter(filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a row of Double values to the csv file.
	 * @param list		a list of doubles
	 */
	public void addRowDoubles(List<Double> list) {
		for (Double value : list) {
			pw.printf("%8.4f,", value);
		}
		pw.println();
	}
	
	/**
	 * Add a row of String values to the csv file.
	 * @param list		a list of strings
	 */
	public void addRowStrings(List<String> list) {
		for (String value : list) {
			pw.printf("%8s,", value);
		}
		pw.println();
	}
	
	/**
	 * Transform rows to columns. 
	 * @require 		length of all lists is the same. 
	 * @param lists		lists of strings or doubles
	 * @param types		lists corresponding to types
	 */
	public void createRowToCol(List[] lists, Class[] types) {
		for (int row = 0; row < lists[0].size(); row++) {
			for (int col = 0; col < lists.length; col++) {
				if (types[col].equals(String.class))
					pw.printf("%8s,", lists[col].get(row));
				if (types[col].equals(Double.class))
					pw.printf("%8.4f,", lists[col].get(row));
			}
			pw.println();
		}
	}
	
	/**
	 * Closes the csv file.
	 */
	public void close() {
		pw.flush();
		pw.close();
	}
}
