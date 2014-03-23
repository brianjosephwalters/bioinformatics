package bio.models;

import java.util.Arrays;
import java.util.List;

public class ViterbiDecoding {
	private HMM hmm;
	private StateSequence<Double> stateProb;
	private StateSequence<String> statePath;
	private List<String> decodedSequence;
	
	public ViterbiDecoding(HMM hmm) {
		this.hmm = hmm;
		this.stateProb = new StateSequence<Double>(hmm.getStates());
		this.statePath = new StateSequence<String>(hmm.getStates());
		this.decodedSequence = decode();
	}
	
	public HMM getHMM() {
		return this.hmm;
	}
	
	public StateSequence<Double> getStateProbabilities() {
		return this.stateProb;
	}
	
	public StateSequence<String> getStatePaths() {
		return this.statePath;
	}
	
	public List<String> getSequence() {
		return decodedSequence;
	}
	
	private List<String> decode() {
		//1) Calculate the start probabilities of the first emission from the beginning state.
		for (String state : hmm.getStates()) {
			//1.a) Get the "previous probability" from the begin state.
			Double initStateProb = hmm.getBeginState().get(state);
			//1.b) Get the emission probability for the current state.
			Double emissionProb = hmm.getEmissionProbability(state, hmm.getEmissionSequence().get(0));
			//1.c) Calculate the probability.
			Double initLogProb = Math.log(initStateProb) + Math.log(emissionProb);
			//1.d) Record the highest probability and state for the first index.
			stateProb.add(state, 0, initLogProb);
			statePath.add(state, 0, state);
		}
		
		//2) Now calculate the path probabilities through the remainder of the sequence.
		for (int i = 1; i < hmm.getEmissionSequence().size(); i++) {
			for (String currentState : hmm.getStates()) {
				Double highestProb = 0.0;
				String highestProbState = "";
				for (String maxingState : hmm.getStates()) {
					//2.a) Get the previous probability for the maxing state.
					Double prevProb = stateProb.get(maxingState, i-1);
					//2.b) Get the transition probability from maxing state to the current state
					Double transitionProb = hmm.getStateTransitionProbability(maxingState, currentState);
					//2.c) Get the emission probability from the current state and this index.
					String emission = hmm.getEmissionSequence().get(i);
					Double emissionProb = hmm.getEmissionProbability(currentState, emission);
					//2.d) Calculate the new probability.
					Double newProb = prevProb + 
							         Math.log(transitionProb) + 
							         Math.log(emissionProb);
					
					//2.e) Determine if this is the highest value for the current state.
					if (highestProb < newProb || highestProb == 0.0) {
						highestProb = newProb;
						highestProbState = maxingState;
					}
				}
				//2.f) Record the highest probability and state for this index.
				stateProb.add(currentState, i, highestProb);
				statePath.add(currentState, i, highestProbState);
			}
		}
		
		//3) Find the most probable state for the last emission of the sequence,
		//   given the end state probabilities.
		Double highestLast = 0.0;
		String highestLastState = "";
		for (String state : hmm.getStates()) {
			Double last = stateProb.getLast(state) + 
					      Math.log(hmm.getEndState().get(state));
			if ( highestLast < last || highestLast == 0.0) {
				highestLast = last;
				highestLastState = state;
			}
		}
		
		//4) Now trace back through the state probabilities to produce the 
		//   most probable path.	
		String[] viterbiStateSequence = new String[hmm.getEmissionSequence().size()];
		viterbiStateSequence[hmm.getEmissionSequence().size() - 1] = highestLastState;
		for (int i = hmm.getEmissionSequence().size() - 1; i > 0 ; i--) {
			viterbiStateSequence[i-1] = statePath.get(highestLastState, i);
			highestLastState = statePath.get(highestLastState, i);
		}
		
		return Arrays.asList(viterbiStateSequence);
	}
}
