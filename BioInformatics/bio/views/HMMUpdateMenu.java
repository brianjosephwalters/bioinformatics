package bio.views;

import bio.models.HMM;

/**
 * A menu for updating an HMM that has been loaded
 * @author bjw
 *
 */
public class HMMUpdateMenu extends AbstractView{

	HMM hmm;
	
	/**
	 * Create a new menu to update the given HMM
	 * @param hmm		a hidden markov model
	 */
	public HMMUpdateMenu(HMM hmm) {
		this.hmm = hmm;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void displayMenu() {
		displayHMM();
		System.out.println("--------------------Create HMM-----------------------");
		System.out.println("Update State Transition Probabilities...............1");
		System.out.println("Update Emission Probabilities.......................2");
		System.out.println("Update Starting States..............................3");
		System.out.println("Update Ending States................................4");
		System.out.println("Back............................................... 0");
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void performChoice(int choice) {
		if (choice == 1) {
			GetTransProbMenu menu = new GetTransProbMenu(hmm.getStates());
			menu.run();
			hmm.setStateTransitionProbabilities(menu.getTable());
		} else if (choice == 2) {
			GetEmissionProbMenu menu = new GetEmissionProbMenu(hmm.getStates(), hmm.getEmissions());
			menu.run();
			hmm.setEmissionProbabilities(menu.getTable());
		} else if (choice == 3) {
			GetBeginStateProb menu = new GetBeginStateProb(hmm.getStates());
			menu.run();
			hmm.setBeginState(menu.getTable());
		} else if (choice == 4) {
			GetEndStateProb menu = new GetEndStateProb(hmm.getStates());
			menu.run();
			hmm.setEndState(menu.getTable());
		}
	}

	/**
	 * Displays the HMM.
	 */
	private void displayHMM() {
		System.out.println("#################################################");
		System.out.println(hmm.getStates());
		System.out.println(hmm.getEmissions());
		for (String state : hmm.getStates()) {
			System.out.println(hmm.getStateTransitionProbabilities(state));
		}
		for (String state : hmm.getStates()) {
			System.out.println(hmm.getEmissionProbabilities(state));
		}
		System.out.println(hmm.getBeginState());
		System.out.println(hmm.getEndState());
		System.out.println("#################################################");
	}
	
	/**
	 * Returns the HMM produced by this menu.
	 * @return		a hidden markov model
	 */
	public HMM getHMM() {
		return this.hmm;
	}
}
