package eu.printingin3d.javascad.tranzitions;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.testutils.TestModel;

public class MirrorTest {
	private static final double MAX = 50.8;
	private static final double MIN = 10.2;
	
	private static final double EPSILON = 0.001;
	private static final TestModel TEST_MODEL = new TestModel("(model)", 
			new Boundaries3d(new Boundary(MIN, MAX), new Boundary(MIN, MAX), new Boundary(MIN, MAX)));
	
	@Before
	public void init() {
		Language.OpenSCAD.setCurrent();
	}

	@Test(expected=IllegalValueException.class)
	public void mirrorXshouldThrowIllegalValueExceptionIfModelIsNull() {
		Mirror.mirrorX(null);
	}
	
	@Test(expected=IllegalValueException.class)
	public void mirrorYshouldThrowIllegalValueExceptionIfModelIsNull() {
		Mirror.mirrorY(null);
	}
	
	@Test(expected=IllegalValueException.class)
	public void mirrorZshouldThrowIllegalValueExceptionIfModelIsNull() {
		Mirror.mirrorZ(null);
	}
	
	@Test
	public void testMirrorX() {
		assertEqualsWithoutWhiteSpaces("mirror([1,0,0]) (model)", Mirror.mirrorX(TEST_MODEL).toScad());
	}
	
	@Test
	public void testMirrorXPovRay() {
		Language.POVRay.setCurrent();
		
		assertEqualsWithoutWhiteSpaces("object { (model) scale <-1,1,1> #attributes }", Mirror.mirrorX(TEST_MODEL).innerToScad());
	}

	@Test
	public void testMirrorY() {
		assertEqualsWithoutWhiteSpaces("mirror([0,1,0]) (model)", Mirror.mirrorY(TEST_MODEL).toScad());
	}
	
	@Test
	public void testMirrorYPovRay() {
		Language.POVRay.setCurrent();
		
		assertEqualsWithoutWhiteSpaces("object { (model) scale <1,-1,1> #attributes }", Mirror.mirrorY(TEST_MODEL).innerToScad());
	}

	@Test
	public void testMirrorZ() {
		assertEqualsWithoutWhiteSpaces("mirror([0,0,1]) (model)", Mirror.mirrorZ(TEST_MODEL).toScad());
	}
	
	@Test
	public void testMirrorZPovRay() {
		Language.POVRay.setCurrent();
		
		assertEqualsWithoutWhiteSpaces("object { (model) scale <1,1,-1> #attributes }", Mirror.mirrorZ(TEST_MODEL).innerToScad());
	}

	@Test
	public void testBoundaryWithMirrorX() {
		Boundaries3d boundaries = Mirror.mirrorX(TEST_MODEL).getBoundaries();
		assertEquals(-MAX, boundaries.getX().getMin(), EPSILON);
		assertEquals(-MIN, boundaries.getX().getMax(), EPSILON);
		assertEquals(MIN, boundaries.getY().getMin(), EPSILON);
		assertEquals(MAX, boundaries.getY().getMax(), EPSILON);
		assertEquals(MIN, boundaries.getZ().getMin(), EPSILON);
		assertEquals(MAX, boundaries.getZ().getMax(), EPSILON);
	}
	
	@Test
	public void testBoundaryWithMirrorY() {
		Boundaries3d boundaries = Mirror.mirrorY(TEST_MODEL).getBoundaries();
		assertEquals(MIN, boundaries.getX().getMin(), EPSILON);
		assertEquals(MAX, boundaries.getX().getMax(), EPSILON);
		assertEquals(-MAX, boundaries.getY().getMin(), EPSILON);
		assertEquals(-MIN, boundaries.getY().getMax(), EPSILON);
		assertEquals(MIN, boundaries.getZ().getMin(), EPSILON);
		assertEquals(MAX, boundaries.getZ().getMax(), EPSILON);
	}
	
	@Test
	public void testBoundaryWithMirrorZ() {
		Boundaries3d boundaries = Mirror.mirrorZ(TEST_MODEL).getBoundaries();
		assertEquals(MIN, boundaries.getX().getMin(), EPSILON);
		assertEquals(MAX, boundaries.getX().getMax(), EPSILON);
		assertEquals(MIN, boundaries.getY().getMin(), EPSILON);
		assertEquals(MAX, boundaries.getY().getMax(), EPSILON);
		assertEquals(-MAX, boundaries.getZ().getMin(), EPSILON);
		assertEquals(-MIN, boundaries.getZ().getMax(), EPSILON);
	}
}
