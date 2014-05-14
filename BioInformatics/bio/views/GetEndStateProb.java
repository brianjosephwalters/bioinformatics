package bio.views;

import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import bio.models.HMM;
import bio.models.HMMFactory;

/**
 * A menu for getting the End State Probabilities
 * for an HMM
 * @author Brian J. Walters
 */
public class GetEndStateProb extends AbstractView {
	Scanner scanner;
	List<String> states;
	Hashtable<String, Double> table;

	/**
	 * Creates a new End State menu.
	 * @param states	a list of states
	 */
	public GetEndStateProb(List<String> states) {
		scanner = new Scanner(System.in);
		this.states = states;
		this.table = new Hashtable<String, Double>();
	}
	
	/**
	 * Returns the table of states created by the user.
	 * @return		a table of states and their values
	 */
	public Hashtable<String, Double> getTable() {
		return this.table;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void displayMenu() {
		System.out.println("################################");
		System.out.println(this.table);
		System.out.println("################################");
		System.out.println("-----End State Probabilities----");
		System.out.println("Enter Probabilities............1");
		System.out.println("Load Casino Probabilities......2");
		System.out.println("Load Weather Probabilities.....3");
		System.out.println("Clear Probabilities............4");
		System.out.println("Save...........................0");
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void performChoice(int choice) {
		if (choice == 1) {
			getEndStateProbabilities();
		} else if (choice == 2) {
			HMMFactory factory = new HMMFactory();
			HMM hmm = factory.createCasinoHMM();
			this.table = hmm.getBeginState();
		} else if (choice == 3) {
			HMMFactory factory = new HMMFactory();
			HMM hmm = factory.createWeatherHMM();
			this.table = hmm.getBeginState();
		} else if (choice == 4) {
			this.table = new Hashtable<String, Double>();
		}
	}

	/**
	 * Prompt the user to enter a value for each of the begin states.
	 */
	private void getEndStateProbabilities() {
		for (String state: states) {
			System.out.println("Enter end state prob for " + state + ": ");
			this.table.put(state, scanner.nextDouble());
		}
	}
}
