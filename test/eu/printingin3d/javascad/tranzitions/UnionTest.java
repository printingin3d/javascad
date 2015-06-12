package eu.printingin3d.javascad.tranzitions;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class UnionTest {

	@Test
	public void testUnion1() {
		Union union = new Union(new Test3dModel("(model1)"), new Test3dModel("(model2)"));
		assertEqualsWithoutWhiteSpaces("union() {(model1) (model2)}", 
				union.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testUnion2() {
		Union union = new Union(Arrays.<Abstract3dModel>asList(new Test3dModel("(model1)"), new Test3dModel("(model2)")));
		assertEqualsWithoutWhiteSpaces("union() {(model1) (model2)}", 
				union.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testUnionWithNullList() {
		List<Abstract3dModel> models = null;
		Union union = new Union(models);
		assertEqualsWithoutWhiteSpaces("", union.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void unionShouldHandleNullElementsInTheList() {
		Union union = new Union(Arrays.<Abstract3dModel>asList(
				new Test3dModel("(model1)"), 
				null, 
				new Test3dModel("(model2)")));
		assertEqualsWithoutWhiteSpaces("union() {(model1) (model2)}", union.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testUnionWithOnlyOneModel() {
		Union union = new Union(Arrays.<Abstract3dModel>asList(new Test3dModel("(model1)")));
		assertEqualsWithoutWhiteSpaces("(model1)", union.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testUnionWithEmptyList() {
		Union union = new Union(Collections.<Abstract3dModel>emptyList());
		assertEqualsWithoutWhiteSpaces("", union.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testUnionWithDoubleEmptyList() {
		Union union = new Union(new Union(Collections.<Abstract3dModel>emptyList()), new Union(Collections.<Abstract3dModel>emptyList()));
		assertEqualsWithoutWhiteSpaces("", union.toScad(ColorHandlingContext.DEFAULT).getScad());
	}

	@Test
	public void boundariesShouldReturnZeroBoundary() {
		Union union = new Union(Collections.<Abstract3dModel>emptyList());
		Boundary b = union.getBoundaries().getZ();
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
		Union union = new Union(Arrays.<Abstract3dModel>asList(model1, model2));
		Boundaries3d boundaries = union.getBoundaries();
		
		assertDoubleEquals(10.2, boundaries.getX().getMin());
		assertDoubleEquals(51.1, boundaries.getX().getMax());
		assertDoubleEquals(10.2, boundaries.getY().getMin());
		assertDoubleEquals(51.1, boundaries.getY().getMax());
		assertDoubleEquals(10.2, boundaries.getZ().getMin());
		assertDoubleEquals(51.1, boundaries.getZ().getMax());
	}
	
	@Test
	public void addModelShouldReturnWithMoreUnion() {
		assertEqualsWithoutWhiteSpaces("union() {(model1) (model2) (model3)}", 
				new Union(new Test3dModel("(model1)"), new Test3dModel("(model2)")).addModel(new Test3dModel("(model3)"))
						.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void addModelShouldKeepMoves() {
		assertEqualsWithoutWhiteSpaces("union() {translate([10,20,30]) union() {(model1) (model2)} (model3)}", 
				new Union(new Test3dModel("(model1)"), new Test3dModel("(model2)"))
						.move(new Coords3d(10, 20, 30))
						.addModel(new Test3dModel("(model3)"))
				.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void addModelShouldKeepRotate() {
		assertEqualsWithoutWhiteSpaces("union() {rotate([10,20,30]) union() {(model1) (model2)} (model3)}", 
				new Union(new Test3dModel("(model1)"), new Test3dModel("(model2)"))
						.rotate(new Angles3d(10, 20, 30))
						.addModel(new Test3dModel("(model3)"))
				.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void subModuleShouldInclude() {
		Abstract3dModel testSubject = new Union(
				new Test3dModel("(model11)").withTag(11),
				new Test3dModel("(model12)").withTag(12)
				);
		
		assertEqualsWithoutWhiteSpaces("(model12)", 
				testSubject.subModel(new ScadGenerationContextFactory().include(12).create()).toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void subModuleShouldExclude() {
		Abstract3dModel testSubject = new Union(
				new Test3dModel("(model11)").withTag(11),
				new Test3dModel("(model12)").withTag(12)
				);
		
		assertEqualsWithoutWhiteSpaces("(model12)", 
				testSubject.subModel(new ScadGenerationContextFactory().exclude(11).create()).toScad(ColorHandlingContext.DEFAULT).getScad());
	}

	@Test
	public void subModuleIncludeExclude() {
		Abstract3dModel testSubject = 
			new Union(
				new Union(
					new Test3dModel("(model11)").withTag(11),
					new Test3dModel("(model12)").withTag(12)
				).withTag(1),
				new Test3dModel("(model2)").withTag(2)
			);
		
		assertEqualsWithoutWhiteSpaces("(model11)", 
				testSubject.subModel(new ScadGenerationContextFactory()
						.include(1)
						.exclude(12)
						.create()).toScad(ColorHandlingContext.DEFAULT).getScad());
	}
}
