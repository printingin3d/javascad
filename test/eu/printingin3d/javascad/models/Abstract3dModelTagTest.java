package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
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
				testSubject.withTag(11).toScad(ScadGenerationContextFactory.excludeThese(Arrays.asList(11))).getScad());
	}
	
	@Test
	public void testExclude2() {
		testSubject = new Union(
				new Test3dModel("(model11)").withTag(11),
				new Test3dModel("(model12)").withTag(12)
				);
		
		assertEqualsWithoutWhiteSpaces("union() {(model12)}", 
				testSubject.toScad(ScadGenerationContextFactory.excludeThese(Arrays.asList(11))).getScad());
	}
	
	@Test
	public void testInclude() {
		testSubject = new Union(
				new Test3dModel("(model11)").withTag(11),
				new Test3dModel("(model12)").withTag(12)
				);
		
		assertEqualsWithoutWhiteSpaces("union() {(model12)}", 
				testSubject.toScad(ScadGenerationContextFactory.includeThese(Arrays.asList(12))).getScad());
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
}
