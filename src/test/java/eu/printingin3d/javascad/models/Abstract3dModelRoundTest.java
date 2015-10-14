package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.BoundaryTest;
import eu.printingin3d.javascad.enums.Plane;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class Abstract3dModelRoundTest {
	private static final Boundaries3d TEST_BOUNDARIES = RandomUtils.getRandomBoundaries();

	@Test
	public void roundShoundReturnWithANewObject() {
		Abstract3dModel testSubject = new Test3dModel("(base)");
		Assert.assertNotSame(testSubject, testSubject.round(Plane.XY, 1.0));
	}

	@Test
	public void roundShouldRenderWithMinkowskiXY() {
		Abstract3dModel testSubject = new Test3dModel("(base)")
				.round(Plane.XY, 2.0);
		assertEqualsWithoutWhiteSpaces("minkowski() {(base) cylinder(r=2, h=0.00001, center=true);}", 
				testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void roundShouldRenderWithMinkowskiYZ() {
		Abstract3dModel testSubject = new Test3dModel("(base)")
				.round(Plane.YZ, 3.0);
		assertEqualsWithoutWhiteSpaces("minkowski() {(base) rotate([0,90,0]) cylinder(r=3, h=0.00001, center=true);}", 
				testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void roundShouldRenderWithMinkowskiXZ() {
		Abstract3dModel testSubject = new Test3dModel("(base)")
				.round(Plane.XZ, 1.5);
		assertEqualsWithoutWhiteSpaces("minkowski() {(base) rotate([90,0,0]) cylinder(r=1.5, h=0.00001, center=true);}", 
				testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void roundShouldRenderWithMinkowskiAll() {
		Abstract3dModel testSubject = new Test3dModel("(base)")
				.round(Plane.ALL, 3.3);
		assertEqualsWithoutWhiteSpaces("minkowski() {(base) sphere(r=3.3);}", 
				testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void roundShouldIncreaseTheSizeXY() {
		Abstract3dModel testSubject = new Test3dModel("(base)", TEST_BOUNDARIES)
				.round(Plane.XY, 2.0);
		Boundaries3d boundaries = testSubject.getBoundaries();
		assertDoubleEquals(TEST_BOUNDARIES.getX().getMin()-2.0, boundaries.getX().getMin());
		assertDoubleEquals(TEST_BOUNDARIES.getX().getMax()+2.0, boundaries.getX().getMax());
		assertDoubleEquals(TEST_BOUNDARIES.getY().getMin()-2.0, boundaries.getY().getMin());
		assertDoubleEquals(TEST_BOUNDARIES.getY().getMax()+2.0, boundaries.getY().getMax());
		
		BoundaryTest.assertBoundaryEquals(TEST_BOUNDARIES.getZ(), boundaries.getZ());
	}
	
	@Test
	public void roundShouldIncreaseTheSizeYZ() {
		Abstract3dModel testSubject = new Test3dModel("(base)", TEST_BOUNDARIES)
				.round(Plane.YZ, 3.3);
		Boundaries3d boundaries = testSubject.getBoundaries();

		BoundaryTest.assertBoundaryEquals(TEST_BOUNDARIES.getX(), boundaries.getX());
		
		assertDoubleEquals(TEST_BOUNDARIES.getY().getMin()-3.3, boundaries.getY().getMin());
		assertDoubleEquals(TEST_BOUNDARIES.getY().getMax()+3.3, boundaries.getY().getMax());
		assertDoubleEquals(TEST_BOUNDARIES.getZ().getMin()-3.3, boundaries.getZ().getMin());
		assertDoubleEquals(TEST_BOUNDARIES.getZ().getMax()+3.3, boundaries.getZ().getMax());
	}
	
	@Test
	public void roundShouldIncreaseTheSizeXZ() {
		Abstract3dModel testSubject = new Test3dModel("(base)", TEST_BOUNDARIES)
				.round(Plane.XZ, 5.3);
		Boundaries3d boundaries = testSubject.getBoundaries();
		
		assertDoubleEquals(TEST_BOUNDARIES.getX().getMin()-5.3, boundaries.getX().getMin());
		assertDoubleEquals(TEST_BOUNDARIES.getX().getMax()+5.3, boundaries.getX().getMax());
		
		BoundaryTest.assertBoundaryEquals(TEST_BOUNDARIES.getY(), boundaries.getY());
		
		assertDoubleEquals(TEST_BOUNDARIES.getZ().getMin()-5.3, boundaries.getZ().getMin());
		assertDoubleEquals(TEST_BOUNDARIES.getZ().getMax()+5.3, boundaries.getZ().getMax());
	}
	
	@Test
	public void roundShouldIncreaseTheSizeALL() {
		Abstract3dModel testSubject = new Test3dModel("(base)", TEST_BOUNDARIES)
				.round(Plane.ALL, 1.9);
		Boundaries3d boundaries = testSubject.getBoundaries();
		
		assertDoubleEquals(TEST_BOUNDARIES.getX().getMin()-1.9, boundaries.getX().getMin());
		assertDoubleEquals(TEST_BOUNDARIES.getX().getMax()+1.9, boundaries.getX().getMax());
		assertDoubleEquals(TEST_BOUNDARIES.getY().getMin()-1.9, boundaries.getY().getMin());
		assertDoubleEquals(TEST_BOUNDARIES.getY().getMax()+1.9, boundaries.getY().getMax());
		assertDoubleEquals(TEST_BOUNDARIES.getZ().getMin()-1.9, boundaries.getZ().getMin());
		assertDoubleEquals(TEST_BOUNDARIES.getZ().getMax()+1.9, boundaries.getZ().getMax());
	}
	
	@Test
	public void roundShouldIncreaseTheSizeAll() {
		Abstract3dModel testSubject = new Test3dModel("(base)")
				.round(Plane.ALL, 3.3);
		assertEqualsWithoutWhiteSpaces("minkowski() {(base) sphere(r=3.3);}", 
				testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}

	@Test(expected = IllegalValueException.class)
	public void negativeRadiusShouldThrowException() {
		Abstract3dModel testSubject = new Test3dModel("(base)");
		testSubject.round(Plane.ALL, -2.0);
	}
}
