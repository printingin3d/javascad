package eu.printingin3d.javascad.models2d;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Dims2d;

public class SquareTest {

	
	@Test
	public void testToScad() {
		Square square = new Square(new Dims2d(10.0, 20.0));
		assertEqualsWithoutWhiteSpaces("square([10,20],center=true);", square.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void toScadShouldBeRounded() {
		Square square = new Square(new Dims2d(Math.PI, 20.0));
		assertEqualsWithoutWhiteSpaces("square([3.1416,20],center=true);", square.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void boundariesShouldContainsXAndYBoundary() {
		Square square = new Square(new Dims2d(10, 20.0));
		Boundaries2d boundaries2d = square.getBoundaries2d();
		Assert.assertNotNull(boundaries2d);
		
		assertDoubleEquals(  -5.0, boundaries2d.getX().getMin());
		assertDoubleEquals(  +5.0, boundaries2d.getX().getMax());
		assertDoubleEquals( -10.0, boundaries2d.getY().getMin());
		assertDoubleEquals( +10.0, boundaries2d.getY().getMax());
	}
}
