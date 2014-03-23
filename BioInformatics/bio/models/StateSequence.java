package bio.models;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class StateSequence<Element> {
	private List<String> states;
	private Map<String, List<Element>> data;
	
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
	 * The value for the state at the index.
	 * @require			data.keySet().contains(state)
	 * @param state		a state
	 * @param index		an index
	 * @return
	 */
	public Element get(String state, Integer index) {
		return data.get(state).get(index);
	}	
	
	public Element getLast(String state) {
		int last = data.get(state).size();
		return data.get(state).get(last);
	}
	/**
	 * 
	 * @require 		data.keySet().contains(state)
	 * @param state
	 * @param index
	 * @param value
	 */
	public void add(String state, Integer index, Element value) {
		if (data.keySet().contains(state))
			data.get(state).add(index, value);
	}
	

}
