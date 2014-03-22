package bio.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class HMMAnalyzer {

	public static List<String> viterbi(HMM hmm) {
		String[] viterbiStateSequence = new String[hmm.getEmissionSequence().size()];
		Hashtable<String, ArrayList<Double>>probPath = new Hashtable<String, ArrayList<Double>>();
		Hashtable<String, ArrayList<String>>path = new Hashtable<String, ArrayList<String>>();
		
		for (String state: hmm.getStates()) {
			probPath.put(state, new ArrayList<Double>());
			path.put(state, new ArrayList<String>());
		}
		
		// Calculate the state probabilities of the first emission from the beginning state.
		for (String state : hmm.getStates()) {
			Double initStateProb = hmm.getBeginState().get(state);
			Double emissionProb = hmm.getEmissionProbability(state, hmm.getEmissionSequence().get(0));
			Double initLogProb = Math.log(initStateProb) + Math.log(emissionProb);
			probPath.get(state).add(0, initLogProb);
			path.get(state).add(0, state);
		}
		
		// Now calculate the path probabilities through the remainder of the sequence.
		for (int i = 1; i < hmm.getEmissionSequence().size(); i++) {
			for (String currentState : hmm.getStates()) {
				Double highestProb = 0.0;
				String highestProbState = "";
				for (String maxingState : hmm.getStates()) {
					String emission = hmm.getEmissionSequence().get(i);
					Double prevProb = probPath.get(maxingState).get(i-1);
					Double transitionProb = hmm.getStateTransitionProbability(maxingState, currentState);
					Double emissionProb = hmm.getEmissionProbability(currentState, emission);
					Double newProb = prevProb + 
							         Math.log(transitionProb) + 
							         Math.log(emissionProb);
					if (highestProb < newProb || highestProb == 0.0) {
						highestProb = newProb;
						highestProbState = maxingState;
					}
				}
				probPath.get(currentState).add(i, highestProb);
				path.get(currentState).add(i, highestProbState);
			}
		}
		
		// Find the most probable state for the last emission of the sequence,
		// given the end state probabilities.
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
		viterbiStateSequence[hmm.getEmissionSequence().size() - 1] = highestLastState;
		
		// Now trace back through the state probabilities to produce the 
		// most probable path.
		for (int i = hmm.getEmissionSequence().size() - 1; i > 0 ; i--) {
			viterbiStateSequence[i-1] = path.get(highestLastState).get(i);
			highestLastState = path.get(highestLastState).get(i);
		}
		
		return Arrays.asList(viterbiStateSequence);
	}
	
	/**
	 * 
	 */
	private static Hashtable<String, ArrayList<Double>> forward(HMM hmm, Integer currentIndex) {
		assert(currentIndex < hmm.getEmissionSequence().size());
		Hashtable<String, ArrayList<Double>> probTotals = new Hashtable<String, ArrayList<Double>>();
		// Setup Results table
		for (String state : hmm.getStates()) {
			probTotals.put(state, new ArrayList<Double>());
		}
		
		// Beginning State
		for (String state : hmm.getStates()) {
			Double initStateProb = hmm.getBeginState().get(state);
			probTotals.get(state).add(0, initStateProb);
		}
		
		// Determine probability up to and including the desired index.
		for (int i = 0; i < currentIndex; i++) {
			for (String state: hmm.getStates()) {
				Double totalProb = 0.0;
				for (String summingState : hmm.getStates()) {
					Double prevProb = probTotals.get(summingState).get(i);
					Double transitionProb = hmm.getStateTransitionProbability(summingState, state);
					totalProb += prevProb * transitionProb;
				}
				Double emissionProb = hmm.getEmissionProbability(state, hmm.getEmissionSequence().get(i));
				probTotals.get(state).add(i + 1, totalProb * emissionProb);
			}
		}
		
		//System.out.println(probTotals);
		return probTotals;
	}
	
	
	private static Hashtable<String, ArrayList<Double>> backward(HMM hmm, Integer currentIndex) {
		assert(currentIndex < hmm.getEmissionSequence().size());
		Hashtable<String, ArrayList<Double>> probTotals = new Hashtable<String, ArrayList<Double>>();
		// Setup Results table
		for (String state : hmm.getStates()) {
			probTotals.put(state, new ArrayList<Double>());
		}
		
		// End State
		for (String state : hmm.getStates()) {
			Double endStateProb = hmm.getEndState().get(state);
			probTotals.get(state).add(0, endStateProb);
		}
		
		// The rest
		for (int i = currentIndex; i > 0; i--) {
			for (String state : hmm.getStates()) {
				Double totalProb = 0.0;
				for (String summingState : hmm.getStates()) {
					String emission = hmm.getEmissionSequence().get(i);
					Double transitionProb = hmm.getStateTransitionProbability(state, summingState);
					Double emissionProb = hmm.getEmissionProbability(summingState, emission);
					Double nextProb = probTotals.get(summingState).get(0);
					totalProb += transitionProb * emissionProb * nextProb;
				}
				probTotals.get(state).add(0, totalProb);
			}
		}
		//System.out.println(probTotals);
		return probTotals;
	}
	
	public static void posterior(HMM hmm, int index) {
		Hashtable<String, ArrayList<Double>> forward = forward(hmm, index);
		Hashtable<String, ArrayList<Double>> backward = backward(hmm, index);
		
		Double forwardResult = 0.0;
		for (String state : hmm.getStates()) {
			forwardResult +=  forward.get(state).get(index) + Math.log(hmm.getEndState().get(state));
		}
		//System.out.println("Forward Result: " + forwardResult);
		
		Double backwardResult = 0.0;
		for (String state : hmm.getStates()) {
			backwardResult += backward.get(state).get(0) + Math.log(hmm.getBeginState().get(state));
		}
		//System.out.println("Backward Result: " + backwardResult);
		
		for (String state : hmm.getStates()) {
			Double forwardProbability = 0.0;
			Double backwardProbability = 0.0;

			forwardProbability = forward.get(state).get(index);
			backwardProbability = backward.get(state).get(0);
//			System.out.println("Probabilities of " + state + 
//					           " at index " + index + ": " + 
//					           forwardProbability + ", " + backwardProbability);

		}
	}
	
	/**
	 * 
	 */
	private static Hashtable<String, ArrayList<Double>> forwardScaled(HMM hmm, ArrayList<Double> scales) {
		Hashtable<String, ArrayList<Double>> stateProbs = new Hashtable<String, ArrayList<Double>>();
		for (String state : hmm.getStates()) {
			stateProbs.put(state, new ArrayList<Double>());
		}
		ArrayList<Double> stateProbTotals = new ArrayList<Double>();

		for (int i = 0; i < hmm.getEmissionSequence().size(); i++) {
			// 1)Get f.x(i) for each state x
			for (String state : hmm.getStates()) {
				Double summingTotal = 0.0;
				for (String summingState : hmm.getStates()) {
					Double prevTotal = 0.0;
					if (i == 0) {
						prevTotal = hmm.getBeginState().get(state);
					} else {
						prevTotal = stateProbs.get(summingState).get(i - 1);
					}
					Double transitionProb = hmm.getStateTransitionProbability(summingState, state);
					summingTotal += prevTotal * transitionProb;
				}
				Double emissionProb = hmm.getEmissionProbability(state, hmm.getEmissionSequence().get(i));
				stateProbs.get(state).add(i, summingTotal * emissionProb);
			}
			
			// 2)identify scaling coefficient = sum over all states l i.e., f.l(i).
			Double indexTotal = 0.0;
			for (String state: hmm.getStates()) {
				indexTotal += stateProbs.get(state).get(i);
			}
			scales.add(indexTotal);
			
			// 3)Apply scaling coefficent to each state's probability i.e., f.x(i)
			//   and save it as the new probability for that state, x, at i.
			for (String state : hmm.getStates()) {
				Double total = stateProbs.get(state).get(i);
				stateProbs.get(state).set(i, total / indexTotal);
			}
			
			// 4)Save the total probability across states for that index, including the scale.
			//   Should be 1?
			Double indexScaledTotal = 0.0;
			for (String state: hmm.getStates()) {
				indexScaledTotal += stateProbs.get(state).get(i);
			}
			stateProbTotals.add(0, indexScaledTotal);
			
		}	
		
		// Termination: P(x)
		Double total = 0.0;
		for (String state : hmm.getStates()) {
			Double lastStateProb = stateProbs.get(state).get(stateProbs.get(state).size() - 1);
			Double endStateProb = hmm.getEndState().get(state);
			total += lastStateProb * endStateProb;
		}
		//System.out.println ("Forward Termination, P(x): " + total);
		
		return stateProbs;
	}
	
	public static Hashtable<String, ArrayList<Double>> backwardScaled(HMM hmm, ArrayList<Double> scales) {
		Hashtable<String, ArrayList<Double>> stateProbs = new Hashtable<String, ArrayList<Double>>();
		for (String state : hmm.getStates()) {
			stateProbs.put(state, new ArrayList<Double>());
		}
		ArrayList<Double> stateProbTotals = new ArrayList<Double>();
				
		// The Emitting States
		for (int i = hmm.getEmissionSequence().size() - 1; i >= 0; i--) {
			// 1) Get b.x(i) for each state x
			for (String state : hmm.getStates()) {
				Double totalProb = 0.0;
				for (String summingState : hmm.getStates()) {
					String emission = hmm.getEmissionSequence().get(i);
					Double transitionProb = hmm.getStateTransitionProbability(state, summingState);
					Double emissionProb = hmm.getEmissionProbability(summingState, emission);
					Double nextProb = 0.0;
					if (i == hmm.getEmissionSequence().size() - 1) {
						nextProb = hmm.getEndState().get(state);
					} else {
						nextProb = stateProbs.get(summingState).get(0);
					}
					totalProb += transitionProb * emissionProb * nextProb;
				}
				// 2) Apply scaling coefficient to total probability for state
				stateProbs.get(state).add(0, totalProb / scales.get(i));
			}
			
			// 4)Save the total probability across states for that index, including the scale.
			Double stateProbTotal = 0.0;
			for (String state : hmm.getStates()) {
				stateProbTotal += stateProbs.get(state).get(0);
			}
			stateProbTotals.add(0, stateProbTotal);
		}		
		
		// Termination: P(x)
		Double total = 0.0;
		for (String state : hmm.getStates()) {
			Double firstStateProb = stateProbs.get(state).get(0);
			Double firstEmissionProb = hmm.getEmissionProbability(state, hmm.getEmissionSequence().get(0));
			Double beginStateProb = hmm.getBeginState().get(state);
			total += firstStateProb * firstEmissionProb * beginStateProb;
		}
		//System.out.println ("Backward Termination, P(x): " + total);
		
		return stateProbs;
	}
	
	public static List<String> posteriorScaled(HMM hmm) {
		ArrayList<Double> scale = new ArrayList<Double>();
		// Forward Algorithm
		Hashtable<String, ArrayList<Double>> forward = forwardScaled(hmm, scale);
//		System.out.println("Forward probTotals:");
//		for (String state : hmm.getStates()) {
//			System.out.println(state + "(" +forward.get(state).size() + "): " + forward.get(state));
//		}
				
		// Backwards Algorithm
		Hashtable<String, ArrayList<Double>> backward = backwardScaled(hmm, scale);
//		System.out.println("Backward probTotals:");
//		for (String state : hmm.getStates()) {
//			System.out.println(state + "(" +backward.get(state).size() + "): " + backward.get(state));
//		}
		
		
		// Posterior State Probabilities
		Hashtable<String, ArrayList<Double>> totals = new Hashtable<String, ArrayList<Double>>();
		for (String state : hmm.getStates()) {
			totals.put(state, new ArrayList<Double>());
		}
		
		for (int i = 0; i < hmm.getEmissionSequence().size(); i++) {
			for (String state : hmm.getStates()) {
				Double total = forward.get(state).get(i) * backward.get(state).get(i);
				// Still need P(x)
				totals.get(state).add(total);
			}
		}
		
//		System.out.println("Posterior Totals:");
//		for (String state : hmm.getStates()) {
//			System.out.println(state + "(" +totals.get(state).size() + "): " + totals.get(state));
//		}
		
		// Create Sequence
		ArrayList<String> results = new ArrayList<String>();
		for (int i = 0; i < hmm.getEmissionSequence().size(); i++) {
			Double highestProb = 0.0;
			String highestProbState = "";
			for (String state : hmm.getStates()) {
				if (highestProb < totals.get(state).get(i)) {
					highestProb = totals.get(state).get(i);
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
			if (!actualStateSequence.get(i).equals(decodedStateSequence.get(i))) {
				misses = misses + 1;
			}
		}
		results = misses / actualStateSequence.size();
		return results;
	}
	
}
