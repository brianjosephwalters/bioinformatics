package bio.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import bio.models.SubstitutionMatrix;

/**
 * Parses the BOSUM50 substitution matrix file found originally at:
 * http://web.calstatela.edu/faculty/jmomand/BLOSUM50.txt
 * 
 * @author Brian J. Walters
 */
public class SubstitutionMatrixParser {
	Scanner scanner;
	
	/**
	 * Creates a new parser for a BOSUM50 Substitution Matrix file.
	 * @param filename		filename and path of the BOSUM50 file.
	 */
	public SubstitutionMatrixParser(String filename) {
		try {
			this.scanner = new Scanner(new File(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the the parser.
	 */
	public void close() {
		this.scanner.close();
	}
	
	/**
	 * Parses the BOSUM50 substitution matrix file and constructs
	 * a SubstitutionMatrix object.
	 * @return	a substitution matrix
	 */
	public SubstitutionMatrix<Integer> parseSubstitutionMatrix() {
		SubstitutionMatrix<Integer> matrix = null;
		ArrayList<String> labels = new ArrayList<String>();
		int gapWeight = 0;
		int lengthWeight = 0;
		
		// For every line in the file...
		while (scanner.hasNextLine()){
			// Get the line...
			String line = scanner.nextLine();
			// Now prepare to parse each line separately
			Scanner lineParser = new Scanner(line);
			if (lineParser.hasNext()) {
				String firstWord = lineParser.next();
				// Get data from the comments
				if (firstWord.charAt(0) == '#') {
					// When we find the line containing gap weight and
					// length weight, store that information.
					if (firstWord.toLowerCase().endsWith("gapweight:")) {
						// Get the gap weight.
						gapWeight = Integer.parseInt(lineParser.next().replace(",", ""));
						lineParser.next();
						// Get the length weight.
						lengthWeight = lineParser.nextInt();
					}
				}
				// If the line is not a comment then it should be the matrix data.
				else {
					// If the next token is a string, then we are at the column header line...
					if (!lineParser.hasNextInt()) {
						// so add each header to the list of column lables.
						do {
							labels.add(firstWord);
							firstWord = lineParser.next();
						} while (lineParser.hasNext());
						labels.add(firstWord);
						// Create the substitution matrix
						matrix = new SubstitutionMatrix<Integer>(gapWeight, lengthWeight, labels, 0);
					} 
					// if the next token is an integer, then we are in the matrix...
					else if (lineParser.hasNextInt()) {
						int column = 0;
						// so add each data value to the appropriate column.
						while (lineParser.hasNextInt()) {
							matrix.set(firstWord, labels.get(column), lineParser.nextInt());
							column++;
						}
					}
				}
			}
			// close the line parser so we can have a fresh parser for the next line.
			lineParser.close();
		}
		return matrix;
	}
}
