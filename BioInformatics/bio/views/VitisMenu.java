package bio.views;

import java.util.Hashtable;
import java.util.List;

import bio.models.Analyzer;

/**
 * A menu for working with FASTA files for Vitis vinifera.
 * 
 * @author Brian J. Walters
 */
public class VitisMenu extends AbstractSequenceView {
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
	
	/**
	 * {@inheritDoc}
	 */
	protected void displayMenu() {
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
	 * {@inheritDoc}
	 */
	protected void performChoice(int choice) {
		Analyzer analyzer = new Analyzer();
		List<Hashtable<String,Integer>> totals;
		List<Hashtable<String,Double>> frequencies;
		if (choice == 1) {
			totals = reportEntireSample(VITIS_DNA_TEST, VITIS_PROTEIN_TEST);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveFrequencyStatistics(totals, frequencies, VITIS_RESULTS_TEST);
		} else if (choice == 2) {
			totals = reportEntireSample(VITIS_DNA_MEDIUM, VITIS_PROTEIN_MEDIUM);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveFrequencyStatistics(totals, frequencies, VITIS_RESULTS_MEDIUM);
		} else if (choice == 3) {
			totals = reportEntireSampleByChunking(VITIS_DNA_MEDIUM, VITIS_PROTEIN_MEDIUM);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveFrequencyStatistics(totals, frequencies, VITIS_RESULTS_MEDIUM);
		} else if (choice == 4) {
			totals = reportEntireSample(VITIS_DNA_FULL, VITIS_PROTEIN_FULL);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveFrequencyStatistics(totals, frequencies, VITIS_RESULTS_FULL);
		} else if (choice == 5) {
			totals = reportEntireSampleByChunking(VITIS_DNA_FULL, VITIS_PROTEIN_FULL);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveFrequencyStatistics(totals, frequencies, VITIS_RESULTS_FULL);
		}
	}	
}
