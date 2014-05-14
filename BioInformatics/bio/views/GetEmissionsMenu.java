package bio.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bio.models.HMM;
import bio.models.HMMFactory;

/**
 * A menu for creating the emission labels for an HMM.
 * @author Brian J. Walters
 */
public class GetEmissionsMenu {
	Scanner scanner;
	List<String> list;
	
	
	/**
	 * Creates a new menu to set emissions for
	 * an HMM
	 */
	public GetEmissionsMenu() {
		this.scanner = new Scanner(System.in);
		this.list = new ArrayList<String>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	private void displayMenu() {
		System.out.println("##########################");
		System.out.println(this.list);
		System.out.println("##########################");
		System.out.println("---------Emissions--------");
		System.out.println("Enter Emissions..........1");
		System.out.println("Load Casino Emissions....2");
		System.out.println("Load Weather Emissions...3");
		System.out.println("Clear Emissions..........4");
		if (list != null) {
		System.out.println("Save.....................0");
		} else {
		System.out.println("Back.....................0");
		}	
	}
	

	/**
	 * {@inheritDoc}
	 */
	private void performChoice(int choice) {
		if (choice == 1) {
			getEmissions();
		} else if (choice == 2) {
			HMMFactory factory = new HMMFactory();
			HMM hmm = factory.createCasinoHMM();
			this.list = hmm.getEmissions();
		} else if (choice == 3) {
			HMMFactory factory = new HMMFactory();
			HMM hmm = factory.createWeatherHMM();
			this.list = hmm.getEmissions();
		} else if (choice == 4) {
			this.list = new ArrayList<String>();
		}
	}
	
	/**
	 * Prompts the user for the emissions to be used in
	 * this HMM.
	 */
	private void getEmissions() {
		String state = "\n";
		while (state != "") {
			System.out.print("Enter an emission: ");
			state = scanner.next();
			this.list.add(state);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @return			a list of the emissions
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
