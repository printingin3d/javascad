package eu.printingin3d.javascad.coords;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static eu.printingin3d.javascad.testutils.RandomUtils.getRandomAngle;
import static eu.printingin3d.javascad.testutils.RandomUtils.getRandomCoords;
import static eu.printingin3d.javascad.testutils.RandomUtils.getRandomDouble;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;

public class Coords3dTest {
	
	public static void assertCoords3dEquals(Coords3d expected, Coords3d actual) {
		assertDoubleEquals(expected.x, actual.x);
		assertDoubleEquals(expected.y, actual.y);
		assertDoubleEquals(expected.z, actual.z);
	}
	
	@Test
	public void testRotateZero() {
		Coords3d start = getRandomCoords();
		Coords3d result = start.rotate(Angles3d.ZERO);
		assertCoords3dEquals(start, result);
	}
	
	@Test
	public void testRotateX() {
		Coords3d start = new Coords3d(0.0,0.0,100.0);
		Coords3d result = start.rotate(Angles3d.ROTATE_PLUS_X);
		assertDoubleEquals(0.0, result.x);
		assertDoubleEquals(-100.0, result.y);
		assertDoubleEquals(0.0, result.z);
		// should create a new object
		assertNotSame(start, result);
	}
	
	@Test
	public void testRotateY() {
		Coords3d start = new Coords3d(100.0,0.0,0.0);
		Coords3d result = start.rotate(Angles3d.ROTATE_PLUS_Y);
		assertDoubleEquals(0.0, result.x);
		assertDoubleEquals(0.0, result.y);
		assertDoubleEquals(-100.0, result.z);
		// should create a new object
		assertNotSame(start, result);
	}
	
	@Test
	public void testRotateZ() {
		Coords3d start = new Coords3d(100.0,0.0,0.0);
		Coords3d result = start.rotate(Angles3d.ROTATE_PLUS_Z);
		assertDoubleEquals(0.0, result.x);
		assertDoubleEquals(100.0, result.y);
		assertDoubleEquals(0.0, result.z);
		// should create a new object
		assertNotSame(start, result);
	}
	
	@Test
	public void rotateShouldNotChangeTheDistanceFromOrig1() {
		Coords3d start = getRandomCoords();
		Coords3d result = start.rotate(getRandomAngle());
		double d1 = start.x*start.x + start.y*start.y + start.z*start.z;
		double d2 = result.x*result.x + result.y*result.y + result.z*result.z;
		assertDoubleEquals(d1, d2);
	}
	
	@Test
	public void rotateShouldNotChangeTheDistanceFromOrig2() {
		Coords3d start = getRandomCoords();
		Coords3d result = start.rotate(new Angles3d(0, getRandomDouble(-1000.0, 1000.0), getRandomDouble(-1000.0, 1000.0)));
		double d1 = start.x*start.x + start.y*start.y + start.z*start.z;
		double d2 = result.x*result.x + result.y*result.y + result.z*result.z;
		assertDoubleEquals(d1, d2);
	}
	
	@Test
	public void rotateShouldNotChangeTheDistanceFromOrig3() {
		Coords3d start = getRandomCoords();
		Coords3d result = start.rotate(new Angles3d(getRandomDouble(-1000.0, 1000.0), 0, getRandomDouble(-1000.0, 1000.0)));
		double d1 = start.x*start.x + start.y*start.y + start.z*start.z;
		double d2 = result.x*result.x + result.y*result.y + result.z*result.z;
		assertDoubleEquals(d1, d2);
	}
	
	@Test
	public void rotateShouldNotChangeTheDistanceFromOrig4() {
		Coords3d start = getRandomCoords();
		Coords3d result = start.rotate(new Angles3d(getRandomDouble(-1000.0, 1000.0), getRandomDouble(-1000.0, 1000.0), 0));
		double d1 = start.x*start.x + start.y*start.y + start.z*start.z;
		double d2 = result.x*result.x + result.y*result.y + result.z*result.z;
		assertDoubleEquals(d1, d2);
	}

	@Test
	public void testStaticConstructors() {
		double value = getRandomDouble(-1000.0, 1000.0);
		Coords3d result = Coords3d.xOnly(value);
		assertDoubleEquals(value, result.x);
		assertDoubleEquals(0.0, result.y);
		assertDoubleEquals(0.0, result.z);
		
		result = Coords3d.yOnly(value);
		assertDoubleEquals(0.0, result.x);
		assertDoubleEquals(value, result.y);
		assertDoubleEquals(0.0, result.z);
		
		result = Coords3d.zOnly(value);
		assertDoubleEquals(0.0, result.x);
		assertDoubleEquals(0.0, result.y);
		assertDoubleEquals(value, result.z);
	}

	@Test
	public void testMidPoint() {
		Coords3d a = getRandomCoords();
		Coords3d b = getRandomCoords();
		
		Coords3d midPoint = Coords3d.midPoint(a, b);
		
		assertDoubleEquals((a.x+b.x)/2.0, midPoint.x);
		assertDoubleEquals((a.y+b.y)/2.0, midPoint.y);
		assertDoubleEquals((a.z+b.z)/2.0, midPoint.z);
	}
	
	@Test
	public void variancesShouldBeSingleElementForZero() {
		assertEquals(Collections.singleton(Coords3d.ZERO), Coords3d.ZERO.createVariances());
	}
	
	@Test
	public void variancesShouldBeTwoElementsForEveryCoordinateWhichHasOnlyOneNonZeroElement() {
		Collection<Coords3d> variances = Coords3d.X.createVariances();
		assertEquals(new HashSet<>(Arrays.asList(Coords3d.X, Coords3d.X.inverse())), variances);
		
		variances = Coords3d.Y.createVariances();
		assertEquals(new HashSet<>(Arrays.asList(Coords3d.Y, Coords3d.Y.inverse())), variances);
		
		variances = Coords3d.Z.createVariances();
		assertEquals(new HashSet<>(Arrays.asList(Coords3d.Z, Coords3d.Z.inverse())), variances);
	}
	
	@Test
	public void variancesShouldBeEightElementsForThreeElementsCoordinates() {
		Collection<Coords3d> variances = new Coords3d(1,1,1).createVariances();
		assertEquals(
				new HashSet<>(Arrays.asList(
						new Coords3d(+1,+1,+1), new Coords3d(+1,+1,-1),
						new Coords3d(+1,-1,+1), new Coords3d(+1,-1,-1),
						new Coords3d(-1,+1,+1), new Coords3d(-1,+1,-1),
						new Coords3d(-1,-1,+1), new Coords3d(-1,-1,-1)
					)), 
				variances);
	}
}
