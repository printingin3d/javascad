package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.testutils.RandomUtils;

public class SphereTest {
	private static final double EPSILON = 0.001;
	
	@Before
	public void init() {
		Language.OpenSCAD.setCurrent();
	}

	@Test
	public void testToScad() {
		Sphere sphere = new Sphere(10.0);
		assertEqualsWithoutWhiteSpaces("sphere(r=10);", sphere.toScad());
	}
	
	@Test
	public void testToPovRay() {
		Language.POVRay.setCurrent();
		
		Sphere sphere = new Sphere(10.0);
		assertEqualsWithoutWhiteSpaces("sphere{<0,0,0> 10 #attributes}", sphere.innerToScad());
	}
	
	@Test
	public void toScadShouldBeRounded() {
		Sphere sphere = new Sphere(Math.PI);
		assertEqualsWithoutWhiteSpaces("sphere(r=3.1416);", sphere.toScad());
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
}
