package bio.models;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Provides analysis methods for FASTA files.
 * @author Brian J. Walters
 *
 */
public class SequenceAnalyzer {
	// Codes used by the FASTA file format.
	public static final String nucleicAcidCodes = "ACGTURYKMSWBDHVNX-";
	public static final String aminoAcidCodes = "ABCDEFGHIJKLMNOPQRSTUVWXYZ-*";
	
	/**
	 * Determines the type of a sequence.
	 * NOTE: This method is not fool-proof.  It works by elimination,
	 *       thus there may be DNA/RNA sequences that completely
	 *       resemble a Protein sequence.
	 * @param sequence	a Sequence object
	 */
	public void determineType(Sequence sequence) {
		// Ensure that the sequences provided are all upper case.
		String seq = sequence.getSequence().toUpperCase();
		// The FASTA format for nucleic acid codes do not contain
		// the following options.  Thus, if they are present,
		// it must be a protein.
		// TODO: use StringUtils.containsAny(s, "EFIGLOPQZ*");
	    if ( seq.contains("E") ||
	    	 seq.contains("F") ||
	    	 seq.contains("I") ||
	    	 seq.contains("J") ||
	    	 seq.contains("L") ||
	    	 seq.contains("O") ||
	    	 seq.contains("P") ||
	    	 seq.contains("Q") ||
	    	 seq.contains("Z") ||
	    	 seq.contains("*") ) {
			sequence.setType(Sequence.PROTEIN);
		} 
	    // DNA does not contain a U code.  
	    else if (sequence.getSequence().contains("U")) {
			sequence.setType(Sequence.RNA);
		} 
	    // If it is neither of the above, it must be DNA.
	    else {
			sequence.setType(Sequence.DNA);
		}
	}
	
	
	/**
     * Determines the type for a list of sequences.
	 * NOTE: This method is not fool-proof.  It works by elimination,
	 *       thus there may be DNA/RNA sequences that completely
	 *       resemble a Protein sequence.	
	 * @param list	a list of Sequence objects.
	 */
	public void determineTypes(List<Sequence> list) {
		for (Sequence sequence : list) {
			determineType(sequence);
		}
	}
	
	/**
	 * Counts the number of each nucleic acid code in a sequence.  
	 * @param sequence	a Sequence object
	 * @return			a hashtable containing all of the present nucleic acid codes
	 *                  and their count in the sequence.
	 */
	public Hashtable<String, Integer> determineNucleicAcidCount(Sequence sequence) {
		Hashtable<String, Integer> table = new Hashtable<String, Integer>();
		// If the sequence is either DNA or RNA...
		if ( sequence.getType() == Sequence.DNA ||
			 sequence.getType() == Sequence.RNA    ) {	
			// For every possible nucleic acid code in the FASTA format...
			for (int i = 0; i < nucleicAcidCodes.length(); i++) {
				// count the number of occurrences of that code...
				int count = StringUtils.countMatches(sequence.getSequence(), nucleicAcidCodes.charAt(i) + "");
				// and store the total in a hashtable.
				table.put(nucleicAcidCodes.charAt(i) + "", count);
			}
		} else {
			System.out.println("Received invalid sequence for Nucleic Acid count.");
		}
		return table;
	}
	
	/**
	 * Counts the number of each amino acid code in a sequence.  
	 * @param sequence	a Sequence object
	 * @return			a hashtable containing all of the present amino acid codes
	 *                  and their count in the sequence.
	 */
	public Hashtable<String, Integer> determineAminoAcidCount(Sequence sequence) {
		Hashtable<String, Integer> table = new Hashtable<String, Integer>();
		// If the sequence is a protein
		if ( sequence.getType() == Sequence.PROTEIN ) {
			// For every possible amino acid code in the FASTA format...
			for (int i = 0; i < aminoAcidCodes.length(); i++) {
				// count the number of occurrences of that code...
				int count = StringUtils.countMatches(sequence.getSequence(),  aminoAcidCodes.charAt(i) + "");
				// and store the total in a hashtable.
				table.put(aminoAcidCodes.charAt(i) + "", count);
			}
		} else {
			System.out.println("Received invalid sequence for Amino Acid count.");
		}
		return table;
	}
	
