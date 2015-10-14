package eu.printingin3d.javascad.tranzitions;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Dims3d;
import eu.printingin3d.javascad.enums.AlignType;
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Cube;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class DifferenceTest {

	@Test(expected=IllegalValueException.class)
	public void shouldThrowExceptionIfFirstModelIsNull() {
		new Difference(null, new Test3dModel("(model1)"));
	}
	
	@Test(expected=IllegalValueException.class)
	public void shouldThrowExceptionIfTheOnlyParameterIsNull() {
		new Difference(null);
	}
	
	@Test(expected=IllegalValueException.class)
	public void shouldThrowExceptionIfFirstModelIsNullWithList() {
		new Difference(null, Arrays.<Abstract3dModel>asList(new Test3dModel("(model1)")));
	}
	
	@Test
	public void testToScad() {
		Difference difference = new Difference(new Test3dModel("(model1)"), new Test3dModel("(model2)"));
		assertEqualsWithoutWhiteSpaces("difference() {(model1) (model2)}", 
				difference.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testToScadWithOneModel() {
		Difference difference = new Difference(new Test3dModel("(model1)"));
		assertEqualsWithoutWhiteSpaces("(model1)", difference.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testToScadWithModeThanOneModelToRemove() {
		Difference difference = new Difference(
				new Test3dModel("(model1)"), 
				new Test3dModel("(model2)"),
				new Test3dModel("(model3)"));
		assertEqualsWithoutWhiteSpaces("difference() {(model1) (model2) (model3)}", 
				difference.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testToScadAbsenceOfSecondModel1() {
		Abstract3dModel model2 = null;
		Difference difference = new Difference(new Test3dModel("(model1)"), model2);
		assertEqualsWithoutWhiteSpaces("(model1)", difference.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testToScadAbsenceOfSecondModel2() {
		List<Abstract3dModel> model2 = null;
		Difference difference = new Difference(new Test3dModel("(model1)"), model2);
		assertEqualsWithoutWhiteSpaces("(model1)", difference.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testToScadWithEmptyList() {
		Difference difference = new Difference(new Test3dModel("(model1)"), 
				Collections.<Abstract3dModel>emptyList());
		assertEqualsWithoutWhiteSpaces("(model1)", difference.toScad(ColorHandlingContext.DEFAULT).getScad());
	}

	@Test
	public void boundaryShouldReturnTheBoundaryOfTheFirstParam() {
		Test3dModel model1 = new Test3dModel("(model1)", new Boundaries3d(new Boundary(10.2, 50.8), new Boundary(10.2, 50.8), new Boundary(10.2, 50.8)));
		Test3dModel model2 = new Test3dModel("(model2)", new Boundaries3d(new Boundary(20.9, 51.1), new Boundary(20.9, 51.1), new Boundary(20.9, 51.1)));
		Difference difference = new Difference(model1, model2);
		Boundaries3d boundaries = difference.getBoundaries();

		assertDoubleEquals(10.2, boundaries.getX().getMin());
		assertDoubleEquals(50.8, boundaries.getX().getMax());
		assertDoubleEquals(10.2, boundaries.getY().getMin());
		assertDoubleEquals(50.8, boundaries.getY().getMax());
		assertDoubleEquals(10.2, boundaries.getZ().getMin());
		assertDoubleEquals(50.8, boundaries.getZ().getMax());
	}
	
	@Test
	public void boundaryShouldBeCalculatedWithCubes() {
		Test3dModel model1 = new Test3dModel("(model1)", new Boundaries3d(new Boundary(10.0, 20.0), new Boundary(15.0, 25.0), new Boundary(0.0, 50.0)));
		Abstract3dModel model2 = Cube.fromCoordinates(new Coords3d(5.5, 12.9, 20.9), new Coords3d(25.0, 25.1, 51.1));
		Abstract3dModel model3 = Cube.fromCoordinates(new Coords3d(5.5, 12.9, -2.0), new Coords3d(15.0, 25.1, 22.0));
		Abstract3dModel model4 = Cube.fromCoordinates(new Coords3d(10.0, 20.9, -2.0), new Coords3d(25.0, 25.1, 22.0));
		Difference difference = new Difference(model1, model2, model3, model4);
		Boundaries3d boundaries = difference.getBoundaries();
		
		assertDoubleEquals(15.0, boundaries.getX().getMin());
		assertDoubleEquals(20.0, boundaries.getX().getMax());
		assertDoubleEquals(15.0, boundaries.getY().getMin());
		assertDoubleEquals(20.9, boundaries.getY().getMax());
		assertDoubleEquals( 0.0, boundaries.getZ().getMin());
		assertDoubleEquals(20.9, boundaries.getZ().getMax());
	}
	
	@Test
	public void boundaryShouldNotBeCalculatedWithCubesIfTheCubesAreRotated() {
		Test3dModel model1 = new Test3dModel("(model1)", new Boundaries3d(new Boundary(10.0, 20.0), new Boundary(15.0, 25.0), new Boundary(0.0, 50.0)));
		Abstract3dModel model2 = Cube.fromCoordinates(new Coords3d(5.5, 12.9, 20.9), new Coords3d(25.0, 25.1, 51.1)).rotate(new Angles3d(0.001, 0, 0));
		Abstract3dModel model3 = Cube.fromCoordinates(new Coords3d(5.5, 12.9, -2.0), new Coords3d(15.0, 25.1, 22.0)).rotate(new Angles3d(0, 0.001, 0));
		Abstract3dModel model4 = Cube.fromCoordinates(new Coords3d(10.0, 20.9, -2.0), new Coords3d(25.0, 25.1, 22.0)).rotate(new Angles3d(0, 0, 0.001));
		Difference difference = new Difference(model1, model2, model3, model4);
		Boundaries3d boundaries = difference.getBoundaries();
		
		assertDoubleEquals(10.0, boundaries.getX().getMin());
		assertDoubleEquals(20.0, boundaries.getX().getMax());
		assertDoubleEquals(15.0, boundaries.getY().getMin());
		assertDoubleEquals(25.0, boundaries.getY().getMax());
		assertDoubleEquals( 0.0, boundaries.getZ().getMin());
		assertDoubleEquals(50.0, boundaries.getZ().getMax());
	}
	
	@Test
	public void shouldWorkIfTheObjectAreTouching() {
		Abstract3dModel base = new Cube(new Dims3d(6.0, 30.0, 6.0));
		Abstract3dModel result = new Difference(base,
						new Cube(100.0)
							.align(new Side(AlignType.MAX, AlignType.CENTER, AlignType.NONE), base, true)
							.align(Side.TOP, base, false));
		result.getBoundaries();
	}
	
	@Test
	public void subtractModelShouldReturnWithMoreRemovedObject() {
		Abstract3dModel difference = new Difference(
				new Test3dModel("(model1)"), 
				new Test3dModel("(model2)"))
			.subtractModel(new Test3dModel("(model3)"));
		assertEqualsWithoutWhiteSpaces("difference() {(model1) (model2) (model3)}", 
				difference.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void subtractModelShouldKeepMoves() {
		Abstract3dModel difference = new Difference(
				new Test3dModel("(model1)"), 
				new Test3dModel("(model2)"))
		.move(new Coords3d(10, 20, 30))
		.subtractModel(new Test3dModel("(model3)"));
		assertEqualsWithoutWhiteSpaces("difference() {translate([10,20,30]) difference() {(model1) (model2)} (model3)}", 
				difference.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void subtractModelShouldKeepRotate() {
		Abstract3dModel difference = new Difference(
				new Test3dModel("(model1)"), 
				new Test3dModel("(model2)"))
		.rotate(new Angles3d(10, 20, 30))
		.subtractModel(new Test3dModel("(model3)"));
		assertEqualsWithoutWhiteSpaces("difference() {rotate([10,20,30]) difference() {(model1) (model2)} (model3)}", 
				difference.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void subModuleShouldInclude() {
		Abstract3dModel testSubject = new Difference(
				new Test3dModel("(model11)").withTag(11),
				new Test3dModel("(model12)").withTag(12)
				);
		
		Assert.assertNull(testSubject.subModel(new ScadGenerationContextFactory().include(12).create()));
	}
	
	@Test
	public void subModuleShouldInclude2() {
		Abstract3dModel testSubject = new Difference(
				new Test3dModel("(model11)").withTag(11),
				new Test3dModel("(model12)").withTag(12)
				);
		
		assertEqualsWithoutWhiteSpaces("(model11)", 
				testSubject.subModel(new ScadGenerationContextFactory().include(11).create()).toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void subModuleShouldExclude() {
		Abstract3dModel testSubject = new Difference(
				new Test3dModel("(model11)").withTag(11),
				new Test3dModel("(model12)").withTag(12)
				);
		
		Assert.assertNull(testSubject.subModel(new ScadGenerationContextFactory().exclude(11).create()));
	}
	
	@Test
	public void subModuleShouldExclude2() {
		Abstract3dModel testSubject = new Difference(
				new Test3dModel("(model11)").withTag(11),
				new Test3dModel("(model12)").withTag(12)
				);
		
		assertEqualsWithoutWhiteSpaces("(model11)", 
				testSubject.subModel(new ScadGenerationContextFactory().exclude(12).create()).toScad(ColorHandlingContext.DEFAULT).getScad());
	}

	@Test
	public void subModuleIncludeExclude() {
		Abstract3dModel testSubject = 
			new Difference(
				new Difference(
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
	
	@Test
	public void subModuleExclude3() {
		Abstract3dModel testSubject = 
			new Difference(
				new Difference(
					new Test3dModel("(model11)").withTag(11),
					new Test3dModel("(model12)").withTag(12)
				).withTag(1),
				new Test3dModel("(model2)").withTag(2)
			);
	
		assertEqualsWithoutWhiteSpaces("difference() {(model11) (model2)}", 
				testSubject.subModel(new ScadGenerationContextFactory()
				.exclude(12)
				.create()).toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
}
