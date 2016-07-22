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

public class TranslateTest {
	private static final double EPSILON = 0.001;

	@Test
	public void testGetTranslate() {
		assertEqualsWithoutWhiteSpaces("translate([10,20,30])", Translate.getTranslate(new Coords3d(10, 20, 30)));
	}
	
	@Test
	public void getTranslateShouldReturnAnEmptyStringIfMoveIsZero() {
		assertEqualsWithoutWhiteSpaces("", Translate.getTranslate(Coords3d.ZERO));
	}
	
	@Test
	public void testToScad() {
		Translate translate = new Translate(new Test3dModel("(model)"), new Coords3d(30, 20, 10));
		assertEqualsWithoutWhiteSpaces("translate([30,20,10]) (model)", translate);
	}
	
	@Test
	public void toScadWithZeroMoveShouldDoNothing() {
		Translate translate = new Translate(new Test3dModel("(model)"), Coords3d.ZERO);
		assertEqualsWithoutWhiteSpaces("(model)", translate);
	}
	
	@Test
	public void testBoundariesWhenMoveIsZero() {
		Boundaries3d boundaries = new Boundaries3d(
				new Boundary(50.2, 13.3), 
				new Boundary(18.3, 78.3), 
				new Boundary(10.0, 43.2));
		Translate translate = new Translate(new Test3dModel("(model)", boundaries), Coords3d.ZERO);
		Boundaries3d newBoundaries = translate.getBoundaries();
		
		Assert.assertEquals(13.3, newBoundaries.getX().getMin(), EPSILON);
		Assert.assertEquals(50.2, newBoundaries.getX().getMax(), EPSILON);
		Assert.assertEquals(18.3, newBoundaries.getY().getMin(), EPSILON);
		Assert.assertEquals(78.3, newBoundaries.getY().getMax(), EPSILON);
		Assert.assertEquals(10.0, newBoundaries.getZ().getMin(), EPSILON);
		Assert.assertEquals(43.2, newBoundaries.getZ().getMax(), EPSILON);
	}
	
	@Test
	public void testBoundaries() {
		Boundaries3d boundaries = new Boundaries3d(
				new Boundary(50.2, 13.3), 
				new Boundary(18.3, 78.3), 
				new Boundary(10.0, 43.2));
		Translate translate = new Translate(new Test3dModel("(model)", boundaries), new Coords3d(30, 20, 10));
		Boundaries3d newBoundaries = translate.getBoundaries();
		
		Assert.assertEquals(43.3, newBoundaries.getX().getMin(), EPSILON);
		Assert.assertEquals(80.2, newBoundaries.getX().getMax(), EPSILON);
		Assert.assertEquals(38.3, newBoundaries.getY().getMin(), EPSILON);
		Assert.assertEquals(98.3, newBoundaries.getY().getMax(), EPSILON);
		Assert.assertEquals(20.0, newBoundaries.getZ().getMin(), EPSILON);
		Assert.assertEquals(53.2, newBoundaries.getZ().getMax(), EPSILON);
	}
	
	@Test(expected=IllegalValueException.class)
	public void shouldThrowIllegalValueExceptionIfModelIsNull() {
		new Translate(null, new Coords3d(30, 20, 10));
	}
	
	@Test(expected=IllegalValueException.class)
	public void shouldThrowIllegalValueExceptionIfMoveIsNull() {
		new Translate(new Test3dModel("(model)"), null);
	}
	
	@Test
	public void subModuleShouldInclude() {
		Abstract3dModel testSubject = new Translate(
				new Test3dModel("(model11)").withTag(11),
				new Coords3d(10, 20, 30)
				).withTag(12);
		
		assertEqualsWithoutWhiteSpaces("translate([10,20,30]) (model11)",  
				testSubject.subModel(new ScadGenerationContextFactory().include(12).create()));
	}
	
	@Test
	public void subModuleShouldInclude2() {
		Abstract3dModel testSubject = new Translate(
				new Test3dModel("(model11)").withTag(11),
				new Coords3d(10, 20, 30)
				).withTag(12);
		
		assertEqualsWithoutWhiteSpaces("translate([10,20,30]) (model11)",  
				testSubject.subModel(new ScadGenerationContextFactory().include(11).create()));
	}
	
	@Test
	public void subModuleShouldExclude() {
		Abstract3dModel testSubject = new Translate(
				new Test3dModel("(model11)").withTag(11),
				new Coords3d(10, 20, 30)
				).withTag(12);
		
		Assert.assertFalse(testSubject.subModel(new ScadGenerationContextFactory().exclude(11).create()).isPresent());
	}
	
	@Test
	public void subModuleShouldExclude2() {
		Abstract3dModel testSubject = new Translate(
				new Test3dModel("(model11)").withTag(11),
				new Coords3d(10, 20, 30)
				).withTag(12);
		
		Assert.assertFalse(testSubject.subModel(new ScadGenerationContextFactory().exclude(12).create()).isPresent());
	}
}
