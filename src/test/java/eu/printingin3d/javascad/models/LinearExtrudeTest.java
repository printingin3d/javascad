package eu.printingin3d.javascad.models;

import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.testutils.AssertEx;
import eu.printingin3d.javascad.testutils.Test2dModel;

public class LinearExtrudeTest {
	@Test
	public void testToScad() {
		Abstract3dModel testSubject = new LinearExtrude(new Test2dModel("(model)", 
				new Boundaries2d(new Boundary(-5, 5), new Boundary(-5, 5))), 10, 20, 2);
		AssertEx.assertEqualsWithoutWhiteSpaces("linear_extrude(height=10, center=true, convexity=10, twist=20, scale=2) (model)", 
				testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testBoundariesWithZeroTwist() {
		Abstract3dModel testSubject = new LinearExtrude(new Test2dModel("(model)", 
				new Boundaries2d(new Boundary(-5, 0), new Boundary(-10, 5))), 30, 0, 2);
		Boundaries3dTest.assertBoundariesEquals(
				new Boundaries3d(new Coords3d(-5, -10, -15), new Coords3d(0, 5, 15)), testSubject.getBoundaries());
	}
	
	@Test
	public void testBoundariesWithNonZeroTwist() {
		Abstract3dModel testSubject = new LinearExtrude(new Test2dModel("(model)", 
				new Boundaries2d(new Boundary(-5, 0), new Boundary(-10, 5))), 30, 20, 2);
		Boundaries3dTest.assertBoundariesEquals(
				new Boundaries3d(new Coords3d(-10, -10, -15), new Coords3d(10, 10, 15)), testSubject.getBoundaries());
	}
}
