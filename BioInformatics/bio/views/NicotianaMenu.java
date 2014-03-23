package bio.views;

import java.util.Hashtable;
import java.util.List;

import bio.models.AcidSequenceAnalyzer;

/**
 * A menu for working with FASTA files for Nicotiana tabacum.
 * 
 * @author Brian J. Walters
 */
public class NicotianaMenu extends AbstractSequenceView {
	// Default file locations
	public static final String NICOTIANA_DNA_TEST = "data\\Nicotiana tabacum DNA test.fasta";
	public static final String NICOTIANA_DNA_MEDIUM = "data\\Nicotiana tabacum DNA medium.fasta";
	public static final String NICOTIANA_DNA_FULL = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum DNA.fasta";
	
	public static final String NICOTIANA_PROTEIN_TEST = "data\\Nicotiana tabacum Protein test.fasta";
	public static final String NICOTIANA_PROTEIN_MEDIUM = "data\\Nicotiana tabacum Protein medium.fasta";
	public static final String NICOTIANA_PROTEIN_FULL = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum Protein.fasta";

	public static final String NICOTIANA_RESULTS_TEST = "results\\Nicotiana tabacum results test.txt";
	public static final String NICOTIANA_RESULTS_MEDIUM = "results\\Nicotiana tabacum results medium.txt";
	public static final String NICOTIANA_RESULTS_FULL = "C:\\Users\\bjw\\Bio Data\\Nicotiana tabacum results.txt";
	
	/**
	 * {@inheritDoc}
	 */
	protected void displayMenu() {
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
	 * {@inheritDoc}
	 */
	protected void performChoice(int choice) {
		AcidSequenceAnalyzer analyzer = new AcidSequenceAnalyzer();
		List<Hashtable<String,Integer>> totals;
		List<Hashtable<String,Double>> frequencies;
		if (choice == 1) {
			totals = reportEntireSample(NICOTIANA_DNA_TEST, NICOTIANA_PROTEIN_TEST);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveFrequencyStatistics(totals, frequencies, NICOTIANA_RESULTS_TEST);
		} else if (choice == 2) {
			totals = reportEntireSample(NICOTIANA_DNA_MEDIUM, NICOTIANA_PROTEIN_MEDIUM);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveFrequencyStatistics(totals, frequencies, NICOTIANA_RESULTS_MEDIUM);
		} else if (choice == 3) {
			totals = reportEntireSampleByChunking(NICOTIANA_DNA_MEDIUM, NICOTIANA_PROTEIN_MEDIUM);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveFrequencyStatistics(totals, frequencies, NICOTIANA_RESULTS_MEDIUM);
		} else if (choice == 4) {
			totals = reportEntireSample(NICOTIANA_DNA_FULL, NICOTIANA_PROTEIN_FULL);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveFrequencyStatistics(totals, frequencies, NICOTIANA_RESULTS_FULL);
		} else if (choice == 5) {
			totals = reportEntireSampleByChunking(NICOTIANA_DNA_FULL, NICOTIANA_PROTEIN_FULL);
			displayTotals(totals);
			frequencies = analyzer.calculateFrequencies(totals);
			displayFrequencies(frequencies);
			saveFrequencyStatistics(totals, frequencies, NICOTIANA_RESULTS_FULL);
		}
	}
}
