package bio.models;

import java.util.ArrayList;

/**
 * Calculates the alignment of two sequences using the Needleman-Wunsch 
 * algorithm.  This implementation uses the algorithm provided by:
 * http://en.wikipedia.org/wiki/Needleman-Wunsch_algorithm
 * @author Brian J. Walters
 *
 */
public class NWAligner {
	private AcidSequence alpha;
	private AcidSequence beta;
	private SubstitutionMatrix<Integer> subMatrix;
	
	private ArrayList<ArrayList<NWCell>> matrix;
	
	/**
	 * Sets up a new Needleman-Wunsch aligner. 
	 * @param alpha			a Sequence to be aligned
	 * @param beta			another Sequence to be aligned
	 * @param subMatrix		the substitution matrix
	 */
	public NWAligner (AcidSequence alpha,
			           AcidSequence beta,
			           SubstitutionMatrix<Integer> subMatrix) {
		this.alpha = alpha;
		this.beta = beta;
		this.subMatrix = subMatrix;
		
		// Initialize the 2-d matrix with empty NWCells.  
		this.matrix = new ArrayList<ArrayList<NWCell>>();
		for (int a = 0; a < alpha.getSequence().length() + 1; a++) {
			this.matrix.add(new ArrayList<NWCell>());
			for (int b = 0; b < beta.getSequence().length() + 1; b++) {
				// Initialize each cell with the two acids it intersects
				// 0 represents an edge of the matrix.
				String alphaValue = "0";
				if (a == 0) {
					alphaValue = String.valueOf(alpha.getSequence().charAt(a));
				}
				String betaValue = "0";
				if (b == 0) {
					betaValue = String.valueOf(beta.getSequence().charAt(b));
				}
				this.matrix.get(a).add(new NWCell(alphaValue, betaValue));
			}
		}
		
		// Calculate scores for the edges of the matrix.
		fillEdges();
		// Calculate score for the body of the matrix
		fillMatrix();	
	}
	
	/**
	 * Calculates the scores for the upper and left edges of the matrix.
	 */
	private void fillEdges() {
		// For each index along the alpha edge... 
		for (int a = 0; a < alpha.getSequence().length() + 1; a++) {
			// get the cell...
			NWCell cell = matrix.get(a).get(0);
			// calculate and store its score.
			cell.setScore(-subMatrix.getGapWeight() * a);
			
			// if it is not the upper left corner cell (0,0)...
			if (a != 0) {
				// set the acids that this cell intersects
				String alphaToken = String.valueOf(alpha.getSequence().charAt(a - 1));
				String betaToken = String.valueOf(beta.getSequence().charAt(0));
				cell.setAlphaBeta(alphaToken, betaToken);
				// set the path origin of this cell (the previous edge cell).
				cell.setOrigin(matrix.get(a-1).get(0));
			}
		}
		// For each index along the beta edge...
		for (int b = 0; b < beta.getSequence().length() + 1; b++) {
			// get the cell...
			NWCell cell = matrix.get(0).get(b);
			// calculate and store its score
			cell.setScore(-subMatrix.getGapWeight() * b);
			
			// if it is not the upper left corner cell (0,0)...
			if (b != 0) {
				// set the acids that this cell intersects
				String alphaToken = String.valueOf(alpha.getSequence().charAt(0));
				String betaToken = String.valueOf(beta.getSequence().charAt(b - 1));
				cell.setAlphaBeta(alphaToken, betaToken);
				// set the path origin of this cell (the previous edge cell).
				cell.setOrigin(matrix.get(0).get(b-1));
			}
		}
	}
	
