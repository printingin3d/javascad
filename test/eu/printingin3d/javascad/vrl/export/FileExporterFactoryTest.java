package eu.printingin3d.javascad.vrl.export;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.exceptions.UnknownFileExtensionException;

public class FileExporterFactoryTest {

	@Test
	public void testStl() {
		IFileExporter exporter = FileExporterFactory.createExporter(new File("/file/to/file.stl"));
		
		Assert.assertThat(exporter, instanceOf(StlBinaryFile.class));
	}
	
	@Test
	public void testPly() {
		IFileExporter exporter = FileExporterFactory.createExporter(new File("/file/to/file.ply"));
		
		Assert.assertThat(exporter, instanceOf(PolygonFile.class));
	}
	
	@Test(expected=UnknownFileExtensionException.class)
	public void testUnknown() {
		FileExporterFactory.createExporter(new File("/file/to/file.unknown"));
	}
	
}
