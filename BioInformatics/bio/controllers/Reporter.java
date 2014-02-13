package bio.controllers;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.List;

import bio.models.Sequence;

/**
 * Produces a report on the total number of acids and the
 * frequency of those acids for a set of Sequences.
 * @author Brian J. Walters
 *
 */
public class Reporter {
	private PrintWriter pw;
	
	/**
	 * Creates a new Reporter object and opens the file
	 * to store report.
	 * @param filename	an output file
	 */
	public Reporter(String filename) {
		try {
			pw = new PrintWriter(filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * Write a report of the total number of acids and their
	 * frequencies to a file.
	 * @param totals		the totals for each acid code for both
	 *                      amino and nucleic acids
	 * @param frequencies   the frequencies for each acid code for both
	 *                      amino and nucleic acids
	 * @param reportTitle   the title of the report
	 */
	public void createFrequencyReport(List<Hashtable<String, Integer>> totals, 
							 List<Hashtable<String, Double>> frequencies,
							 String reportTitle) {
		pw.println(reportTitle);
		pw.println("Totals:");
		pw.println("  Nucleic Acids:");
		for (String key : totals.get(0).keySet()) {
			pw.println("  " + key + ": " + totals.get(0).get(key) + ", " + frequencies.get(0).get(key));
		}
		pw.println("\n");
		pw.println("  Amino Acids:");
		for (String key : totals.get(1).keySet()) {
			pw.println("  " + key + ": " + totals.get(1).get(key) + ", " + frequencies.get(1).get(key));
		}
		pw.println("\n");
		pw.flush();
	}
	
	/**
	 * Writes two aligned sequences to a file.
	 * @param totals		the totals for each acid code for both
	 *                      amino and nucleic acids
	 * @param frequencies   the frequencies for each acid code for both
	 *                      amino and nucleic acids
	 * @param reportTitle   the title of the report
	 */
	public void createSequenceAlignmentReport(List<Sequence> list) {
		for (Sequence seq : list) {
			pw.println(seq.getSequence());
		}
		pw.println("\n");
		pw.flush();
	}
	
	/**
	 * Close the report file.
	 */
	public void close() {
		pw.close();
	}
}
