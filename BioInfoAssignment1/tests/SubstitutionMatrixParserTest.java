package tests;

import java.util.Hashtable;

import bio.controllers.SubstitutionMatrixParser;
import junit.framework.TestCase;

public class SubstitutionMatrixParserTest extends TestCase{
	SubstitutionMatrixParser parser;
	
	protected void setUp() {
		parser = new SubstitutionMatrixParser("data\\BLOSUM50.txt");
	}
	
	protected void tearDown() {
		parser.close();
	}
	
	public void testGetGapWeight() {
		assertTrue(parser.getGapWeight() == 0);
		parser.parseSubstitutionMatrix();
		assertTrue(parser.getGapWeight() == 16);
	}
	
	public void testGetLengthWeight() {
		assertTrue(parser.getLengthWeight() == 0);
		parser.parseSubstitutionMatrix();
		assertTrue(parser.getLengthWeight() == 4);
	}
	
	public void testParseSubstitutionMatrix() {
		Hashtable<String, Hashtable<String, Integer>> table = parser.parseSubstitutionMatrix();
		System.out.println(table);
	}
	
}
