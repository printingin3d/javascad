package eu.printingin3d.javascad.coords2d;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Abstract2dTest {
	
	public static class TestAbstract2d extends Abstract2d {
		public TestAbstract2d(double x, double y) {
			super(x, y);
		}
	}
	
	@Test
	public void testIsXZero() {
		TestAbstract2d testSubject = new TestAbstract2d(10.0, 10.0);
		assertFalse(testSubject.isXZero());
		testSubject = new TestAbstract2d(0.0, 10.0);
		assertTrue(testSubject.isXZero());
	}

	@Test
	public void testIsYZero() {
		TestAbstract2d testSubject = new TestAbstract2d(10.0, 10.0);
		assertFalse(testSubject.isYZero());
		testSubject = new TestAbstract2d(10.0, 0.0);
		assertTrue(testSubject.isYZero());
	}

	@Test
	public void testIsZero() {
		TestAbstract2d testSubject = new TestAbstract2d(10.0, 10.0);
		assertFalse(testSubject.isZero());
		testSubject = new TestAbstract2d(0.0, 0.0);
		assertTrue(testSubject.isZero());
	}

	@Test
	public void testToString() {
		TestAbstract2d testSubject = new TestAbstract2d(10.0, 20.0);
		assertEqualsWithoutWhiteSpaces("[10,20]", testSubject.toString());
	}

	@Test
	public void testToStringRounding() {
		TestAbstract2d testSubject = new TestAbstract2d(10.7, Math.E);
		assertEqualsWithoutWhiteSpaces("[10.7,2.7183]", testSubject.toString());
	}
}
