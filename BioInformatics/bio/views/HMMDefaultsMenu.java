package bio.views;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import bio.controllers.CSVGenerator;
import bio.controllers.DecodingReporter;
import bio.models.HMM;
import bio.models.HMMAnalyzer;
import bio.models.HMMFactory;
import bio.models.HMMSequenceGenerator;
import bio.models.PosteriorDecoding;
import bio.models.ViterbiDecoding;

/**
 * A menu for working with predefined HMMS.
 * @author Brian J. Walters
 *
 */
public class HMMDefaultsMenu extends AbstractView {

	/**
	 * {@inheritDoc}
	 */
	protected void displayMenu() {
		System.out.println("-------------Hidden Markov Models-------------------");
		System.out.println("-------------------Defaults-------------------------");
		System.out.println("Viterbi Algorithm (sample data)................... 1");
		System.out.println("Viterbi Algorithm (random data)....................2");
		System.out.println("Posterior Algorithm (sample data)..................3");
		System.out.println("Posterior Algorithm (weather sample)...............4");
		System.out.println("Posterior Algorithm (random data)..................5");
		System.out.println("Viterbi & Posterior (sample data)..................6");
		System.out.println("Viterbi & Posterior (random data)..................7");
		System.out.println("Viterbi & Posterior Low Transition (random data)...8");
		System.out.println("****Run Tests......................................9");
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
			ViterbiDecoding viterbi = new ViterbiDecoding(hmm);
			DecodingReporter reporter = null;
			try {
				reporter = new DecodingReporter("data\\viterbi - casino - sample.txt");
			} catch (IOException e) {
				System.out.println("Unable to open file.");
			}
			reporter.displayReport("Casino Sample Data - viterbi", viterbi);
			reporter.close();
		} else if (choice == 2) {
			HMMFactory hmmFactory = new HMMFactory();
			HMM hmm = hmmFactory.createCasinoHMM();
			HMMSequenceGenerator.createSequenceFromHMM(hmm, 1000);
			ViterbiDecoding viterbi = new ViterbiDecoding(hmm);

			System.out.println(hmm.getEmissionSequence());
			System.out.println(hmm.getStateSequence());
			System.out.println(viterbi.getSequence());
	    } else if (choice == 3) {
			HMMFactory hmmFactory = new HMMFactory();
			HMM hmm = hmmFactory.createCasinoHMM();
			hmmFactory.addCasinoTestSequence(hmm);
			PosteriorDecoding decoding = new PosteriorDecoding(hmm);

			System.out.println(hmm.getStateSequence());
			System.out.println(decoding.getSequence());
			System.out.println("  Error: " + hmm.accuracy(decoding.getSequence()));
		} else if (choice == 4) {
			HMMFactory hmmFactory = new HMMFactory();
			HMM hmm = hmmFactory.createWeatherHMM();
			hmmFactory.addWeatherTestSequence(hmm);
			PosteriorDecoding decoding = new PosteriorDecoding(hmm);
			
			System.out.println(hmm.getStateSequence());
			System.out.println(decoding.getSequence());
			System.out.println("  Error: " + hmm.accuracy(decoding.getSequence()));
		} else if (choice == 5) {
			HMMFactory hmmFactory = new HMMFactory();
			HMM hmm = hmmFactory.createCasinoHMM();
			HMMSequenceGenerator.createSequenceFromHMM(hmm, 1000);
			PosteriorDecoding decoding = new PosteriorDecoding(hmm);
			
			System.out.println(hmm.getEmissionSequence());
			System.out.println(hmm.getStateSequence());
			System.out.println(decoding.getSequence());
		} else if (choice == 6) {
			HMMFactory hmmFactory = new HMMFactory();
			HMM hmm = hmmFactory.createCasinoHMM();
			hmmFactory.addCasinoTestSequence(hmm);
			ViterbiDecoding viterbi = new ViterbiDecoding(hmm);
			PosteriorDecoding posterior = new PosteriorDecoding(hmm);
			DecodingReporter reporter = null;
			try {
				reporter = new DecodingReporter("data\\decoding - both - random.txt");
			} catch (IOException e) {
				System.out.println("Unable to open file.");
			}
			reporter.displayReport("Viterbi:", viterbi);
			reporter.displayReport("Posterior:", posterior);
			reporter.close();
		} else if (choice == 7) {
			HMMFactory hmmFactory = new HMMFactory();
			HMM hmm = hmmFactory.createCasinoHMM();
			HMMSequenceGenerator.createSequenceFromHMM(hmm, 1000);
			ViterbiDecoding viterbi = new ViterbiDecoding(hmm);
			PosteriorDecoding posterior = new PosteriorDecoding(hmm);
			
			System.out.println("Viterbi:");
			System.out.println(hmm.getStateSequence());
			System.out.println(viterbi.getSequence());
			System.out.println("  Error: " + hmm.accuracy(viterbi.getSequence()));
			System.out.println("Posterior:");
			System.out.println(hmm.getStateSequence());
			System.out.println(posterior.getSequence());
			System.out.println("  Error: " + hmm.accuracy(posterior.getSequence()));
		} else if (choice == 8) {
			HMMFactory hmmFactory = new HMMFactory();
			Double[][] transProb = { {.99, .01}, {.10, .90} };
			HMM hmm = hmmFactory.createCasinoHMM(transProb);
			HMMSequenceGenerator.createSequenceFromHMM(hmm, 10000);
			ViterbiDecoding viterbi = new ViterbiDecoding(hmm);
			PosteriorDecoding posterior = new PosteriorDecoding(hmm);
			System.out.println("Sequence");
			System.out.println(hmm.getStateSequence());
			System.out.println(viterbi.getSequence());
			System.out.println(posterior.getSequence());
			System.out.println("  L:" + StringUtils.countMatches(hmm.getStateSequence().toString(), "L"));

			System.out.println("Viterbi:");
			System.out.println("  Error: " + hmm.accuracy(viterbi.getSequence()));
			System.out.println("  L:" + StringUtils.countMatches(viterbi.toString(), "L"));
			System.out.println("Posterior:");
			System.out.println("  Error: " + hmm.accuracy(posterior.getSequence()));
			System.out.println("  L:" + StringUtils.countMatches(posterior.toString(), "L"));
			
			CSVGenerator csv = new CSVGenerator("data\\casinoData.csv");
			List[] lists = {hmm.getStateSequence(), posterior.getPosterior().getValuesForState("F")};
			Class[] classes = {String.class, Double.class};
			csv.createRowToCol(lists, classes);
			csv.close();
		} else if (choice == 9) {
			HMMAnalyzer.runTests(10);
		}
	}
}
