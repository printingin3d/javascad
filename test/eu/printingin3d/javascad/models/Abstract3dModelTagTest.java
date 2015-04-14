package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import java.awt.Color;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.context.TagColors;
import eu.printingin3d.javascad.context.TagColorsBuilder;
import eu.printingin3d.javascad.testutils.Test3dModel;
import eu.printingin3d.javascad.tranzitions.Union;

public class Abstract3dModelTagTest {
	
	private Abstract3dModel testSubject;
	
	@Before
	public void init() {
		testSubject = new Test3dModel("(empty)");
	}

	@Test
	public void testDefault() {
		assertEqualsWithoutWhiteSpaces("(empty)", testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testExclude() {
		assertEqualsWithoutWhiteSpaces("", 
				testSubject.withTag(11).toScad(new ScadGenerationContextFactory().exclude(11).create()).getScad());
	}
	
	@Test
	public void testExclude2() {
		testSubject = new Union(
				new Test3dModel("(model11)").withTag(11),
				new Test3dModel("(model12)").withTag(12)
				);
		
		assertEqualsWithoutWhiteSpaces("union() {(model12)}", 
				testSubject.toScad(new ScadGenerationContextFactory().exclude(11).create()).getScad());
	}
	
	@Test
	public void testInclude() {
		testSubject = new Union(
				new Test3dModel("(model11)").withTag(11),
				new Test3dModel("(model12)").withTag(12)
				);
		
		assertEqualsWithoutWhiteSpaces("union() {(model12)}", 
				testSubject.toScad(new ScadGenerationContextFactory().include(12).create()).getScad());
	}

	@Test
	public void testIncludeExclude() {
		testSubject = 
			new Union(
				new Union(
					new Test3dModel("(model11)").withTag(11),
					new Test3dModel("(model12)").withTag(12)
				).withTag(1),
				new Test3dModel("(model2)").withTag(2)
			);
		
		assertEqualsWithoutWhiteSpaces("union() { union() {(model11)}}", 
				testSubject.toScad(new ScadGenerationContextFactory()
						.include(1)
						.exclude(12)
						.create()).getScad());
	}
	
	@Test
	public void testTagColors() {
		testSubject = 
				new Union(
					new Union(
						new Test3dModel("(model11)").withTag(11),
						new Test3dModel("(model12)").withTag(12)
					).withTag(1),
					new Test3dModel("(model2)").withTag(2)
				);
		
		TagColors tagColors = new TagColorsBuilder()
				.addTag(12, Color.RED)
				.buildTagColors();
		assertEqualsWithoutWhiteSpaces("union() { union() {(model11) color([1,0,0]) {(model12)}} (model2)}", 
				testSubject.toScad(new ScadGenerationContextFactory(tagColors)
						.create()).getScad());
	}
	
	@Test
	public void testTagColorsOnlyOnLeafs() {
		testSubject = 
				new Union(
						new Union(
								new Test3dModel("(model11)").withTag(11),
								new Test3dModel("(model12)").withTag(12)
								).withTag(1),
								new Test3dModel("(model2)").withTag(2)
						);
		
		TagColors tagColors = new TagColorsBuilder()
				.addTag(1, Color.GREEN)
				.buildTagColors();
		assertEqualsWithoutWhiteSpaces("union() { union() {color([0,1,0]) {(model11)} color([0,1,0]) {(model12)}} (model2)}", 
				testSubject.toScad(new ScadGenerationContextFactory(tagColors)
				.create()).getScad());
	}
	
	@Test
	public void testTagColorsLowerLevelShouldHavePriority() {
		testSubject = 
				new Union(
						new Union(
								new Test3dModel("(model11)").withTag(11),
								new Test3dModel("(model12)").withTag(12)
								).withTag(1),
								new Test3dModel("(model2)").withTag(2)
						);
		
		TagColors tagColors = new TagColorsBuilder()
				.addTag(12, Color.RED)
				.addTag(1, Color.GREEN)
				.buildTagColors();
		assertEqualsWithoutWhiteSpaces("union() { union() {color([0,1,0]) {(model11)} color([1,0,0]) {(model12)}} (model2)}", 
				testSubject.toScad(new ScadGenerationContextFactory(tagColors)
				.create()).getScad());
	}
	
	@Test
	public void subModuleShouldReturnWithTheCloneOfTheModelIfTheItemIsIncluded() {
		Abstract3dModel testSubject2 = new Test3dModel("(model)").withTag(11);
		Abstract3dModel subModel = testSubject2
				.subModel(new ScadGenerationContextFactory().include(11).create());
		assertEqualsWithoutWhiteSpaces("(model)", subModel.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
		Assert.assertNotSame(testSubject2, subModel);
	}
}
