package eu.printingin3d.javascad.tranzitions;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import java.util.Arrays;

import org.junit.Test;

import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class IntersectionTest {

	@Test
	public void testToScad() {
		Intersection intersection = new Intersection(new Test3dModel("(model1)"), new Test3dModel("(model2)"));
		assertEqualsWithoutWhiteSpaces("intersection() {(model1) (model2)}", 
				intersection.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testToScadWithOnlyOneModel() {
		Intersection intersection = new Intersection(new Test3dModel("(model1)"));
		assertEqualsWithoutWhiteSpaces("(model1)", intersection.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testToScadWithNoModel() {
		Intersection intersection = new Intersection();
		assertEqualsWithoutWhiteSpaces("", intersection.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
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
		Intersection intersect = new Intersection(Arrays.<Abstract3dModel>asList(model1, model2));
		Boundaries3d boundaries = intersect.getBoundaries();
		
		assertDoubleEquals(20.9, boundaries.getX().getMin());
		assertDoubleEquals(50.8, boundaries.getX().getMax());
		assertDoubleEquals(20.9, boundaries.getY().getMin());
		assertDoubleEquals(50.8, boundaries.getY().getMax());
		assertDoubleEquals(20.9, boundaries.getZ().getMin());
		assertDoubleEquals(50.8, boundaries.getZ().getMax());
	}
	
	@Test
	public void subModuleShouldInclude() {
		Abstract3dModel testSubject = new Intersection(
				new Test3dModel("(model11)").withTag(11),
				new Test3dModel("(model12)").withTag(12)
				);
		
		assertEqualsWithoutWhiteSpaces("(model12)", 
				testSubject.subModel(new ScadGenerationContextFactory().include(12).create()).toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void subModuleShouldExclude() {
		Abstract3dModel testSubject = new Intersection(
				new Test3dModel("(model11)").withTag(11),
				new Test3dModel("(model12)").withTag(12)
				);
		
		assertEqualsWithoutWhiteSpaces("(model12)", 
				testSubject.subModel(new ScadGenerationContextFactory().exclude(11).create()).toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}

	@Test
	public void subModuleIncludeExclude() {
		Abstract3dModel testSubject = 
			new Intersection(
				new Intersection(
					new Test3dModel("(model11)").withTag(11),
					new Test3dModel("(model12)").withTag(12)
				).withTag(1),
				new Test3dModel("(model2)").withTag(2)
			);
		
		assertEqualsWithoutWhiteSpaces("(model11)", 
				testSubject.subModel(new ScadGenerationContextFactory()
						.include(1)
						.exclude(12)
						.create()).toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
}
