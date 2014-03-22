package bio.models;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class LookupTable<Element> {
	List<String> alpha;
	List<String> beta;
	Hashtable<String, Hashtable<String, Element>> table;
	
	
	public LookupTable (List<String> alpha, List<String> beta, Element defaultValue) {
		this.alpha = alpha;
		this.beta = beta;
		this.table = new Hashtable<String, Hashtable<String, Element>>();
		
		for (String alphaKey : alpha) {
			this.table.put(alphaKey, new Hashtable<String, Element>());
			for (String betaKey : beta) {
				this.table.get(alphaKey).put(betaKey, defaultValue);
			}
		}
	}
	
	/**
	 * 
	 * @param alpha		
	 * @param beta
	 * @param values	2d array of elements ordered according to values[alpha][beta]
	 */
	public LookupTable (List<String> alpha, List<String> beta, Element[][] values) {
		this.alpha = alpha;
		this.beta = beta;
		this.table = new Hashtable<String, Hashtable<String, Element>>();

		for (int i = 0; i < alpha.size(); i++) {
			this.table.put(alpha.get(i), new Hashtable<String, Element>());
			for (int j = 0; j < beta.size(); j++) {
				this.table.get(alpha.get(i)).put(beta.get(j), values[i][j]);
			}
		}
	}
	
	public List<String> getAlphaKeys() {
		return this.alpha;
	}
	
	public List<String> getBetaKeys() {
		return this.beta;
	}
		
	public Element get(String alphaKey, String betaKey) {
		return table.get(alphaKey).get(betaKey);
	}
	
	public Hashtable<String, Element> get(String alphaKey) {
		return table.get(alphaKey);
	}
	
	public void set(String alphaKey, String betaKey, Element e) {
		if (table.containsKey(alphaKey) && table.get(alphaKey).containsKey(betaKey))
			table.get(alphaKey).put(betaKey, e);
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		
		return result.toString();
	}
	
	
	
}
