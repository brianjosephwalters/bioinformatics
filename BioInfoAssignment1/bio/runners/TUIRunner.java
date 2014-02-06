package bio.runners;

import bio.view.BioTUI;

/**
 * Runs a program which loads, parses, and analyzes FASTA files.
 * @author Brian J. Walters
 *
 */
public class TUIRunner {
	public static void main (String[] args) {
		BioTUI tui = new BioTUI();
		tui.run();
	}
}
