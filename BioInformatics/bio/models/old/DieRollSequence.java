package bio.models.old;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class DieRollSequence {
	List<String> rolls;
	List<String> roller;
	double[][] weights;
	
	public DieRollSequence() {
		this.rolls = new ArrayList<String>();
		this.roller = new ArrayList<String>();
	}
	
	public void addRoll(String roll, String roller) {
		this.rolls.add(roll);
		this.roller.add(roller);
	}
	
	public String getRoll(int index) {
		return rolls.get(index);
	}
	
	public void setRoll(int index, String roll) {
		this.rolls.set(index, roll);
	}
	
	public String getRoller(int index) {
		return roller.get(index);
	}
	
	public void setRoller(int index, String roller) {
		this.roller.set(index, roller);
	}
	
	public int size() {
		return rolls.size();
	}
	
	public int countRoller(String roller) {
		int count = 0;
		for (int i = 0; i < this.roller.size(); i++) {
			if (this.roller.get(i).equals(roller)) {
				count += 1;
			}
		}
		return count;
	}
	
	public List<Integer> getIndexes(String state) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < rolls.size(); i++) {
			if (rolls.get(i).equals(state)) {
				list.add(i);
			}
		}
		return list;
	}
	
	public DieRollSequence copy() {
		DieRollSequence sequence = new DieRollSequence();
		for (int i = 0; i < this.size(); i++) {
			sequence.addRoll(this.getRoll(i), this.getRoller(i));
		}
		return sequence;
	}
		
	public String toString() {
		return this.rolls.toString() + "\n" + this.roller.toString();
	}
	
}
