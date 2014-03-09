package eu.printingin3d.javascad.coords;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Abstract2dTest {
	
	private static class TestAbstract2d extends Abstract2d {
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
	
	@Test
	public void testEqualsAndHashCode() {
		TestAbstract2d test1 = new TestAbstract2d(0.1, 20.0);
		TestAbstract2d test21 = new TestAbstract2d(5.0, 20.0);
		TestAbstract2d test22 = new TestAbstract2d(0.1, -20.0);
		TestAbstract2d test3 = new TestAbstract2d(1.0/10.0, 20.0);
	
		assertFalse(test1.equals(null));
		assertFalse(test1.equals("alma"));
		assertFalse(test1.equals(test21));
		assertFalse(test1.equals(test22));
		
		assertEquals(test1, test1);
		assertEquals(test1, test3);
		
		assertEquals(test1.hashCode(), test3.hashCode());
	}

}
