package bio.models;

import java.util.List;

public class HMMAnalyzer {
		
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
