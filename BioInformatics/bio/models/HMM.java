package bio.models;

import java.util.Hashtable;
import java.util.List;

public class HMM {
	List<String> emissions;
	List<String> states;
	LookupTable<Double> emissionProb;
	LookupTable<Double> stateProb;
	Hashtable<String, Double> beginStateProb;
	Hashtable<String, Double> endStateProb;
	
	List<String> emissionSequence;
	List<String> stateSequence;
	
	/**
	 * Create a new Hidden Markov Model.  
	 * @param emissions			strings representing possible emissions
	 * @param states			strings representing possible states
	 * @param emissionProb		emission probabilities for each state
	 * @param stateProb			transition probabilities for each state
	 * @param beginStateProb	begin state transition probabilities
	 * @param endStateProb		end state transition probabilities
	 */
	public HMM(List<String> emissions, List<String> states, 
			   LookupTable<Double> emissionProb, LookupTable<Double> stateProb,
			   Hashtable<String, Double> beginStateProb,
			   Hashtable<String, Double> endStateProb) {
		this.emissions = emissions;
		this.states = states;
		this.emissionProb = emissionProb;
		this.stateProb = stateProb;
		this.beginStateProb = beginStateProb;
		this.endStateProb = endStateProb;
	}
	
	/**
	 * Create a new Hidden Markov Model.  
	 * @param emissionProb		emission probabilities for each state
	 * @param stateProb			transition probabilities for each state
	 * @param beginStateProb	begin state transition probabilities
	 * @param endStateProb		end state transition probabilities
	 */
	public HMM(LookupTable<Double> emissionProb, LookupTable<Double> stateProb,
			   Hashtable<String, Double> beginStateProb,
			   Hashtable<String, Double> endStateProb) {
		this(emissionProb.getAlphaKeys(), stateProb.getBetaKeys(),
			 emissionProb, stateProb,
			 beginStateProb, endStateProb);
	}
	
	/**
	 * Create a new Hidden Markov Model.  
	 * @param emissions			strings representing possible emissions
	 * @param states			strings representing possible states
	 * @param emissionProb		emission probabilities for each state
	 * @param stateProb			transition probabilities for each state
	 * @param beginStateProb	begin state transition probabilities
	 * @param endStateProb		end state transition probabilities
	 */
	public HMM(List<String> emissions, List<String> states,
			   Double[][] emissionProb, Double[][] stateProb,
			   Hashtable<String, Double> beginStateProb,
			   Hashtable<String, Double> endStateProb) {
		this(emissions, states, 
			 new LookupTable<Double>(states, emissions, emissionProb),
			 new LookupTable<Double>(states, states, stateProb),
			 beginStateProb, endStateProb);
	}

	/**
	 * Returns a list of the possible emissions for the HMM.
	 * @return	a list of emissions
	 */
	public List<String> getEmissions() {
		return this.emissions;
	}
	
	/**
	 * Sets the list of emissions for the HMM.
	 * @param emissions		a list of emissions
	 */
	public void setEmissions(List<String> emissions) {
		this.emissions = emissions;
	}
	
	/**
	 * Returns a list of the possible states for the HMM.
	 * @return	a list of states
	 */
	public List<String> getStates() {
		return this.states;
	}
	
	/**
	 * Sets the list of states for the HMM.
	 * @param emissions		a list of states
	 */
	public void setStates(List<String> states) {
		this.states = states;
	}
	
	/**
	 * Returns the emission probability for an emission given a particular state.
	 * @require				getStates().contains(state) && getEmissions().contains(emission)
	 * @param state			a state key in this HMM
	 * @param emission		an emission key in this HMM
	 * @return				the probability of an emission from a state
	 */
	
	public Double getEmissionProbability(String state, String emission) {
		return this.emissionProb.get(state, emission);
	}
	
	/**
	 * Returns the emission probabilities for a state.
	 * @require			getStates().contains(state)
	 * @param state		a state key in this HMM
	 * @return			the emission probabilities for a state
	 */
	public Hashtable<String, Double> getEmissionProbabilities(String state) {
		return this.emissionProb.get(state);
	}
		
