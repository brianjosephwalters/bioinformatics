package bio.models.old;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class HMMAnalyzerDice {
	List<String> dice;
	List<String> sides;
	Hashtable<String, Double> initialDieProbability;
	Double[][] diceSelectionProbability;
	Double[][] sideObservationProbability;
	LookupTable<Double> dieProb;
	LookupTable<Double> sideProb;
	
	DieRollSequence sequence;
	DieRollSequence viterbiSequence;
	Hashtable<String, ArrayList<Double>> probPath;
	Hashtable<String, ArrayList<String>> path;
	
	public HMMAnalyzerDice() {
		dice = new ArrayList<String>(Arrays.asList("F", "L"));
		sides = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6"));
		
		initialDieProbability = new Hashtable<String, Double>();
		for (String die: dice) {
			initialDieProbability.put(die, 1.0/dice.size());
		}
		diceSelectionProbability = new Double[][]{ {.95, .05}, {.10, .90} };
		sideObservationProbability = new Double[][]{ {1/6.0, 1/6.0, 1/6.0, 1/6.0, 1/6.0, 1/6.0},
													 {1/10.0, 1/10.0, 1/10.0, 1/10.0, 1/10.0, 1/2.0} };
		dieProb = new LookupTable<Double>(dice, dice, diceSelectionProbability);
		sideProb = new LookupTable<Double>(dice, sides, sideObservationProbability);
	}
	
	public DieRollSequence viterbiForDice(DieRollSequence sequence) {
		this.sequence = sequence;
		probPath = new Hashtable<String, ArrayList<Double>>();
		path = new Hashtable<String, ArrayList<String>>();
		for (String die: dice) {
			probPath.put(die, new ArrayList<Double>());
			path.put(die, new ArrayList<String>());
		}
		
		// For first roll, we need the special probability of a starting state
		for (String currDie : dice) {
			Double initDieProb = Math.log(initialDieProbability.get(currDie));
			Double initSideProb = Math.log(sideProb.get(currDie, sequence.getRoll(0)));
			Double initProb = initDieProb + initSideProb;
			probPath.get(currDie).add(0, initProb);
			path.get(currDie).add(0, currDie);
		}
		
		for (int i = 1; i < sequence.size(); i++ ) {
			// Find the most probable transition...
			// for each previous die type...
			for (String currDie: dice) {
				double bestProb = 0.0;
				String bestProbDie = "";
				// against each possible current die type by...
				for (String maxingDie: dice) {
					// calculating this transition's probability
					String side = sequence.getRoll(i);
					Double prevProb = probPath.get(maxingDie).get(i-1);  // does this need to be log?
					Double dieTransitionProb = Math.log(dieProb.get(maxingDie, currDie));
					Double sideEmissionProb = Math.log(sideProb.get(currDie, side));
					Double newProb = prevProb + dieTransitionProb + sideEmissionProb;
					
					// and if the current permutation is better...
					if (bestProb < newProb || bestProb == 0.0) {
						// update the path's highest transition probability ...
						bestProb = newProb;
						// and set the path to the current die type to be 
						// the one resulting from the previous die type.
						bestProbDie = maxingDie;
					}
				}
				probPath.get(currDie).add(i, bestProb);
				path.get(currDie).add(i, bestProbDie);
			}
		}
		
		// Now trace back through creating a new Sequence with our predicted die types.
		viterbiSequence = sequence.copy();
		
		// First, find the most probable die type for the last roll.
		String lastRollType = dice.get(0);
		Double typeAmount = 0.0;
		for (String die: dice) {
			if (probPath.get(die).get(sequence.size()-1) < typeAmount) {
				lastRollType = die;
				typeAmount = probPath.get(die).get(sequence.size()-1);
			}
		}
		// Store it in the tail of the new sequence.
		viterbiSequence.setRoller(sequence.size() - 1, lastRollType);

		// Now, using the next roll's die type, trace back through the most probable path.
		for (int i = sequence.size() - 1; i > 1; i--) {
			//System.out.println(path.get(lastRollType));
			viterbiSequence.setRoller(i, path.get(lastRollType).get(i));
			lastRollType = path.get(lastRollType).get(i);
		}
		
		return viterbiSequence;
	}
	
	public void displayPaths() {
		int count1 = 0;
		int count2 = 0;
		for (String die: dice) {
			for (int i = 0; i < path.get(die).size(); i++) {
				if (path.get(die).get(i).equals(dice.get(1))){
					if (die.equals(dice.get(0)))
						count1 += 1;
					else
						count2 += 1;
				}
			}		
		}
		
		System.out.println("Fair: ");
		System.out.println(path.get(dice.get(0)));
		System.out.println("  Number Loaded: " + count1);
		System.out.println("Loaded: ");
		System.out.println(path.get(dice.get(1)));
		System.out.println("  Number Loaded: " + count2);
	}
	
	public void displayStateAtIndex(int start, int end) {
		String col1Format = "%8s";
		String col2SFormat = " | %-5s";
		String col2DFormat = " | %-5d";
		String col2FFormat = " | %-5.2f";
		
		for (int index = start; index < end + 1; index++) {
			System.out.format(col1Format, "index:");
			System.out.format(col2DFormat, index);
			System.out.print("\n");
			System.out.format(col1Format, "roll:");
			System.out.format(col2SFormat, sequence.getRoll(index));
			System.out.print("\n");
			System.out.format(col1Format, "die:");
			System.out.format(col2SFormat, sequence.getRoller(index));
			System.out.print("\n");
			for (String row : dice) {
				System.out.format(col1Format, row);
				System.out.format(col2FFormat, probPath.get(row).get(index) );
				System.out.format(col2SFormat, path.get(row).get(index));
				System.out.print("\n");
			}
			System.out.print("\n");
		}
	}
}
