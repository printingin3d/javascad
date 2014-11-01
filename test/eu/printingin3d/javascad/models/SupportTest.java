package eu.printingin3d.javascad.models;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Dims3d;

public class SupportTest {
	@Test
	public void theBoundariesMustMatchToTheGivenSizeXLargerThanY() {
		Abstract3dModel testObject = new Support(new Dims3d(100.0, 53.2, 10.2), 0.5);
		
		Boundaries3dTest.assertBoundariesEquals(new Boundaries3d(
				new Boundary(-50.0, +50.0), 
				new Boundary(-26.6, +26.6), 
				new Boundary(-5.1, +5.1)), testObject.getBoundaries());
	}
	
	@Test
	public void theBoundariesMustMatchToTheGivenSizeYLargerThanX() {
		Abstract3dModel testObject = new Support(new Dims3d(24.4, 54.8, 10.2), 0.5);
		
		Boundaries3dTest.assertBoundariesEquals(new Boundaries3d(
				new Boundary(-12.2, +12.2), 
				new Boundary(-27.4, +27.4), 
				new Boundary( -5.1, + 5.1)), testObject.getBoundaries());
	}
}
