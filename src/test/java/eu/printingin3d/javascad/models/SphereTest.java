package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.vrl.CSG;

public class SphereTest {
	private static final double EPSILON = 0.001;

	@Test
	public void testToScad() {
		Sphere sphere = new Sphere(10.0);
		assertEqualsWithoutWhiteSpaces("sphere(r=10);", sphere);
	}
	
	@Test
	public void toScadShouldBeRounded() {
		Sphere sphere = new Sphere(Math.PI);
		assertEqualsWithoutWhiteSpaces("sphere(r=3.1416);", sphere);
	}
	
	@Test(expected = IllegalValueException.class)
	public void shouldThrowExceptionForANegativeRadius() {
		new Sphere(-10.0);
	}
	
	@Test
	public void testSomeTautologiesRegardingBoundariesOnRandomSpheres() {
		for (int i=0;i<10;i++) {
			Sphere sphere = new Sphere(RandomUtils.getRandomDouble(0, 1000));
			Boundaries3d boundaries = sphere.getBoundaries();
			// every cube should be in the origo
			Assert.assertEquals(0.0, boundaries.getX().getMiddle(), EPSILON);
			Assert.assertEquals(0.0, boundaries.getY().getMiddle(), EPSILON);
			Assert.assertEquals(0.0, boundaries.getZ().getMiddle(), EPSILON);
			// every cube's min value should be equals with max value except the sign
			Assert.assertEquals(-boundaries.getX().getMin(), boundaries.getZ().getMax(), 0.001);
			Assert.assertEquals(-boundaries.getY().getMin(), boundaries.getZ().getMax(), 0.001);
			Assert.assertEquals(-boundaries.getZ().getMin(), boundaries.getZ().getMax(), 0.001);
		}
	}
	
	@Test
	public void allBoundaryShouldBeTheRadiusOfTheSphere() {
		Sphere sphere = new Sphere(15.0);
		
		Assert.assertEquals(15.0, sphere.getBoundaries().getX().getMax(), EPSILON);
		Assert.assertEquals(15.0, sphere.getBoundaries().getY().getMax(), EPSILON);
		Assert.assertEquals(15.0, sphere.getBoundaries().getZ().getMax(), EPSILON);
	}
	
	@Test
	public void csgShouldGenerateAllPointsEqualDistance() {
		CSG csg = new Sphere(15.0).toCSG();

		for (Coords3d v : csg.getPoints()) {
			Assert.assertEquals(15.0, v.distance(Coords3d.ZERO), EPSILON);
		}
	}
}
