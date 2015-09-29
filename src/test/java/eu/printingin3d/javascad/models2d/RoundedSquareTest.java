package eu.printingin3d.javascad.models2d;

import static eu.printingin3d.javascad.testutils.AssertEx.assertContainsWithoutWhiteSpaces;
import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;
import static eu.printingin3d.javascad.testutils.AssertEx.assertMatchCount;
import static eu.printingin3d.javascad.testutils.AssertEx.assertMatchToExpressionWithoutWhiteSpaces;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Dims2d;

public class RoundedSquareTest {
	
	@Test
	public void testToScad() {
		RoundedSquare square = new RoundedSquare(new Dims2d(3, 3), 0.5);
		
		String actual = square.toScad(ColorHandlingContext.DEFAULT).getScad();
		
		assertMatchToExpressionWithoutWhiteSpaces("union\\(\\) \\{.*\\}", actual);
		assertContainsWithoutWhiteSpaces("square([2,3], center=true);", actual);
		assertContainsWithoutWhiteSpaces("square([3,2], center=true);", actual);
		assertContainsWithoutWhiteSpaces("translate([ 1, 1]) circle(r=0.5, center=true);", actual);
		assertContainsWithoutWhiteSpaces("translate([ 1,-1]) circle(r=0.5, center=true);", actual);
		assertContainsWithoutWhiteSpaces("translate([-1, 1]) circle(r=0.5, center=true);", actual);
		assertContainsWithoutWhiteSpaces("translate([-1,-1]) circle(r=0.5, center=true);", actual);
		
		assertMatchCount("square", actual, 2);
		assertMatchCount("circle", actual, 4);
	}
	
	@Test
	public void testToScadWithNoStraightX() {
		RoundedSquare square = new RoundedSquare(new Dims2d(1, 3), 0.5);
		
		String actual = square.toScad(ColorHandlingContext.DEFAULT).getScad();
		
		assertMatchToExpressionWithoutWhiteSpaces("union\\(\\) \\{.*\\}", actual);
		assertContainsWithoutWhiteSpaces("square([1,2], center=true);", actual);
		assertContainsWithoutWhiteSpaces("translate([0, 1]) circle(r=0.5, center=true);", actual);
		assertContainsWithoutWhiteSpaces("translate([0,-1]) circle(r=0.5, center=true);", actual);
		
		assertMatchCount("square", actual, 1);
		assertMatchCount("circle", actual, 2);
	}
	
	@Test
	public void testToScadWithNoStraightY() {
		RoundedSquare square = new RoundedSquare(new Dims2d(3, 1), 0.5);
		
		String actual = square.toScad(ColorHandlingContext.DEFAULT).getScad();
		
		assertMatchToExpressionWithoutWhiteSpaces("union\\(\\) \\{.*\\}", actual);
		assertContainsWithoutWhiteSpaces("square([2,1], center=true);", actual);
		assertContainsWithoutWhiteSpaces("translate([ 1, 0]) circle(r=0.5, center=true);", actual);
		assertContainsWithoutWhiteSpaces("translate([-1, 0]) circle(r=0.5, center=true);", actual);
		
		assertMatchCount("square", actual, 1);
		assertMatchCount("circle", actual, 2);
	}
	
	@Test
	public void testToScadWithNoStraightXY() {
		RoundedSquare square = new RoundedSquare(new Dims2d(1, 1), 0.5);
		
		String actual = square.toScad(ColorHandlingContext.DEFAULT).getScad();
		
		assertEqualsWithoutWhiteSpaces("circle(r=0.5, center=true);", actual);
	}
	
	@Test
	public void toScadShouldBeRounded() {
		RoundedSquare square = new RoundedSquare(new Dims2d(Math.PI*10.0, 20.0), Math.PI);
		
		String actual = square.toScad(ColorHandlingContext.DEFAULT).getScad();
		assertMatchToExpressionWithoutWhiteSpaces("union\\(\\) \\{.*\\}", actual);
		assertContainsWithoutWhiteSpaces("square([25.1327,20], center=true);", actual);
		assertContainsWithoutWhiteSpaces("square([31.4159,13.7168], center=true);", actual);
		assertContainsWithoutWhiteSpaces("translate([ 12.5664, 6.8584]) circle(r=3.1416, center=true);", actual);
		assertContainsWithoutWhiteSpaces("translate([ 12.5664,-6.8584]) circle(r=3.1416, center=true);", actual);
		assertContainsWithoutWhiteSpaces("translate([-12.5664, 6.8584]) circle(r=3.1416, center=true);", actual);
		assertContainsWithoutWhiteSpaces("translate([-12.5664,-6.8584]) circle(r=3.1416, center=true);", actual);
		
		assertMatchCount("square", actual, 2);
		assertMatchCount("circle", actual, 4);
	}
	
	@Test
	public void boundariesShouldContainsXAndYBoundary() {
		RoundedSquare square = new RoundedSquare(new Dims2d(10, 20.0), 4.0);
		Boundaries2d boundaries2d = square.getBoundaries2d();
		Assert.assertNotNull(boundaries2d);
		
		assertDoubleEquals(  -5.0, boundaries2d.getX().getMin());
		assertDoubleEquals(  +5.0, boundaries2d.getX().getMax());
		assertDoubleEquals( -10.0, boundaries2d.getY().getMin());
		assertDoubleEquals( +10.0, boundaries2d.getY().getMax());
	}

}
