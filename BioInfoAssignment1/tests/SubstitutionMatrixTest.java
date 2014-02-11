package tests;

import java.util.ArrayList;

import bio.models.SubstitutionMatrix;
import junit.framework.TestCase;

public class SubstitutionMatrixTest extends TestCase {
	ArrayList<String> labels;
	SubstitutionMatrix<Integer> matrix;
	
	protected void setUp() throws Exception {
		super.setUp();
		labels = new ArrayList<String>();
		labels.add("A");
		labels.add("B");
		labels.add("C");
		labels.add("D");
		matrix = new SubstitutionMatrix<Integer>(10, 10, labels, 0);
		matrix.set("A", "A", 5);
		matrix.set("A", "B", 10);
		
	}

	public void testGet() {
		assertTrue(matrix.get("A", "A") ==  5);
		assertTrue(matrix.get("A", "B") == 10);
	}

	public void testSet() {
		matrix.set("B", "A", 15);
		assertTrue(matrix.get("B", "A") == 15);
		matrix.set("A", "A", 20);
		assertTrue(matrix.get("A", "A") == 20);
	}

}
