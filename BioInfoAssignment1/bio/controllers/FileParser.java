package bio.controllers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bio.models.Sequence;

/**
 * Parses a FASTA file.
 * @author Brian J. Walters
 *
 */
public class FileParser {
	
	/**
	 * Parses a file to produce list of Sequence objects.
	 * @param filename	the FASTA file to parse
	 * @return			a list of Sequence objects
	 */
	public List<Sequence> parseFile(String filename) {
		ArrayList<Sequence> list = new ArrayList<Sequence>();
		
		// Create a scanner from the provided filename.
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Begin with an initial sequence
		// TODO: assumes file is not empty
		Sequence sequence = new Sequence();
		// While we have not reached the end of the file... 
		while (scanner.hasNextLine()) {
			// Get a line from the FASTA file...
			String line = scanner.nextLine();
			
			// If the line begins with a '>'...
			if (!line.isEmpty() && line.charAt(0) == '>') {
				// then it is the start of a new Sequence
				// and should be used as the description.
				sequence = new Sequence(line.substring(1));
			}
			// If the line is empty, then we have reached
			// the end of a sequence...
			else if (line.isEmpty() || 
					 line.charAt(0) == '\n' ||
					 !scanner.hasNextLine()) {
				// so add it to the list.
				list.add(sequence);
			}
			// Otherwise, the line must be part of the current
			// sequence's data...
			else {
				// so append it to the data of the current Sequence.
				sequence.appendSequence(line);
			}
		}
		// Close the file and return the list.
		scanner.close();
		return list;
	}
}
