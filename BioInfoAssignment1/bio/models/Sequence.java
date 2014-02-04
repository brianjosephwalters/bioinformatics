package bio.models;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author Brian J. Walters
 *
 */
public class Sequence {
	public static final int DNA = 1;
	public static final int RNA = 2;
	public static final int PROTEIN = 3;
	
	String description;
	StringBuilder sequence;
	int type;
	
	public Sequence() {
		this.description = "";
		this.sequence = new StringBuilder();
	}
	
	public Sequence(String description) {
		this.description = description;
		this.sequence = new StringBuilder();
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getSequence() {
		return this.sequence.toString();
	}
	
	public int getType() {
		return this.type;
	}
	
	public String getReadableType() {
		String result = "";
		
		if (this.type == 1) {
			result = "DNA";
		} else if (this.type == 2) {
			result = "RNA";
			
		} else if (this.type == 3) {
			result = "Protein";
		}
		
		return result;
	}
	
	public int getCount() {
		return this.sequence.length();
	}
	
	public int getCountValue(String s) {
		return StringUtils.countMatches(this.sequence.toString(), s);
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setType(int type) {
		if (type == DNA || type == RNA || type == PROTEIN) {
			this.type = type;
		}
	}
	
	public void appendSequence(String seq) {
		this.sequence.append(seq);
	}
	
	public String toString() {
		return "\n" + this.description + ":\n" + this.sequence.toString();
	}
}
