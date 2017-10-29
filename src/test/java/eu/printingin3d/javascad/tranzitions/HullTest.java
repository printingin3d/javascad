package eu.printingin3d.javascad.tranzitions;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class HullTest {

	@Test
	public void testHull1() {
		Hull Hull = new Hull(new Test3dModel("(model1)"), new Test3dModel("(model2)"));
		assertEqualsWithoutWhiteSpaces("hull() {(model1) (model2)}", 
				Hull);
	}
	
	@Test
	public void testHull2() {
		Hull Hull = new Hull(Arrays.<Abstract3dModel>asList(new Test3dModel("(model1)"), new Test3dModel("(model2)")));
		assertEqualsWithoutWhiteSpaces("hull() {(model1) (model2)}", 
				Hull);
	}
	
	@Test
	public void testHullWithNullList() {
		List<Abstract3dModel> models = null;
		Hull Hull = new Hull(models);
		assertEqualsWithoutWhiteSpaces("", Hull);
	}
	
	@Test
	public void HullShouldHandleNullElementsInTheList() {
		Hull Hull = new Hull(Arrays.<Abstract3dModel>asList(
				new Test3dModel("(model1)"), 
				null, 
				new Test3dModel("(model2)")));
		assertEqualsWithoutWhiteSpaces("hull() {(model1) (model2)}", Hull);
	}
	
	@Test
	public void testHullWithOnlyOneModel() {
		Hull Hull = new Hull(Arrays.<Abstract3dModel>asList(new Test3dModel("(model1)")));
		assertEqualsWithoutWhiteSpaces("(model1)", Hull);
	}
	
	@Test
	public void testHullWithEmptyList() {
		Hull Hull = new Hull(Collections.<Abstract3dModel>emptyList());
		assertEqualsWithoutWhiteSpaces("", Hull);
	}
	
	@Test
	public void testHullWithDoubleEmptyList() {
		Hull Hull = new Hull(new Hull(Collections.<Abstract3dModel>emptyList()), new Hull(Collections.<Abstract3dModel>emptyList()));
		assertEqualsWithoutWhiteSpaces("", Hull);
	}

	@Test
	public void boundariesShouldReturnZeroBoundary() {
		Hull Hull = new Hull(Collections.<Abstract3dModel>emptyList());
		Boundary b = Hull.getBoundaries().getZ();
		assertDoubleEquals(0.0, b.getMin());
		assertDoubleEquals(0.0, b.getMax());
	}
	
	@Test
	public void boundariesShouldBeTheLowestAndHighestValueOfTheList() {
		Test3dModel model1 = new Test3dModel("(model1)", new Boundaries3d(
				new Boundary(10.2, 50.8), 
				new Boundary(10.2, 50.8), 
				new Boundary(10.2, 50.8)));
		Test3dModel model2 = new Test3dModel("(model2)", new Boundaries3d(
				new Boundary(20.9, 51.1), 
				new Boundary(20.9, 51.1), 
				new Boundary(20.9, 51.1)));
		Hull Hull = new Hull(Arrays.<Abstract3dModel>asList(model1, model2));
		Boundaries3d boundaries = Hull.getBoundaries();
		
		assertDoubleEquals(10.2, boundaries.getX().getMin());
		assertDoubleEquals(51.1, boundaries.getX().getMax());
		assertDoubleEquals(10.2, boundaries.getY().getMin());
		assertDoubleEquals(51.1, boundaries.getY().getMax());
		assertDoubleEquals(10.2, boundaries.getZ().getMin());
		assertDoubleEquals(51.1, boundaries.getZ().getMax());
	}
	
	@Test
	public void subModuleShouldInclude() {
		Abstract3dModel testSubject = new Hull(
				new Test3dModel("(model11)").withTag(11),
				new Test3dModel("(model12)").withTag(12)
				);
		
		assertEqualsWithoutWhiteSpaces("(model12)", 
				testSubject.subModel(new ScadGenerationContextFactory().include(12).create()));
	}
	
	@Test
	public void subModuleShouldExclude() {
		Abstract3dModel testSubject = new Hull(
				new Test3dModel("(model11)").withTag(11),
				new Test3dModel("(model12)").withTag(12)
				);
		
		assertEqualsWithoutWhiteSpaces("(model12)", 
				testSubject.subModel(new ScadGenerationContextFactory().exclude(11).create()));
	}

	@Test
	public void subModuleIncludeExclude() {
		Abstract3dModel testSubject = 
			new Hull(
				new Hull(
					new Test3dModel("(model11)").withTag(11),
					new Test3dModel("(model12)").withTag(12)
				).withTag(1),
				new Test3dModel("(model2)").withTag(2)
			);
		
		assertEqualsWithoutWhiteSpaces("(model11)", 
				testSubject.subModel(new ScadGenerationContextFactory()
						.include(1)
						.exclude(12)
						.create()));
	}
}
