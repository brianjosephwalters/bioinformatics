package bio.views;

import java.util.List;
import java.util.Scanner;

import bio.models.HMM;
import bio.models.HMMFactory;
import bio.models.LookupTable;

/**
 * Creates a TUI for getting emission probabilities from the user.
 * @author Brian J. Walters
 */
public class GetEmissionProbMenu extends AbstractView{
	Scanner scanner;
	List<String> states;
	List<String> emissions;
	LookupTable<Double> table;
	
	/**
	 * Create a new Emission Probability Menu.
	 * @param states		the states 
	 * @param emissions		the emissions
	 */
	public GetEmissionProbMenu(List<String> states, List<String> emissions) {
		this.scanner = new Scanner(System.in);
		this.states = states;
		this.emissions = emissions;
		this.table = new LookupTable<Double>(states, emissions, 0.0);
	}

	/**
	 * Returns the table of states created by the user.
	 * @return		a table of states and their values
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
		System.out.println("----Emission Probabilities----");
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
			getEmissionProbabilities();
		} else if (choice == 2) {
			HMMFactory factory = new HMMFactory();
			HMM hmm = factory.createCasinoHMM();
			for (String state : states) {
				this.table.set(state, hmm.getEmissionProbabilities(state));
			}
		} else if (choice == 3) {
			HMMFactory factory = new HMMFactory();
			HMM hmm = factory.createWeatherHMM();
			for (String state : states) {
				this.table.set(state, hmm.getEmissionProbabilities(state));
			}
		} else if (choice == 4) {
			this.table = new LookupTable<Double>(states, emissions, 0.0);
		}
	}

	/**
	 * Prompt the user to enter emission probabilities for each
	 * emission in each state.
	 */
	private void getEmissionProbabilities() {
		for (String state : states) {
			for (String emission : emissions) {
				System.out.println("Enter Emission Prob for " + emission + " in state " + state + ": ");
				this.table.set(state, emission, scanner.nextDouble());
			}
		}
	}
}
