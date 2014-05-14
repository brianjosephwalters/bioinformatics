package bio.models;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * An indexed sequence of values associated with states.
 * @author Brian J. Walters
 *
 * @param <Element>		the values to be held
 */
public class StateSequence<Element> {
	private List<String> states;
	private Map<String, List<Element>> data;
	
	/**
	 * Creates a new StateSequence for a list of states.
	 * @param states	a list of states
	 */
	public StateSequence(List<String> states) {
		this.states = states;
		data = new Hashtable<String, List<Element>>();
		for (String state: states) {
			data.put(state, new ArrayList<Element>());
		}
	}
	
	/**
	 * The states in this StateSequence.
	 * @return	the states
	 */
	public List<String> getStates() {
		return states;
	}
	
	/**
	 * Returns a list of all of the values stored for a state.
	 * @require 		data.keySet().contains(state)
	 * @param state		a state
	 * @return			a list of values
	 */
	public List<Element> getValuesForState(String state) {
		return data.get(state);
	}
	/**
	 * The value for the state at the index.
	 * @require			data.keySet().contains(state)
	 * @param state		a state
	 * @param index		an index
	 * @return			a value
	 */
	public Element get(String state, Integer index) {
		return data.get(state).get(index);
	}	
	
	/**
	 * The last value in the sequence for the state.
	 * @require			data.keySet().contains(state)
	 * @param state		a state
	 * @return			a value
	 */
	public Element getLast(String state) {
		int last = data.get(state).size() - 1;
		return data.get(state).get(last);
	}
	/**
	 * Add a value to the sequence at the given index for a given state.
	 * @require 		data.keySet().contains(state)
	 * @param state		a state
	 * @param index		an index in the sequence
	 * @param value		a value
	 */
	public void add(String state, Integer index, Element value) {
		if (data.keySet().contains(state))
			data.get(state).add(index, value);
	}
	
	/**
	 * Set a value to the sequence at the given index for a given state.
	 * @require 		data.keySet().contains(state)
	 * @param state		a state
	 * @param index		an index in the sequence
	 * @param value		a value
	 */
	public void set(String state, Integer index, Element value) {
		if (data.keySet().contains(state))
			data.get(state).set(index, value);
	}

	/**
	 * A String representation of the StateSequence
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String state : states) {
			sb.append(data.get(state));
		}
		return sb.toString();
	}
}
