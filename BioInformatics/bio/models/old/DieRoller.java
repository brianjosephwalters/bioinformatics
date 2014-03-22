package bio.models.old;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DieRoller {
	public static final int FAIR = 0;
	public static final int LOADED = 1;
	
	public static DieRollSequence testSequence() {
		DieRollSequence sequence = new DieRollSequence();
		sequence.addRoll("5", "F");
		sequence.addRoll("2", "F");
		sequence.addRoll("6", "L");
		sequence.addRoll("3", "F");
		sequence.addRoll("5", "F");
		sequence.addRoll("5", "F");
		sequence.addRoll("3", "F");
		sequence.addRoll("3", "F");
		sequence.addRoll("2", "F");
		return sequence;
	}
	
	public static DieRollSequence testSequence2() {
		DieRollSequence sequence = new DieRollSequence();
		sequence.addRoll("3", "F");
		sequence.addRoll("1", "F");
		sequence.addRoll("5", "F");
		sequence.addRoll("1", "F");
		sequence.addRoll("1", "F");
		sequence.addRoll("6", "F");
		sequence.addRoll("2", "F");
		sequence.addRoll("4", "F");
		sequence.addRoll("6", "F");
		
		sequence.addRoll("4", "F");
		sequence.addRoll("4", "F");
		sequence.addRoll("6", "F");
		sequence.addRoll("6", "F");
		sequence.addRoll("4", "F");
		sequence.addRoll("4", "F");
		sequence.addRoll("2", "F");
		sequence.addRoll("4", "F");
		sequence.addRoll("5", "F");
		
		sequence.addRoll("3", "F");
		sequence.addRoll("1", "F");
		sequence.addRoll("1", "F");
		sequence.addRoll("3", "F");
		sequence.addRoll("2", "F");
		sequence.addRoll("1", "F");
		sequence.addRoll("6", "F");
		sequence.addRoll("3", "F");
		sequence.addRoll("1", "F");
		
		sequence.addRoll("1", "F");
		sequence.addRoll("6", "F");
		sequence.addRoll("4", "F");
		sequence.addRoll("1", "F");
		sequence.addRoll("5", "F");
		sequence.addRoll("2", "F");
		sequence.addRoll("1", "F");
		sequence.addRoll("3", "F");
		sequence.addRoll("3", "F");
		
		sequence.addRoll("6", "F");
		sequence.addRoll("2", "F");
		sequence.addRoll("5", "F");
		sequence.addRoll("1", "F");
		sequence.addRoll("4", "F");
		sequence.addRoll("4", "F");
		sequence.addRoll("5", "F");
		sequence.addRoll("4", "F");
		sequence.addRoll("3", "F");
		
		sequence.addRoll("6", "L");
		sequence.addRoll("3", "L");
		sequence.addRoll("1", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("5", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("2", "L");
		sequence.addRoll("6", "L");
		
		sequence.addRoll("5", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		
		sequence.addRoll("6", "L");
		sequence.addRoll("5", "L");
		sequence.addRoll("1", "L");
		sequence.addRoll("1", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("4", "F");
		sequence.addRoll("5", "F");
		sequence.addRoll("3", "F");
		
		sequence.addRoll("1", "F");
		sequence.addRoll("3", "F");
		sequence.addRoll("2", "F");
		sequence.addRoll("6", "F");
		sequence.addRoll("5", "F");
		sequence.addRoll("1", "F");
		sequence.addRoll("2", "F");
		sequence.addRoll("4", "F");
		sequence.addRoll("5", "F");
		
		sequence.addRoll("6", "L");
		sequence.addRoll("3", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("4", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("3", "L");
		sequence.addRoll("1", "L");
		
		sequence.addRoll("6", "L");
		sequence.addRoll("3", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("3", "L");
		sequence.addRoll("1", "L");
		sequence.addRoll("6", "F");
		sequence.addRoll("2", "F");
		
		sequence.addRoll("3", "F");
		sequence.addRoll("2", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("4", "L");
		sequence.addRoll("5", "L");
		sequence.addRoll("5", "L");
		sequence.addRoll("2", "L");
		sequence.addRoll("3", "L");
		sequence.addRoll("6", "L");
		
		sequence.addRoll("6", "L");
		sequence.addRoll("3", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("4", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("3", "L");
		sequence.addRoll("1", "L");

		sequence.addRoll("2", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "L");
		sequence.addRoll("6", "F");
		sequence.addRoll("2", "F");
		sequence.addRoll("5", "F");
		
		sequence.addRoll("1", "F");
		sequence.addRoll("5", "F");
		sequence.addRoll("1", "F");
		sequence.addRoll("6", "F");
		sequence.addRoll("3", "F");
		sequence.addRoll("1", "F");
		
		return sequence;
	}
	
	public static DieRollSequence getUnknownDieRolls(int times) {
		DieRollSequence sequence = new DieRollSequence();
		double probabilityLoaded = .05;
		double[] dieWeights = {probabilityLoaded, 1-probabilityLoaded};
		Random r = new Random();
		for (int i = 0; i < times; i++) {
			double randomDouble = r.nextDouble();
			int die = rouletteWheel(dieWeights, randomDouble);
			if (die == 0) {
				sequence.addRoll(rollLoadedDie() + "", "L");
			} else {
				sequence.addRoll(rollDie() + "", "F");
			}
		}
		return sequence;
	}
	
	public static DieRollSequence getFairDieRolls(int times) {
		DieRollSequence sequence = new DieRollSequence();
		Random r = new Random();
		for (int i = 0; i < times; i++) {
			sequence.addRoll((r.nextInt(5) + 1) + "", "F");
		}
		return sequence;
	}
	
	public static DieRollSequence getLoadedDieRolls(int times) {
		DieRollSequence sequence = new DieRollSequence();
		Random r = new Random();
		for (int i = 0; i < times; i++) {
			sequence.addRoll(rollLoadedDie() + "", "L");
		}
		return sequence;
	}
	
	public static int rollDie() {
		return (new Random()).nextInt(6) + 1;
	}
	
	public static int rollLoadedDie() {
		double[] weights = {1/10.0, 1/10.0, 1/10.0, 1/10.0, 1/10.0, 1/2.0};
		Random randomGenerator = new Random();
		double randomDouble = randomGenerator.nextDouble();
		int side = rouletteWheel(weights, randomDouble) + 1;
		return side;
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
	
	/**
	 * Check that the provided weights are a reasonable set of probabilities
	 * by ensuring their sum is less than 1 and greater than .999
	 * @param weights
	 * @return
	 */
	private static boolean checkWeights(double[] weights) {
		double total = 0.0;
		for (int i = 0; i < weights.length; i++) {
			total = total + weights[i];
		}
		if (total < 1.0 && total > .999) {
			return true;
		}
		return false;
	}
}
