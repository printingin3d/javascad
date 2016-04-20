package eu.printingin3d.javascad.basic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AngleTest {
	private static final double EPSILON = 1E-6;

	@Test
	public void testFactoryMethods() {
		assertEquals(Angle.A45, Angle.atan2(1, 1));
		assertEquals(Angle.A90, Angle.ofDegree(90));
		assertEquals(Angle.A180, Angle.ofRadian(Math.PI));
	}
	
	@Test
	public void testDegree() {
		assertEquals(45, Angle.A45.asDegree(), EPSILON);
		assertEquals(150, Angle.ofDegree(150).asDegree(), EPSILON);
	}
	
	@Test
	public void testRadian() {
		assertEquals(Math.PI/4, Angle.A45.asRadian(), EPSILON);
		assertEquals(1, Angle.ofRadian(1).asRadian(), EPSILON);
	}
	
	@Test
	public void testNormalize() {
		assertEquals(Angle.A45, Angle.ofDegree(720+45).normalize());
		assertEquals(Angle.ofDegree(-90), Angle.ofDegree(-90).normalize());
		assertEquals(Angle.A90, Angle.ofDegree(-270).normalize());
	}
	
	@Test
	public void testMul() {
		assertEquals(99, Angle.ofDegree(33).mul(3).asDegree(), EPSILON);
		assertEquals(Angle.ZERO, Angle.ofDegree(33).mul(0));
	}
	
	@Test
	public void testDivide() {
		assertEquals(33, Angle.ofDegree(99).divide(3).asDegree(), EPSILON);
	}
	
	@Test
	public void testSubstract() {
		assertEquals(0.5, Angle.ofRadian(1).substract(Angle.ofRadian(0.5)).asRadian(), EPSILON);
	}
	
	@Test
	public void testAdd() {
		assertEquals(1.5, Angle.ofRadian(1).add(Angle.ofRadian(0.5)).asRadian(), EPSILON);
	}
	
	@Test
	public void testInverse() {
		assertEquals(Angle.ofDegree(-45), Angle.A45.inverse());
	}
}
