package eu.printingin3d.javascad.tranzitions;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import java.awt.Color;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.testutils.AssertEx;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class ColorizeTest {
	@Test(expected=IllegalValueException.class)
	public void testNullModel() {
		new Colorize(Color.BLACK, null);		
	}
	
	@Test(expected=IllegalValueException.class)
	public void testNullColor() {
		new Colorize(null, new Test3dModel("(model)"));		
	}
	
	@Test
	public void testWithoutAlpha() {
		Color color = new Color(100, 120, 200);
		AssertEx.assertEqualsWithoutWhiteSpaces("color([0.3922, 0.4706, 0.7843]) (model)", 
				new Colorize(color, new Test3dModel("(model)")).toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testWithAlpha() {
		Color color = new Color(100, 120, 200, 55);
		AssertEx.assertEqualsWithoutWhiteSpaces("color([0.3922, 0.4706, 0.7843, 0.2157]) (model)", 
				new Colorize(color, new Test3dModel("(model)")).toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void subModuleShouldInclude() {
		Abstract3dModel testSubject = new Colorize(
				Color.BLACK,
				new Test3dModel("(model12)").withTag(12)
				).withTag(11);
		
		assertEqualsWithoutWhiteSpaces("color([0,0,0]) (model12)", 
				testSubject.subModel(new ScadGenerationContextFactory().include(12).create()).toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void subModuleShouldInclude2() {
		Abstract3dModel testSubject = new Colorize(
				Color.BLACK,
				new Test3dModel("(model12)").withTag(12)
				).withTag(11);
		
		assertEqualsWithoutWhiteSpaces("color([0,0,0]) (model12)", 
				testSubject.subModel(new ScadGenerationContextFactory().include(11).create()).toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void subModuleShouldExclude() {
		Abstract3dModel testSubject = new Colorize(
				Color.BLACK,
				new Test3dModel("(model12)").withTag(12)
				).withTag(11);
		
		Assert.assertNull(testSubject.subModel(new ScadGenerationContextFactory().exclude(11).create()));
	}
	
	@Test
	public void subModuleShouldExclude2() {
		Abstract3dModel testSubject = new Colorize(
				Color.BLACK,
				new Test3dModel("(model12)").withTag(12)
				).withTag(11);
		
		Assert.assertNull(testSubject.subModel(new ScadGenerationContextFactory().exclude(12).create()));
	}

}
