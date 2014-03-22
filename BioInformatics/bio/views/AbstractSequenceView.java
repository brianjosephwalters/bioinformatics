package bio.views;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import bio.controllers.ChunkParser;
import bio.controllers.FileParser;
import bio.controllers.Reporter;
import bio.models.Analyzer;
import bio.models.Sequence;

public abstract class AbstractSequenceView extends AbstractView {
	/**
	 * Given FASTA files, returns the results of parsing each of the files and
	 * counting the number of each individual acid code. 
	 * @param files    Any number of FASTA files to be parsed.
	 * @return         a list containing separate totals for nucleic and amino acids.  
	 */
	protected List<Hashtable<String,Integer>> reportEntireSample(String... files) {
		FileParser fileParser = new FileParser();
		Analyzer analyzer = new Analyzer();
		List<Sequence> list = new ArrayList<Sequence>();
		// For every provided file...
		for (String file : files) {
			// parse the file and add the resulting list of sequences to the complete list.
			list.addAll(fileParser.parseFile(file));
		}
		// Determine the types for each sequence in the list.
		// (Whether DNA, RNA, or Protein)
		analyzer.determineTypes(list);
		// Calculate the totals for nucleic acids and amino acids.
		// NOTE: Since acid codes overlap, we give the analyzer ONE list of sequences
		//       but are given back two hashtables - one for amino acid codes, and one for 
		//       nucleic acid codes.
		List<Hashtable<String, Integer>> data = analyzer.determineTotals(list);
		return data;
	}
	
	/**
	 * Given FASTA files, returns the results of parsing each of the files and
	 * counting the number of each individual acid code.
	 * NOTE: Uses chunking, which is required for large files.
	 * @param files    Any number of FASTA files to be parsed.
	 * @return         a list containing separate totals for nucleic and amino acids.  
	 */
	protected List<Hashtable<String,Integer>> reportEntireSampleByChunking(String... files) {
		Analyzer analyzer = new Analyzer();
		// Prepare our data structure.
		List<Hashtable<String, Integer>> aggregateData = new ArrayList<Hashtable<String,Integer>>();
		aggregateData.add(new Hashtable<String, Integer>());
		aggregateData.add(new Hashtable<String, Integer>());
		
		// For every provided file...
		for (String file : files) {
			List<Sequence> list = new ArrayList<Sequence>();
			// prepare a chunk parser for the file.
			ChunkParser parser = new ChunkParser(file);
			// Until the file is empty, do...
			do {
				// Get a chunk of the file and store the sequences in a list.
				list = parser.parseChunk();
				// Determine the types for those chunks of sequences.
				analyzer.determineTypes(list);
				// Calculate the total amino for those chunks of sequences.
				// NOTE: Since acid codes overlap, we give the analyzer ONE list of sequences
				//       but are given back two hashtables - one for amino acid codes, and one for 
				//       nucleic acid codes.
				List<Hashtable<String,Integer>> currentData = analyzer.determineTotals(list);
				// Aggregate the results so that memory the sequences can be freed.
				aggregateData = analyzer.aggregateTotals(aggregateData, currentData);
				// The list of sequences and the hashtables of calculations are "freed" 
				// each iteration to be GCed.  
			} while (list.size() > 0);
		}
		return aggregateData;
	}
	
	/**
	 * Writes of the results (total number of acids and their frequencies)
	 * to a file.
	 * @param totals        the count of each acid
	 * @param frequencies   the frequency of each acid
	 * @param filename      the name of the file to store the results
	 */
	protected void saveFrequencyStatistics(List<Hashtable<String, Integer>> totals, 
										   List<Hashtable<String, Double>> frequencies,
										   String filename) {
		// Prepare a report file.
		Reporter reporter = new Reporter(filename);
		// Write total and frequencies to the report file.  
		// The report name is constructed from the provided file name.
		reporter.createFrequencyReport(totals, frequencies,
			StringUtils.substringAfterLast(StringUtils.substringBeforeLast(filename, "."), "\\"));
		reporter.close();
	}

	
	/**
	 * Displays the frequency calculations to the screen.
	 * @param frequencies   the frequency of each acid
	 */
	protected void displayFrequencies(List<Hashtable<String, Double>> frequencies) {
		System.out.println("Frequencies:");
		System.out.println("  Nucleic Acids:");
		// Prints the frequency of each nucleic acid to screen.
		for (String key : frequencies.get(0).keySet()) {
			System.out.println("  " + key + ": " + frequencies.get(0).get(key));
		}
		System.out.println("\n");
		// Prints the frequency of each amino acid to screen.
		System.out.println("  Amino Acids:");
		for (String key : frequencies.get(1).keySet()) {
			System.out.println("  " + key + ": " + frequencies.get(1).get(key));
		}
		System.out.println("\n");
	}

	/**
	 * Displays the total count of each acid to the screen.
	 * @param totals        the count of each acid
	 */
	protected void displayTotals(List<Hashtable<String, Integer>> totals) {
		System.out.println("Totals:");
		System.out.println("  Nucleic Acids:");
		// Prints the count of each nucleic acid to the screen.
		for (String key : totals.get(0).keySet()) {
			System.out.println("  " + key + ": " + totals.get(0).get(key));
		}
		System.out.println("\n");
		// Prints the count of each amino acid to the screen.
		System.out.println("  Amino Acids:");
		for (String key : totals.get(1).keySet()) {
			System.out.println("  " + key + ": " + totals.get(1).get(key));
		}
		System.out.println("\n");
	}
}
