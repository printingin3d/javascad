package eu.printingin3d.javascad.models2d;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.coords2d.Boundaries2d;

public class CircleTest {
	@Test
	public void testToScad() {
		Circle circle = new Circle(5.5);
		assertEqualsWithoutWhiteSpaces("circle(r=5.5,center=true);", circle.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void toScadShouldBeRounded() {
		Circle circle = new Circle(Math.PI);
		assertEqualsWithoutWhiteSpaces("circle(r=3.1416,center=true);", circle.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void boundariesShouldContainsXAndYBoundary() {
		Circle circle = new Circle(5.5);
		Boundaries2d boundaries2d = circle.getBoundaries2d();
		Assert.assertNotNull(boundaries2d);
		
		assertDoubleEquals(-5.5, boundaries2d.getX().getMin());
		assertDoubleEquals(+5.5, boundaries2d.getX().getMax());
		assertDoubleEquals(-5.5, boundaries2d.getY().getMin());
		assertDoubleEquals(+5.5, boundaries2d.getY().getMax());
	}
}
