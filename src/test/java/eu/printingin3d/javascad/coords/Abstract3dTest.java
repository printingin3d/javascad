package eu.printingin3d.javascad.coords;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import eu.printingin3d.javascad.enums.OutputFormat;

public class Abstract3dTest {
	
	public static class TestAbstract3d extends Abstract3d {
		public TestAbstract3d(double x, double y, double z) {
			super(x, y, z);
		}
	}
	
	@Test
	public void testIsXZero() {
		TestAbstract3d testSubject = new TestAbstract3d(10.0, 10.0, 10.0);
		assertFalse(testSubject.isXZero());
		testSubject = new TestAbstract3d(0.0, 10.0, 10.0);
		assertTrue(testSubject.isXZero());
	}

	@Test
	public void testIsYZero() {
		TestAbstract3d testSubject = new TestAbstract3d(10.0, 10.0, 10.0);
		assertFalse(testSubject.isYZero());
		testSubject = new TestAbstract3d(10.0, 0.0, 10.0);
		assertTrue(testSubject.isYZero());
	}

	@Test
	public void testIsZZero() {
		TestAbstract3d testSubject = new TestAbstract3d(10.0, 10.0, 10.0);
		assertFalse(testSubject.isZZero());
		testSubject = new TestAbstract3d(10.0, 10.0, 0.0);
		assertTrue(testSubject.isZZero());
	}

	@Test
	public void testIsZero() {
		TestAbstract3d testSubject = new TestAbstract3d(10.0, 10.0, 10.0);
		assertFalse(testSubject.isZero());
		testSubject = new TestAbstract3d(0.0, 0.0, 0.0);
		assertTrue(testSubject.isZero());
	}

	@Test
	public void testToString() {
		TestAbstract3d testSubject = new TestAbstract3d(10.0, 20.0, 30.0);
		assertEqualsWithoutWhiteSpaces("[10,20,30]", testSubject.toString());
	}

	@Test
	public void testToStringRounding() {
		TestAbstract3d testSubject = new TestAbstract3d(10.7, Math.E, Math.PI);
		assertEqualsWithoutWhiteSpaces("[10.7,2.7183,3.1416]", testSubject.toString());
	}
	
	@Test
	public void testFormat() {
		TestAbstract3d testSubject = new TestAbstract3d(10.0, 20.0, 30.0);
		assertEqualsWithoutWhiteSpaces("10 20 30", testSubject.format(OutputFormat.STL));
	}
	
	@Test
	public void testFormatRounding() {
		TestAbstract3d testSubject = new TestAbstract3d(10.7, Math.E, Math.PI);
		assertEqualsWithoutWhiteSpaces("10.7 2.7183 3.1416", testSubject.format(OutputFormat.STL));
	}
	
	@Test
	public void closeEqualsShouldReturnTrueIfBothValueAreNull() {
		assertTrue(Abstract3d.closeEquals(null, null));
	}
	
	@Test
	public void closeEqualsShouldReturnFalseIfExactlyOneOfTheTwoValuesIsNull() {
		assertFalse(Abstract3d.closeEquals(Angles3d.ZERO, null));
		assertFalse(Abstract3d.closeEquals(null, Angles3d.ZERO));
	}
	
	@Test
	public void closeEqualsShouldReturnFalseIfTheTwoValuesAreDifferentTypes() {
		assertFalse(Abstract3d.closeEquals(Angles3d.ZERO, Coords3d.ZERO));
	}
	
	@Test
	public void closeEqualsShouldReturnFalseIfTheValuesAreSlightlyDifferent() {
		Angles3d one = new Angles3d(10, 10, 10);
		Angles3d two1 = new Angles3d(10, 10, 10.0001);
		assertFalse(Abstract3d.closeEquals(one, two1));
		assertEquals(one, two1);
		
		Angles3d two2 = new Angles3d(10, 10.0001, 10);
		assertFalse(Abstract3d.closeEquals(one, two2));
		assertEquals(one, two2);
		
		Angles3d two3 = new Angles3d(10.0001, 10, 10);
		assertFalse(Abstract3d.closeEquals(one, two3));
		assertEquals(one, two3);
	}

}
