package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Test;

import eu.printingin3d.javascad.exceptions.IllegalValueException;

public class PrismTest {

	@Test
	public void testWhenOnlyOneRadiusIsGiven() {
		Prism prism = new Prism(10.0, 5.0, 8);
		assertEqualsWithoutWhiteSpaces("cylinder(h=10, r=5, $fn=8, center=true);", 
				prism.toScad(ScadGenerationContext.DEFAULT));
	}
	
	@Test
	public void testWhenTwoDifferentRadiusAreGiven() {
		Prism prism = new Prism(10.0, 5.0, 8.5, 8);
		assertEqualsWithoutWhiteSpaces("cylinder(h=10, r1=5, r2=8.5, $fn=8, center=true);", 
				prism.toScad(ScadGenerationContext.DEFAULT));
	}
	
	@Test
	public void testWhenTwoEqualRadiusAreGiven() {
		Prism prism = new Prism(10.0, 8.5, 8.5, 8);
		assertEqualsWithoutWhiteSpaces("cylinder(h=10, r=8.5, $fn=8, center=true);", 
				prism.toScad(ScadGenerationContext.DEFAULT));
	}

	@Test(expected = IllegalValueException.class)
	public void negativeLengthShouldThrowException1() {
		new Prism(-10.0, 5.0, 5);
	}
	
	@Test(expected = IllegalValueException.class)
	public void negativeLengthShouldThrowException2() {
		new Prism(-10.0, 5.0, 7.0, 5);
	}
	
	@Test(expected = IllegalValueException.class)
	public void negativeRadiusShouldThrowException() {
		new Prism(10.0, -5.0, 5);
	}
	
	@Test(expected = IllegalValueException.class)
	public void negativeRadius1ShouldThrowException() {
		new Prism(10.0, -5.0, 5.0, 5);
	}
	
	@Test(expected = IllegalValueException.class)
	public void negativeRadius2ShouldThrowException() {
		new Prism(10.0, 5.0, -5.0, 5);
	}
	
	@Test(expected = IllegalValueException.class)
	public void negativeNumberOfSidesShouldThrowException1() {
		new Prism(10.0, 5.0, -5);
	}
	
	@Test(expected = IllegalValueException.class)
	public void negativeNumberOfSidesShouldThrowException2() {
		new Prism(10.0, 5.0, 5.0, -5);
	}
}
