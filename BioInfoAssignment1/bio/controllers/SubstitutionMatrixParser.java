package bio.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import bio.models.SubstitutionMatrix;

public class SubstitutionMatrixParser {
	Scanner scanner;
	int gapWeight;
	int lengthWeight;
	
	public SubstitutionMatrixParser(String filename) {
		this.gapWeight = 0;
		this.lengthWeight = 0;
		try {
			this.scanner = new Scanner(new File(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getGapWeight() {
		return this.gapWeight;
	}
	
	public int getLengthWeight() {
		return this.lengthWeight;
	}
	
	public void close() {
		this.scanner.close();
	}
	
	public Hashtable<String, Hashtable<String, Integer>> parseSubstitutionMatrix() {
		Hashtable<String, Hashtable<String, Integer>> matrix = new Hashtable<String, Hashtable<String, Integer>>();
		ArrayList<String> columns = new ArrayList<String>();
		
		// For every line in the file...
		while (scanner.hasNextLine()){
			String line = scanner.nextLine();
			Scanner lineParser = new Scanner(line);
			if (lineParser.hasNext()) {
				String firstWord = lineParser.next();
				if (firstWord.charAt(0) == '#') {
					if (firstWord.toLowerCase().endsWith("gapweight:")) {
						gapWeight = Integer.parseInt(lineParser.next().replace(",", ""));
						lineParser.next();
						lengthWeight = lineParser.nextInt();
					}
				}
				else {
					// if the next token is a string, then we are at a column header line.
					if (!lineParser.hasNextInt()) {
						do {
							columns.add(firstWord);
							matrix.put(firstWord, new Hashtable<String, Integer>());
							firstWord = lineParser.next();
						} while (lineParser.hasNext());
						columns.add(firstWord);
						matrix.put(firstWord, new Hashtable<String, Integer>());
						//System.out.println(columns);
					} 
					// if the next token is an integer, then we are in the matrix.
					else if (lineParser.hasNextInt()) {
						int column = 0;
						Hashtable<String, Integer> entry = matrix.get(firstWord);
						while (lineParser.hasNextInt()) {
							entry.put(columns.get(column), lineParser.nextInt());
							column++;
						}
					}
				}
			}
			lineParser.close();
		}
		return matrix;
	}
	
	public SubstitutionMatrix<Integer> parseSubstitutionMatrix2() {
		SubstitutionMatrix<Integer> matrix = null;
		ArrayList<String> labels = new ArrayList<String>();
		int gapWeight = 0;
		int lengthWeight = 0;
		
		// For every line in the file...
		while (scanner.hasNextLine()){
			String line = scanner.nextLine();
			Scanner lineParser = new Scanner(line);
			if (lineParser.hasNext()) {
				String firstWord = lineParser.next();
				// Get data from the comments
				if (firstWord.charAt(0) == '#') {
					if (firstWord.toLowerCase().endsWith("gapweight:")) {
						// Get the gap weight.
						gapWeight = Integer.parseInt(lineParser.next().replace(",", ""));
						lineParser.next();
						// Get the length weight.
						lengthWeight = lineParser.nextInt();
					}
				}
				else {
					// if the next token is a string, then we are at a column header line.
					if (!lineParser.hasNextInt()) {
						do {
							labels.add(firstWord);
							firstWord = lineParser.next();
						} while (lineParser.hasNext());
						labels.add(firstWord);
						// Create the substitution matrix
						matrix = new SubstitutionMatrix<Integer>(gapWeight, lengthWeight, labels, 0);
					} 
					// if the next token is an integer, then we are in the matrix.
					else if (lineParser.hasNextInt()) {
						int column = 0;
						while (lineParser.hasNextInt()) {
							matrix.set(firstWord, labels.get(column), lineParser.nextInt());
							column++;
						}
					}
				}
			}
			lineParser.close();
		}
		
		return matrix;
	}
}
