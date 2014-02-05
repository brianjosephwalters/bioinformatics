package bio.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bio.models.Sequence;

public class ChunkParser {
	private int chunkSize;
	private Scanner scanner;
	
	public ChunkParser(String filename) {
		this(filename, 100);
	}
	
	public ChunkParser(String filename, int chunkSize) {
		try {
			scanner = new Scanner( new File(filename) );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.chunkSize = chunkSize;
	}
	
	public List<Sequence> parseChunk() {
		ArrayList<Sequence> list = new ArrayList<Sequence>();
		
		Sequence sequence = new Sequence();
		while (scanner.hasNextLine() && list.size() < chunkSize) {
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
		return list;
	}
	
	public void close() {
		scanner.close();
	}
}
