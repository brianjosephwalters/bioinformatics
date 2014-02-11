package bio.models;

import java.util.ArrayList;

public class NWAligner2 {
	private Sequence alpha;
	private Sequence beta;
	private SubstitutionMatrix<Integer> subMatrix;
	
	private ArrayList<ArrayList<NWCell>> matrix;
	
	public NWAligner2 (Sequence alpha,
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
			matrix.get(a).get(0).setScore(-subMatrix.getGapWeight() * a);
		}
		for (int b = 0; b < beta.getSequence().length() + 1; b++) {
			matrix.get(0).get(b).setScore(-subMatrix.getGapWeight() * b);
		}
	}
	
	private void fillMatrix() {
		for (int a = 1; a < alpha.getSequence().length() + 1; a++) {
			for (int b = 1; b < beta.getSequence().length() + 1; b++) {
				String aChar = String.valueOf(alpha.getSequence().charAt(a - 1));
				String bChar = String.valueOf(beta.getSequence().charAt(b - 1));
				int aPrev = matrix.get(a-1).get(b).getScore();
				int bPrev = matrix.get(a).get(b-1).getScore();
				int diaPrev = matrix.get(a-1).get(b-1).getScore();
				
				int match = diaPrev + subMatrix.get(aChar, bChar);
				int delete = aPrev + -subMatrix.getGapWeight();
				int insert = bPrev + -subMatrix.getGapWeight();
				
				if (match > delete && match > insert) {
					matrix.get(a).get(b).setScore(match);
					matrix.get(a).get(b).setOrigin(matrix.get(a-1).get(b-1));
				} else if (delete > match && delete > insert) {
					matrix.get(a).get(b).setScore(delete);
					matrix.get(a).get(b).setOrigin(matrix.get(a-1).get(b));
				} else {
					matrix.get(a).get(b).setScore(insert);
					matrix.get(a).get(b).setOrigin(matrix.get(a).get(b-1));
				}
			}
		}
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
