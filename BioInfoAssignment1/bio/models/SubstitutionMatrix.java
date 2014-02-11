package bio.models;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class SubstitutionMatrix<Element> {
	int gapWeight;
	int lengthWeight;
	ArrayList<String> labels;
	Hashtable<String, Hashtable<String, Element>> matrix;
	
	public SubstitutionMatrix(int gapWeight, 
							  int lengthWeight, 
							  ArrayList<String> labels,
							  Element defaultValue) {
		this.gapWeight = gapWeight;
		this.lengthWeight = lengthWeight;
		this.labels = labels;
		
		this.matrix = new Hashtable<String, Hashtable<String, Element>>();

		for (String alphaLabel : labels) {
			Hashtable<String, Element> beta = new Hashtable<String, Element>();
			this.matrix.put(alphaLabel, beta);
			for (String betaLabel : labels) {
				beta.put(betaLabel, defaultValue);
			}
		}
	}
	
	public int getGapWeight() {
		return this.gapWeight;
	}
	
	public int getLengthWeight() {
		return this.lengthWeight;
	}
	
	public ArrayList<String> getLabels() {
		return this.labels;
	}
	
	public Element get(String alpha, String beta) {
		return matrix.get(alpha).get(beta);
	}
	
	// sets a currently existing pair
	public void set(String alpha, String beta, Element element) {
		if (matrix.containsKey(alpha) && matrix.get(alpha).containsKey(beta))
			matrix.get(alpha).put(beta, element);
	}
	
	public void setGapWeight(int gapWeight) {
		this.gapWeight = gapWeight;
	}
	
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
