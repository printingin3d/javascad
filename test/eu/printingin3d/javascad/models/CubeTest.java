package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Dims3d;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.testutils.RandomUtils;

public class CubeTest {
	private static final double EPSILON = 0.001;
	
	@Before
	public void init() {
		Language.OpenSCAD.setCurrent();
	}
	
	@Test
	public void testToScad1() {
		Cube cube = new Cube(10.0);
		assertEqualsWithoutWhiteSpaces("cube([10,10,10],center=true);", cube.toScad());
	}
	
	@Test
	public void testToScad2() {
		Cube cube = new Cube(new Dims3d(10.0, 20.0, 30.0));
		assertEqualsWithoutWhiteSpaces("cube([10,20,30],center=true);", cube.toScad());
	}
	
	@Test
	public void testPovRay() {
		Language.POVRay.setCurrent();
		
		Cube cube = new Cube(new Dims3d(10.0, 20.0, 30.0));
		assertEqualsWithoutWhiteSpaces("box { <-5, -10, -15> <5, 10, 15> #attributes }", cube.innerToScad());
	}
	
	@Test
	public void toScadShouldBeRounded() {
		Cube cube = new Cube(new Dims3d(Math.PI, 20.0, 30.0));
		assertEqualsWithoutWhiteSpaces("cube([3.1416,20,30],center=true);", cube.toScad());
	}
	
	@Test
	public void testSomeTautologiesRegardingBoundariesOnRandomCubes() {
		for (int i=0;i<10;i++) {
			Cube cube = new Cube(RandomUtils.getRandomDims());
			Boundaries3d boundaries = cube.getBoundaries();
			// every cube should be in the origo
			assertEquals(0.0, boundaries.getX().getMiddle(), EPSILON);
			assertEquals(0.0, boundaries.getY().getMiddle(), EPSILON);
			assertEquals(0.0, boundaries.getZ().getMiddle(), EPSILON);
			// every cube's min value should be equals with max value except the sign
			assertEquals(-boundaries.getX().getMin(), boundaries.getX().getMax(), 0.001);
			assertEquals(-boundaries.getY().getMin(), boundaries.getY().getMax(), 0.001);
			assertEquals(-boundaries.getZ().getMin(), boundaries.getZ().getMax(), 0.001);
		}
	}
	
	@Test
	public void verticalBoundaryShouldBeHalfOfTheHeightOfTheCube() {
		Cube cube = new Cube(new Dims3d(10.0, 20.0, 30.0));
		
		assertEquals(15.0, cube.getBoundaries().getZ().getMax(), EPSILON);
	}
	
	@Test
	public void horizontalBoundaryShouldBeHalfOfTheWidthOfTheCube() {
		Cube cube = new Cube(new Dims3d(10.0, 20.0, 30.0));
		
		assertEquals(5.0, cube.getBoundaries().getX().getMax(), EPSILON);
	}
	
	@Test
	public void depthBoundaryShouldBeHalfOfTheDepthOfTheCube() {
		Cube cube = new Cube(new Dims3d(10.0, 20.0, 30.0));
		
		assertEquals(10.0, cube.getBoundaries().getY().getMax(), EPSILON);
	}
}
