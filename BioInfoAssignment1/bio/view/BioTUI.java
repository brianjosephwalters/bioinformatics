package bio.view;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import bio.controllers.Controller;
import bio.models.Analyzer;
import bio.models.Sequence;

public class BioTUI {
	public static final int EXIT = 0;
	public static final int VITIS = 1;
	public static final int FILE = 10;
	
	//public static final String VITIS_DNA;
	public static final String VITIS_DNA_TEST = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera DNA test.fasta";
	//public static final String VITIS_PROTEIN;
	public static final String VITIS_PROTEIN_TEST = "C:\\Users\\bjw\\Bio Data\\Vitis vinifera Protein test.fasta";
	
	
	Scanner scanner;
	Controller controller;
	Analyzer analyzer;
	
	public BioTUI() {
		this.scanner = new Scanner(System.in);
		this.controller = new Controller();
		this.analyzer = new Analyzer();
	}
	
	
	public void displayMenu() {
		System.out.println("Load sequences for Vitis vinifera..... 1");
		System.out.println("Enter a file..........................10");
		System.out.println("Exit.................................. 0");
	}
	
	public void performChoice(int choice) {
		if (choice == 1) {
			System.out.println("Parsing the files...\n");
			ArrayList<Sequence> list = controller.parseFile(VITIS_DNA_TEST);
			list.addAll(controller.parseFile(VITIS_PROTEIN_TEST));
			
			System.out.println("Determining types for each Sequence...\n");
			analyzer.determineTypes(list);
			
			System.out.println("Determining frequences for each acid...\n");
			List<Hashtable<String, Integer>> tables = analyzer.determineTotals(list);
		
			System.out.println("Totals:");
			System.out.println("  Nucleic Acids:");
			for (String key : tables.get(0).keySet()) {
				System.out.println("  " + key + ": " + tables.get(0).get(key));
			}
			System.out.println("\n");
			System.out.println("Amino Acids:");
			for (String key : tables.get(1).keySet()) {
				System.out.println("  " + key + ": " + tables.get(1).get(key));
			}
			System.out.println("\n");
		}
		if (choice == 10) {
			
		}
	}
	
	public void run() {
		int choice = -1;
		while (choice != EXIT) {
			displayMenu();
			choice = scanner.nextInt();
			performChoice(choice);
		}
	}
}
