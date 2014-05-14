package bio.models;

import java.util.Hashtable;
import java.util.List;

/**
 * A Lookup table of values given row and column labels.
 * @author Brian J. Walters
 *
 * @param <Element>		elements held in the table
 */
public class LookupTable<Element> {
	List<String> alpha;
	List<String> beta;
	Hashtable<String, Hashtable<String, Element>> table;
	
	/**
	 * Creates a new LookupTable.
	 * @param alpha			a list of alpha labels
	 * @param beta			a list of beta labels
	 * @param defaultValue	a with which to initialize the table
	 */
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
	 * Creates a new LookupTable.
	 * @param alpha		a list of alpha labels
	 * @param beta		a list of beta labels
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
	
	/**
	 * The alpha keys for the Lookup Table.
	 * @return		a list of alpha keys
	 */
	public List<String> getAlphaKeys() {
		return this.alpha;
	}
	
	/**
	 * The beta key for the Lookup Table.
	 * @return		a list of beta keys
	 */
	public List<String> getBetaKeys() {
		return this.beta;
	}
	
	/**
	 * Get a value from the LookupTable at the alpha and beta coordinate.
	 * @require				alpha.contains(alphaKey)
	 * @require				beta.contains(betaKey)
	 * @param alphaKey		an alpha key
	 * @param betaKey		a beta key
	 * @return				a value at (alpha, beta)
	 */
	public Element get(String alphaKey, String betaKey) {
		return table.get(alphaKey).get(betaKey);
	}
	
	/**
	 * Returns a Hashtable of all of the beta keys and their values for 
	 * the given alpha key
	 * @require				alpha.contains(alphaKey)
	 * @param alphaKey		an alpha key
	 * @return				a hashtable of beta keys and their values
	 */
	public Hashtable<String, Element> get(String alphaKey) {
		return table.get(alphaKey);
	}
	
	/**
	 * Sets a series of beta keys and their values for an alpha key
	 * @require				alpha.contains(alphaKey)
	 * @require				for each betaKey in table.keySet()
	 *                         beta.contains(betaKey)
	 * @param alphaKey		an alpha key
	 * @param table			a hashtable of beta keys and thier values
	 */
	public void set(String alphaKey, Hashtable<String, Element> table) {
		this.table.put(alphaKey, table);
	}
	
	/**
	 * Sets a value at the alpha and beta coordinate.
	 * @require				alpha.contains(alphaKey)
	 * @require				beta.contains(betaKey)
	 * @param alphaKey		an alpha key
	 * @param betaKey		a beta key
	 * @param e				a value to store in the table
	 */
	public void set(String alphaKey, String betaKey, Element e) {
		if (table.containsKey(alphaKey) && table.get(alphaKey).containsKey(betaKey))
			table.get(alphaKey).put(betaKey, e);
	}
	
	/**
	 * Provides a String representation of this LookupTable
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%8s ", " "));
		for (String betaKey : this.beta) {
			sb.append(String.format("%8s ", betaKey));
		}
		sb.append("\n");
		for (String alphaKey : this.alpha) {
			sb.append(String.format("%8s ", alphaKey));
			for (String betaKey: this.beta) {
				sb.append(String.format("%8.2f ", table.get(alphaKey).get(betaKey)));
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
	
}
