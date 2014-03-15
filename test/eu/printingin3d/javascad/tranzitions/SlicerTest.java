package eu.printingin3d.javascad.tranzitions;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.BoundaryTest;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class SlicerTest {
	private static final double EPSILON = 0.001;
	private static final Abstract3dModel TEST_MODEL = new Test3dModel("(model)",
			new Boundaries3d(new Boundary(0.0, 24.0), new Boundary(-12.0, 36.0), new Boundary(6.0, 18.0)));

	@Test(expected = IllegalValueException.class)
	public void constructorShouldThrowExceptionIfIndexIsNegative() {
		new Slicer(TEST_MODEL, Direction.X, 1, -1);
	}
	
	@Test(expected = IllegalValueException.class)
	public void constructorShouldThrowExceptionIfIndexIsGreaterOrEqualsWithNumberOfPieces() {
		new Slicer(TEST_MODEL, Direction.X, 3, 5);
	}
	
	@Test(expected = IllegalValueException.class)
	public void constructorShouldThrowExceptionIfNumberOfPiecesIsLessThanOne() {
		new Slicer(TEST_MODEL, Direction.X, 0, 0);
	}
	
	@Test(expected = IllegalValueException.class)
	public void constructorShouldThrowExceptionIfDirectionIsNull() {
		new Slicer(TEST_MODEL, null, 2, 0);
	}
	
	@Test(expected = IllegalValueException.class)
	public void constructorShouldThrowExceptionIfModelIsNull() {
		new Slicer(null, Direction.X, 2, 0);
	}
	
	@Test(expected = IllegalValueException.class)
	public void constructorShouldThrowExceptionIfCuttedRateIsMoreThanOne() {
		new Slicer(TEST_MODEL, Direction.X, 0.7, 0.4);
	}

	@Test
	public void shouldReturnWithTheModelsToScadIfThereIsOnlyOnePiece() {
		assertEqualsWithoutWhiteSpaces("(model)", new Slicer(TEST_MODEL, Direction.X, 1, 0).toScad());
	}

	@Test
	public void shouldXReturnWithTheFirstHalfIfThereAreTwoPieces() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) translate([18.5,12,12]) cube([13,49,13],center=true);}", 
				new Slicer(TEST_MODEL, Direction.X, 2, 0).toScad());
	}
	
	@Test
	public void shouldXReturnWithTheSecondHalfIfThereAreTwoPiecesAndTheIndexIsOne() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) translate([5.5,12,12]) cube([13,49,13],center=true);}", 
				new Slicer(TEST_MODEL, Direction.X, 2, 1).toScad());
	}

	@Test
	public void shouldXReturnWithTheFirstThirdIfThereAreThreePieces() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) translate([16.5,12,12]) cube([17,49,13],center=true);}", 
				new Slicer(TEST_MODEL, Direction.X, 3, 0).toScad());
	}
	
	@Test
	public void shouldXReturnWithTheLastThirdIfThereAreThreePiecesAndTheIndexIsTwo() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) translate([7.5,12,12]) cube([17,49,13],center=true);}", 
				new Slicer(TEST_MODEL, Direction.X, 3, 2).toScad());
	}
	
	@Test
	public void shouldXReturnWithTheMiddleThirdIfThereAreThreePiecesAndTheIndexIsOne() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) union() { translate([3.5,12,12]) cube([9,49,13],center=true);translate([20.5,12,12]) cube([9,49,13],center=true);}}", 
				new Slicer(TEST_MODEL, Direction.X, 3, 1).toScad());
	}
	
	@Test
	public void shouldYReturnWithTheFirstHalfIfThereAreTwoPieces() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) translate([12,24.5,12]) cube([25,25,13],center=true);}", 
				new Slicer(TEST_MODEL, Direction.Y, 2, 0).toScad());
	}
	
	@Test
	public void shouldYReturnWithTheSecondHalfIfThereAreTwoPiecesAndTheIndexIsOne() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) translate([12,-0.5,12]) cube([25,25,13],center=true);}", 
				new Slicer(TEST_MODEL, Direction.Y, 2, 1).toScad());
	}
	
	@Test
	public void shouldYReturnWithTheFirstThirdIfThereAreThreePieces() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) translate([12,20.5,12]) cube([25,33,13],center=true);}", 
				new Slicer(TEST_MODEL, Direction.Y, 3, 0).toScad());
	}
	
	@Test
	public void shouldYReturnWithTheLastThirdIfThereAreThreePiecesAndTheIndexIsTwo() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) translate([12,3.5,12]) cube([25,33,13],center=true);}", 
				new Slicer(TEST_MODEL, Direction.Y, 3, 2).toScad());
	}
	
	@Test
	public void shouldYReturnWithTheMiddleThirdIfThereAreThreePiecesAndTheIndexIsOne() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) union() { translate([12,-4.5,12]) cube([25,17,13],center=true);translate([12,28.5,12]) cube([25,17,13],center=true);}}", 
				new Slicer(TEST_MODEL, Direction.Y, 3, 1).toScad());
	}
	
	@Test
	public void shouldZReturnWithTheFirstHalfIfThereAreTwoPieces() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) translate([12,12,15.5]) cube([25,49,7],center=true);}", 
				new Slicer(TEST_MODEL, Direction.Z, 2, 0).toScad());
	}
	
	@Test
	public void shouldZReturnWithTheSecondHalfIfThereAreTwoPiecesAndTheIndexIsOne() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) translate([12,12,8.5]) cube([25,49,7],center=true);}", 
				new Slicer(TEST_MODEL, Direction.Z, 2, 1).toScad());
	}
	
	@Test
	public void shouldZReturnWithTheFirstThirdIfThereAreThreePieces() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) translate([12,12,14.5]) cube([25,49,9],center=true);}", 
				new Slicer(TEST_MODEL, Direction.Z, 3, 0).toScad());
	}
	
	@Test
	public void shouldZReturnWithTheLastThirdIfThereAreThreePiecesAndTheIndexIsTwo() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) translate([12,12,9.5]) cube([25,49,9],center=true);}", 
				new Slicer(TEST_MODEL, Direction.Z, 3, 2).toScad());
	}
	
	@Test
	public void shouldZReturnWithTheMiddleThirdIfThereAreThreePiecesAndTheIndexIsOne() {
		assertEqualsWithoutWhiteSpaces("difference() {(model) union() { translate([12,12,7.5]) cube([25,49,5],center=true);translate([12,12,16.5]) cube([25,49,5],center=true);}}", 
				new Slicer(TEST_MODEL, Direction.Z, 3, 1).toScad());
	}
	
	
	@Test
	public void boundariesShouldBeTheSameIfThereIsOnlyOnePiece() {
		Boundaries3d boundaries = new Slicer(TEST_MODEL, Direction.X, 1, 0).getBoundaries();
		Boundaries3d originalBoundaries = TEST_MODEL.getBoundaries();
		Boundaries3dTest.assertBoundariesEquals(originalBoundaries, boundaries);
	}
	
	@Test
	public void xBoundariesShouldBeTheFirstHalfIfThereAreTwoPieces() {
		Boundaries3d boundaries = new Slicer(TEST_MODEL, Direction.X, 2, 0).getBoundaries();
		Boundaries3d originalBoundaries = TEST_MODEL.getBoundaries();
		assertEquals(originalBoundaries.getX().getMin(), boundaries.getX().getMin(), EPSILON);
		assertEquals(originalBoundaries.getX().getMiddle(), boundaries.getX().getMax(), EPSILON);
		BoundaryTest.assertBoundaryEquals(originalBoundaries.getY(), boundaries.getY());
		BoundaryTest.assertBoundaryEquals(originalBoundaries.getZ(), boundaries.getZ());
	}
	
	@Test
	public void yBoundariesShouldBeTheFirstHalfIfThereAreTwoPieces() {
		Boundaries3d boundaries = new Slicer(TEST_MODEL, Direction.Y, 2, 0).getBoundaries();
		Boundaries3d originalBoundaries = TEST_MODEL.getBoundaries();
		BoundaryTest.assertBoundaryEquals(originalBoundaries.getX(), boundaries.getX());
		assertEquals(originalBoundaries.getY().getMin(), boundaries.getY().getMin(), EPSILON);
		assertEquals(originalBoundaries.getY().getMiddle(), boundaries.getY().getMax(), EPSILON);
		BoundaryTest.assertBoundaryEquals(originalBoundaries.getZ(), boundaries.getZ());
	}
	
	@Test
	public void zBoundariesShouldBeTheFirstHalfIfThereAreTwoPieces() {
		Boundaries3d boundaries = new Slicer(TEST_MODEL, Direction.Z, 2, 0).getBoundaries();
		Boundaries3d originalBoundaries = TEST_MODEL.getBoundaries();
		BoundaryTest.assertBoundaryEquals(originalBoundaries.getX(), boundaries.getX());
		BoundaryTest.assertBoundaryEquals(originalBoundaries.getY(), boundaries.getY());
		assertEquals(originalBoundaries.getZ().getMin(), boundaries.getZ().getMin(), EPSILON);
		assertEquals(originalBoundaries.getZ().getMiddle(), boundaries.getZ().getMax(), EPSILON);
	}

	@Test
	public void xBoundariesShouldCalculateXSizeAccordingly() {
		Boundaries3d boundaries = new Slicer(TEST_MODEL, Direction.X, 0.7, 0.2).getBoundaries();
		Boundaries3d originalBoundaries = TEST_MODEL.getBoundaries();
		assertEquals(originalBoundaries.getX().getSize()*0.1, boundaries.getX().getSize(), EPSILON);
		BoundaryTest.assertBoundaryEquals(originalBoundaries.getY(), boundaries.getY());
		BoundaryTest.assertBoundaryEquals(originalBoundaries.getZ(), boundaries.getZ());
	}
	
	@Test
	public void xBoundariesShouldCalculateYSizeAccordingly() {
		Boundaries3d boundaries = new Slicer(TEST_MODEL, Direction.Y, 0.7, 0.2).getBoundaries();
		Boundaries3d originalBoundaries = TEST_MODEL.getBoundaries();
		BoundaryTest.assertBoundaryEquals(originalBoundaries.getX(), boundaries.getX());
		assertEquals(originalBoundaries.getY().getSize()*0.1, boundaries.getY().getSize(), EPSILON);
		BoundaryTest.assertBoundaryEquals(originalBoundaries.getZ(), boundaries.getZ());
	}
	
	@Test
	public void xBoundariesShouldCalculateZSizeAccordingly() {
		Boundaries3d boundaries = new Slicer(TEST_MODEL, Direction.Z, 0.7, 0.2).getBoundaries();
		Boundaries3d originalBoundaries = TEST_MODEL.getBoundaries();
		BoundaryTest.assertBoundaryEquals(originalBoundaries.getX(), boundaries.getX());
		BoundaryTest.assertBoundaryEquals(originalBoundaries.getY(), boundaries.getY());
		assertEquals(originalBoundaries.getZ().getSize()*0.1, boundaries.getZ().getSize(), EPSILON);
	}
}
