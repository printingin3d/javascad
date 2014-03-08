package eu.printingin3d.javascad.tranzitions;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.testutils.TestModel;

public class IntersectionTest {
	
	@Before
	public void init() {
		Language.OpenSCAD.setCurrent();
	}

	@Test
	public void testToScad() {
		Intersection intersection = new Intersection(new TestModel("(model1)"), new TestModel("(model2)"));
		assertEqualsWithoutWhiteSpaces("intersection() {(model1) (model2)}", intersection.toScad());
	}
	
	@Test
	public void testToPovRay() {
		Language.POVRay.setCurrent();
		
		Intersection intersection = new Intersection(new TestModel("(model1)"), new TestModel("(model2)"));
		assertEqualsWithoutWhiteSpaces("intersection {(model1) (model2) #attributes}", intersection.innerToScad());
	}
	
	@Test
	public void testToScadWithOnlyOneModel() {
		Intersection intersection = new Intersection(new TestModel("(model1)"));
		assertEqualsWithoutWhiteSpaces("(model1)", intersection.toScad());
	}
	
	@Test
	public void testToScadWithNoModel() {
		Intersection intersection = new Intersection();
		assertEqualsWithoutWhiteSpaces("", intersection.toScad());
	}

	
	@Test
	public void boundariesShouldBeTheLowestAndHighestValueOfTheList() {
		TestModel model1 = new TestModel("(model1)", new Boundaries3d(
				new Boundary(10.2, 50.8), 
				new Boundary(10.2, 50.8), 
				new Boundary(10.2, 50.8)));
		TestModel model2 = new TestModel("(model2)", new Boundaries3d(
				new Boundary(20.9, 51.1), 
				new Boundary(20.9, 51.1), 
				new Boundary(20.9, 51.1)));
		Intersection intersect = new Intersection(Arrays.<Abstract3dModel>asList(model1, model2));
		Boundaries3d boundaries = intersect.getBoundaries();
		
		assertDoubleEquals(20.9, boundaries.getX().getMin());
		assertDoubleEquals(50.8, boundaries.getX().getMax());
		assertDoubleEquals(20.9, boundaries.getY().getMin());
		assertDoubleEquals(50.8, boundaries.getY().getMax());
		assertDoubleEquals(20.9, boundaries.getZ().getMin());
		assertDoubleEquals(50.8, boundaries.getZ().getMax());
	}
}
