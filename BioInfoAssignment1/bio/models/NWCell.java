package bio.models;

public class NWCell {
	public int score;
	public NWCell origin;
	public String alpha;
	public String beta;
	
	public NWCell(String alpha, String beta) {
		this.alpha = alpha;
		this.beta = beta;
	}
	
	public String getAlpha() {
		return this.alpha;
	}
	
	public String getBeta() {
		return this.beta;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public NWCell getOrigin() {
		return this.origin;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void setOrigin(NWCell origin) {
		this.origin = origin;
	}
	
	public String toString() {
		return String.valueOf(this.score);
	}
}
