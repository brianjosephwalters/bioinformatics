package bio.view;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import bio.controllers.ChunkParser;
import bio.controllers.FileParser;
import bio.controllers.Reporter;
import bio.controllers.SubstitutionMatrixParser;
import bio.models.Analyzer;
import bio.models.NWAligner;
import bio.models.Sequence;
import bio.models.SubstitutionMatrix;

/**
 * A TUI for loading FASTA files, determining whether sequences are DNA, RNA or a Protein,
 * and calculating the frequency of each acid.  
 * @author Brian J. Walters
 *
 */
public class BioTUI {
	
	// Default file locations
	public static final String VITIS_DNA_TEST = "data\\Vitis vinifera DNA test.fasta";
	public static final String VITIS_DNA_MEDIUM = "data\\Vitis vinifera DNA medium.fasta";
	public static final String VITIS_DNA_FULL = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera DNA.fasta";
	
	public static final String VITIS_PROTEIN_TEST = "data\\Vitis vinifera Protein test.fasta";
	public static final String VITIS_PROTEIN_MEDIUM = "data\\Vitis vinifera Protein medium.fasta";
	public static final String VITIS_PROTEIN_FULL = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera Protein.fasta";
	
	public static final String VITIS_RESULTS_TEST = "results\\Vitis vinifera results test.txt";
	public static final String VITIS_RESULTS_MEDIUM = "results\\Vitis vinifera results medium.txt";
	public static final String VITIS_RESULTS_FULL = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera results.txt";
	
	public static final String NICOTIANA_DNA_TEST = "data\\Nicotiana tabacum DNA test.fasta";
	public static final String NICOTIANA_DNA_MEDIUM = "data\\Nicotiana tabacum DNA medium.fasta";
	public static final String NICOTIANA_DNA_FULL = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum DNA.fasta";
	
	public static final String NICOTIANA_PROTEIN_TEST = "data\\Nicotiana tabacum Protein test.fasta";
	public static final String NICOTIANA_PROTEIN_MEDIUM = "data\\Nicotiana tabacum Protein medium.fasta";
	public static final String NICOTIANA_PROTEIN_FULL = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum Protein.fasta";

	public static final String NICOTIANA_RESULTS_TEST = "results\\Nicotiana tabacum results test.txt";
	public static final String NICOTIANA_RESULTS_MEDIUM = "results\\Nicotiana tabacum results medium.txt";
	public static final String NICOTIANA_RESULTS_FULL = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum results.txt";
	
	public static final String MUS_MUSCULU = "data\\Mus musculu.fasta";
	public static final String XENOPUS_LAEVIS = "data\\Xenopus laevis.fasta";
	public static final String BLOSUM50 = "data\\BLOSUM50.txt";
	
	Scanner scanner;			// Used to receive user input.
	FileParser fileParser;      // Used to parse entire files.
	ChunkParser chunkParser;    // Used to parse chunks of files.
	Analyzer analyzer;          // Provides methods for analyzing sequences.
	NWAligner aligner2;
	
	/**
	 * Creates an instance of the TUI.
	 */
	public BioTUI() {
		this.scanner = new Scanner(System.in);
		this.fileParser = new FileParser();
		this.analyzer = new Analyzer();
	}
	
	/**
	 * Prepares the writing of the results (total number of acids and their frequencies)
	 * to a file.
	 * @param totals        the count of each acid
	 * @param frequencies   the frequency of each acid
	 * @param filename      the name of the file to store the results
	 */
	public void saveResults(List<Hashtable<String, Integer>> totals, 
							List<Hashtable<String, Double>> frequencies,
							String filename) {
		// Prepare a report file.
		Reporter reporter = new Reporter(filename);
		// Write total and frequencies to the report file.  
		// The report name is constructed from the provided file name.
		reporter.createReport(totals, frequencies,
							  StringUtils.substringAfterLast(StringUtils.substringBeforeLast(filename, "."), "\\"));
	}
	