	/**
	 * Calculates the scores for the body of the matrix.
	 */
	private void fillMatrix() {
		// For every cell that is not an upper or left edge...
		for (int a = 1; a < alpha.getSequence().length() + 1; a++) {
			for (int b = 1; b < beta.getSequence().length() + 1; b++) {
				// get the acids that this cell intersects
				String alphaToken = String.valueOf(alpha.getSequence().charAt(a - 1));
				String betaToken = String.valueOf(beta.getSequence().charAt(b - 1));
				// get the scores for the upper, left, and upper-left cells.
				int aPrev = matrix.get(a-1).get(b).getScore();
				int bPrev = matrix.get(a).get(b-1).getScore();
				int diaPrev = matrix.get(a-1).get(b-1).getScore();
				// calculate an operation score using the upper-left, upper, and left 
				// cells, plus the gap penalty.
				int match = diaPrev + subMatrix.get(alphaToken, betaToken);
				int delete = aPrev + -subMatrix.getGapWeight();
				int insert = bPrev + -subMatrix.getGapWeight();

				// Get the current cell...
				NWCell cell = matrix.get(a).get(b);
				// If the match operation score was the highest...
				if (match >= delete && match >= insert) {
					// set the score using match
					cell.setScore(match);
					// and set the origin as the upper-left cell.
					cell.setOrigin(matrix.get(a-1).get(b-1));
					
				} 
				// otherwise if the delete operation score was the highest...
				else if (delete >= match && delete >= insert) {
					// set the score using delete
					cell.setScore(delete);
					// and set the origin as the upper cell.
					cell.setOrigin(matrix.get(a-1).get(b));
				} 
				// otherwise the insert operation score must be the highest...
				else {
					// so set the score using insert
					cell.setScore(insert);
					// and set the origin as the left cell.
					cell.setOrigin(matrix.get(a).get(b-1));
				}
				// Set the acids that the cell intersects.
				// TODO: Isn't this already done?
				cell.setAlphaBeta(alphaToken, betaToken);
				// Store the coordinates for the acids on their respective sequences.
				cell.setAlphaBetaCoordinates(a, b);
			}
		}
	}
	
	/**
	 * Create two aligned sequences using the Needleman-Wunsch algorithm. 
	 * @return	a list containing two aligned Sequences.
	 */
	public ArrayList<AcidSequence> getAlignment() {
		ArrayList<AcidSequence> alignedSequences = new ArrayList<AcidSequence>();
		// Create two new sequences to store the aligned sequences.
		AcidSequence alignedAlpha = new AcidSequence();
		AcidSequence alignedBeta = new AcidSequence();
		// Get the length indexes of the last acids in each sequence.
		int alphaStart = alpha.getSequence().length();
		int betaStart = beta.getSequence().length();
		// Get the bottom-most, right-most cell on the matrix.
		NWCell currentCell = matrix.get(alphaStart).get(betaStart);
		// While we have not arrived back at the start of the matrix...
		while (currentCell.getOrigin() != null) {
			// get the origin for the current cell.
			NWCell origin = currentCell.getOrigin();
			// If the origin was the upper cell...
			if (origin.getAlphaCoordinate() == currentCell.getAlphaCoordinate()) {
				// put a gap in the alpha sequence
				alignedAlpha.prependSequence("-");
				// and the expected acid in the beta sequence.
				alignedBeta.prependSequence(currentCell.getBeta());
			}
			// If the origin was the left cell...
			else if (origin.getBetaCoordinate() == currentCell.getBetaCoordinate()) {
				// put the expected acid in the alpha sequence...
				alignedAlpha.prependSequence(currentCell.getAlpha());
				// and a gap in the beta sequence.
				alignedBeta.prependSequence("-");
			}
			// If the origin was the upper-left cell...
			else {
				// put the expected acid in the alpha sequence...
				alignedAlpha.prependSequence(currentCell.getAlpha());
				// and the expected acid in the beta sequence.
				alignedBeta.prependSequence(currentCell.getBeta());
			}
			currentCell = origin;
		}
		
		// Because the rows/columns are flipped, the order here needs to be reversed.
		alignedSequences.add(alignedBeta);
		alignedSequences.add(alignedAlpha);
		return alignedSequences;
	}
	
	/**
	 * Return a String representation of the alignment matrix.
	 */
	public String toString() {
		StringBuilder results = new StringBuilder();
		for (int a = 0; a < alpha.getSequence().length() + 1; a++) {
			for (int b = 0; b < beta.getSequence().length() + 1; b++) {
				results.append(matrix.get(a).get(b) + ", ");
			}
			results.append("\n");
		}
		return results.toString();
	}
}
