package eu.printingin3d.javascad.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import eu.printingin3d.javascad.batchtests.CloneModelTest;
import eu.printingin3d.javascad.batchtests.CloneModelTest.TestCase;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.testutils.AssertEx;

@RunWith(Parameterized.class)
public class BatchPovRayImplementationTest {
	private final Abstract3dModel model;
	private final boolean expectSimpleResult;

	public BatchPovRayImplementationTest(String name, Abstract3dModel model, boolean expectSimpleResult) {
		this.model = model;
		this.expectSimpleResult = expectSimpleResult;
	}

	@Parameterized.Parameters(name="{0} {index}")
	public static Collection<Object[]> testCases() {
		List<Object[]> result = new ArrayList<Object[]>();
		for (TestCase TestCase : CloneModelTest.createTestSubjects()) {
			result.add(new Object[] {TestCase.getModel().getClass().getSimpleName(), TestCase.getModel(), Boolean.valueOf(TestCase.isExpectSimpleResult())});
		}
		return result;
	}

	@Before
	public void init() {
		Language.POVRay.setCurrent();
	}
	
	@Test
	public void testImplementation() {
		String str = model.innerToScad();
		
		if (expectSimpleResult) {
			Assert.assertFalse(str.contains(Abstract3dModel.ATTRIBUTES_PLACEHOLDER));
		}
		else {
			Assert.assertTrue(str.contains(Abstract3dModel.ATTRIBUTES_PLACEHOLDER));
		}
	}
	
	@Test
	public void testBasicFormat() {
		String str = model.innerToScad();
		
		AssertEx.assertMatchToExpressionWithoutWhiteSpaces("([a-z]+\\{.+\\})?", str);
	}
}
