package bio.models.old;

import bio.models.DieRoller;
import junit.framework.TestCase;

public class TestDieRoller extends TestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testRouletteWheel() {
		double[] weights = {1/10.0, 1/10.0, 1/10.0, 1/10.0, 1/10.0, 1/2.0};
		assertTrue(DieRoller.rouletteWheel(weights, .01) == 0);
		assertTrue(DieRoller.rouletteWheel(weights, .05) == 0);
		assertTrue(DieRoller.rouletteWheel(weights, .15) == 1);
		assertTrue(DieRoller.rouletteWheel(weights, .25) == 2);
		assertTrue(DieRoller.rouletteWheel(weights, .35) == 3);
		assertTrue(DieRoller.rouletteWheel(weights, .45) == 4);
		assertTrue(DieRoller.rouletteWheel(weights, .55) == 5);
		assertTrue(DieRoller.rouletteWheel(weights, .95) == 5);
		assertTrue(DieRoller.rouletteWheel(weights, .99) == 5);
	}
}
