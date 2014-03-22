package bio.views;

import java.util.Scanner;

public abstract class AbstractView implements ViewInterface {
	// Instance Variables
	private Scanner scanner;
	
	// Constructors
	/**
	 * Initializes abstract features of an AbstractView. 
	 */
	public AbstractView() {
		this.scanner = new Scanner(System.in);
	}
	
	/**
	 * Displays the menu to the terminal.
	 */
	protected abstract void displayMenu();
	
	/**
	 * Performs responses to the menu.
	 * @param choice	the user's choice 	 
	 */
	protected abstract void performChoice(int choice);
	
	/**
	 * {@inheritDoc}
	 */
	public void run() {
		int choice = -1;
		while (choice != 0) {
			displayMenu();
			choice = scanner.nextInt();
			performChoice(choice);
		}
	}
}
