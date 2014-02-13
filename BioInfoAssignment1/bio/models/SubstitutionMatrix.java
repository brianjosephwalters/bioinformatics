package bio.models;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * A substitution matrix.
 * @author Brian Joseph Walters
 * @param <Element>		the value held in the matrix
 */
public class SubstitutionMatrix<Element> {
	int gapWeight;
	int lengthWeight;
	ArrayList<String> labels;
	Hashtable<String, Hashtable<String, Element>> matrix;
	
	/**
	 * Creates a new SubstitionMatrix.
	 * @param gapWeight			the gap weight for the matrix
	 * @param lengthWeight		the length weight for the matrix
	 * @param labels			the acid codes 
	 * @param defaultValue		initial value of each cell in the matrix
	 */
	public SubstitutionMatrix(int gapWeight, 
							  int lengthWeight, 
							  ArrayList<String> labels,
							  Element defaultValue) {
		this.gapWeight = gapWeight;
		this.lengthWeight = lengthWeight;
		this.labels = labels;
		
		this.matrix = new Hashtable<String, Hashtable<String, Element>>();
		
		// initializes a two 2D hashtable using the labels as keys
		for (String alphaLabel : labels) {
			Hashtable<String, Element> beta = new Hashtable<String, Element>();
			this.matrix.put(alphaLabel, beta);
			for (String betaLabel : labels) {
				beta.put(betaLabel, defaultValue);
			}
		}
	}
	
	/**
	 * The weight to use for gaps when using this substitution matrix
	 * @return		the gap weight
	 */
	public int getGapWeight() {
		return this.gapWeight;
	}
	
	
	/**
	 * The length weight for this substitution matrix.
	 * @return		the length weight 
	 */
	public int getLengthWeight() {
		return this.lengthWeight;
	}
	
	/**
	 * The labels used by this substitution matrix.
	 * @return		a list of column and row labels
	 */
	public ArrayList<String> getLabels() {
		return this.labels;
	}
	
	/**
	 * The value in a cell in the matrix.
	 * @require			getLabels().contains(alpha) == true
	 * @require     	getLabels().contains(beta) == true
	 * @param alpha		the key for one coordinate
	 * @param beta		the key for the other coordinate
	 * @return			the value of the cell in the matrix.
	 */
	public Element get(String alpha, String beta) {
		return matrix.get(alpha).get(beta);
	}
	
	/**
	 * Sets the value of a cell in the matrix.
	 * @require			getLabels().contains(alpha) == true
	 * @require     	getLabels().contains(beta) == true	 
	 * @param alpha		the key for one coordinate
	 * @param beta		the key for the other coordinate
	 * @param element	the value to store in the cell
	 */
	public void set(String alpha, String beta, Element element) {
		if (matrix.containsKey(alpha) && matrix.get(alpha).containsKey(beta))
			matrix.get(alpha).put(beta, element);
	}
	
	/**
	 * Sets the gap weight for this substitution matrix.
	 * @param gapWeight		the gap weight value
	 */
	public void setGapWeight(int gapWeight) {
		this.gapWeight = gapWeight;
	}
	
	/**
	 * A String representation of the values in this SubstitutionMatrix.
	 */
	public String toString() {
		StringBuilder results = new StringBuilder();
		for (String alphaLabel : labels) {
			for (String betaLabel : labels) {
				results.append(matrix.get(alphaLabel).get(betaLabel) + ", ");
			}
			results.append("\n");
		}
		return results.toString();
	} 
}
