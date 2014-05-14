package bio.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A decoding produced from a Hidden Markov Model using the
 * posterior algorithm
 * @author Brian J. Walters
 */
public class PosteriorDecoding implements DecodingInterface {
	private HMM hmm;
	private StateSequence<Double> forward;
	private StateSequence<Double> backward;
	private StateSequence<Double> posterior;
	private List<String> decodedSequence;
	
	/**
	 * Creates a new Posterior Decoding from a Hidden Markov Model.
	 * @param hmm		a hidden markov model
	 */
	public PosteriorDecoding(HMM hmm) {
		this.hmm = hmm;
		// Compute the forward probabilities.
		this.forward = createScaledForward();
		// Compute the backwards probabilities.
		this.backward = createScaledBackward();
		// Compute the posterior probabilities.
		this.posterior = createScaledPosterior();
		// Use the posterior probabilities to construct a decoded sequence.
		this.decodedSequence = createSequence();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public HMM getHMM() {
		return this.hmm;
	}
	
	/**
	 * Returns the forward probabilities for this decoding
	 * @return		the forward probabilities
	 */
	public StateSequence<Double> getForward() {
		return this.forward;
	}
	
	/**
	 * Returns the backwards probabilities for this decoding
	 * @return		the backwards probabilities
	 */
	public StateSequence<Double> getBackward() {
		return this.backward;
	}
	
	/**
	 * Returns the posterior probabilities for this decoding
	 * @return		the posterior probabilities
	 */
	public StateSequence<Double> getPosterior() {
		return this.posterior;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<String> getSequence() {
		return this.decodedSequence;
	}
	
	/**
	 * Create a sequence of forward probabilities from the associated
	 * Hidden Markov Model.
	 * NOTE: Uses scaling to prevent underflow.
	 * @return		a sequence of forward probabilities
	 */
	private StateSequence<Double> createScaledForward() {
		StateSequence<Double> stateProbs = new StateSequence<Double>(hmm.getStates());
		ArrayList<Double> scales = new ArrayList<Double>();

		for (int i = 0; i < hmm.getEmissionSequence().size(); i++) {
			//1) Get the forward probability for each state.
			for (String state : hmm.getStates()) {
				Double summingTotal = 0.0;
				for (String summingState : hmm.getStates()) {
					//If we are at the start...
					if (i == 0) {
						//1.a) get the previous probability from the Begin state...
						Double prevTotal = hmm.getBeginState().get(state);
						//1.b) Get the transition probability to the current state from the summing state.
						Double transitionProb = hmm.getStateTransitionProbability(summingState, state);
						//1.c) Get the emission probability for the current state.
						Double emissionProb = hmm.getEmissionProbability(state, hmm.getEmissionSequence().get(i));
						//1.d) Aggregate the total for the current state.
						summingTotal += prevTotal * transitionProb * emissionProb;
					} 
					//otherwise... 
					else {
						//1.a) get the previous (scaled) probability for the summing state.
						Double prevTotal = stateProbs.get(summingState, i - 1);
						//1.b) Get the transition probability to the current state from the summing state.
						Double transitionProb = hmm.getStateTransitionProbability(summingState, state);
						//1.c) Get the emission probability for the current state.
						Double emissionProb = hmm.getEmissionProbability(state, hmm.getEmissionSequence().get(i));
						//1.d) Aggregate the total for the current state.
						summingTotal += prevTotal * transitionProb * emissionProb;
					}
				}				
				
				//1.e) Compute the total probability for the current state.
				stateProbs.add(state, i, summingTotal);
			}
			
			//2) Identify scaling coefficient = the sum over all states.
			Double indexTotal = 0.0;
			for (String state: hmm.getStates()) {
				indexTotal += stateProbs.get(state, i);
			}
			scales.add(indexTotal);
			
			//3) Apply scaling coefficent to each state's probability
			//   and save it as the new probability for that state, x, at i.
			for (String state : hmm.getStates()) {
				Double total = stateProbs.get(state, i);
				stateProbs.set(state, i, total / indexTotal);
			}			
		}	
		return stateProbs;
	}
	
	/**
	 * Create a sequence of backwards probabilities from the associated
	 * Hidden Markov Model.
	 * NOTE: Uses scaling to prevent underflow.
	 *       This does _not_ use the same scaling factors as the forward algorithm
	 *       (as recommended in "Biological Sequence Analysis" by Durbin, Eddy, et al. 
	 *       (1998) p.78.  Instead we use the technique discussed at:
	 *       http://en.wikipedia.org/wiki/Forward%E2%80%93backward_algorithm 
	 * @return		a sequence of backwards probabilities
	 */
	private StateSequence<Double> createScaledBackward() {
		ArrayList<Double> scales = new ArrayList<Double>();
		StateSequence<Double>  stateProbs = new StateSequence<Double>(hmm.getStates());

		for (String state : hmm.getStates()) {
			for (int i = 0; i < hmm.getEmissionSequence().size(); i++) {
				stateProbs.add(state, i, 0.0);
			}
		}
		
		for (int i = hmm.getEmissionSequence().size() - 1; i >= 0; i--) {
			//1) Get the backward probability for each state.
			for (String state : hmm.getStates()) {
				Double totalProb = 0.0;
				for (String summingState : hmm.getStates()) {
					//If we are at the end...
					if (i == hmm.getEmissionSequence().size() - 1) {
						//1.a) Get the next probability from the end state.
						Double nextProb = hmm.getEndState().get(state);
						//1.b) Get the transition probability from the current state to the summing state.
						Double transitionProb = hmm.getStateTransitionProbability(state, summingState);
						//1.c) Get the emission probability for the next state from the summing state.
						String emission = hmm.getEmissionSequence().get(i);
						Double emissionProb = hmm.getEmissionProbability(summingState, emission);

						//1.c) Aggregate the total for the current state.
						totalProb += nextProb * transitionProb * emissionProb;

					} 
					// otherwise
					else {
						//1.a) Get the next probability from the next state.
						Double nextProb = stateProbs.get(summingState, i+1);
						//1.b) Get the transition probability from the current state to the summing state.
						Double transitionProb = hmm.getStateTransitionProbability(state, summingState);
						//1.c) Get the emission probability for the next state from the summing state.
						String emission = hmm.getEmissionSequence().get(i);
						Double emissionProb = hmm.getEmissionProbability(summingState, emission);
						//1.d) Aggregate the total for the current state.
						totalProb += transitionProb * emissionProb * nextProb;
					}
				}
				stateProbs.set(state, i, totalProb);
			}
			
			//2) Identify scaling coefficient = the sum over all states.
			Double indexTotal = 0.0;
			for (String state: hmm.getStates()) {
				indexTotal += stateProbs.get(state, i);
			}
			scales.add(indexTotal);
			
			//3) Apply scaling coefficent to each state's probability
			//   and save it as the new probability for that state at i.
			for (String state : hmm.getStates()) {
				Double total = stateProbs.get(state, i);
				stateProbs.set(state, i, total / indexTotal);
			}				
		}		
		return stateProbs;
	}
	
	/**
	 * Create a sequence of posterior probabilities from the associated
	 * Hidden Markov Model.
	 * NOTE: Uses the scaling technique found at 
	 *       http://en.wikipedia.org/wiki/Forward%E2%80%93backward_algorithm
	 * @return		a sequence of posterior probabilities
	 */
	private StateSequence<Double> createScaledPosterior() {
		StateSequence<Double> posterior = new StateSequence<Double>(hmm.getStates());
		List<Double> posteriorScales = new ArrayList<Double>();
		// For every emission...
		// NOTE: forward sequence is is one behind the backwards.
		for (int i = 0; i <= hmm.getEmissionSequence().size(); i++) {
			//1) For every hidden state that can produce the emission...
			for (String state : hmm.getStates()) {
				Double total = 0.0;
				// If it is the first emission, include the probabilities for the Begin State
				if (i == 0) {
					Double f = hmm.getBeginState().get(state);
					Double b = backward.get(state, i);
					total = f * b;
				} 
				// If it is the last emissions, include the probabilities for the End State.
				else if (i == hmm.getEmissionSequence().size()) {
					Double f = forward.get(state, i - 1);
					Double b = hmm.getEndState().get(state);
					total = f * b;
				}
				// Otherwise use the current probabilities
				else {
					Double f = forward.get(state, i - 1);
					Double b = backward.get(state, i);
					total = f * b;
				}
				posterior.add(state, i, total);
			}
			//2) Identify scaling coefficient = the sum over all states.
			Double indexTotal = 0.0;
			for (String state: hmm.getStates()) {
				indexTotal += posterior.get(state, i);
			}
			posteriorScales.add(indexTotal);
			
			//3) Apply scaling coefficent to each state's probability
			//   and save it as the new probability for that state, x, at i.
			for (String state : hmm.getStates()) {
				Double total = posterior.get(state, i);
				posterior.set(state, i, total / indexTotal);
			}				
		}
		return posterior;
	}
	
	/**
	 * Predict a sequence of states based on the probability of each state
	 * produced by the posterior algorithm.
	 * @return		a sequence of hidden states
	 */
	private List<String> createSequence() {
		ArrayList<String> results = new ArrayList<String>();
		for (int i = 0; i < hmm.getEmissionSequence().size(); i++) {
			Double highestProb = 0.0;
			String highestProbState = "";
			for (String state : hmm.getStates()) {
				if (highestProb < posterior.get(state, i + 1)) {
					highestProb = posterior.get(state, i + 1);
					highestProbState = state;
				}
			}
			results.add(highestProbState);
		}
		return results;
	}
}
