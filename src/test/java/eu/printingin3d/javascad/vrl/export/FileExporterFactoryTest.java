package eu.printingin3d.javascad.vrl.export;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.exceptions.UnknownFileExtensionException;

public class FileExporterFactoryTest {

	@Test
	public void testStl() throws IOException {
		File f = File.createTempFile("test", ".stl");
		f.deleteOnExit();
		
		IFileExporter exporter = FileExporterFactory.createExporter(f);
		
		Assert.assertThat(exporter, instanceOf(StlBinaryFile.class));
	}
	
	@Test
	public void testPly() throws IOException {
		File f = File.createTempFile("test", ".ply");
		f.deleteOnExit();
		
		IFileExporter exporter = FileExporterFactory.createExporter(f);
		
		Assert.assertThat(exporter, instanceOf(PolygonFile.class));
	}
	
	@Test(expected=UnknownFileExtensionException.class)
	public void testUnknown() throws IOException {
		File f = File.createTempFile("test", ".unknown");
		f.deleteOnExit();
		
		FileExporterFactory.createExporter(f);
	}
	
}
