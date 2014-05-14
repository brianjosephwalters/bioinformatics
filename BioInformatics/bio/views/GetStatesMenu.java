package bio.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bio.models.HMM;
import bio.models.HMMFactory;

/**
 * A menu for creating the state labels for an HMM
 * @author Brian J. Walters
 */
public class GetStatesMenu {
	Scanner scanner;
	List<String> list;
	
	/**
	 * Create a new menu to get set the states for
	 * an HMM.
	 */
	public GetStatesMenu() {
		this.scanner = new Scanner(System.in);
		this.list = new ArrayList<String>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	private void displayMenu() {
		System.out.println("#######################");
		System.out.println(this.list);
		System.out.println("#######################");
		System.out.println("---------States--------");
		System.out.println("Enter States..........1");
		System.out.println("Load Casino States....2");
		System.out.println("Load Weather States...3");
		System.out.println("Clear States..........4");
		if (list != null) {
		System.out.println("Save..................0");
		} else {
		System.out.println("Back..................0");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	private void performChoice(int choice) {
		if (choice == 1) {
			getStates();
		} else if (choice == 2) {
			HMMFactory factory = new HMMFactory();
			HMM hmm = factory.createCasinoHMM();
			this.list = hmm.getStates();
		} else if (choice == 3) {
			HMMFactory factory = new HMMFactory();
			HMM hmm = factory.createWeatherHMM();
			this.list = hmm.getStates();
		} else if (choice == 4) {
			this.list = new ArrayList<String>();
		}
	}
	
	/**
	 * Prompts the user to enter the states for an HMM.
	 */
	private void getStates() {
		String state = "\n";
		while (state != "") {
			System.out.print("Enter a state: ");
			state = scanner.next();
			this.list.add(state);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @return			a list of states
	 */
	public List<String> run() {
		int choice = -1;
		while (choice != 0) {
			displayMenu();
			choice = scanner.nextInt();
			performChoice(choice);
		}
		return this.list;
	}
}
