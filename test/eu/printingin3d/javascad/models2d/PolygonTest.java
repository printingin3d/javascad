package eu.printingin3d.javascad.models2d;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.testutils.AssertEx;

public class PolygonTest {
	@Test
	public void testScad() {
		SCAD scad = new Polygon(Arrays.asList(new Coords2d(5, 10), new Coords2d(Math.PI, 50), new Coords2d(-5, -10.2)))
				.toScad(ScadGenerationContextFactory.DEFAULT);
		AssertEx.assertEqualsWithoutWhiteSpaces("polygon([[5,10],[3.1416,50],[-5,-10.2]]);", scad.getScad());
	}
	
	@Test(expected=IllegalValueException.class)
	public void tooFewParameters() {
		new Polygon(Collections.<Coords2d>emptyList());
	}
}
