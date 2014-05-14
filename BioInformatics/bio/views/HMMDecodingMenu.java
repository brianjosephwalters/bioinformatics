package bio.views;

import bio.controllers.DecodingReporter;
import bio.models.DecodingInterface;
import bio.models.HMM;
import bio.models.PosteriorDecoding;
import bio.models.ViterbiDecoding;

/**
 * A menu for decoding an HMM
 * @author Brian J. Walters
 */
public class HMMDecodingMenu extends AbstractView {
	private HMM hmm;
	private DecodingInterface decoding;
	
	/**
	 * Create a new mnu for decoding HMMs.
	 * @param hmm		a hidden Markov Model
	 */
	public HMMDecodingMenu(HMM hmm) {
		this.hmm = hmm;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void displayMenu() {
		System.out.println("-------------HMM Decoding Options-------------------");
		System.out.println("Decoding using Viterbi............................ 1");
		System.out.println("Decoding using Posterior...........................2");
		if (decoding != null) {
		System.out.println("Display Statistics.................................3");
		System.out.println("Save HMM Decoding statitics........................4");
		}
		System.out.println("Back.............................................. 0");
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void performChoice(int choice) {
		if (choice == 1) {
			decoding = new ViterbiDecoding(hmm);
		}
		else if (choice == 2) {
			decoding = new PosteriorDecoding(hmm);
		}
		else if (choice == 3) {
			if (decoding != null){
				DecodingReporter reporter = null;
				try {
					reporter = new DecodingReporter("");
				} catch (Exception e) {

				}
				reporter.displayReport("", decoding);
			}
		}
		else if (choice == 4) {
			if (decoding != null) {
				boolean done = false; 
				DecodingReporter reporter = null;
				while (!done) {
					String filename = getLine("Enter a file path");
					try {
						reporter = new DecodingReporter(filename);
						done = true;
					} catch (Exception e) {
						System.out.println("Unable to open file.");
					} finally {
						reporter.close();
					}
				}
				String reportName = getLine("Enter a report title");
				reporter.generateDecodingReport(reportName, decoding);
				reporter.close();
			}
		}
	}
}
