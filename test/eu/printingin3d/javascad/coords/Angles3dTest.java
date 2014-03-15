package eu.printingin3d.javascad.coords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Angles3d;
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
	public void testRad() {
		Angles3d testSubject = new Angles3d(45.0, 30.0, 90.0);
		assertEquals(Math.PI/4.0, testSubject.getXRad(), EPSILON);
		assertEquals(Math.PI/6.0, testSubject.getYRad(), EPSILON);
		assertEquals(Math.PI/2.0, testSubject.getZRad(), EPSILON);
	}
	
	@Test
	public void equalsShouldReturnTrueForTheSameObject() {
		Angles3d testSubject = RandomUtils.getRandomAngle();
		assertTrue(testSubject.equals(testSubject));
	}
	
	@Test
	public void equalsShouldReturnFalseForNull() {
		Angles3d testSubject = RandomUtils.getRandomAngle();
		assertFalse(testSubject.equals(null));
	}
	
	@Test
	public void equalsShouldReturnFalseForNonAngleObject() {
		Angles3d testSubject = RandomUtils.getRandomAngle();
		assertFalse(testSubject.equals("asd"));
	}
	
	@Test
	public void equalsShouldReturnTrueForTheSameAngleWithDifferentImplementation() {
		Angles3d angle1 = new Angles3d(152.3225,125.6452,95.0465);
		Angles3d angle2 = new Angles3d(-27.6775,54.3548,-84.9535);
		assertEquals(angle1, angle2);
	}
}