	/**
	 * Determines the total number of acids in a list of sequences
	 * @param list	a list of Sequence objects
	 * @return 		a set of hashtables containing the totals for each
	 *              amino acid and nucleic acid code.  
	 */
	public List<Hashtable<String, Integer>> determineTotals(List<Sequence> list) {
		// Prepare a data structure
		List<Hashtable<String, Integer>> tables = new ArrayList<Hashtable<String,Integer>>();
		Hashtable<String, Integer> aminoAcidTable = new Hashtable<String, Integer>();
		Hashtable<String, Integer> nucleicAcidTable = new Hashtable<String, Integer>();
		
		// For every sequence in the list...
		for (Sequence sequence : list) {
			// if it contains nucleic acids...
			if ( sequence.getType() == Sequence.DNA ||
				 sequence.getType() == Sequence.RNA    ) {	
				// determine the total for the sequence...
				Hashtable<String, Integer> tempTable = determineNucleicAcidCount(sequence);
				// and then add the totals to its aggregating hashtable.
				for (String key: tempTable.keySet()) {
					int additionalAmount = tempTable.get(key);
					int currentTotal = 0;
					if (additionalAmount != 0) {
						if (nucleicAcidTable.containsKey(key)) {
							currentTotal = nucleicAcidTable.get(key);
						}
						nucleicAcidTable.put(key,  currentTotal + additionalAmount);
					}
				}
			} 
			// if it contains amino acids...
			else if ( sequence.getType() == Sequence.PROTEIN ) {
				// determine the total for the sequence...
				Hashtable<String, Integer> tempTable = determineAminoAcidCount(sequence);
				// and then add the totals to its aggregating hashtable.
				for (String key: tempTable.keySet()) {
					int additionalAmount = tempTable.get(key);
					int currentTotal = 0;
					if (additionalAmount != 0) {
						if (aminoAcidTable.containsKey(key)) {
							currentTotal = aminoAcidTable.get(key);
						}
						aminoAcidTable.put(key,  currentTotal + additionalAmount);
					}
				}
			} else {
				System.out.println("Received sequence of undetermined type.");
			}
		}
		
		// Put the two hashtables in a list and return the data structure.
		tables.add(nucleicAcidTable);
		tables.add(aminoAcidTable);
		return tables;
	}
	
	/**
	 * Combines two lists, each containing a hashtable for nucleic acids and a hashtable
	 * for amino acids.  
	 * NOTE: Used for chunk parsing a set of sequences.
	 * @param aggregate	a running total of the amino and nucleic acids.
	 * @param list		a total of amino and nucleic acids for a chunk of sequences.
	 * @return			a running total of the amino and nucleic acids.
	 */
	public List<Hashtable<String, Integer>> aggregateTotals (List<Hashtable<String, Integer>> aggregate, 
															 List<Hashtable<String, Integer>> list) {
		// Nucleic Acid Hashtable = 0, Amino Acid Hashtable = 1;
		for (int table = 0; table < 2; table++) {
			for (String key : list.get(table).keySet()) {
				int additionalAmount = list.get(table).get(key);
				int currentTotal = 0;
				if (additionalAmount != 0) {
					if (aggregate.get(table).containsKey(key)) {
						currentTotal = aggregate.get(table).get(key);
					}
					aggregate.get(table).put(key, currentTotal + additionalAmount);
				}
			}
		}
		return aggregate;
	}
	
	/**
	 * Calculates the total number of acids.
	 * @param table		a hashtable of acids and the counts
	 * @return			total number of acids in the table
	 */
	public int calculateTotalAcids(Hashtable<String, Integer> table) {
		int total = 0;
		// For every acid in the table...
		for (String key : table.keySet()) {
			// aggregate their count.
			total += table.get(key);
		}
		return total;
	}
	
	/**
	 * Calculates the frequency of a set of acids, given their count.
	 * @param totals	a hashtable of acids and their counts
	 * @return			a hashtable of acids and their relative frequency.
	 */
	public List<Hashtable<String, Double>> calculateFrequencies (List<Hashtable<String, Integer>> totals) {
		List<Hashtable<String, Double>> frequencies = new ArrayList<Hashtable<String,Double>>();
		frequencies.add(new Hashtable<String, Double>());
		frequencies.add(new Hashtable<String, Double>());
		// For both tables:
		// Nucleic Acid Hashtable = 0, Amino Acid Hashtable = 1;
		for (int table = 0; table < 2; table ++) {
			// get the total number of acids in the table...
			int totalAcids = calculateTotalAcids(totals.get(table));
			// and for every acid...
			for (String key : totals.get(table).keySet()) {
				// calculate its relative frequency and store it.
				Double amount = new Double(totals.get(table).get(key)); 
				frequencies.get(table).put(key, amount/totalAcids);
			}
		}
		return frequencies;
	}
	
}
