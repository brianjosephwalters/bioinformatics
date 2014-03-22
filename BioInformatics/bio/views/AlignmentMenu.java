package bio.views;

import java.util.ArrayList;
import java.util.List;

import bio.controllers.FileParser;
import bio.controllers.Reporter;
import bio.controllers.SubstitutionMatrixParser;
import bio.models.NWAligner;
import bio.models.Sequence;
import bio.models.SubstitutionMatrix;

/**
 * A menu for performing sequence alignments.
 * 
 * @author Brian J. Walters
 */
public class AlignmentMenu extends AbstractView {
	// Default file locations
	public static final String MUS_MUSCULU = "data\\Mus musculu.fasta";
	public static final String XENOPUS_LAEVIS = "data\\Xenopus laevis.fasta";
	public static final String MUS_XENOPUS_ALIGNMENT = "data\\Mus Xenopus alignment.txt";
	
	public static final String HYPHANTRIA_CUNEA = "data\\Hyphantria cunea stat.fasta";
	public static final String ANOPHELES_GAMBIAE = "data\\Anopheles gambiae stat.fasta";
	public static final String HYPHANTRIA_ANOPHELES_ALIGNMENT = "data\\Hyphantria Anopheles alignment.txt";

	public static final String BLOSUM50 = "data\\BLOSUM50.txt";
	
	/**
	 * Writes the aligned sequences to a file.
	 * @param list			a list of aligned sequences
	 * @param filename		the name of the file to store the sequences
	 */
	private void saveSequenceAlignment(ArrayList<Sequence> list, String filename) {
		Reporter reporter = new Reporter(filename);
		reporter.createSequenceAlignmentReport(list);
		reporter.close();
	}
	
	/**
	 * Displays the aligned sequences to the screen.
	 * @param list		a list of aligned sequences
	 */
	private void displaySequenceAlignment(List<Sequence> list) {
		for (Sequence seq : list) {
			System.out.println(seq.getSequence());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void displayMenu() {
		System.out.println("-----------------------Alignments-------------------");
		System.out.println("Test Sequences.....................................1");
		System.out.println("Align Mus musculu and Xenopus laevis sequences.....2");
		System.out.println("Anopheles gambiae and Hyphantria cunea sequences...3");
		System.out.println("Back...............................................0");
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void performChoice(int choice) {
		if (choice == 1) {
			SubstitutionMatrixParser parser = new SubstitutionMatrixParser(BLOSUM50);
			SubstitutionMatrix<Integer> subMatrix = parser.parseSubstitutionMatrix();
			subMatrix.setGapWeight(8);
			Sequence alpha = new Sequence();
			alpha.appendSequence("PAWHEAE");
			Sequence beta = new Sequence();
			beta.appendSequence("HEAGAWGHEE");
			NWAligner aligner = new NWAligner(alpha, beta, subMatrix);
			System.out.println(aligner);
			ArrayList<Sequence> alignedSequences = aligner.getAlignment();
			displaySequenceAlignment(alignedSequences);
		}
		else if (choice == 2) {
			SubstitutionMatrixParser parser = new SubstitutionMatrixParser(BLOSUM50);
			SubstitutionMatrix<Integer> subMatrix = parser.parseSubstitutionMatrix();
			subMatrix.setGapWeight(8);
			FileParser fileParser = new FileParser();
			Sequence mus = fileParser.parseFile(MUS_MUSCULU).get(0);
			Sequence xenopus = fileParser.parseFile(XENOPUS_LAEVIS).get(0);
			NWAligner aligner = new NWAligner(mus, xenopus, subMatrix);
			ArrayList<Sequence> alignedSequences = aligner.getAlignment();
			displaySequenceAlignment(alignedSequences);
			saveSequenceAlignment(alignedSequences, MUS_XENOPUS_ALIGNMENT);
		}
		else if (choice == 3) {
			SubstitutionMatrixParser parser = new SubstitutionMatrixParser(BLOSUM50);
			SubstitutionMatrix<Integer> subMatrix = parser.parseSubstitutionMatrix();
			subMatrix.setGapWeight(8);
			FileParser fileParser = new FileParser();
			Sequence hyphantria = fileParser.parseFile(HYPHANTRIA_CUNEA).get(0);
			Sequence anopheles = fileParser.parseFile(ANOPHELES_GAMBIAE).get(0);
			NWAligner aligner = new NWAligner(hyphantria, anopheles, subMatrix);
			ArrayList<Sequence> alignedSequences = aligner.getAlignment();
			displaySequenceAlignment(alignedSequences);
			saveSequenceAlignment(alignedSequences, HYPHANTRIA_ANOPHELES_ALIGNMENT);
		}
	}
}
