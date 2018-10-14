package eu.printingin3d.javascad;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import eu.printingin3d.javascad.context.ITagColors;
import eu.printingin3d.javascad.context.TagColorsBuilder;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Cube;
import eu.printingin3d.javascad.models.Cylinder;
import eu.printingin3d.javascad.models.LinearExtrude;
import eu.printingin3d.javascad.models2d.Polygon;
import eu.printingin3d.javascad.tranzitions.Difference;
import eu.printingin3d.javascad.tranzitions.Union;
import eu.printingin3d.javascad.utils.SaveScadFiles;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;
import eu.printingin3d.javascad.vrl.export.FileExporterFactory;

public class CSGTestMain {

	public static void main(String[] args) throws IOException {
		
		ITagColors tagColors = new TagColorsBuilder()
				.addTag(1, new Color(139, 90, 43))
				.addTag(2, Color.GRAY)
				.buildTagColors();
		
		Abstract3dModel cyl = 
				new Difference( 
				new Cylinder(20, 5).withTag(1),
				new Cylinder(21, 2).withTag(2))
			.moves(Arrays.asList(Coords3d.xOnly(-20), Coords3d.xOnly(20)));
/*
		FileWriter fileWriter = new FileWriter("c:/temp/csgtest.stl");
		FacetGenerationContext context = FacetGenerationContext.DEFAULT;
		context.setFsAndFa(1.0, 20);
		fileWriter.append(cyl.toCSG(context).toStlString());
		fileWriter.close();
		
		new Cylinder(20, 5).toCSG(context).toBinaryStlFile(new File("c:/temp/csgtest2.stl"));
	*/	
/*		new PolygonFile(new File("C:/temp/color-test.ply")).writeToFile(cyl.toCSG(new FacetGenerationContext(tagColors, null, 0)).toFacets());
		new StlTextFile(new File("C:/temp/color-test.stl")).writeToFile(cyl.toCSG(new FacetGenerationContext(tagColors, null, 0)).toFacets());
		new StlBinaryFile(new File("C:/temp/color-test.bin.stl")).writeToFile(cyl.toCSG(new FacetGenerationContext(tagColors, null, 0)).toFacets());*/
		
        LinearExtrude spiral = new LinearExtrude(new Polygon(
        		Arrays.asList(new Coords2d(-2, -2), new Coords2d(2, -2), new Coords2d(2, 2), new Coords2d(0, 4), new Coords2d(-2, 2))).move(new Coords2d(10, 0)), 40, 360, 0.8);
		Abstract3dModel cubes = new Union(spiral, 
				new Cube(20).align(Side.TOP, spiral, false),
				new Cube(20).align(Side.LEFT, spiral, false),
				new Cube(20).align(Side.BACK, spiral, false)
				);
        new SaveScadFiles(new File("C:/temp"))
        		.addModel("test.scad", cubes)
        		.saveScadFiles();
		
		
		FileExporterFactory.createExporter(new File("C:/temp/export-factory-test.ply")).writeToFile(cyl.toCSG(new FacetGenerationContext(tagColors, null, 0)).toFacets());
	}

}
