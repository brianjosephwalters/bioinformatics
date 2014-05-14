package bio.models;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 * Generates sequences of emissions and hidden states for a Hidden Markov Model.
 * @author Brian J. Walters
 */
public class HMMSequenceGenerator {
	
	/**
	 * Creates a sequence of emissions and states producing those emissions
	 * using the Hidden Markov Model provided.  The results are added to the
	 * Hidden Markov Model.
	 * @param hmm			a Hidden Markov Model
	 * @param entries		the number of emissions to produce
	 */
	public static void createSequenceFromHMM(HMM hmm, int entries) {
		List<String> emissionSequence = new ArrayList<String>();	// The resulting emissions
		List<String> stateSequence = new ArrayList<String>();		// The resulting states
		Random r = new Random();									// A random number generator

		// Get Beginning State:
		// 	 Begin State is just two states and their probability for being in
		//   the begin state.
		//   a) Order the states with their probabilities
		List<String> keys = new ArrayList<String>();
		List<Double> values = new ArrayList<Double>();
		for (String key: hmm.getBeginState().keySet()) {
			keys.add(key);
			values.add(hmm.getBeginState().get(key));
		}
		//   b) Determine which probability wins
		int stateIndex = rouletteWheel(values, r.nextDouble());
		//   c) Record the state for the winning probability.
		String state = keys.get(stateIndex);

		// Create Entries
		for (int index = 0; index < entries; index++) {
			// Create Entry using previous state
			// a) Get transition probabilities from last state.
			Hashtable<String, Double> transitions = hmm.getStateTransitionProbabilities(state);
			// b) Order the next states with their values
			List<String> keys2 = new ArrayList<String>();
			List<Double> values2 = new ArrayList<Double>();
			for (String key: transitions.keySet()) {
				keys2.add(key);
				values2.add(transitions.get(key));
			}
			// b) Determine which next state wins 
			stateIndex = rouletteWheel(values2, r.nextDouble());
			// c) Store that next state as the current state
			state = keys2.get(stateIndex);
			
			// d) Get the emission probabilities for the current state
			Hashtable<String, Double> emissions = hmm.getEmissionProbabilities(state);
			// e) Order the emission probabilities for the current state
			List<String> keys3 = new ArrayList<String>();
			List<Double> values3 = new ArrayList<Double>();
			for (String key: emissions.keySet()) {
				keys3.add(key);
				values3.add(emissions.get(key));
			}
			// f) Determine which emission wins for this state
			int emissionIndex = rouletteWheel(values3, r.nextDouble());
			// g) Store the emission
			String emission = keys3.get(emissionIndex);
			
			// h) Record the emission and current state.
			emissionSequence.add(emission);
			stateSequence.add(state);
		}
		
		// Set results in HMM
		hmm.setEmissionSequence(emissionSequence);
		hmm.setStateSequence(stateSequence);
	}
	
	
	
	/**
	 * Returns the index of the weight which was selected by a Roulette
	 * Wheel algorithm.  
	 * @require			an ordered list of weights, the sum of whose values = 1.0
	 * @param weights	a list of weights
	 * @return			the index of the weight which was selected.
	 */
	public static int rouletteWheel(List<Double> weights, Double randomDouble) {
		for (int i = 0; i < weights.size(); i++) {
			randomDouble = randomDouble - weights.get(i);
			if (randomDouble <= 0) {
				return i;
			}
		}
		return weights.size() - 1;
	}

}
