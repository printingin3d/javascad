package eu.printingin3d.javascad.models;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.testutils.AssertEx;
import eu.printingin3d.javascad.vrl.export.StlBinaryFile;

public class ImportTest {
	private byte[] byteArray;
	
	@Before
	public void init() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (StlBinaryFile exporter = new StlBinaryFile(out)) {
			exporter.writeToFile(new Cube(10).toCSG().toFacets());
		}
		
		byteArray = out.toByteArray();
	}
	
	@Test
	public void integrationTest() throws IOException {
		Abstract3dModel import1 = new Import(null, byteArray);
		
		Boundaries3d boundaries = import1.getBoundaries();
		Boundaries3dTest.assertBoundariesEquals(new Boundaries3d(new Coords3d(-5, -5, -5), new Coords3d(5, 5, 5)), boundaries);
	}
	
	@Test
	public void testScadGen() throws IOException {
		File f = File.createTempFile("prefix", ".stl");
		f.deleteOnExit();
		
		Abstract3dModel import1 = new Import(f, byteArray);
		
		AssertEx.assertEqualsWithoutWhiteSpaces("import(\""+f.getAbsolutePath().replace("\\", "\\\\")+"\", convexity=10);", import1);
	}
}
