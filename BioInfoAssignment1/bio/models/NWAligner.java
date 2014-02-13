package bio.models;

import java.util.ArrayList;

public class NWAligner {
	private Sequence alpha;
	private Sequence beta;
	private SubstitutionMatrix<Integer> subMatrix;
	
	private ArrayList<ArrayList<NWCell>> matrix;
	
	public NWAligner (Sequence alpha,
			           Sequence beta,
			           SubstitutionMatrix<Integer> subMatrix) {
		this.alpha = alpha;
		this.beta = beta;
		this.subMatrix = subMatrix;

		this.matrix = new ArrayList<ArrayList<NWCell>>();
		for (int a = 0; a < alpha.getSequence().length() + 1; a++) {
			this.matrix.add(new ArrayList<NWCell>());
			for (int b = 0; b < beta.getSequence().length() + 1; b++) {
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
		
		initializeEdges();
		fillMatrix();	
	}
	
	private void initializeEdges() {
		for (int a = 0; a < alpha.getSequence().length() + 1; a++) {
			NWCell cell = matrix.get(a).get(0);
			cell.setScore(-subMatrix.getGapWeight() * a);
			if (a != 0) {
				String alphaToken = String.valueOf(alpha.getSequence().charAt(a - 1));
				String betaToken = String.valueOf(beta.getSequence().charAt(0));
				cell.setAlphaBeta(alphaToken, betaToken);
				cell.setOrigin(matrix.get(a-1).get(0));
			}
		}
		for (int b = 0; b < beta.getSequence().length() + 1; b++) {
			NWCell cell = matrix.get(0).get(b);
			cell.setScore(-subMatrix.getGapWeight() * b);
			if (b != 0) {
				String alphaToken = String.valueOf(alpha.getSequence().charAt(0));
				String betaToken = String.valueOf(beta.getSequence().charAt(b - 1));
				cell.setAlphaBeta(alphaToken, betaToken);
				cell.setOrigin(matrix.get(0).get(b-1));
			}
		}
	}
	
	private void fillMatrix() {
		for (int a = 1; a < alpha.getSequence().length() + 1; a++) {
			for (int b = 1; b < beta.getSequence().length() + 1; b++) {
				String alphaToken = String.valueOf(alpha.getSequence().charAt(a - 1));
				String betaToken = String.valueOf(beta.getSequence().charAt(b - 1));
				int aPrev = matrix.get(a-1).get(b).getScore();
				int bPrev = matrix.get(a).get(b-1).getScore();
				int diaPrev = matrix.get(a-1).get(b-1).getScore();
				
				int match = diaPrev + subMatrix.get(alphaToken, betaToken);
				int delete = aPrev + -subMatrix.getGapWeight();
				int insert = bPrev + -subMatrix.getGapWeight();

				NWCell cell = matrix.get(a).get(b);
				if (match >= delete && match >= insert) {
					cell.setScore(match);
					cell.setOrigin(matrix.get(a-1).get(b-1));
					
				} else if (delete >= match && delete >= insert) {
					cell.setScore(delete);
					cell.setOrigin(matrix.get(a-1).get(b));
				} else {
					cell.setScore(insert);
					cell.setOrigin(matrix.get(a).get(b-1));
				}
				cell.setAlphaBeta(alphaToken, betaToken);
				cell.setAlphaBetaCoordinates(a, b);
			}
		}
	}
	
	public ArrayList<Sequence> getAlignment() {
		ArrayList<Sequence> alignedSequences = new ArrayList<Sequence>();
		Sequence alignedAlpha = new Sequence();
		Sequence alignedBeta = new Sequence();
		int alphaStart = alpha.getSequence().length();
		int betaStart = beta.getSequence().length();
		
		NWCell currentCell = matrix.get(alphaStart).get(betaStart);
		while (currentCell.getOrigin() != null) {
			NWCell origin = currentCell.getOrigin();
			// Was Remove
			if (origin.getAlphaCoordinate() == currentCell.getAlphaCoordinate()) {
				alignedAlpha.prependSequence("-");
				alignedBeta.prependSequence(currentCell.getBeta());
			}
			// Was Delete
			else if (origin.getBetaCoordinate() == currentCell.getBetaCoordinate()) {
				alignedAlpha.prependSequence(currentCell.getAlpha());
				alignedBeta.prependSequence("-");
			}
			// Was Match
			else {
				alignedAlpha.prependSequence(currentCell.getAlpha());
				alignedBeta.prependSequence(currentCell.getBeta());
			}
			currentCell = origin;
		}
		
		// Because the rows/columns are flipped, the order here needs to be reversed.
		alignedSequences.add(alignedBeta);
		alignedSequences.add(alignedAlpha);
		return alignedSequences;
	}
	
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
