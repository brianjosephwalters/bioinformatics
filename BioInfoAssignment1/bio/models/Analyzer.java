package bio.models;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Analyzer {
	public static final String nucleicAcidCodes = "ACGTURYKMSWBDHVNX-";
	public static final String aminoAcidCodes = "ABCDEFGHIJKLMNOPQRSTUVWXYZ-*";
	
	public void determineType(Sequence sequence) {
		String seq = sequence.getSequence().toUpperCase();
		// use StringUtils.containsAny(s, "EFIGLOPQZ");
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
		} else if (sequence.getSequence().contains("U")) {
			sequence.setType(Sequence.RNA);
		} else {
			sequence.setType(Sequence.DNA);
		}
	}
	
	public void determineTypes(List<Sequence> list) {
		for (Sequence sequence : list) {
			determineType(sequence);
		}
	}
	
	public Hashtable<String, Integer> determineNucleicAcidCount(Sequence sequence) {
		Hashtable<String, Integer> table = new Hashtable<String, Integer>();

		if ( sequence.getType() == Sequence.DNA ||
			 sequence.getType() == Sequence.RNA    ) {	
			for (int i = 0; i < nucleicAcidCodes.length(); i++) {
				int count = StringUtils.countMatches(sequence.getSequence(), nucleicAcidCodes.charAt(i) + "");
				table.put(nucleicAcidCodes.charAt(i) + "", count);
			}
		} else {
			System.out.println("Received invalid sequence for Nucleic Acid count.");
		}
		return table;
	}
	
	public Hashtable<String, Integer> determineAminoAcidCount(Sequence sequence) {
		Hashtable<String, Integer> table = new Hashtable<String, Integer>();

		if ( sequence.getType() == Sequence.PROTEIN ) {
			for (int i = 0; i < aminoAcidCodes.length(); i++) {
				int count = StringUtils.countMatches(sequence.getSequence(),  aminoAcidCodes.charAt(i) + "");
				table.put(aminoAcidCodes.charAt(i) + "", count);
			}
		} else {
			System.out.println("Received invalid sequence for Amino Acid count.");
		}
		return table;

	}
	
	
	
	public List<Hashtable<String, Integer>> determineTotals(List<Sequence> list) {
		List<Hashtable<String, Integer>> tables = new ArrayList<Hashtable<String,Integer>>();
		Hashtable<String, Integer> aminoAcidTable = new Hashtable<String, Integer>();
		Hashtable<String, Integer> nucleicAcidTable = new Hashtable<String, Integer>();
		
		for (Sequence sequence : list) {
			if ( sequence.getType() == Sequence.DNA ||
				 sequence.getType() == Sequence.RNA    ) {	
				Hashtable<String, Integer> tempTable = determineNucleicAcidCount(sequence);
				for (String key: tempTable.keySet()) {
					int additionalAmount = tempTable.get(key);
					int currentTotal = 0;
					if (additionalAmount != 0) {
						if (nucleicAcidTable .containsKey(key)) {
							currentTotal = nucleicAcidTable.get(key);
						}
						nucleicAcidTable.put(key,  currentTotal + additionalAmount);
					}
				}
			} else if ( sequence.getType() == Sequence.PROTEIN ) {
				Hashtable<String, Integer> tempTable = determineAminoAcidCount(sequence);
				for (String key: tempTable.keySet()) {
					int additionalAmount = tempTable.get(key);
					int currentTotal = 0;
					if (additionalAmount != 0) {
						if (aminoAcidTable .containsKey(key)) {
							currentTotal = aminoAcidTable.get(key);
						}
						aminoAcidTable.put(key,  currentTotal + additionalAmount);
					}
				}
			} else {
				System.out.println("Received sequence of undetermined type.");
			}
		}
		
		tables.add(nucleicAcidTable);
		tables.add(aminoAcidTable);
		return tables;
	}
}
