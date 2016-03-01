package eu.printingin3d.javascad.models;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.coords2d.Dims2d;
import eu.printingin3d.javascad.models2d.Abstract2dModel;
import eu.printingin3d.javascad.models2d.Square;
import eu.printingin3d.javascad.testutils.AssertEx;
import eu.printingin3d.javascad.testutils.Test2dModel;
import eu.printingin3d.javascad.tranzitions2d.Union;
import eu.printingin3d.javascad.utils.SaveScadFiles;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;
import eu.printingin3d.javascad.vrl.export.FileExporterFactory;

public class LinearExtrudeTest {
	@Test
	public void testToScad() {
		Abstract3dModel testSubject = new LinearExtrude(new Test2dModel("(model)", 
				new Boundaries2d(new Boundary(-5, 5), new Boundary(-5, 5))), 10, 20, 2);
		AssertEx.assertEqualsWithoutWhiteSpaces("linear_extrude(height=10, center=true, convexity=10, twist=20, scale=2) (model)", 
				testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testBoundariesWithZeroTwist() {
		Abstract3dModel testSubject = new LinearExtrude(new Test2dModel("(model)", 
				new Boundaries2d(new Boundary(-5, 0), new Boundary(-10, 5))), 30, 0, 2);
		Boundaries3dTest.assertBoundariesEquals(
				new Boundaries3d(new Coords3d(-5, -10, -15), new Coords3d(0, 5, 15)), testSubject.getBoundaries());
	}
	
	@Test
	public void testBoundariesWithNonZeroTwist() {
		Abstract3dModel testSubject = new LinearExtrude(new Test2dModel("(model)", 
				new Boundaries2d(new Boundary(-5, 0), new Boundary(-10, 5))), 30, 20, 2);
		Boundaries3dTest.assertBoundariesEquals(
				new Boundaries3d(new Coords3d(-10, -10, -15), new Coords3d(10, 10, 15)), testSubject.getBoundaries());
	}
	
	@Test
	public void test() throws IOException {
		Abstract2dModel union = new Union(Arrays.<Abstract2dModel>asList(
				new Square(new Dims2d(10, 3)),
				new Square(new Dims2d(3, 10)).move(Coords2d.yOnly(3.5)))
			);
/*		Abstract2dModel union = new Union(Arrays.asList(
				new Circle(5).move(Coords2d.xOnly(-5)),
				new Circle(5), 
				new Circle(5).move(Coords2d.xOnly(5))));*/
		Abstract3dModel test = new LinearExtrude(union, 10, 45)
//				.addModelTo(Side.TOP, new Cube(10))
//				.addModelTo(Side.LEFT, new Cube(10))
				;
		
		new SaveScadFiles(new File("C:/temp"))
			.addModel("test.scad", test)
			.saveScadFiles();
		
		FacetGenerationContext context = new FacetGenerationContext(null, null, 0);
		context.setFsAndFa(1, 12);
		
		FileExporterFactory.createExporter(new File("C:/temp/test.stl"))
			.writeToFile(test.toCSG(context).toFacets());
	}
}
