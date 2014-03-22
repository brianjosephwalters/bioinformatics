package bio.views;

/**
 * A TUI for BioInformatics Projects including
 * 		1) Loading FASTA files, 
 * 			determining whether sequences are DNA, RNA or a Protein,
 * 			and calculating the frequency of each acid.
 *      2) Producing sequence alignments.
 *      3) Working with Hidden Markov Models. 
 *       
 * @author Brian J. Walters
 */
public class BioTUI extends AbstractView{
	
	/**
	 * {@inheritDoc}
	 */
	protected void displayMenu() {
		System.out.println("----------------------Main Menu------------------------");
		System.out.println("Calculate frequencies for Vitis vinifera............. 1");
		System.out.println("Calculate frequencies for Nicotiana tabacum.......... 2");
		System.out.println("Produce Alignments................................... 3");
		System.out.println("Hidden Markov Models................................. 4");
		System.out.println("Exit................................................. 0");
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void performChoice(int choice) {
		if (choice == 1) {
			VitisMenu menu = new VitisMenu();
			menu.run();
		} else if (choice == 2) {
			NicotianaMenu menu = new NicotianaMenu();
			menu.run();
		} else if (choice == 3) {
			AlignmentMenu menu = new AlignmentMenu();
			menu.run();
		} else if (choice == 4) {
			HMMMenu menu = new HMMMenu();
			menu.run();
		} 
	}
}
