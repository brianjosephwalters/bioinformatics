package bio.models;

import java.util.ArrayList;
import java.util.Hashtable;
//TODO: first column problem
public class NWAligner {
	private Sequence alpha;
	private Sequence beta;
	private int gapWeight;
	private Hashtable<String, Hashtable<String, Integer>> subMatrix;
	
	private ArrayList<ArrayList<NWCell>> matrix;
	
	public NWAligner (Sequence alpha, 
					  Sequence beta,
					  int gapWeight,
					  Hashtable<String, Hashtable<String, Integer>> subMatrix) {
		this.alpha = alpha;
		this.beta = beta;
		this.gapWeight = gapWeight;
		this.subMatrix = subMatrix;
		
		for (int a = 0; a < alpha.getSequence().length(); a++) {
			this.matrix.add(new ArrayList<NWCell>());
			for (int b = 0; b < beta.getSequence().length(); b++) {
				String alphaValue = String.valueOf(alpha.getSequence().charAt(a));
				String betaValue = String.valueOf(beta.getSequence().charAt(b));
				this.matrix.get(a).add(new NWCell(alphaValue, betaValue));
			}
		}	
	}
	
	private void initializeEdges() {
		for (int a = 0; a < alpha.getSequence().length(); a++) {
			matrix.get(a).get(0).setScore(-gapWeight * a);
		}
		for (int b = 0; b < beta.getSequence().length(); b++) {
			matrix.get(0).get(b).setScore(-gapWeight * b);
		}
	}
	
	private void fillMatrix() {
		for (int a = 1; a < alpha.getSequence().length(); a++) {
			for (int b = 1; b < beta.getSequence().length(); b++) {
				String aChar = String.valueOf(alpha.getSequence().charAt(a));
				String bChar = String.valueOf(beta.getSequence().charAt(b));
				int aPrev = matrix.get(a-1).get(b).getScore();
				int bPrev = matrix.get(a).get(b-1).getScore();
				int diaPrev = matrix.get(a-1).get(b-1).getScore();
				
				int match = diaPrev + subMatrix.get(aChar).get(bChar);
				int delete = aPrev + -gapWeight;
				int insert = bPrev + -gapWeight;
				
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
		for (int a = 0; a < alpha.getSequence().length(); a++) {
			for (int b = 0; b < beta.getSequence().length(); b++) {
				results.append(matrix.get(a).get(b) + ", ");
			}
			results.append("\n");
		}
		return results.toString();
	}
	
}
