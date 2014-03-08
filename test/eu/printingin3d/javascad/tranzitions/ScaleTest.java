package eu.printingin3d.javascad.tranzitions;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.testutils.TestModel;

public class ScaleTest {
	private static final double EPSILON = 0.001;
	
	@Before
	public void init() {
		Language.OpenSCAD.setCurrent();
	}

	@Test
	public void testToScad() {
		assertEqualsWithoutWhiteSpaces("scale([1,1.5,3.1416]) (model)", new Scale(TestModel.DEFAULT, new Coords3d(1, 1.5, Math.PI)).toScad());
	}
	
	@Test
	public void testToPovRay() {
		Language.POVRay.setCurrent();
		
		Scale scale = new Scale(TestModel.DEFAULT, new Coords3d(1, 1.5, Math.PI));
		assertEqualsWithoutWhiteSpaces("object { (model) scale <1, 1.5, 3.1416> #attributes }", scale.innerToScad());
	}
	
	@Test
	public void testToScadWithIdentScale() {
		assertEqualsWithoutWhiteSpaces("(model)", new Scale(TestModel.DEFAULT, new Coords3d(1, 1, 1)).toScad());
	}

	@Test
	public void testBoundaries() {
		Boundaries3d boundaries = new Boundaries3d(
				new Boundary(50.2, 13.3), 
				new Boundary(18.3, 78.3), 
				new Boundary(10.0, 43.2));
		Scale scale = new Scale(new TestModel("(model)", boundaries), new Coords3d(0.3, 2, 8));
		Boundaries3d newBoundaries = scale.getBoundaries();
		
		Assert.assertEquals(  3.99, newBoundaries.getX().getMin(), EPSILON);
		Assert.assertEquals( 15.06, newBoundaries.getX().getMax(), EPSILON);
		Assert.assertEquals( 36.60, newBoundaries.getY().getMin(), EPSILON);
		Assert.assertEquals(156.60, newBoundaries.getY().getMax(), EPSILON);
		Assert.assertEquals( 80.00, newBoundaries.getZ().getMin(), EPSILON);
		Assert.assertEquals(345.60, newBoundaries.getZ().getMax(), EPSILON);
	}
	
	@Test
	public void testBoundariesWithIdent() {
		Boundaries3d boundaries = new Boundaries3d(
				new Boundary(50.2, 13.3), 
				new Boundary(18.3, 78.3), 
				new Boundary(10.0, 43.2));
		Scale scale = new Scale(new TestModel("(model)", boundaries), new Coords3d(1.0, 1.0, 1.0));
		Boundaries3d newBoundaries = scale.getBoundaries();
		
		Assert.assertEquals(boundaries.getX().getMin(), newBoundaries.getX().getMin(), EPSILON);
		Assert.assertEquals(boundaries.getX().getMax(), newBoundaries.getX().getMax(), EPSILON);
		Assert.assertEquals(boundaries.getY().getMin(), newBoundaries.getY().getMin(), EPSILON);
		Assert.assertEquals(boundaries.getY().getMax(), newBoundaries.getY().getMax(), EPSILON);
		Assert.assertEquals(boundaries.getZ().getMin(), newBoundaries.getZ().getMin(), EPSILON);
		Assert.assertEquals(boundaries.getZ().getMax(), newBoundaries.getZ().getMax(), EPSILON);
	}
	
	@Test(expected=IllegalValueException.class)
	public void shouldThrowIllegalValueExceptionIfModelIsNull() {
		new Scale(null, Coords3d.ZERO);
	}
	
	@Test(expected=IllegalValueException.class)
	public void shouldThrowIllegalValueExceptionIfScaleIsNull() {
		new Scale(TestModel.DEFAULT, null);
	}
}
