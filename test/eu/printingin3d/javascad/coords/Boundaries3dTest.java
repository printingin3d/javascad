package eu.printingin3d.javascad.coords;

import static eu.printingin3d.javascad.coords.BoundaryTest.assertBoundaryEquals;
import static eu.printingin3d.javascad.coords.Coords3dTest.assertCoords3dEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import eu.printingin3d.javascad.exceptions.IllegalValueException;

public class Boundaries3dTest {
	private static final double DELTA = 15.0;
	private static final double MIN_X = 53.1;
	private static final double MAX_X = 98.2;
	private static final double MIN_Y = 13.3;
	private static final double MAX_Y = 43.3;
	private static final double MIN_Z = 33.1;
	private static final double MAX_Z = 90.1;
	private static final Boundaries3d TEST_SUBJECT = new Boundaries3d(
				new Boundary(MIN_X, MAX_X),
				new Boundary(MIN_Y, MAX_Y),
				new Boundary(MIN_Z, MAX_Z)
			); 

	public static void assertBoundariesEquals(Boundaries3d expected, Boundaries3d actual) {
		assertBoundaryEquals(expected.getX(), actual.getX());
		assertBoundaryEquals(expected.getY(), actual.getY());
		assertBoundaryEquals(expected.getZ(), actual.getZ());
	}
	
	@Test
	public void testAdd() {
		Boundaries3d b = TEST_SUBJECT.add(TEST_SUBJECT);
		assertDoubleEquals(MIN_X*2, b.getX().getMin());
		assertDoubleEquals(MAX_X*2, b.getX().getMax());
		assertDoubleEquals(MIN_Y*2, b.getY().getMin());
		assertDoubleEquals(MAX_Y*2, b.getY().getMax());
		assertDoubleEquals(MIN_Z*2, b.getZ().getMin());
		assertDoubleEquals(MAX_Z*2, b.getZ().getMax());
	}
	
	@Test
	public void testMoveX() {
		Boundaries3d b = TEST_SUBJECT.move(Coords3d.xOnly(DELTA));
		assertDoubleEquals(MIN_X+DELTA, b.getX().getMin());
		assertDoubleEquals(MAX_X+DELTA, b.getX().getMax());
	}
	
	@Test
	public void testMoveY() {
		Boundaries3d b = TEST_SUBJECT.move(Coords3d.yOnly(DELTA));
		assertDoubleEquals(MIN_Y+DELTA, b.getY().getMin());
		assertDoubleEquals(MAX_Y+DELTA, b.getY().getMax());
	}
	
	@Test
	public void testMoveZ() {
		Boundaries3d b = TEST_SUBJECT.move(Coords3d.zOnly(DELTA));
		assertDoubleEquals(MIN_Z+DELTA, b.getZ().getMin());
		assertDoubleEquals(MAX_Z+DELTA, b.getZ().getMax());
	}
	
	@Test
	public void testScale() {
		Boundaries3d b = TEST_SUBJECT.scale(new Coords3d(2.0, 3.0, 1.5));
		assertDoubleEquals(MIN_X*2.0, b.getX().getMin());
		assertDoubleEquals(MAX_X*2.0, b.getX().getMax());
		assertDoubleEquals(MIN_Y*3.0, b.getY().getMin());
		assertDoubleEquals(MAX_Y*3.0, b.getY().getMax());
		assertDoubleEquals(MIN_Z*1.5, b.getZ().getMin());
		assertDoubleEquals(MAX_Z*1.5, b.getZ().getMax());
	}

	@Test
	public void theTwoConstructorShouldReturnWithTheSameValues() {
		Coords3d minCorner = new Coords3d(MIN_X, MIN_Y, MIN_Z);
		Coords3d maxCorner = new Coords3d(MAX_X, MAX_Y, MAX_Z);
		Boundaries3d b = new Boundaries3d(minCorner, maxCorner);
		assertBoundariesEquals(TEST_SUBJECT, b);
	}
	
	@Test
	public void theTwoConstructorShouldReturnWithTheSameValuesEvenIfTheCoordinatesAreNotInTheRightOrder() {
		Coords3d minCorner = new Coords3d(MIN_X, MAX_Y, MAX_Z);
		Coords3d maxCorner = new Coords3d(MAX_X, MIN_Y, MIN_Z);
		Boundaries3d b = new Boundaries3d(minCorner, maxCorner);
		assertBoundariesEquals(TEST_SUBJECT, b);
	}
	
	@Test
	public void getMinCornerShouldReturnWithTheLeftBottomFrontCorner() {
		Coords3d minCorner = new Coords3d(MIN_X, MIN_Y, MIN_Z);
		assertCoords3dEquals(minCorner, TEST_SUBJECT.getMinCorner());
	}
	
	@Test
	public void getMaxCornerShouldReturnWithTheRightTopBackCorner() {
		Coords3d maxCorner = new Coords3d(MAX_X, MAX_Y, MAX_Z);
		assertCoords3dEquals(maxCorner, TEST_SUBJECT.getMaxCorner());
	}
	