	/**
	 * Displays the frequency calculations to the screen.
	 * @param frequencies   the frequency of each acid
	 */
	public void displayFrequencies(List<Hashtable<String, Double>> frequencies) {
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
	public void displayTotals(List<Hashtable<String, Integer>> totals) {
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
	
	/**
	 * Given FASTA files, returns the results of parsing each of the files and
	 * counting the number of each individual acid code. 
	 * @param files    Any number of FASTA files to be parsed.
	 * @return         a list containing separate totals for nucleic and amino acids.  
	 */
	public List<Hashtable<String,Integer>> reportEntireSample(String... files) {
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
	public List<Hashtable<String,Integer>> reportEntireSampleByChunking(String... files) {
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
	 * Display the menu for performing sequence alignments.
	 */
	
	public void displayAlignmentMenu() {
		System.out.println("---------------------Alignments-------------------");
		System.out.println("Test Sequences...................................1");
		System.out.println("Align Mus musculu and Xenopus laevis sequences...2");
		System.out.println("Back.............................................0");
	}
		
	/**
	 * Displays the menu for working with Vitis vinifera.
	 */
	public void displayVitisMenu() {
		System.out.println("-----------------Vitis vinifera-------------------");
		System.out.println("Report data for small sample.....................1");
		System.out.println("Report data for medium sample....................2");
		System.out.println("Report data for medium sample using chunking*....3");
		System.out.println("############ LARGE DATA NOT PROVIDED #############");
		System.out.println("Report data for entire sample....................4");
		System.out.println("Report data for entire sample using chunking*....5");
		System.out.println("Back.............................................0");
		System.out.println("                    * Recommended for large files.");
	}
	
	/**
	 * Displays the menu for working with Nicotiana tabacum.
	 */
	public void displayNicotianaMenu() {
		System.out.println("---------------Nicotiana tabacum------------------");
		System.out.println("Report data for small sample.....................1");
		System.out.println("Report data for medium sample....................2");
		System.out.println("Report data for medium sample using chunking*....3");
		System.out.println("############ LARGE DATA NOT PROVIDED #############");
		System.out.println("Report data for entire sample....................4");
		System.out.println("Report data for entire sample using chunking*....5");
		System.out.println("Back.............................................0");
		System.out.println("                    * Recommended for large files.");
	}
	
	/**
	 * Displays the main menu.
	 */
	public void displayMainMenu() {
		System.out.println("----------------------Main Menu------------------------");
		System.out.println("Calculate frequencies for Vitis vinifera............. 1");
		System.out.println("Calculate frequencies for Nicotiana tabacum.......... 2");
		System.out.println("Produce Alignments................................... 3");
		// TODO: Implement single file options.
		//System.out.println("Calculate frequencies from FASTA file................10");
		System.out.println("Exit................................................. 0");
	}
	
	/**
	 * Performs responses for Vitis vinifera menu. 
	 * @param choice	the user's choice
	 */
	public void performChoiceVitis(int choice) {
		List<Hashtable<String,Integer>> totals;
		List<Hashtable<String,Double>> frequencies;
		if (choice == 1) {
			totals = reportEntireSample(VITIS_DNA_TEST, VITIS_PROTEIN_TEST);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveResults(totals, frequencies, VITIS_RESULTS_TEST);
		} else if (choice == 2) {
			totals = reportEntireSample(VITIS_DNA_MEDIUM, VITIS_PROTEIN_MEDIUM);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveResults(totals, frequencies, VITIS_RESULTS_MEDIUM);
		} else if (choice == 3) {
			totals = reportEntireSampleByChunking(VITIS_DNA_MEDIUM, VITIS_PROTEIN_MEDIUM);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveResults(totals, frequencies, VITIS_RESULTS_MEDIUM);
		} else if (choice == 4) {
			totals = reportEntireSample(VITIS_DNA_FULL, VITIS_PROTEIN_FULL);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveResults(totals, frequencies, VITIS_RESULTS_FULL);
		} else if (choice == 5) {
			totals = reportEntireSampleByChunking(VITIS_DNA_FULL, VITIS_PROTEIN_FULL);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveResults(totals, frequencies, VITIS_RESULTS_FULL);
		}
	}
	
	/**
	 * Performs responses for the Nicotiana tabacum menu.
	 * @param choice	the user's choice
	 */
	public void performChoiceNicotiana(int choice) {
		List<Hashtable<String,Integer>> totals;
		List<Hashtable<String,Double>> frequencies;
		if (choice == 1) {
			totals = reportEntireSample(NICOTIANA_DNA_TEST, NICOTIANA_PROTEIN_TEST);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveResults(totals, frequencies, NICOTIANA_RESULTS_TEST);
		} else if (choice == 2) {
			totals = reportEntireSample(NICOTIANA_DNA_MEDIUM, NICOTIANA_PROTEIN_MEDIUM);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveResults(totals, frequencies, NICOTIANA_RESULTS_MEDIUM);
		} else if (choice == 3) {
			totals = reportEntireSampleByChunking(NICOTIANA_DNA_MEDIUM, NICOTIANA_PROTEIN_MEDIUM);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveResults(totals, frequencies, NICOTIANA_RESULTS_MEDIUM);
		} else if (choice == 4) {
			totals = reportEntireSample(NICOTIANA_DNA_FULL, NICOTIANA_PROTEIN_FULL);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveResults(totals, frequencies, NICOTIANA_RESULTS_FULL);
		} else if (choice == 5) {
			totals = reportEntireSampleByChunking(NICOTIANA_DNA_FULL, NICOTIANA_PROTEIN_FULL);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveResults(totals, frequencies, NICOTIANA_RESULTS_FULL);
		}
	}
	
	/**
	 * Menu to select sequences on which to perform alignments.
	 * @param choice	the user's choice
	 */
	public void performChoiceAlignment(int choice) {
		if (choice == 1) {
			SubstitutionMatrixParser parser = new SubstitutionMatrixParser(BLOSUM50);
			SubstitutionMatrix<Integer> subMatrix = parser.parseSubstitutionMatrix2();
			subMatrix.setGapWeight(8);
			Sequence alpha = new Sequence();
			alpha.appendSequence("HEAGAWGHEE");
			Sequence beta = new Sequence();
			beta.appendSequence("PAWHEAE");
			aligner2 = new NWAligner(alpha, beta, subMatrix);
			System.out.println(aligner2);
		}
		else if (choice == 2) {
			SubstitutionMatrixParser parser = new SubstitutionMatrixParser(BLOSUM50);
			SubstitutionMatrix<Integer> subMatrix = parser.parseSubstitutionMatrix2();
			Sequence mus = fileParser.parseFile(MUS_MUSCULU).get(0);
			Sequence xenopus = fileParser.parseFile(XENOPUS_LAEVIS).get(0);
			aligner2 = new NWAligner(mus, xenopus, subMatrix);
			System.out.println(aligner2);
		}
	}
	
	/**
	 * Allows the user to provide an input FASTA file and an output file name
	 *  and then calculates the totals and frequencies for the FASTA file.
	 *  TODO: Needs to be implemented
	 */
	
	public void performChoiceFasta() {
		//System.out.println("Enter a FASTA file to load:");
		//String filename = scanner.nextLine();
	}
	
	/**
	 * Performs responses to the main menu.
	 * @param choice	the user's choice
	 */
	public void performChoiceMainMenu(int choice) {
		
		if (choice == 1) {
			int subchoice = -1;
			while (subchoice != 0) {
				displayVitisMenu();
				subchoice = scanner.nextInt();
				performChoiceVitis(subchoice);
			}
		} else if (choice == 2) {
			int subchoice = -1;
			while (subchoice != 0) {
				displayNicotianaMenu();
				subchoice = scanner.nextInt();
				performChoiceNicotiana(subchoice);
			}
		} else if (choice == 3) {
			int subchoice = -1;
			while (subchoice != 0) {
				displayAlignmentMenu();
				subchoice = scanner.nextInt();
				performChoiceAlignment(subchoice);
			}
		} else if (choice == 10) {
			performChoiceFasta();
		}
	}
	
	/**
	 * Runs the TUI.
	 */
	public void run() {
		int choice = -1;
		while (choice != 0) {
			displayMainMenu();
			choice = scanner.nextInt();
			performChoiceMainMenu(choice);
		}
	}
}
