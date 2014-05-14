package bio.views;

import bio.models.HMM;
import bio.models.HMMSequenceGenerator;

/**
 * A menu for working with Hidden Markov Models.
 * 
 * @author Brian J. Walters
 */
public class HMMMenu extends AbstractView {

	private HMM hmm;
	
	/**
	 * {@inheritDoc}
	 */
	protected void displayMenu() {
		System.out.println("-------------Hidden Markov Models-------------------");
		System.out.println("Perform Predefined HMM tests...................... 1");
		if (hmm == null) {
			System.out.println("Construct HMM......................................2");
		} else {
			System.out.println("Construct new HMM..................................2");
		}
		if (this.hmm != null) {
		System.out.println("Update HMM.........................................3");
		System.out.println("Create Sequence from HMM...........................4");
		}
		if (this.hmm != null && this.hmm.getStateSequence() != null) {
		System.out.println("Decode Sequence for HMM............................5");
		}
		System.out.println("Back.............................................. 0");
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void performChoice(int choice) {
		if (choice == 1) {
			HMMDefaultsMenu menu = new HMMDefaultsMenu();
			menu.run();
		}
		else if (choice == 2) {
			HMMConstructionMenu menu = new HMMConstructionMenu();
			menu.run();
			hmm = menu.getHMM();
		}
		else if (choice == 3) {
			if (this.hmm != null) {
				HMMUpdateMenu menu = new HMMUpdateMenu(this.hmm);
				menu.run();
				hmm = menu.getHMM();
			}
		}
		else if (choice == 4) {
			if (this.hmm != null) {
				int count = getInteger("How many entries?");
				HMMSequenceGenerator.createSequenceFromHMM(hmm, count);
			}
		}
		else if (choice == 5) {
			if (this.hmm != null && this.hmm.getStateSequence() != null) {
				HMMDecodingMenu menu = new HMMDecodingMenu(this.hmm);
				menu.run();
			}
		}
	}
}
