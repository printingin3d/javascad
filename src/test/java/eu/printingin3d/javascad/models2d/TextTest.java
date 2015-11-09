package eu.printingin3d.javascad.models2d;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.coords2d.Dims2d;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.testutils.AssertEx;

public class TextTest {
	@Test
	public void testScadGeneration() {
		SCAD scad = new Text("test text", new Dims2d(30, 10)).toScad(ColorHandlingContext.DEFAULT);
		assertEqualsWithoutWhiteSpaces(
				"text(text=\"test text\", size=10, halign=\"center\", valign=\"center\");", 
				scad.getScad()
			);
	}
	
	@Test
	public void testBoundaries() {
		Boundaries2d boundaries2d = new Text("test text", new Dims2d(30, 10)).getBoundaries2d();
		
		AssertEx.assertDoubleEquals(-15, boundaries2d.getX().getMin());
		AssertEx.assertDoubleEquals(+15, boundaries2d.getX().getMax());
		AssertEx.assertDoubleEquals(-5, boundaries2d.getY().getMin());
		AssertEx.assertDoubleEquals(+5, boundaries2d.getY().getMax());
	}
	
	@Test
	public void testMovedBoundaries() {
		Boundaries2d boundaries2d = new Text("test text", new Dims2d(30, 10))
				.move(new Coords2d(10, 10))
				.getBoundaries2d();
		
		AssertEx.assertDoubleEquals(-5, boundaries2d.getX().getMin());
		AssertEx.assertDoubleEquals(25, boundaries2d.getX().getMax());
		AssertEx.assertDoubleEquals( 5, boundaries2d.getY().getMin());
		AssertEx.assertDoubleEquals(15, boundaries2d.getY().getMax());
	}
}
