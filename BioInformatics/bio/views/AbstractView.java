package bio.views;

import java.util.Scanner;
/**
 * An abstract class for the BioInformatics TUI.
 * @author Brian J. Waters
 */
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
	 * Displays a prompt to the user and waits for an
	 * Integer to be entered
	 * @param prompt	a string to display
	 * @return			an integer the user entered
	 */
	protected Integer getInteger(String prompt) {
		Integer choice = null;
		while (choice == null) {
			System.out.println(prompt);
			choice = scanner.nextInt();
		}
		return choice;
	}
	
	/**
	 * Displays a prompt to the user and waits for a
	 * line of characters to be entered
	 * @param prompt	a string to display
	 * @return			a line of characters
	 */
	protected String getLine(String prompt) {
		String result = "";
		while (result == "") {
			System.out.println(prompt);
			result = scanner.nextLine();
		}
		return result;
	}
	
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
