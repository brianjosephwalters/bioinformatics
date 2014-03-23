package bio.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bio.models.AcidSequence;

/**
 * Parses a FASTA file, providing chunks of Sequence objects
 * at a time.
 * @author bjw
 *
 */
public class ChunkParser {
	private int chunkSize;		// The number of sequences to parse at a time.
	private Scanner scanner;	// A file scanner.
	
	/**
	 * Creates a ChunkParser object with a default chunk size of 100.
	 * @param filename
	 */
	public ChunkParser(String filename) {
		this(filename, 100);
	}
	
	/**
	 * Creates a ChunkParser
	 * @param filename		the FASTA file to parse
	 * @param chunkSize		the number of Sequence objects to parse at once.
	 */
	public ChunkParser(String filename, int chunkSize) {
		try {
			// Create a scanner from the provided file.
			scanner = new Scanner( new File(filename) );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.chunkSize = chunkSize;
	}
	
	/**
	 * Parses a chunk of Sequence objects from the file.
	 * @return	a list of Sequence objects.
	 */
	public List<AcidSequence> parseChunk() {
		ArrayList<AcidSequence> list = new ArrayList<AcidSequence>();
		
		// Begin with an initial sequence
		// TODO: assumes file is not empty
		AcidSequence sequence = new AcidSequence();
		// While the file is not finished and we have not reached the 
		// total number of sequences per chunk...
		while (scanner.hasNextLine() && list.size() < chunkSize) {
			// Get a line from the FASTA file...
			String line = scanner.nextLine();
			
			// If the line begins with a '>'...
			if (!line.isEmpty() && line.charAt(0) == '>') {
				// then it is the start of a new Sequence
				// and should be used as the description.
				sequence = new AcidSequence(line.substring(1));
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
		return list;
	}
	
	/**
	 * Closes the file opened by the ChunkParser.
	 */
	public void close() {
		scanner.close();
	}
}
