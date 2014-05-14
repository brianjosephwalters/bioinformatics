package bio.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import bio.models.DecodingInterface;

/**
 * Creates a report for decodings of an HMM.
 * @author Brian J. Walters
 */
public class DecodingReporter {
	private PrintWriter pw;
	
	/**
	 * Creates a decoding reporter.
	 * @param filename		filename of file to write or ""
	 * @throws IOException	
	 */
	public DecodingReporter(String filename) throws IOException {
		if (filename != "") {
			pw = new PrintWriter(filename);
		}
	}
	
	/**
	 * Creates a decoding report for the HMM.  
	 * @param title			title of the 
	 * @param decoding		
	 * @return				
	 */
	public String generateDecodingReport(String title, DecodingInterface decoding) {
		StringBuilder sb = new StringBuilder();
		sb.append(title + "\n");
		sb.append("Original Sequence: " + decoding.getHMM().getStateSequence() + "\n");
		sb.append("Decoded Sequence:  " + decoding.getSequence() + "\n");
		sb.append(String.format("Accuracy: %8.4f\n", 
				  decoding.getHMM().accuracy(decoding.getSequence())));
		sb.append(String.format("Num Transitions: %8.4f\n", 
				  decoding.getHMM().compareNumTransitions(decoding.getSequence())));
		for (String state : decoding.getHMM().getStates()) {
			sb.append(state + ":\n");
			sb.append(String.format("  True Positives: %d\n", 
					  decoding.getHMM().truePositives(decoding.getSequence(), state)));
			sb.append(String.format("  False Positives: %d\n", 
					  decoding.getHMM().falsePositives(decoding.getSequence(), state)));
			sb.append(String.format("  True Negatives: %d\n", 
					  decoding.getHMM().truePositives(decoding.getSequence(), state)));
			sb.append(String.format("  False Negatives: %d\n",
					  decoding.getHMM().falseNegatives(decoding.getSequence(), state)));
			sb.append(String.format("  Sensitivity: %8.4f\n",
					  decoding.getHMM().sensitivity(decoding.getSequence(), state)));
			sb.append(String.format("  Specificity: %8.4f\n",
					  decoding.getHMM().specificity(decoding.getSequence(), state)));
		}
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * Write a decoding report to a file.
	 * @param title			title of the report
	 * @param decoding		the decoding on which to produce a report
	 */
	public void writeReport(String title, DecodingInterface decoding) {
		pw.print(generateDecodingReport(title, decoding));
	}
	
	/**
	 * Display a decoding report to the console.
	 * @param title			title of the report
	 * @param decoding		the decoding on which to produce a report
	 */
	public void displayReport(String title, DecodingInterface decoding) {
		System.out.print(generateDecodingReport(title, decoding));
	}
	
	/**
	 * Close the reporter.
	 */
	public void close() {
		if (pw != null) {
			pw.flush();
			pw.close();
		}
	}
}
