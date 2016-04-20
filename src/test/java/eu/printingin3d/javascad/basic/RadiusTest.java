package eu.printingin3d.javascad.basic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.printingin3d.javascad.exceptions.IllegalValueException;

public class RadiusTest {
	private static final double EPSILON = 1E-6;

	@Test
	public void testFactoryMethods() {
		assertEquals(Radius.fromDiameter(100), Radius.fromRadius(50));
		assertEquals(Radius.fromDiameter(0), Radius.ZERO);
	}
	
	@Test(expected=IllegalValueException.class)
	public void negativeDiameterShoudThrowException() {
		Radius.fromDiameter(-1);
	}
	
	@Test(expected=IllegalValueException.class)
	public void negativeRadiusShoudThrowException() {
		Radius.fromRadius(-1);
	}

	@Test
	public void testMul() {
		assertEquals(99, Radius.fromRadius(33).mul(3).getRadius(), EPSILON);
		assertEquals(Radius.ZERO, Radius.fromRadius(33).mul(0));
	}
	
	@Test
	public void testDivide() {
		assertEquals(33, Radius.fromRadius(99).divide(3).getRadius(), EPSILON);
	}
	
	@Test
	public void testSubstract() {
		assertEquals(0.5, Radius.fromRadius(1).substract(Radius.fromRadius(0.5)).getRadius(), EPSILON);
	}
	
	@Test
	public void testAdd() {
		assertEquals(1.5, Radius.fromRadius(1).add(Radius.fromRadius(0.5)).getRadius(), EPSILON);
	}
	
	@Test
	public void testPlusRadius() {
		assertEquals(1.5, Radius.fromRadius(1).plusRadius(0.5).getRadius(), EPSILON);
	}
	
	@Test
	public void testPlusDiameter() {
		assertEquals(1.5, Radius.fromRadius(1).plusDiameter(1.0).getRadius(), EPSILON);
	}
	
	@Test(expected=IllegalValueException.class)
	public void invertNonZeroRadiusShoudThrowException() {
		Radius.fromDiameter(10).inverse();
	}

}
