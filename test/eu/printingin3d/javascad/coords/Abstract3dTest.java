package eu.printingin3d.javascad.coords;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.enums.Language;

public class Abstract3dTest {
	
	private static class TestAbstract3d extends Abstract3d {
		public TestAbstract3d(double x, double y, double z) {
			super(x, y, z);
		}
	}
	
	@Before
	public void init() {
		Language.OpenSCAD.setCurrent();
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
	public void testToStringPovRay() {
		Language.POVRay.setCurrent();

		TestAbstract3d testSubject = new TestAbstract3d(10.0, 20.0, 30.0);
		assertEqualsWithoutWhiteSpaces("<10,20,30>", testSubject.toString());
	}

	@Test
	public void testToStringRounding() {
		TestAbstract3d testSubject = new TestAbstract3d(10.7, Math.E, Math.PI);
		assertEqualsWithoutWhiteSpaces("[10.7,2.7183,3.1416]", testSubject.toString());
	}
	
	@Test
	public void testEqualsAndHashCode() {
		TestAbstract3d test1 = new TestAbstract3d(0.1, 20.0, 30.0);
		TestAbstract3d test21 = new TestAbstract3d(5.0, 20.0, 30.0);
		TestAbstract3d test22 = new TestAbstract3d(0.1, -20.0, 30.0);
		TestAbstract3d test23 = new TestAbstract3d(0.1, 20.0, 30.1);
		TestAbstract3d test3 = new TestAbstract3d(1.0/10.0, 20.0, 30.0);
	
		assertFalse(test1.equals(null));
		assertFalse(test1.equals("alma"));
		assertFalse(test1.equals(test21));
		assertFalse(test1.equals(test22));
		assertFalse(test1.equals(test23));
		
		assertEquals(test1, test1);
		assertEquals(test1, test3);
		
		assertEquals(test1.hashCode(), test3.hashCode());
	}

}
