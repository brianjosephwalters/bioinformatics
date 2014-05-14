package bio.models;

import java.util.List;

/**
 * A decoding of a Hidden Markov Model.
 * @author Brian J. Walters
 */
public interface DecodingInterface {
	/**
	 * Get the Hidden Markov Model associated with this decoding.
	 * @return		a hidden markov model
	 */
	public HMM getHMM();
	
	/**
	 * Get the decoded sequenced produced from the associated
	 * Hidden Markov Model.
	 * @return		a sequence of hidden states
	 */
	public List<String> getSequence();
}
