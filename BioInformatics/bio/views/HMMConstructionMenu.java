package bio.views;

import java.util.Hashtable;
import java.util.List;

import bio.models.HMM;
import bio.models.LookupTable;

/**
 * A menu for constructing HMM
 * @author Brian J. Walters
 */
public class HMMConstructionMenu extends AbstractView{
	private HMM hmm;
	private List<String> states;
	private List<String> emissions;
	private LookupTable<Double> emissionProb;
	private LookupTable<Double> stateProb;
	private Hashtable<String, Double> beginStateProb;
	private Hashtable<String, Double> endStateProb;

	/**
	 * {@inheritDoc}
	 */
	protected void displayMenu() {
		displayData();
		System.out.println("-------------------Create HMM-----------------------");
		System.out.println("Enter States...................................... 1");
		System.out.println("Enter Emissions....................................2");
		if (this.states != null) {
			System.out.println("Enter State Transition Probabilities...............3");
			if (this.emissions != null) {
				System.out.println("Enter Emission Probabilities.......................4");
				System.out.println("Enter Starting States..............................5");
				System.out.println("Enter Ending States................................6");
				if (this.emissionProb != null && this.stateProb != null &&
					this.beginStateProb != null && this.endStateProb != null) {
				System.out.println("Create HMM.........................................7");
				}
			}
		}
		if (this.hmm == null) {
		System.out.println("Back...............................................0");
		} else {
		System.out.println("Save...............................................0");
		}
	}
	
	/**
	 * Displays the HMM produced so far.
	 */
	private void displayData() {
		if (this.states != null) {
			System.out.println("#################################################");
			System.out.println("States:    " + this.states);
			if (this.emissions != null) {
				System.out.println("Emissions: " + this.emissions);
				if (this.stateProb != null) {
					System.out.println("State Transition Probabilities:\n");
					for (String state : this.states) {
						System.out.println("  " + stateProb.get(state));
					} 
				}
				if (this.emissionProb != null) {
					System.out.println("Emission Probabilities:\n");
					for (String state : this.states) {
						System.out.println("  " + emissionProb.get(state));
					}
					System.out.println();
				}
				if (this.beginStateProb != null) {
					System.out.println("Begin State Probabilities:\n");
					System.out.println(this.beginStateProb);
				}
				if (this.endStateProb != null) {
					System.out.println("End State Probabilities:\n");
					System.out.println(this.endStateProb);
				}
			}
			System.out.println("#################################################");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void performChoice(int choice) {
		if (choice == 1) {
			GetStatesMenu menu = new GetStatesMenu();
			this.states = menu.run();
			this.emissions = null;
			this.stateProb = null;
			this.emissionProb = null;
			this.beginStateProb = null;
			this.endStateProb = null;
		} 
		else if (choice == 2) {
			GetEmissionsMenu menu = new GetEmissionsMenu();
			this.emissions = menu.run();
			this.stateProb = null;
			this.emissionProb = null;
			this.beginStateProb = null;
			this.endStateProb = null;
		} 
		else if (choice == 3) {
			if (this.states != null) {
				GetTransProbMenu menu = new GetTransProbMenu(states);
				menu.run();
				this.stateProb = menu.getTable();
			}
		} 
		else if (choice == 4) {
			if (this.states != null && this.emissions != null) {
				GetEmissionProbMenu menu = new GetEmissionProbMenu(states, emissions);
				menu.run();
				this.emissionProb = menu.getTable();
			}
		} 
		else if (choice == 5) {
			if (this.states != null) {
				GetBeginStateProb menu = new GetBeginStateProb(states);
				menu.run();
				this.beginStateProb = menu.getTable();
			}
		} 
		else if (choice == 6) {
			if (this.states != null) {
				GetEndStateProb menu = new GetEndStateProb(states);
				menu.run();
				this.endStateProb = menu.getTable();
			}		
		} 
		else if (choice == 7) {
			if (this.emissionProb != null && this.stateProb != null &&
				this.beginStateProb != null && this.endStateProb != null) {
				createHMM();
			}
		}
	}
	
	/**
	 * Create an HMM from the data gathered and display the values
	 * to the terminal.
	 */
	private void createHMM() {
		hmm = new HMM(this.emissions, this.states,
				      this.emissionProb, this.stateProb,
				      this.beginStateProb, this.endStateProb);
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
	 * Get the HMM produced by this menu.
	 * @return		a hidden markov model
	 */
	public HMM getHMM() {
		return this.hmm;
	}
}
