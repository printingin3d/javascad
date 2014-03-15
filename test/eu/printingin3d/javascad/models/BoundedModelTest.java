package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.BoundedModel;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class BoundedModelTest {
	
	@Test
	public void testInnerToScad() {
		Test3dModel baseModel = new Test3dModel("(model)");
		BoundedModel testSubject = new BoundedModel(baseModel, null);
		Assert.assertEquals(baseModel.toScad(), testSubject.toScad());
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
		BoundedModel testSubject = new BoundedModel(baseModel, null);
		assertEqualsWithoutWhiteSpaces("translate([10,20,30])(model)", testSubject.toScad());
	}

	@Test
	public void testWithNullModel() {
		BoundedModel testSubject = new BoundedModel(null, null);
		assertEqualsWithoutWhiteSpaces("", testSubject.toScad());
	}
}
