package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class BoundedModelTest {
	
	@Test(expected=IllegalValueException.class)
	public void modelShouldNotBeNull() {
		new BoundedModel(null, RandomUtils.getRandomBoundaries());
	}
	
	@Test(expected=IllegalValueException.class)
	public void boundariesShouldNotBeNull() {
		new BoundedModel(new Test3dModel("(model)"), null);
	}
	
	@Test
	public void testInnerToScad() {
		Test3dModel baseModel = new Test3dModel("(model)");
		BoundedModel testSubject = new BoundedModel(baseModel, RandomUtils.getRandomBoundaries());
		Assert.assertEquals(baseModel.toScad(ColorHandlingContext.DEFAULT), testSubject.toScad(ColorHandlingContext.DEFAULT));
	}
	
	@Test
	public void testBoundaries() {
		Test3dModel baseModel = new Test3dModel("(model)");
		Boundaries3d boundaries = RandomUtils.getRandomBoundaries();
		BoundedModel testSubject = new BoundedModel(baseModel, boundaries);
		Boundaries3dTest.assertBoundariesEquals(boundaries, testSubject.getBoundaries());
	}
	
	@Test
	public void testMove() {
		Abstract3dModel baseModel = new Test3dModel("(model)").move(new Coords3d(10.0, 20.0, 30.0));
		BoundedModel testSubject = new BoundedModel(baseModel, RandomUtils.getRandomBoundaries());
		assertEqualsWithoutWhiteSpaces("translate([10,20,30])(model)", 
				testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void subModuleShouldInclude() {
		Abstract3dModel testSubject = new BoundedModel(
				new Test3dModel("(model11)").withTag(11),
				RandomUtils.getRandomBoundaries()
				).withTag(12);
		
		assertEqualsWithoutWhiteSpaces("(model11)",  
				testSubject.subModel(new ScadGenerationContextFactory().include(12).create()).toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void subModuleShouldInclude2() {
		Abstract3dModel testSubject = new BoundedModel(
				new Test3dModel("(model11)").withTag(11),
				RandomUtils.getRandomBoundaries()
				).withTag(12);
		
		assertEqualsWithoutWhiteSpaces("(model11)",  
				testSubject.subModel(new ScadGenerationContextFactory().include(11).create()).toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void subModuleShouldExclude() {
		Abstract3dModel testSubject = new BoundedModel(
				new Test3dModel("(model11)").withTag(11),
				RandomUtils.getRandomBoundaries()
				).withTag(12);
		
		Assert.assertNull(testSubject.subModel(new ScadGenerationContextFactory().exclude(11).create()));
	}
	
	@Test
	public void subModuleShouldExclude2() {
		Abstract3dModel testSubject = new BoundedModel(
				new Test3dModel("(model11)").withTag(11),
				RandomUtils.getRandomBoundaries()
				).withTag(12);
		
		Assert.assertNull(testSubject.subModel(new ScadGenerationContextFactory().exclude(12).create()));
	}
}
