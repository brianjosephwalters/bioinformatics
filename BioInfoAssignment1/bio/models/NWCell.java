package bio.models;

public class NWCell {
	private String alpha;
	private int alphaCoordinate;
	private String beta;
	private int betaCoordinate;
	private int score;
	private NWCell origin;
	
	public NWCell(String alpha, String beta) {
		this.alpha = alpha;
		this.alphaCoordinate = 0;
		this.beta = beta;
		this.betaCoordinate = 0;
		this.score = 0;
		this.origin = null;
	}
	
	public String getAlpha() {
		return this.alpha;
	}
	
	public int getAlphaCoordinate() {
		return this.alphaCoordinate;
	}
	
	public String getBeta() {
		return this.beta;
	}
	
	public int getBetaCoordinate() {
		return this.betaCoordinate;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public NWCell getOrigin() {
		return this.origin;
	}
	
	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}
	
	public void setAlphaCoordinate(int alphaCoordinate) {
		this.alphaCoordinate = alphaCoordinate;
	}
	
	public void setBeta(String beta) {
		this.beta = beta;
	}
	
	public void setBetaCoordinate(int betaCoordinate) {
		this.betaCoordinate = betaCoordinate;
	}
	
	public void setAlphaBeta(String alpha, String beta) {
		this.alpha = alpha;
		this.beta = beta;
	}
	
	public void setAlphaBetaCoordinates(int alphaCoordinate, int betaCoordinate) {
		this.alphaCoordinate = alphaCoordinate;
		this.betaCoordinate = betaCoordinate;
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
