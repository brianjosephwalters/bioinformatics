package bio.models.old;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class HMMWeather extends HMM {
	public HMMWeather() {
		super(new ArrayList<String>(Arrays.asList("See Umbrella", "See No Umbrella")),
			  new ArrayList<String>(Arrays.asList("Rain", "No Rain")),
			  new Double[][] { {0.9, 0.1}, {0.2, 0.8} },
			  new Double[][] { {0.7, 0.3}, {0.3, 0.7} });
		Hashtable<String, Double> beginStates = new Hashtable<String, Double>();
		beginStates.put("Rain", .5);
		beginStates.put("No Rain", .5);
		this.setBeginState(beginStates);
		
		Hashtable<String, Double> endStates = new Hashtable<String, Double>();
		endStates.put("Rain", 1.0);
		endStates.put("No Rain", 1.0);
		this.setEndState(endStates);
	}
	
	public void createTestSequence() {
		this.setEmissionSequence(Arrays.asList("See Umbrella",
											   "See Umbrella",
											   "See No Umbrella",
											   "See Umbrella",
											   "See Umbrella"));
		this.setStateSequence(Arrays.asList("Rain",
				                            "Rain",
				                            "No Rain",
				                            "Rain",
				                            "Rain"));
	}
}
