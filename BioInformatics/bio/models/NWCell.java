package bio.models;

/**
 * A cell in the Needleman-Wunch alignment matrix.
 * @author Brian Joseph Walters
 */
public class NWCell {
	private String alpha;
	private int alphaCoordinate;
	private String beta;
	private int betaCoordinate;
	private int score;
	private NWCell origin;
	
	/**
	 * Creates a new cell for the Needleman-Wunsch alignment matrix.
	 * @param alpha		An acid code from the alpha sequence of the matrix
	 * @param beta		An acid code from the beta sequence of the matrix
	 */
	public NWCell(String alpha, String beta) {
		this.alpha = alpha;
		this.alphaCoordinate = 0;
		this.beta = beta;
		this.betaCoordinate = 0;
		this.score = 0;
		this.origin = null;
	}
	
	/**
	 * Returns the acid code from the alpha sequence of the matrix corresponding
	 * to this cell.
	 * @return		an acid code
	 */
	public String getAlpha() {
		return this.alpha;
	}
	
	/**
	 * Returns alpha sequence index corresponding to this cell.
	 * @return		an index on the alpha sequence
	 */
	public int getAlphaCoordinate() {
		return this.alphaCoordinate;
	}
	
	/**
	 * Returns the acid code from the beta sequence of the matrix corresponding
	 * to this cell.
	 * @return		an acid code
	 */
	public String getBeta() {
		return this.beta;
	}
	
	/**
	 * Returns beta sequence index corresponding to this cell.
	 * @return		an index on the beta sequence
	 */
	public int getBetaCoordinate() {
		return this.betaCoordinate;
	}
	
	/**
	 * The alignment score for the alpha acid and beta acid intersecting
	 * with this cell.
	 * @return		an alignment score
	 */
	public int getScore() {
		return this.score;
	}
	
	/**
	 * The cell on the Needleman-Wunsch matrix which was used to produce
	 * the alignment score for this cell.
	 * @return	another cell on the Needleman-Wunsch matrix
	 */
	public NWCell getOrigin() {
		return this.origin;
	}
	
	/**
	 * Set the acid code from the corresponding alpha sequence of this cell.
	 * @param alpha		an acid code
	 */
	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}
	
	/**
	 * Sets the coordinate on the alpha sequence that intersects this cell.
	 * @param alphaCoordinate	an index on the alpha sequence
	 */
	public void setAlphaCoordinate(int alphaCoordinate) {
		this.alphaCoordinate = alphaCoordinate;
	}
	
	/**
	 * Set the acid code from the corresponding beta sequence of this cell.
	 * @param beta		an acid code
	 */
	public void setBeta(String beta) {
		this.beta = beta;
	}
	
	/**
	 * Sets the coordinate on the beta sequence that intersects this cell.
	 * @param beatCoordinate	an index on the beta sequence
	 */
	public void setBetaCoordinate(int betaCoordinate) {
		this.betaCoordinate = betaCoordinate;
	}
	
	/**
	 * Sets both the alpha and beta acid codes corresponding to this cell.
	 * @param alpha		an acid code from the alpha sequence
	 * @param beta		an acid code from the beta sequence
	 */
	public void setAlphaBeta(String alpha, String beta) {
		this.alpha = alpha;
		this.beta = beta;
	}
	
	/**
	 * Sets the coordinates on the alpha and beta sequences corresponding
	 * to this cell.
	 * @param alphaCoordinate	an index on the alpha sequence
	 * @param betaCoordinate	an index on the beta sequence
	 */
	public void setAlphaBetaCoordinates(int alphaCoordinate, int betaCoordinate) {
		this.alphaCoordinate = alphaCoordinate;
		this.betaCoordinate = betaCoordinate;
	}
	
	/**
	 * Sets the alignment score for the alpha acid and beta acid intersecting
	 * with this cell.	 
	 * @param score		the alignment score
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * Sets the cell on the Needleman-Wunsch matrix which was used to produce
	 * the alignment score for this cell.
	 * @param			another cell on the Needleman-Wunsch matrix
	 */
	public void setOrigin(NWCell origin) {
		this.origin = origin;
	}
	
	/**
	 * A String representing the alignment score of this cell.
	 */
	public String toString() {
		return String.valueOf(this.score);
	}
}
