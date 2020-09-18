package testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import datastructures.Relationship;

public class RelationshipTest {

	@Test
	public void testConstructor() {
		Relationship r = new Relationship("A", "B", "C");

		assertEquals("Relationship name should be set properly.", "A", r.getName());
		assertEquals("First class name should be set properly.", "B", r.getFirstClass());
		assertEquals("Second class name should be set properly.", "C", r.getSecondClass());
	}

	@Test
    public void testEquals()
    {
    	Relationship r1 = new Relationship("A","B","C");
    	Relationship r2 = new Relationship("A","B","C");
    	Relationship r3 = new Relationship("D","B","C");
    	Relationship r4 = new Relationship("A","D","C");
    	Relationship r5 = new Relationship("A","B","D");
    	String s = "";
    	
    	// Null check
    	assertFalse("Null should result in false", r1.equals(null));
    	
    	// Type check
    	assertFalse("Differing object types should result in false", r1.equals(s));
    	
       	// Identity check
    	assertTrue("Relationship should be equal with itself.", r1.equals(r1));
    	
    	// Equality check
    	assertTrue("Relationships should be equal.", r1.equals(r2));
    	assertFalse("Differing names should result in false", r1.equals(r3));
    	assertFalse("Differing first classes should result in false", r1.equals(r4));
    	assertFalse("Differing second classes should result in false", r1.equals(r5));
    }

}