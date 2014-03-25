package bio.models;

import java.util.ArrayList;
import java.util.List;

public class PosteriorDecoding {
	private HMM hmm;
	private StateSequence<Double> forward;
	private StateSequence<Double> backward;
	private StateSequence<Double> posterior;
	private List<String> decodedSequence;
	
	public PosteriorDecoding(HMM hmm) {
		this.hmm = hmm;

		this.forward = createScaledForward();
		this.backward = createScaledBackward();
		this.posterior = createScaledPosterior();
		this.decodedSequence = createSequence();
	}
	
	public StateSequence<Double> getForward() {
		return this.forward;
	}
	
	public StateSequence<Double> getBackward() {
		return this.backward;
	}
	
	public StateSequence<Double> getPosterior() {
		return this.posterior;
	}
	
	public List<String> getSequence() {
		return this.decodedSequence;
	}
	
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
	
	private StateSequence<Double> createScaledPosterior() {
		StateSequence<Double> posterior = new StateSequence<Double>(hmm.getStates());
		List<Double> posteriorScales = new ArrayList<Double>();
		
		for (int i = 0; i <= hmm.getEmissionSequence().size(); i++) {
			
			for (String state : hmm.getStates()) {
				Double total = 0.0;
				if (i == 0) {
					Double f = hmm.getBeginState().get(state);
					Double b = backward.get(state, i);
					total = f * b;
				} 
				else if (i == hmm.getEmissionSequence().size()) {
					Double f = forward.get(state, i - 1);
					Double b = hmm.getEndState().get(state);
					total = f * b;
				}
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
