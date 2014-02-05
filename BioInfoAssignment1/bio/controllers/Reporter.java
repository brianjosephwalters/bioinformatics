package bio.controllers;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.List;

public class Reporter {
	private PrintWriter pw;
	
	public Reporter(String filename) {
		try {
			pw = new PrintWriter(filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
		
	public void createReport(List<Hashtable<String, Integer>> totals, 
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
	
	public void close() {
		pw.close();
	}
}