	@Test
	public void rotateXPlusShouldMoveYToZ() {
		Boundaries3d b = TEST_SUBJECT.rotate(Angles3d.ROTATE_PLUS_X);
		
		assertBoundaryEquals(TEST_SUBJECT.getX(), b.getX());
		
		assertDoubleEquals(MAX_Y, b.getZ().getMax());
		assertDoubleEquals(MIN_Y, b.getZ().getMin());
	}
	
	@Test
	public void rotateXMinusShouldMoveZToY() {
		Boundaries3d b = TEST_SUBJECT.rotate(Angles3d.ROTATE_MINUS_X);
		
		assertBoundaryEquals(TEST_SUBJECT.getX(), b.getX());
		
		assertDoubleEquals(MAX_Z, b.getY().getMax());
		assertDoubleEquals(MIN_Z, b.getY().getMin());
	}
	
	@Test
	public void rotateYPlusShouldMoveZToX() {
		Boundaries3d b = TEST_SUBJECT.rotate(Angles3d.ROTATE_PLUS_Y);
		
		assertBoundaryEquals(TEST_SUBJECT.getY(), b.getY());
		
		assertDoubleEquals(MAX_Z, b.getX().getMax());
		assertDoubleEquals(MIN_Z, b.getX().getMin());
	}
	
	@Test
	public void rotateYMinusShouldMoveXToZ() {
		Boundaries3d b = TEST_SUBJECT.rotate(Angles3d.ROTATE_MINUS_Y);
		
		assertBoundaryEquals(TEST_SUBJECT.getY(), b.getY());
		
		assertDoubleEquals(MAX_X, b.getZ().getMax());
		assertDoubleEquals(MIN_X, b.getZ().getMin());
	}
	
	@Test
	public void rotateZPlusShouldMoveXToY() {
		Boundaries3d b = TEST_SUBJECT.rotate(Angles3d.ROTATE_PLUS_Z);
		
		assertBoundaryEquals(TEST_SUBJECT.getZ(), b.getZ());
		
		assertDoubleEquals(MAX_X, b.getY().getMax());
		assertDoubleEquals(MIN_X, b.getY().getMin());
	}
	
	@Test
	public void rotateZMinusShouldMoveYToX() {
		Boundaries3d b = TEST_SUBJECT.rotate(Angles3d.ROTATE_MINUS_Z);
		
		assertBoundaryEquals(TEST_SUBJECT.getZ(), b.getZ());
		
		assertDoubleEquals(MAX_Y, b.getX().getMax());
		assertDoubleEquals(MIN_Y, b.getX().getMin());
	}
	
	@Test
	public void combineShouldReturnTheMinAndMaxValueOfAList() {
		Boundaries3d b1 = new Boundaries3d(
				new Boundary(50.0, 20.3, 18.3),
				new Boundary(34.3, 90.1, -13.3),
				new Boundary(85.3, 34.1, 77.0)
			);
		Boundaries3d b2 = new Boundaries3d(
				new Boundary(10.1),
				new Boundary(29.2),
				new Boundary(66.1)
			);
		Boundaries3d b3 = new Boundaries3d(
				new Boundary(29.3, 60.0),
				new Boundary(0.0),
				new Boundary(999.99, 0.0)
			);
		Boundaries3d b = Boundaries3d.combine(Arrays.asList(b1, b2, b3));
		assertDoubleEquals(10.1, b.getX().getMin());
		assertDoubleEquals(60.0, b.getX().getMax());
		
		assertDoubleEquals(-13.3, b.getY().getMin());
		assertDoubleEquals(90.1, b.getY().getMax());
		
		assertDoubleEquals(0.0, b.getZ().getMin());
		assertDoubleEquals(999.99, b.getZ().getMax());
	}
	
	@Test
	public void intersectShouldReturnTheMinAndMaxValueOfAList() {
		Boundaries3d b1 = new Boundaries3d(
				new Boundary(50.0, 20.3, 18.3),
				new Boundary(34.3, 90.1, -13.3),
				new Boundary(85.3, 34.1, 77.0)
				);
		Boundaries3d b2 = new Boundaries3d(
				new Boundary(10.1, 30.3),
				new Boundary(29.2, 32.2),
				new Boundary(66.1, 10.2)
				);
		Boundaries3d b3 = new Boundaries3d(
				new Boundary(29.3, 60.0),
				new Boundary(0.0, 33.3),
				new Boundary(999.99, 0.0)
				);
		Boundaries3d b = Boundaries3d.intersect(Arrays.asList(b1, b2, b3));
		assertDoubleEquals(29.3, b.getX().getMin());
		assertDoubleEquals(30.3, b.getX().getMax());
		
		assertDoubleEquals(29.2, b.getY().getMin());
		assertDoubleEquals(32.2, b.getY().getMax());
		
		assertDoubleEquals(34.1, b.getZ().getMin());
		assertDoubleEquals(66.1, b.getZ().getMax());
	}
	
	@Test(expected=IllegalValueException.class)
	public void intersectShouldThrowExceptionInCaseOfAnEmptyList() {
		Boundaries3d.intersect(Collections.<Boundaries3d>emptyList());
	}
}
