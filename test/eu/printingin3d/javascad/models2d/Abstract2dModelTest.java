package eu.printingin3d.javascad.models2d;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.models.ScadGenerationContext;
import eu.printingin3d.javascad.testutils.AssertEx;
import eu.printingin3d.javascad.testutils.Test2dModel;

public class Abstract2dModelTest {
	private Abstract2dModel testSubject;
	
	@Before
	public void init() {
		testSubject = new Test2dModel("(model)");
	}
	
	@Test
	public void shouldReturnWithTheInnerScadValue() {
		AssertEx.assertEqualsWithoutWhiteSpaces("(model)", testSubject.toScad(ScadGenerationContext.DEFAULT));
	}
}
