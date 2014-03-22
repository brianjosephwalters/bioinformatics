package bio.views;

import bio.models.HMM;
import bio.models.HMMAnalyzer;
import bio.models.HMMFactory;

/**
 * A menu for working with Hidden Markov Models.
 * 
 * @author Brian J. Walters
 */
public class HMMMenu extends AbstractView {

	/**
	 * {@inheritDoc}
	 */
	protected void displayMenu() {
		System.out.println("-------------Hidden Markov Models-------------------");
		System.out.println("Viterbi Algorithm (sample data)................... 1");
		System.out.println("Posterior Algorithm (sample data)..................2");
		System.out.println("Posterior Algorithm (weather sample)...............3");
		System.out.println("Back.............................................. 0");
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void performChoice(int choice) {
		if (choice == 1) {
			HMMFactory hmmFactory = new HMMFactory();
			HMM hmm = hmmFactory.createCasinoHMM();
			hmmFactory.addCasinoTestSequence(hmm);
			System.out.println(hmm.getEmissions());
			System.out.println(hmm.getStates());
			System.out.println(hmm.getEmissionSequence());
			System.out.println(hmm.getStateSequence());
			System.out.println(HMMAnalyzer.viterbi(hmm));
		} else if (choice == 2) {
			HMMFactory hmmFactory = new HMMFactory();
			HMM hmm = hmmFactory.createCasinoHMM();
			hmmFactory.addCasinoTestSequence(hmm);
			HMMAnalyzer.posteriorScaled(hmm);
		} else if (choice == 3) {
			HMMFactory hmmFactory = new HMMFactory();
			HMM hmm = hmmFactory.createWeatherHMM();
			hmmFactory.addWeatherTestSequence(hmm);
			HMMAnalyzer.posteriorScaled(hmm);
		}
	}
}
