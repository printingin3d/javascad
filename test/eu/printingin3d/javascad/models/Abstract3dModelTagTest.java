package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.testutils.Test3dModel;

public class Abstract3dModelTagTest {
	
	private Abstract3dModel testSubject;
	
	@Before
	public void init() {
		testSubject = new Test3dModel("(empty)");
	}

	@Test
	public void testDefault() {
		assertEqualsWithoutWhiteSpaces("(empty)", testSubject.toScad(ScadGenerationContext.DEFAULT));
	}
	
	@Test
	public void testExclude() {
		assertEqualsWithoutWhiteSpaces("", 
				testSubject.withTag(11).toScad(ScadGenerationContext.excludeThese(Arrays.asList(11))));
	}

}
