package bio.view;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import bio.controllers.ChunkParser;
import bio.controllers.FileParser;
import bio.controllers.Reporter;
import bio.models.Analyzer;
import bio.models.Sequence;

public class BioTUI {
	public static final String VITIS_DNA_TEST = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera DNA test.fasta";
	public static final String VITIS_DNA_MEDIUM = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera DNA medium.fasta";
	public static final String VITIS_DNA_FULL = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera DNA.fasta";
	
	public static final String VITIS_PROTEIN_TEST = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera Protein test.fasta";
	public static final String VITIS_PROTEIN_MEDIUM = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera Protein medium.fasta";
	public static final String VITIS_PROTEIN_FULL = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera Protein.fasta";
	
	public static final String VITIS_RESULTS_TEST = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera results test.fasta";
	public static final String VITIS_RESULTS_MEDIUM = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera results medium.fasta";
	public static final String VITIS_RESULTS_FULL = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera results.fasta";
	
	public static final String NICOTIANA_DNA_TEST = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum DNA test.fasta";
	public static final String NICOTIANA_DNA_MEDIUM = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum DNA medium.fasta";
	public static final String NICOTIANA_DNA_FULL = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum DNA.fasta";
	
	public static final String NICOTIANA_PROTEIN_TEST = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum Protein test.fasta";
	public static final String NICOTIANA_PROTEIN_MEDIUM = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum Protein medium.fasta";
	public static final String NICOTIANA_PROTEIN_FULL = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum Protein.fasta";

	public static final String NICOTIANA_RESULTS_TEST = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum results test.fasta";
	public static final String NICOTIANA_RESULTS_MEDIUM = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum results medium.fasta";
	public static final String NICOTIANA_RESULTS_FULL = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum results.fasta";
	
	Scanner scanner;
	FileParser fileParser;
	ChunkParser chunkParser;
	Analyzer analyzer;
	
	public BioTUI() {
		this.scanner = new Scanner(System.in);
		this.fileParser = new FileParser();
		this.analyzer = new Analyzer();
	}
	
	public void saveResults(List<Hashtable<String, Integer>> totals, 
							List<Hashtable<String, Double>> frequencies,
							String filename) {
		Reporter reporter = new Reporter(filename);
		reporter.createReport(totals, frequencies,
							  StringUtils.substringAfterLast(StringUtils.substringBeforeLast(filename, "."), "\\"));
	}
	
	public void displayFrequencies(List<Hashtable<String, Double>> frequencies) {
		System.out.println("Frequencies:");
		System.out.println("  Nucleic Acids:");
		for (String key : frequencies.get(0).keySet()) {
			System.out.println("  " + key + ": " + frequencies.get(0).get(key));
		}
		System.out.println("\n");
		System.out.println("  Amino Acids:");
		for (String key : frequencies.get(1).keySet()) {
			System.out.println("  " + key + ": " + frequencies.get(1).get(key));
		}
		System.out.println("\n");
	}
	
	public void displayTotals(List<Hashtable<String, Integer>> totals) {
		System.out.println("Totals:");
		System.out.println("  Nucleic Acids:");
		for (String key : totals.get(0).keySet()) {
			System.out.println("  " + key + ": " + totals.get(0).get(key));
		}
		System.out.println("\n");
		System.out.println("  Amino Acids:");
		for (String key : totals.get(1).keySet()) {
			System.out.println("  " + key + ": " + totals.get(1).get(key));
		}
		System.out.println("\n");
	}
	
	public List<Hashtable<String,Integer>> reportEntireSample(String... files) {
		List<Sequence> list = new ArrayList<Sequence>();
		for (String file : files) {
			list.addAll(fileParser.parseFile(file));
		}
		analyzer.determineTypes(list);
		List<Hashtable<String, Integer>> data = analyzer.determineTotals(list);
		return data;
	}
	
	public List<Hashtable<String,Integer>> reportEntireSampleByChunking(String... files) {
		List<Hashtable<String, Integer>> aggregateData = new ArrayList<Hashtable<String,Integer>>();
		aggregateData.add(new Hashtable<String, Integer>());
		aggregateData.add(new Hashtable<String, Integer>());

		for (String file : files) {
			List<Sequence> list = new ArrayList<Sequence>();
			ChunkParser parser = new ChunkParser(file);
			do {
				list = parser.parseChunk();
				analyzer.determineTypes(list);
				List<Hashtable<String,Integer>> currentData = analyzer.determineTotals(list);
				aggregateData = analyzer.aggregateTotals(aggregateData, currentData);

			} while (list.size() > 0);
		}
		return aggregateData;
	}
		
	public void displayVitisMenu() {
		System.out.println("-----------------Vitis vinifera-------------------");
		System.out.println("Report data for small sample.....................1");
		System.out.println("Report data for medium sample....................2");
		System.out.println("Report data for medium sample using chunking*....3");
		System.out.println("Report data for entire sample....................4");
		System.out.println("Report data for entire sample using chunking*....5");
		System.out.println("Back.............................................0");
		System.out.println("                    * Recommended for large files.");
	}
	
	public void displayNicotianaMenu() {
		System.out.println("---------------Nicotiana tabacum------------------");
		System.out.println("Report data for small sample.....................1");
		System.out.println("Report data for medium sample....................2");
		System.out.println("Report data for medium sample using chunking*....3");
		System.out.println("Report data for entire sample....................4");
		System.out.println("Report data for entire sample using chunking*....5");
		System.out.println("Back.............................................0");
		System.out.println("                    * Recommended for large files.");
	}
	
	public void displayMainMenu() {
		System.out.println("----------------------Main Menu------------------------");
		System.out.println("Calculate frequencies for Vitis vinifera............. 1");
		System.out.println("Calculate frequencies for Nicotiana tabacum.......... 2");
		System.out.println("Calculate frequencies from FASTA file................10");
		System.out.println("Exit................................................. 0");
	}
	
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
	
	public void performChoiceFasta() {
		System.out.println("Enter a FASTA file to load:");
		String filename = scanner.nextLine();
	}
	
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
			performChoiceFasta();
		}
	}
	
	public void run() {
		int choice = -1;
		while (choice != 0) {
			displayMainMenu();
			choice = scanner.nextInt();
			performChoiceMainMenu(choice);
		}
	}
}
