package eu.printingin3d.javascad.tranzitions;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class RotateTest {
	
	@Test
	public void testGetRotate() {
		assertEqualsWithoutWhiteSpaces("rotate([10,20,30])", Rotate.getRotate(new Angles3d(10, 20, 30)));
	}
	
	@Test
	public void testToScad() {
		assertEqualsWithoutWhiteSpaces("rotate([5,100,50]) (model)", 
				new Rotate(new Test3dModel("(model)"), 
						new Angles3d(5, 100, 50)).toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testBoundary() {
		Angles3d angle = RandomUtils.getRandomAngle();
		Boundaries3d boundaries = RandomUtils.getRandomBoundaries();
		Rotate rotate = new Rotate(new Test3dModel("(model)", boundaries), angle);
		Boundaries3dTest.assertBoundariesEquals(boundaries.rotate(angle), rotate.getBoundaries());
	}
	
	@Test(expected = IllegalValueException.class)
	public void shouldThrowExceptionIfModelIsNull() {
		new Rotate(null, new Angles3d(5, 100, 50));
	}
	
	@Test(expected = IllegalValueException.class)
	public void shouldThrowExceptionIfAnglesIsNull() {
		new Rotate(new Test3dModel("(model)"), null);
	}
	
	@Test
	public void subModuleShouldInclude() {
		Abstract3dModel testSubject = new Rotate(
				new Test3dModel("(model11)").withTag(11),
				Angles3d.ROTATE_MINUS_X
				).withTag(12);
		
		assertEqualsWithoutWhiteSpaces("rotate([-90,0,0]) (model11)",  
				testSubject.subModel(new ScadGenerationContextFactory().include(12).create()).toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void subModuleShouldInclude2() {
		Abstract3dModel testSubject = new Rotate(
				new Test3dModel("(model11)").withTag(11),
				Angles3d.ROTATE_MINUS_X
				).withTag(12);
		
		assertEqualsWithoutWhiteSpaces("rotate([-90,0,0]) (model11)",  
				testSubject.subModel(new ScadGenerationContextFactory().include(11).create()).toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void subModuleShouldExclude() {
		Abstract3dModel testSubject = new Rotate(
				new Test3dModel("(model11)").withTag(11),
				Angles3d.ROTATE_MINUS_X
				).withTag(12);
		
		Assert.assertNull(testSubject.subModel(new ScadGenerationContextFactory().exclude(11).create()));
	}
	
	@Test
	public void subModuleShouldExclude2() {
		Abstract3dModel testSubject = new Rotate(
				new Test3dModel("(model11)").withTag(11),
				Angles3d.ROTATE_MINUS_X
				).withTag(12);
		
		Assert.assertNull(testSubject.subModel(new ScadGenerationContextFactory().exclude(12).create()));
	}
}
