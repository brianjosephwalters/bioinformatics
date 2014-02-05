package bio.controllers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bio.models.Sequence;


public class FileParser {
	
	public List<Sequence> parseFile(String filename) {
		ArrayList<Sequence> list = new ArrayList<Sequence>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Sequence sequence = new Sequence();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (!line.isEmpty() && line.charAt(0) == '>') {
				sequence = new Sequence(line.substring(1));
			}
			else if (line.isEmpty() || 
					 line.charAt(0) == '\n' ||
					 !scanner.hasNextLine()) {
				list.add(sequence);
			}
			else {
				sequence.appendSequence(line);
			}
		}
		scanner.close();
		return list;
	}
}
