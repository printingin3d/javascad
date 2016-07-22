package eu.printingin3d.javascad.tranzitions;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class ScaleTest {
	private static final double EPSILON = 0.001;

	@Test
	public void testToScad() {
		assertEqualsWithoutWhiteSpaces("scale([1,1.5,3.1416]) (model)", 
				new Scale(Test3dModel.DEFAULT, new Coords3d(1, 1.5, Math.PI)));
	}
	
	@Test
	public void testToScadWithIdentScale() {
		assertEqualsWithoutWhiteSpaces("(model)", 
				new Scale(Test3dModel.DEFAULT, new Coords3d(1, 1, 1)));
	}

	@Test
	public void testBoundaries() {
		Boundaries3d boundaries = new Boundaries3d(
				new Boundary(50.2, 13.3), 
				new Boundary(18.3, 78.3), 
				new Boundary(10.0, 43.2));
		Scale scale = new Scale(new Test3dModel("(model)", boundaries), new Coords3d(0.3, 2, 8));
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
		Scale scale = new Scale(new Test3dModel("(model)", boundaries), new Coords3d(1.0, 1.0, 1.0));
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
		new Scale(Test3dModel.DEFAULT, null);
	}
	
	@Test
	public void subModuleShouldInclude() {
		Abstract3dModel testSubject = new Scale(
				new Test3dModel("(model12)").withTag(12),
				new Coords3d(10, 20, 30)
				).withTag(11);
		
		assertEqualsWithoutWhiteSpaces("scale([10,20,30]) (model12)", 
				testSubject.subModel(new ScadGenerationContextFactory().include(12).create()));
	}
	
	@Test
	public void subModuleShouldInclude2() {
		Abstract3dModel testSubject = new Scale(
				new Test3dModel("(model12)").withTag(12),
				new Coords3d(10, 20, 30)
				).withTag(11);
		
		assertEqualsWithoutWhiteSpaces("scale([10,20,30]) (model12)",
				testSubject.subModel(new ScadGenerationContextFactory().include(11).create()));
	}
	
	@Test
	public void subModuleShouldExclude() {
		Abstract3dModel testSubject = new Scale(
				new Test3dModel("(model12)").withTag(12),
				new Coords3d(10, 20, 30)
				).withTag(11);
		
		Assert.assertFalse(testSubject.subModel(new ScadGenerationContextFactory().exclude(11).create()).isPresent());
	}
	
	@Test
	public void subModuleShouldExclude2() {
		Abstract3dModel testSubject = new Scale(
				new Test3dModel("(model12)").withTag(12),
				new Coords3d(10, 20, 30)
				).withTag(11);
		
		Assert.assertFalse(testSubject.subModel(new ScadGenerationContextFactory().exclude(12).create()).isPresent());
	}

}
