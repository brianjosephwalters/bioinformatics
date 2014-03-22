package bio.models.old;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Random;

public class HMMDice extends HMM {
	
	public HMMDice() {
		super(new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6")),
			  new ArrayList<String>(Arrays.asList("F", "L")), 
			  new Double[][] { {1/6.0, 1/6.0, 1/6.0, 1/6.0, 1/6.0, 1/6.0},
					           {1/10.0, 1/10.0, 1/10.0, 1/10.0, 1/10.0, 1/2.0} }, 
			  new Double[][] { {.95, .05}, {.10, .90} },
			  );
		
		Hashtable<String, Double> beginStates = new Hashtable<String, Double>();
		beginStates.put("F", .95);
		beginStates.put("L", .05);
		this.setBeginState(beginStates);
		
		Hashtable<String, Double> endStates = new Hashtable<String, Double>();
		endStates.put("F", .95);
		endStates.put("L", .05);
		this.setEndState(endStates);
	}
	
	public void createTestSequence() {
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
		this.setEmissionSequence(Arrays.asList(emissions.split("(?!^)")));
		this.setStateSequence(Arrays.asList(states.split("(?!^)")));
	}
}
