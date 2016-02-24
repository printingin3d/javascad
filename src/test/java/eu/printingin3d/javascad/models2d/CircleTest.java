package eu.printingin3d.javascad.models2d;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.testutils.AssertEx;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

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
	
	@Test
	public void testPointsDistance() {
		Circle circle = new Circle(10.5);
		
		for (Area2d lc : circle.getPointCircle(FacetGenerationContext.DEFAULT)) {
			for (Coords2d c : lc) {
				double dist = Math.sqrt(c.getX()*c.getX()+c.getY()*c.getY());
				AssertEx.assertDoubleEquals(10.5, dist);
			}
		}
	}
	
	@Test
	public void testPointsDistanceEvenMoved() {
		Abstract2dModel circle = new Circle(10.5).move(new Coords2d(10, 20));
		
		for (Area2d lc : circle.getPointCircle(FacetGenerationContext.DEFAULT)) {
			for (Coords2d c : lc) {
				double dist = Math.sqrt((c.getX()-10)*(c.getX()-10)+(c.getY()-20)*(c.getY()-20));
				AssertEx.assertDoubleEquals(10.5, dist);
			}
		}
	}
}
