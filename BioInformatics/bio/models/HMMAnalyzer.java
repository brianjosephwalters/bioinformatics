package bio.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class HMMAnalyzer {
	
	public static List<String> viterbi(HMM hmm) {
		Hashtable<String, ArrayList<Double>>probPath = new Hashtable<String, ArrayList<Double>>();
		Hashtable<String, ArrayList<String>>path = new Hashtable<String, ArrayList<String>>();
		
		for (String state: hmm.getStates()) {
			probPath.put(state, new ArrayList<Double>());
			path.put(state, new ArrayList<String>());
		}
		
		//1) Calculate the start probabilities of the first emission from the beginning state.
		for (String state : hmm.getStates()) {
			//1.a) Get the "previous probability" from the begin state.
			Double initStateProb = hmm.getBeginState().get(state);
			//1.b) Get the emission probability for the current state.
			Double emissionProb = hmm.getEmissionProbability(state, hmm.getEmissionSequence().get(0));
			//1.c) Calculate the probability.
			Double initLogProb = Math.log(initStateProb) + Math.log(emissionProb);
			//1.d) Record the highest probability and state for the first index.
			probPath.get(state).add(0, initLogProb);
			path.get(state).add(0, state);
		}
		
		//2) Now calculate the path probabilities through the remainder of the sequence.
		for (int i = 1; i < hmm.getEmissionSequence().size(); i++) {
			for (String currentState : hmm.getStates()) {
				Double highestProb = 0.0;
				String highestProbState = "";
				for (String maxingState : hmm.getStates()) {
					//2.a) Get the previous probability for the maxing state.
					Double prevProb = probPath.get(maxingState).get(i-1);
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
				probPath.get(currentState).add(i, highestProb);
				path.get(currentState).add(i, highestProbState);
			}
		}
		
		//3) Find the most probable state for the last emission of the sequence,
		//   given the end state probabilities.
		Double highestLast = 0.0;
		String highestLastState = "";
		for (String state : hmm.getStates()) {
			Double last = probPath.get(state).get(probPath.size() - 1) + 
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
			viterbiStateSequence[i-1] = path.get(highestLastState).get(i);
			highestLastState = path.get(highestLastState).get(i);
		}
		
		return Arrays.asList(viterbiStateSequence);
	}
	
	private static Hashtable<String, ArrayList<Double>> forwardScaled(HMM hmm, ArrayList<Double> scales) {
		Hashtable<String, ArrayList<Double>> stateProbs = new Hashtable<String, ArrayList<Double>>();
		for (String state : hmm.getStates()) {
			stateProbs.put(state, new ArrayList<Double>());
		}

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
						Double prevTotal = stateProbs.get(summingState).get(i - 1);
						//1.b) Get the transition probability to the current state from the summing state.
						Double transitionProb = hmm.getStateTransitionProbability(summingState, state);
						//1.c) Get the emission probability for the current state.
						Double emissionProb = hmm.getEmissionProbability(state, hmm.getEmissionSequence().get(i));
						//1.d) Aggregate the total for the current state.
						summingTotal += prevTotal * transitionProb * emissionProb;
					}
				}				
				
				//1.e) Compute the total probability for the current state.
				stateProbs.get(state).add(i, summingTotal);
			}
			
			//2) Identify scaling coefficient = the sum over all states.
			Double indexTotal = 0.0;
			for (String state: hmm.getStates()) {
				indexTotal += stateProbs.get(state).get(i);
			}
			scales.add(indexTotal);
			
			//3) Apply scaling coefficent to each state's probability
			//   and save it as the new probability for that state, x, at i.
			for (String state : hmm.getStates()) {
				Double total = stateProbs.get(state).get(i);
				stateProbs.get(state).set(i, total / indexTotal);
			}			
		}	
		
		return stateProbs;
	}
	
	public static Hashtable<String, ArrayList<Double>> backwardScaled(HMM hmm, ArrayList<Double> scales) {
		Hashtable<String, ArrayList<Double>> stateProbs = new Hashtable<String, ArrayList<Double>>();
		for (String state : hmm.getStates()) {
			stateProbs.put(state, new ArrayList<Double>());
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
						//1.c) Aggregate the total for the current state.
						totalProb += nextProb * transitionProb;

					} 
					// otherwise
					else {
						//1.a) Get the next probability from the next state.
						Double nextProb = stateProbs.get(summingState).get(0);
						//1.b) Get the transition probability from the current state to the summing state.
						Double transitionProb = hmm.getStateTransitionProbability(state, summingState);
						//1.c) Get the emission probability for the next state from the summing state.
						String emission = hmm.getEmissionSequence().get(i+1);
						Double emissionProb = hmm.getEmissionProbability(summingState, emission);
						//1.d) Aggregate the total for the current state.
						totalProb += transitionProb * emissionProb * nextProb;

					}
				}
				//2) Apply scaling coefficient to total probability for state
				stateProbs.get(state).add(0, totalProb / scales.get(i));
			}
		}		
		return stateProbs;
	}
	
	public static List<String> posteriorScaled(HMM hmm) {
		ArrayList<Double> scale = new ArrayList<Double>();
		// ##Forward Algorithm
		Hashtable<String, ArrayList<Double>> forward = forwardScaled(hmm, scale);
		System.out.println("Forward probTotals:");
		for (String state : hmm.getStates()) {
			System.out.println(state + "(" +forward.get(state).size() + "): " + forward.get(state));
		}
		
		// Forward Termination: P(x)
		Double forwardTotal = 0.0;
		for (String state : hmm.getStates()) {
			Double lastStateProb = forward.get(state).get(forward.get(state).size() - 1);
			Double endStateProb = hmm.getEndState().get(state);
			forwardTotal += lastStateProb * endStateProb;
		}
		//System.out.println ("Forward Termination, P(x): " + forwardTotal);
		
		//System.out.println("Scales:" + scale);
		
		// ##Backwards Algorithm
		Hashtable<String, ArrayList<Double>> backward = backwardScaled(hmm, scale);
		System.out.println("Backward probTotals:");
		for (String state : hmm.getStates()) {
			System.out.println(state + "(" +backward.get(state).size() + "): " + backward.get(state));
		}
		
		// Backward Termination: P(x)
		Double backwardTotal = 0.0;
		for (String state : hmm.getStates()) {
			Double firstStateProb = backward.get(state).get(0);
			Double firstEmissionProb = hmm.getEmissionProbability(state, hmm.getEmissionSequence().get(0));
			Double beginStateProb = hmm.getBeginState().get(state);
			backwardTotal += firstStateProb * firstEmissionProb * beginStateProb;
		}
		//System.out.println ("Backward Termination, P(x): " + backwardTotal);
		
		// ##Scale P(x): ??
		Double logScaleSum = 0.0;
		for (int i = 0; i < scale.size(); i++) {
			logScaleSum += Math.log10(scale.get(i));
		}
		logScaleSum = -logScaleSum;
		logScaleSum = Math.pow(10, logScaleSum);
		System.out.println(" Log Scale: " + logScaleSum);
		
		// ##Posterior State Probabilities
		Hashtable<String, ArrayList<Double>> posterior = new Hashtable<String, ArrayList<Double>>();
		for (String state : hmm.getStates()) {
			posterior.put(state, new ArrayList<Double>());
		}
		
		for (int i = 0; i < hmm.getEmissionSequence().size(); i++) {
			for (String state : hmm.getStates()) {
				//Double total = forward.get(state).get(i) * backward.get(state).get(i);
				Double total = (forward.get(state).get(i) * backward.get(state).get(i)) / logScaleSum;
				posterior.get(state).add(total);
			}
		}
		
		System.out.println("Posterior Totals:");
		for (String state : hmm.getStates()) {
			System.out.println(state + "(" +posterior.get(state).size() + "): " + posterior.get(state));
		}
		
		// Create Sequence
		ArrayList<String> results = new ArrayList<String>();
		for (int i = 0; i < hmm.getEmissionSequence().size(); i++) {
			Double highestProb = 0.0;
			String highestProbState = "";
			for (String state : hmm.getStates()) {
				if (highestProb < posterior.get(state).get(i)) {
					highestProb = posterior.get(state).get(i);
					highestProbState = state;
				}
			}
			results.add(highestProbState);
		}
		//System.out.println("Posterior: " + results);
		return results;
	}
	
	public static Double compareStateSequences(List<String> actualStateSequence, 
			                            	   List<String> decodedStateSequence) {
		assert(actualStateSequence.size() == decodedStateSequence.size());
		Double results = 0.0;
		Double misses = 0.0;
		for (int i = 0; i < actualStateSequence.size(); i++) {
			if (!(actualStateSequence.get(i).equals(decodedStateSequence.get(i)))) {
				misses = misses + 1;
			}
		}
		results = misses / actualStateSequence.size();
		return results;
	}
	
	public static Double compareTransitions(List<String> actualStateSequence,
			                                List<String> decodedStateSequence) {
		assert(actualStateSequence.size() == decodedStateSequence.size());
		Double results = 0.0;
		Double numActualTransitions = 0.0;
		for (int i = 1; i < actualStateSequence.size(); i++) {
			if (!(actualStateSequence.get(i).equals(actualStateSequence.get(i-1)))) {
				numActualTransitions = numActualTransitions + 1;
			}
		}
		Double numDecodedTransitions = 0.0;

		for (int i = 1; i < decodedStateSequence.size(); i++) {
			if (!(decodedStateSequence.get(i).equals(decodedStateSequence.get(i-1)))) {
				numDecodedTransitions = numDecodedTransitions + 1;
			}
		}
		results = numDecodedTransitions / numActualTransitions;
		return results;
	}
	
}
