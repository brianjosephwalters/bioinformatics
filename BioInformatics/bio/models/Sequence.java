package bio.models;

import org.apache.commons.lang3.StringUtils;

/**
 * A Sequence of either nucleic or amino acids.
 * @author Brian J. Walters
 *
 */
public class Sequence {
	// Constants
	public static final int DNA = 1;
	public static final int RNA = 2;
	public static final int PROTEIN = 3;
	
	String description;		// The description of a sequence provided by a FASTA file
	StringBuilder sequence;	// A sequence of acid codes
	int type;				// The type of the sequence: DNA, RNA, or Protein.
	
	/**
	 * Creates a new Sequence object.
	 */
	public Sequence() {
		this.description = "";
		this.sequence = new StringBuilder();
	}
	
	/**
	 * Creates a new Sequence object.
	 * @param description	A description of a sequence provided by a FASTA file
	 */
	public Sequence(String description) {
		this.description = description;
		this.sequence = new StringBuilder();
	}
	
	/**
	 * The FASTA description of this Sequence.
	 * @return	a description of a sequence
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * A series of FASTA format acid codes
	 * @return	a series of acid codes
	 */
	public String getSequence() {
		return this.sequence.toString();
	}
	
	/**
	 * The type of this Sequence.
	 * @return	DNA || RNA || PROTEIN
	 */
	public int getType() {
		return this.type;
	}
	
	/**
	 * The type of the Sequence in a readable format.
	 * @return	"DNA" || "RNA" || "Protein"
	 */
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
	
	/**
	 * The number of acid codes in this Sequence.
	 * @return	number of acid codes in the sequence
	 */
	public int getCount() {
		return this.sequence.length();
	}
	
	/**
	 * The count of a particular acid code in this Sequence.
	 * @param s		a valid acid code
	 * @return		the number of occurrences of an acid code in the sequence
	 */
	public int getCountValue(String s) {
		return StringUtils.countMatches(this.sequence.toString(), s);
	}
	
	/**
	 * Sets the description of this Sequence.
	 * @param description	a FASTA formatted description of the sequence
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Sets the type of this Sequence
	 * @param type	DNA || RNA || PROTEIN
	 */
	public void setType(int type) {
		if (type == DNA || type == RNA || type == PROTEIN) {
			this.type = type;
		}
	}
	
	/**
	 * Appends acid codes to the series of acid codes in this Sequence.
	 * @param seq	a string of acid codes
	 */
	public void appendSequence(String seq) {
		this.sequence.append(seq);
	}
	
	/**
	 * Prepends acid codes to the series of acid codes in this Sequence.
	 * @param seq	a string of acid codes
	 */
	
	public void prependSequence(String seq) {
		this.sequence.insert(0, seq);
	}
	
	/**
	 * A text representation of this Sequence.
	 */
	public String toString() {
		return "\n" + this.description + ":\n" + this.sequence.toString();
	}
}