	/**
	 * Returns the state transition probability from state alpha to state beta.
	 * NOTE: Order matters
	 * @require			getStates().contains(alpha) && getStates().contains(beta)
	 * @param alpha		a state key in this HMM
	 * @param beta		a state key in this HMM
	 * @return			the transition probability from state alpha to state beta
	 */
	public Double getStateTransitionProbability(String alpha, String beta) {
		return this.stateProb.get(alpha, beta);
	}
	
	/**
	 * Returns all of the state transition probabilities for a given alpha state.
	 * @require			getStates().contains(alpha)
	 * @param alpha		a state key in this HMM
	 * @return			the transition probabilities for a state.
	 */
	public Hashtable<String, Double> getStateTransitionProbabilities(String alpha) {
		return this.stateProb.get(alpha);
	}
	
	/**
	 * Returns an ordered list of hidden states for the emission sequence modeled by
	 * this HMM.
	 * @require		for state in getStateSequence():
	 * 					getStates().contains(state)
	 * @return		a list of states
	 */
	public List<String> getStateSequence() {
		return this.stateSequence;
	}
	
	/**
	 * Sets the hidden states which correspond to the emission sequence modeled by
	 * this HMM.
	 * @require 				getStateSequence().size() == getEmissionSequence().size()
	 * @require					for state in getStateSequence():
	 * 								getStates().contains(state)
	 * @param stateSequence		a list of states
	 */
	public void setStateSequence(List<String> stateSequence) {
		this.stateSequence = stateSequence;
	}
	
	/**
	 * Returns an ordered list of emissions modeled by this HMM.
	 * @require		for emission in getEmissionSequence():
	 * 					getEmissions().contains(emission)
	 * @return		a list of emissions
	 */
	public List<String> getEmissionSequence() {
		return this.emissionSequence;
	}
	
	/**
	 * Sets the emissions modeled by this HMM.
	 * @require 				getEmissionSequence().size() == getStateSequence().size() 
	 * @require					for emission in getEmissionSequence():
	 * 								getEmissions().contains(emission)
	 * @param emissionSequence	a list of emissions
	 */
	public void setEmissionSequence(List<String> emissionSequence) {
		this.emissionSequence = emissionSequence;
	}

	/**
	 * Provides a list of transition probabilities from the begin state to another state
	 * in the HMM
	 * @require 	for key in getBeginState().keys():
	 * 					getStates().contains(key)
	 * @return		a list of states and the transition probability to that state
	 */
	public Hashtable<String, Double> getBeginState() {
		return beginStateProb;
	}
	
	/**
	 *  Sets the transition probabilities from the begin state to another state for this HMM.
	 * @require 				for key in beginStateProb.keys():
	 * 								getStates().contains(key)	
	 * @param beginStateProb	a list of states and the transition probability to that state
	 */
	public void setBeginState(Hashtable<String, Double> beginStateProb) {
		this.beginStateProb = beginStateProb;
	}
	
	/**
	 * Provides a list of transition probabilities from a state to the end state
	 * for this HMM.
	 * @require 	for key in getEndState().keys():
	 * 					getStates().contains(key)
	 * @return		a list of states and transition probabilities from those states
	 */
	public Hashtable<String, Double> getEndState() {
		return endStateProb;
	}
	
	/**
	 * Sets the transition probabilities from a state to the end state for this HMM.
	 * @require					for key in endStateProb.keys():
	 * 								getStates().contains(key)
	 * @param endStateProb		a list of states and transition probabilities from that state
	 */
	public void setEndState(Hashtable<String, Double> endStateProb) {
		this.endStateProb = endStateProb;
	}
	
	/**
	 * Returns the proportion of index that are not coded to the same state.
	 * @param decodedSequence	a sequence of states to compare with this HMM
	 * @return
	 */
	public Double compareDecodedSequence(List<String> decodedSequence) {
		assert(this.stateSequence.size() == decodedSequence.size());
		Double results = 0.0;
		Double misses = 0.0;
		for (int i = 0; i < this.stateSequence.size(); i++) {
			if (!(this.stateSequence.get(i).equals(decodedSequence.get(i)))) {
				misses = misses + 1;
			}
		}
		results = misses / this.stateSequence.size();
		return results;
	}
	
	/**
	 * A String representation of the Hidden Markov Model.
	 */
	public String toString() {
		return this.emissionSequence + "\n" + this.stateSequence;
	}
}
