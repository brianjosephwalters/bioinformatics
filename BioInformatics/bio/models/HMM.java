package bio.models;

import java.util.Hashtable;
import java.util.List;

/**
 * A Hidden Markov Model (HMM).
 * @author Brian J. Walters
 *
 */
public class HMM {
	private List<String> emissions;
	private List<String> states;
	private LookupTable<Double> emissionProb;
	private LookupTable<Double> stateProb;
	private Hashtable<String, Double> beginStateProb;
	private Hashtable<String, Double> endStateProb;
	
	private List<String> emissionSequence;
	private List<String> stateSequence;
	
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
	 * Sets the emission probabilities for this HMM.
	 * @require			for each state in table.getAlphaKeys()
	 *                      this.states.contains(state)
	 *                  for each emission in table.getBetaKeys()
	 *                      this.emissions.contains(emission)
	 * @param table		a table of emission probabilities for a state
	 */
	public void setEmissionProbabilities(LookupTable<Double> table) {
		this.emissionProb = table;
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
	 * Sets the state transition probabilities for this HMM.
	 * @require			for each state in table.getAlphaKeys()
	 *                      this.states.contains(state)
	 *                  for each state in table.getBetaKeys()
	 *                      this.states.contains(state)
	 * @param table
	 */
	public void setStateTransitionProbabilities(LookupTable<Double> table) {
		this.stateProb = table;
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
	 * Returns the proportion of indexes that are not coded to the same state.
	 * @param decodedSequence	a sequence of states to compare with this HMM
	 * @return
	 */
	public Double accuracy(List<String> decodedSequence) {
		assert(this.stateSequence.size() == decodedSequence.size());
		Double results = 0.0;
		Double hits = 0.0;
		for (int i = 0; i < this.stateSequence.size(); i++) {
			if (this.stateSequence.get(i).equals(decodedSequence.get(i))) {
				hits = hits + 1;
			}
		}
		results = hits / this.stateSequence.size();
		return results;
	}
	
	/**
	 * Counts the number of state changes in a provided sequence and compares
	 * that to the number of state changes in this sequence.
	 * @param decodedSequence
	 * @return
	 */
	public Double compareNumTransitions(List<String> decodedSequence) {
		assert(this.stateSequence.size() == decodedSequence.size());
		Double results = 0.0;
		Double numActualTransitions = 0.0;
		for (int i = 1; i < this.stateSequence.size(); i++) {
			if (!(this.stateSequence.get(i).equals(this.stateSequence.get(i-1)))) {
				numActualTransitions = numActualTransitions + 1;
			}
		}
		Double numDecodedTransitions = 0.0;

		for (int i = 1; i < decodedSequence.size(); i++) {
			if (!(decodedSequence.get(i).equals(decodedSequence.get(i-1)))) {
				numDecodedTransitions = numDecodedTransitions + 1;
			}
		}
		results = numDecodedTransitions / numActualTransitions;
		return results;

	}
	
	/**
	 * Calculates the number of true positives for a provided sequence
	 * compared to the hidden states of this HMM.
	 * @require 				states.contains(state)
	 * @require					decodedSequence.size() == this.stateSequence.size()
	 * @param decodedSequence	a sequence of hidden states
	 * @param state				a state as the main condition
	 * @return					number of true positives
	 */
	public Integer truePositives(List<String> decodedSequence, String state) {
		Integer count = 0;
		for (int i = 0; i < this.stateSequence.size(); i++) {
			if (this.stateSequence.get(i).equals(state) &&
				decodedSequence.get(i).equals(state))
				count++;
		}
		return count;
	}
	
	/**
	 * Calculates the number of false positives for a provided sequence
	 * compared to the hidden states of this HMM.
	 * @require 				states.contains(state)
	 * @require					decodedSequence.size() == this.stateSequence.size()
	 * @param decodedSequence	a sequence of hidden states
	 * @param state				a state as the main condition
	 * @return					number of false positives
	 */
	public Integer falsePositives(List<String> decodedSequence, String state) {
		Integer count = 0;
		for (int i = 0; i < this.stateSequence.size(); i++) {
			if (!this.stateSequence.get(i).equals(state) &&
			    decodedSequence.get(i).equals(state))
				count++;
		}
		return count;
	}
	
	/**
	 * Calculates the number of true negatives for a provided sequence
	 * compared to the hidden states of this HMM.
	 * @require 				states.contains(state)
	 * @require					decodedSequence.size() == this.stateSequence.size()
	 * @param decodedSequence	a sequence of hidden states
	 * @param state				a state as the main condition
	 * @return					number of true negatives
	 */
	public Integer trueNegatives(List<String> decodedSequence, String state) {
		Integer count = 0;
		for (int i = 0; i < this.stateSequence.size(); i++){
			if (!this.stateSequence.get(i).equals(state) &&
				!decodedSequence.get(i).equals(state))
				count++;
		}
		return count;
	}
	
	/**
	 * Calculates the number of false negatives for a provided sequence
	 * compared to the hidden states of this HMM.
	 * @require 				states.contains(state)
	 * @require					decodedSequence.size() == this.stateSequence.size()
	 * @param decodedSequence	a sequence of hidden states
	 * @param state				a state as the main condition
	 * @return					number of false negatives
	 */
	public Integer falseNegatives(List<String> decodedSequence, String state) {
		Integer count = 0;
		for (int i = 0; i < this.stateSequence.size(); i++) {
			if (this.stateSequence.get(i).equals(state) &&
			    !decodedSequence.get(i).equals(state))
				count++;
		}
		return count;
	}
	
	/**
	 * Calculates the sensitivity of a provided sequence to the hidden states of this HMM.
	 * @require 				states.contains(state)
	 * @require					decodedSequence.size() == this.stateSequence.size()
	 * @param decodedSequence	a sequence of hidden states
	 * @param state				a state as the main condition
	 * @return					the sensitivity of this sequence
	 */
	public Double sensitivity(List<String> decodedSequence, String state) {
		Integer TP = truePositives(decodedSequence, state);
		Integer FN = falseNegatives(decodedSequence, state);
		return TP / Double.valueOf(TP + FN);
	}
	
	/**
	 * Calculates the specificity of a provided sequence to the hidden states of this HMM.
	 * @require 				states.contains(state)
	 * @require					decodedSequence.size() == this.stateSequence.size()
	 * @param decodedSequence	a sequence of hidden states
	 * @param state				a state as the main condition
	 * @return					the sensitivity of this sequence
	 */
	public Double specificity(List<String> decodedSequence, String state) {
		Integer TN = trueNegatives(decodedSequence, state);
		Integer FP = falsePositives(decodedSequence, state);
		return TN / Double.valueOf(TN + FP);
	}
	
	/**
	 * A String representation of the Hidden Markov Model.
	 */
	public String toString() {
		return this.emissionSequence + "\n" + this.stateSequence;
	}
}
