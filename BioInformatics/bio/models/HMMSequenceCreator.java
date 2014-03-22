package bio.models;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class HMMSequenceCreator {
	
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

		// Get Beginning State
		List<String> keys = new ArrayList<String>();
		List<Double> values = new ArrayList<Double>();
		for (String key: hmm.getBeginState().keySet()) {
			keys.add(key);
			values.add(hmm.getBeginState().get(key));
		}
		int stateIndex = rouletteWheel(values, r.nextDouble());
		String state = keys.get(stateIndex);
			
		// Create Entries
		Hashtable<String, Double> transitions = hmm.getStateTransitionProbabilities(state);
		Hashtable<String, Double> emissions = hmm.getEmissionProbabilities(state);
		for (int index = 0; index < entries; index++) {
			List<String> keys2 = new ArrayList<String>();
			List<Double> values2 = new ArrayList<Double>();
			for (String key: transitions.keySet()) {
				keys2.add(key);
				values2.add(transitions.get(key));
			}
			stateIndex = rouletteWheel(values2, r.nextDouble());
			state = keys2.get(stateIndex);
			
			List<String> keys3 = new ArrayList<String>();
			List<Double> values3 = new ArrayList<Double>();
			for (String key: emissions.keySet()) {
				keys3.add(key);
				values3.add(emissions.get(key));
			}
			int emissionIndex = rouletteWheel(values3, r.nextDouble());
			String emission = keys3.get(emissionIndex);
			
			emissionSequence.add(emission);
			stateSequence.add(state);
		}
		// Set results in HMM
		hmm.setEmissionSequence(emissionSequence);
		hmm.setStateSequence(stateSequence);
	}
	
	
	
	/**
	 * Returns the index of the weight which was selected by a Roulette
	 * wheel algorithm.  
	 * @require		an ordered list of weights, the sum of whose values = 1.0
	 * @param weights
	 * @return		the index of the weight which was selected.
	 */
	public static int rouletteWheel(double[] weights, double randomDouble) {
		assert(checkWeights(weights));
		for (int i = 0; i < weights.length; i++) {
			randomDouble = randomDouble - weights[i];
			if (randomDouble <= 0) {
				return i;
			}
		}
		return weights.length - 1;
	}
	
	public static int rouletteWheel(Double[] weights, Double randomDouble) {
		for (int i = 0; i < weights.length; i++) {
			randomDouble = randomDouble - weights[i];
			if (randomDouble <= 0) {
				return i;
			}
		}
		return weights.length - 1;
	}
	
	public static int rouletteWheel(List<Double> weights, Double randomDouble) {
		for (int i = 0; i < weights.size(); i++) {
			randomDouble = randomDouble - weights.get(i);
			if (randomDouble <= 0) {
				return i;
			}
		}
		return weights.size() - 1;
	}
	
	/**
	 * Check that the provided weights are a reasonable set of probabilities
	 * by ensuring their sum is less than or equal to 1 and greater than .999
	 * @param weights
	 * @return
	 */
	private static boolean checkWeights(double[] weights) {
		double total = 0.0;
		for (int i = 0; i < weights.length; i++) {
			total = total + weights[i];
		}
		if (total <= 1.0 && total > .999) {
			return true;
		}
		return false;
	}
}
