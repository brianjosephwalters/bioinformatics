package bio.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class HMMFactory {
	public HMM createCasinoHMM() {
		ArrayList<String> emissions = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6"));
		ArrayList<String> states = new ArrayList<String>(Arrays.asList("F", "L"));
		Double[][] emissionProb = new Double[][] { {1/6.0, 1/6.0, 1/6.0, 1/6.0, 1/6.0, 1/6.0},
		           								   {1/10.0, 1/10.0, 1/10.0, 1/10.0, 1/10.0, 1/2.0} };
		Double[][] transitionProb = new Double[][] { {.95, .05}, {.10, .90} };
		
		Hashtable<String, Double> beginStates = new Hashtable<String, Double>();
		beginStates.put("F", .95);
		beginStates.put("L", .05);
		
		Hashtable<String, Double> endStates = new Hashtable<String, Double>();
		endStates.put("F", .95);
		endStates.put("L", .05);
		
		return new HMM(emissions, states, emissionProb, transitionProb, beginStates, endStates);
	}
	
	public HMM createCasinoHMM(Double[][] transitionProb) {
		ArrayList<String> emissions = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6"));
		ArrayList<String> states = new ArrayList<String>(Arrays.asList("F", "L"));
		Double[][] emissionProb = new Double[][] { {1/6.0, 1/6.0, 1/6.0, 1/6.0, 1/6.0, 1/6.0},
		           								   {1/10.0, 1/10.0, 1/10.0, 1/10.0, 1/10.0, 1/2.0} };
		
		Hashtable<String, Double> beginStates = new Hashtable<String, Double>();
		beginStates.put("F", .95);
		beginStates.put("L", .05);
		
		Hashtable<String, Double> endStates = new Hashtable<String, Double>();
		endStates.put("F", .95);
		endStates.put("L", .05);
		
		return new HMM(emissions, states, emissionProb, transitionProb, beginStates, endStates);
	}
		
	public void addCasinoTestSequence(HMM hmm) {
		String emissions = 
				"315116246" + "446644245" + "311321631" + 
				"164152133" + "625144543" + "631656626" + "566666" +
				"651166453" + "132651245" + "636664631" +
				"636663162" + "326455236" + "266666625" + "151631" +
				"222555441" + "666566563" + "564324364" + 
				"131513465" + "146353411" + "126414626" + "253356" +
				"366163666" + "466232534" + "413661661" +
				"163252562" + "462255265" + "252266435" + "353336" +
				"233121625" + "364414432" + "335163243" + 
				"633665562" + "466662632" + "666612355" + "245242";
		
		String states = 
				"FFFFFFFFF" + "FFFFFFFFF" + "FFFFFFFFF" +
			    "FFFFFFFFF" + "FFFFFFFFF" + "LLLLLLLLL" + "LLLLLL" +
				"LLLLLLFFF" + "FFFFFFFFF" + "LLLLLLLLL" +
			    "LLLLLLLFF" + "FLLLLLLLL" + "LLLLLLFFF" + "FFFFFF" +
				"FFFFFFFFL" + "LLLLLLLLL" + "LLLFFFFFF" +
			    "FFFFFFFFF" + "FFFFFFFFF" + "FFFFFFFFF" + "FFFFLL" +
				"LLLLLLLLF" + "FFFFFFFFF" + "FFFFFFFFF" +
				"FFFFFFFFF" + "FFFFFFFFF" + "FFFFFFFFF" + "FFFFFF" +
				"FFFFFFFFF" + "FFFFFFFFF" + "FFFFFFFFF" +
				"LLLLLLLLL" + "LLLLLLLLL" + "LLLLFFFFF" + "FFFFFF";
		hmm.setEmissionSequence(Arrays.asList(emissions.split("(?!^)")));
		hmm.setStateSequence(Arrays.asList(states.split("(?!^)")));
	}

	public HMM createWeatherHMM() {
		ArrayList<String> emissions = new ArrayList<String>(Arrays.asList("See Umbrella", "See No Umbrella"));
		ArrayList<String> states = new ArrayList<String>(Arrays.asList("Rain", "No Rain"));
		Double[][] emissionProb = new Double[][] { {0.9, 0.1}, {0.2, 0.8} };
		Double[][] transitionProb = new Double[][] { {0.7, 0.3}, {0.3, 0.7} };
		
		Hashtable<String, Double> beginStates = new Hashtable<String, Double>();
		beginStates.put("Rain", .5);
		beginStates.put("No Rain", .5);
		
		Hashtable<String, Double> endStates = new Hashtable<String, Double>();
		endStates.put("Rain", 1.0);
		endStates.put("No Rain", 1.0);
		
		return new HMM(emissions, states, emissionProb, transitionProb, beginStates, endStates);
	}
	
	public void addWeatherTestSequence(HMM hmm) {
		hmm.setEmissionSequence(Arrays.asList("See Umbrella",
											  "See Umbrella",
											  "See No Umbrella",
											  "See Umbrella",
											  "See Umbrella"));
		hmm.setStateSequence(Arrays.asList("Rain",
								           "Rain",
								           "No Rain",
								           "Rain",
								           "Rain"));
	}
	
	public void runTest() {
		Double[] transitionProbabilities = {.01, .02, .03, .04, .05, .06, .07, .08, .09, .10,
				                            .11, .12, .13, .14, .15, .16, .17, .18, .19, .20};
		for (int i = 0; i < transitionProbabilities.length; i++) {
			System.out.println("Transition Probability: " + transitionProbabilities[i]);
			List<Double> viterbiRuns = new ArrayList<Double>();
			List<Double> posteriorRuns = new ArrayList<Double>();
			for (int j = 0; j < 10; j++) {
				Double[][] transProb = { {1-transitionProbabilities[i], transitionProbabilities[i]},
						                 {.10, .9} };
				HMM hmm = createCasinoHMM(transProb);
				HMMSequenceCreator.createSequenceFromHMM(hmm, 1000);
				List<String> viterbi = HMMAnalyzer.viterbi(hmm);
				List<String> posterior = HMMAnalyzer.posteriorScaled(hmm);
				Double viterbiPerformance = HMMAnalyzer
						.compareStateSequences(hmm.getStateSequence(), viterbi);
				Double posteriorPerformance = HMMAnalyzer
						.compareStateSequences(hmm.getStateSequence(), posterior);
				viterbiRuns.add(viterbiPerformance);
				posteriorRuns.add(posteriorPerformance);
			}
			
			Double viterbiAverage = 0.0;
			for (Double value : viterbiRuns) {
				viterbiAverage += value;
			}
			viterbiAverage = viterbiAverage / viterbiRuns.size();
			
			Double posteriorAverage = 0.0;
			for (Double value : posteriorRuns) {
				posteriorAverage += value;
			}
			posteriorAverage = posteriorAverage / posteriorRuns.size();
			
			System.out.println("Vertbi %error: " + viterbiRuns);
			System.out.println("  Average: " + viterbiAverage);
			System.out.println("Posterior %error: " + posteriorRuns);
			System.out.println("  Average: " + posteriorAverage);
			System.out.println(); 
		}
	}
}
