package bio.view;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import bio.controllers.ChunkParser;
import bio.controllers.FileParser;
import bio.models.Analyzer;
import bio.models.Sequence;

public class BioTUI {
	public static final int EXIT = 0;
	public static final int VITIS = 1;
	public static final int VITIS_CHUNKABLE = 2;
	public static final int FILE = 10;
	
	//public static final String VITIS_DNA;
	public static final String VITIS_DNA_TEST = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera DNA test.fasta";
	public static final String VITIS_DNA_MEDIUM = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera DNA chunkable.fasta";
	
	//public static final String VITIS_PROTEIN;
	public static final String VITIS_PROTEIN_TEST = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera Protein test.fasta";
	public static final String VITIS_PROTEIN_MEDIUM = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera Protein chunkable.fasta";
	
	Scanner scanner;
	FileParser fileParser;
	ChunkParser chunkParser;
	Analyzer analyzer;
	
	public BioTUI() {
		this.scanner = new Scanner(System.in);
		this.fileParser = new FileParser();
		this.analyzer = new Analyzer();
	}
	
	public void reportEntireSample(String... files) {
		List<Sequence> list = new ArrayList<Sequence>();
		for (String file : files) {
			list.addAll(fileParser.parseFile(file));
		}
		analyzer.determineTypes(list);
		List<Hashtable<String, Integer>> data = analyzer.determineTotals(list);
		displayTotals(data);
	}
	
	public void reportEntireSampleByChunking(String... files) {
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
		displayTotals(aggregateData);
	}
		
	public void displayVitisMenu() {
		System.out.println("Vitis vinifera");
		System.out.println("Report data for small sample...................1");
		System.out.println("Report data for medium sample..................2");
		System.out.println("Report data for medium sample using chunking...3");
		System.out.println("Report data for entire sample..................4");
		System.out.println("Report data for entire sample using chunking...5");
		System.out.println("Back...........................................0");
	}
	
	public void displayMainMenu() {
		System.out.println("Main Menu");
		System.out.println("Calculate frequencies for Vitis vinifera............. 1");
		System.out.println("Calculate frequencies for .... 2");
		System.out.println("Calculate frequencies from FASTA file................10");
		System.out.println("Exit................................................. 0");
	}
	
	public void performChoiceVitis(int choice) {
		if (choice == 1) {
			reportEntireSample(VITIS_DNA_TEST, VITIS_PROTEIN_TEST);
		} else if (choice == 2) {
			reportEntireSample(VITIS_DNA_MEDIUM, VITIS_PROTEIN_MEDIUM);
		} else if (choice == 3) {
			reportEntireSampleByChunking(VITIS_DNA_MEDIUM, VITIS_PROTEIN_MEDIUM);
		}
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
		
		} else if (choice == 3) {
			
		}
	}
	
	public void displayTotals(List<Hashtable<String, Integer>> list) {
		System.out.println("Totals:");
		System.out.println("  Nucleic Acids:");
		for (String key : list.get(0).keySet()) {
			System.out.println("  " + key + ": " + list.get(0).get(key));
		}
		System.out.println("\n");
		System.out.println("  Amino Acids:");
		for (String key : list.get(1).keySet()) {
			System.out.println("  " + key + ": " + list.get(1).get(key));
		}
		System.out.println("\n");
	}
	
	public void run() {
		int choice = -1;
		while (choice != EXIT) {
			displayMainMenu();
			choice = scanner.nextInt();
			performChoiceMainMenu(choice);
		}
	}
}
