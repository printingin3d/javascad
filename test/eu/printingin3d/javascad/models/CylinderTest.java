package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;
import static eu.printingin3d.javascad.testutils.RandomUtils.getRandomDouble;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;

public class CylinderTest {
	private static final double EPSILON = 0.001;

	@Test
	public void testCylinder() {
		Cylinder cylinder = new Cylinder(10.0, 20.0);
		assertEqualsWithoutWhiteSpaces("cylinder(h=10, r=20, center=true);", cylinder.toScad());
	}

	@Test(expected = IllegalValueException.class)
	public void negativeLengthShouldThrowException1() {
		new Cylinder(-10.0, 5.0);
	}
	
	@Test(expected = IllegalValueException.class)
	public void negativeLengthShouldThrowException2() {
		new Cylinder(-10.0, 5.0, 7.0);
	}
	
	@Test(expected = IllegalValueException.class)
	public void negativeRadiusShouldThrowException() {
		new Cylinder(10.0, -5.0);
	}
	
	@Test(expected = IllegalValueException.class)
	public void negativeRadius1ShouldThrowException() {
		new Cylinder(10.0, -5.0, 5.0);
	}
	
	@Test(expected = IllegalValueException.class)
	public void negativeRadius2ShouldThrowException() {
		new Cylinder(10.0, 5.0, -5.0);
	}
	
	@Test
	public void testCylinderWhenTheTwoRadiusesAreTheSame() {
		Cylinder cylinder = new Cylinder(10.0, 20.0, 20.0);
		assertEqualsWithoutWhiteSpaces("cylinder(h=10, r=20, center=true);", cylinder.toScad());
	}
	
	@Test
	public void testCone() {
		Cylinder cylinder = new Cylinder(10.0, 20.0, 5.0);
		assertEqualsWithoutWhiteSpaces("cylinder(h=10, r1=20, r2=5, center=true);", cylinder.toScad());
	}

	@Test
	public void testSomeTautologiesRegardingBoundariesOnRandomCylinders() {
		for (int i=0;i<10;i++) {
			Cylinder cylinder = new Cylinder(getRandomDouble(0, 1000.0), getRandomDouble(0, 1000.0));
			Boundaries3d boundaries = cylinder.getBoundaries();
			// every cube should be in the origo
			Assert.assertEquals(0.0, boundaries.getX().getMiddle(), EPSILON);
			Assert.assertEquals(0.0, boundaries.getY().getMiddle(), EPSILON);
			Assert.assertEquals(0.0, boundaries.getZ().getMiddle(), EPSILON);
			// every cube's min value should be equals with max value except the sign
			Assert.assertEquals(-boundaries.getX().getMin(), boundaries.getX().getMax(), 0.001);
			Assert.assertEquals(-boundaries.getY().getMin(), boundaries.getY().getMax(), 0.001);
			Assert.assertEquals(-boundaries.getZ().getMin(), boundaries.getZ().getMax(), 0.001);
		}
	}
	
	@Test
	public void verticalBoundaryShouldBeHalfOfTheHeightOfTheCylinder() {
		Cylinder cylinder = new Cylinder(50.0, 10.0);
		
		Assert.assertEquals(25.0, cylinder.getBoundaries().getZ().getMax(), EPSILON);
	}
	
	@Test
	public void horizontalAndDepthBoundaryShouldBeTheRadiusOfTheCylinder() {
		Cylinder cylinder = new Cylinder(50.0, 10.0);
		
		Assert.assertEquals(10.0, cylinder.getBoundaries().getX().getMax(), EPSILON);
		Assert.assertEquals(10.0, cylinder.getBoundaries().getY().getMax(), EPSILON);
	}
	
	@Test
	public void horizontalAndDepthBoundaryShouldBeTheBiggerRadiusOfTheCylinderIfTheyAreDifferent() {
		Cylinder cylinder = new Cylinder(50.0, 10.0, 33.3);
		
		Assert.assertEquals(33.3, cylinder.getBoundaries().getX().getMax(), EPSILON);
		Assert.assertEquals(33.3, cylinder.getBoundaries().getY().getMax(), EPSILON);
		
		cylinder = new Cylinder(50.0, 15.0, 3.3);
		
		Assert.assertEquals(15.0, cylinder.getBoundaries().getX().getMax(), EPSILON);
		Assert.assertEquals(15.0, cylinder.getBoundaries().getY().getMax(), EPSILON);		
	}
}
