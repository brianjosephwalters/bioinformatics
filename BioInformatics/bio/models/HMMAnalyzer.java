package bio.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bio.controllers.CSVGenerator;

public class HMMAnalyzer {
		
	public static void runTests(int count) {
		Double[] transitionProbabilities = {.001, .003, .005, .007, .009,
											.01, .02, .03, .04, .05, .06, .07, .08, .09, .10,
				                            .11, .12, .13, .14, .15, .16, .17, .18, .19, .20};
		CSVGenerator viterbiCSV = new CSVGenerator("results\\Viterbi Decoder Tests.csv");
		CSVGenerator posteriorCSV = new CSVGenerator("results\\Posterior Decoder Tests.csv");
		CSVGenerator combinedCSV = new CSVGenerator("results\\Combined Decoder Tests.csv");
		List<String> labels = new ArrayList<String>(
				Arrays.asList("Trans. Prob.", "Accuracy", 
						      "Sensitivity", "Specificity"));
		viterbiCSV.addRowStrings(labels);
		posteriorCSV.addRowStrings(labels);
		List<String> combinedLabels = new ArrayList<String>(
				Arrays.asList("Trans. Prob", 
							  "V. Accuracy", "V. Sensitivity", "V. Specificity",
				              "P. Accuracy", "P. Sensitivity", "P. Specificity"));
		combinedCSV.addRowStrings(combinedLabels);

		for (int i = 0; i < transitionProbabilities.length; i++) {
			System.out.println("Running " + transitionProbabilities[i] + "...");
			List<Double> viterbiRow = new ArrayList<Double>();
			List<Double> posteriorRow = new ArrayList<Double>();

			List<Double> viterbiAccuracy = new ArrayList<Double>();
			List<Double> viterbiSensitivity = new ArrayList<Double>();
			List<Double> viterbiSpecificity = new ArrayList<Double>();

			
			List<Double> posteriorError = new ArrayList<Double>();
			List<Double> posteriorSensitivity = new ArrayList<Double>();
			List<Double> posteriorSpecificity = new ArrayList<Double>();

			for (int j = 0; j < count; j++) {
				Double[][] transProb = { {1-transitionProbabilities[i], transitionProbabilities[i]},
						                 {.10, .9} };
				HMMFactory hmmFactory = new HMMFactory();
				HMM hmm = hmmFactory.createCasinoHMM(transProb);
				if (transitionProbabilities[i] < .01)
					HMMSequenceGenerator.createSequenceFromHMM(hmm, 10000);
				else 
					HMMSequenceGenerator.createSequenceFromHMM(hmm, 1000);
				ViterbiDecoding viterbi = new ViterbiDecoding(hmm);
				PosteriorDecoding posterior = new PosteriorDecoding(hmm);
				viterbiAccuracy.add(hmm.accuracy(viterbi.getSequence()));
				viterbiSensitivity.add(hmm.sensitivity(viterbi.getSequence(), "F"));
				viterbiSpecificity.add(hmm.specificity(viterbi.getSequence(), "F"));

				posteriorError.add(hmm.accuracy(posterior.getSequence()));
				posteriorSensitivity.add(hmm.sensitivity(posterior.getSequence(), "F"));
				posteriorSpecificity.add(hmm.specificity(posterior.getSequence(), "F"));

			}
			viterbiRow.add(transitionProbabilities[i]);
			viterbiRow.add(averageList(viterbiAccuracy));
			viterbiRow.add(averageList(viterbiSensitivity));
			viterbiRow.add(averageList(viterbiSpecificity));

			viterbiCSV.addRowDoubles(viterbiRow);
			
			posteriorRow.add(transitionProbabilities[i]);
			posteriorRow.add(averageList(posteriorError));
			posteriorRow.add(averageList(posteriorSensitivity));

			posteriorRow.add(averageList(posteriorSpecificity));

			posteriorCSV.addRowDoubles(posteriorRow);
			
			combinedCSV.addRowDoubles(Arrays.asList(
					transitionProbabilities[i], averageList(viterbiAccuracy), 
					averageList(viterbiSensitivity), averageList(viterbiSpecificity),
					averageList(posteriorError),
					averageList(posteriorSensitivity), averageList(posteriorSpecificity)));
		}
		
		viterbiCSV.close();
		posteriorCSV.close();
		combinedCSV.close();
	}
	
	private static Double averageList(List<Double> list) {
		Double total = 0.0;
		for (Double value : list) {
			total += value;
		}
		return total / list.size();
	}
}
