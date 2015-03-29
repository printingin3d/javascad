package eu.printingin3d.javascad.coords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import eu.printingin3d.javascad.testutils.RandomUtils;

public class Angles3dTest {
	private static final double EPSILON = 0.001;
	
	@Test
	public void testNormalization() {
		Angles3d testSubject = new Angles3d(500.0, -270.0, 720.0);
		assertEquals(140.0, testSubject.x, EPSILON);
		assertEquals(90.0, testSubject.y, EPSILON);
		assertEquals(0.0, testSubject.z, EPSILON);
	}

	@Test
	public void rotateWithZeroShouldReturnWithTheSameAngle() {
		Angles3d testSubject = RandomUtils.getRandomAngle();
		assertEquals(testSubject, testSubject.rotate(Angles3d.ZERO));
	}
	
	@Test
	public void rotateTest1() {
		Angles3d testSubject = new Angles3d(90, 0, 0);
		assertEquals(new Angles3d(90, 90.0, 0.0), testSubject.rotate(Angles3d.ROTATE_PLUS_Y));
	}
	
	@Test
	public void rotateTest2() {
		assertEquals(new Angles3d(7.0692,143.9934,-64.4547), new Angles3d(25.0, 63.0, 0.0).rotate(new Angles3d(55.0, 72.3, 0.0)));
	}
	
	@Test
	public void rotateTest3() {
		assertFalse(Angles3d.ZERO.rotate(new Angles3d(0.001, 0, 0)).isZero());
		assertFalse(Angles3d.ZERO.rotate(new Angles3d(0, 0.001, 0)).isZero());
		assertFalse(Angles3d.ZERO.rotate(new Angles3d(0, 0, 0.001)).isZero());
	}

	@Test
	public void testRad() {
		Angles3d testSubject = new Angles3d(45.0, 30.0, 90.0);
		assertEquals(Math.PI/4.0, testSubject.getXRad(), EPSILON);
		assertEquals(Math.PI/6.0, testSubject.getYRad(), EPSILON);
		assertEquals(Math.PI/2.0, testSubject.getZRad(), EPSILON);
	}
}
