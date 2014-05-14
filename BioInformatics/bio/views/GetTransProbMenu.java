package bio.views;

import java.util.List;
import java.util.Scanner;

import bio.models.HMM;
import bio.models.HMMFactory;
import bio.models.LookupTable;

/**
 * A menu for setting the transition probabilities between
 * states for an HMM
 * @author Brian J. Walters
 */
public class GetTransProbMenu extends AbstractView{
	Scanner scanner;
	List<String> states;
	LookupTable<Double> table;
	
	/**
	 * Create a new menu for getting the transitions probabilities
	 * based on state transitions.
	 * @param states		the states in the HMM
	 */
	public GetTransProbMenu(List<String> states) {
		this.scanner = new Scanner(System.in);
		this.states = states;
		this.table = new LookupTable<Double>(states, states, 0.0);
	}
	
	/**
	 * Returns the table of transition probabilities generated
	 * by this menu.
	 * @return		a LookupTable of transition probabilities
	 */
	public LookupTable<Double> getTable() {
		return this.table;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void displayMenu() {
		System.out.println("##############################");
		System.out.println(this.table);
		System.out.println("##############################");
		System.out.println("---Transition Probabilities---");
		System.out.println("Enter Probabilities..........1");
		System.out.println("Load Casino Probabilities....2");
		System.out.println("Load Weather Probabilities...3");
		System.out.println("Clear Probabilities..........4");
		System.out.println("Save.........................0");
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void performChoice(int choice) {
		if (choice == 1) {
			getTransitionProbabilities();
		} else if (choice == 2) {
			HMMFactory factory = new HMMFactory();
			HMM hmm = factory.createCasinoHMM();
			for (String state : states) {
				this.table.set(state, hmm.getStateTransitionProbabilities(state));
			}
		} else if (choice == 3) {
			HMMFactory factory = new HMMFactory();
			HMM hmm = factory.createWeatherHMM();
			for (String state : states) {
				this.table.set(state, hmm.getStateTransitionProbabilities(state));
			}
		} else if (choice == 4) {
			this.table = new LookupTable<Double>(states, states, 0.0);
		}
	}
	
	/**
	 * Prompt the user for the transition probabilities from one state to another.
	 */
	private void getTransitionProbabilities() {
		for (String from : states) {
			for (String to : states) {
				System.out.println("Enter Transition Prob from: " + from + " to: " + to + ": ");
				this.table.set(from, to, scanner.nextDouble());
			}
		}
	}
}
