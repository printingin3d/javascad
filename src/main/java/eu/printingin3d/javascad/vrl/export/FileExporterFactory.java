package eu.printingin3d.javascad.vrl.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import eu.printingin3d.javascad.exceptions.UnknownFileExtensionException;

/**
 * Creates a {@link eu.printingin3d.javascad.vrl.export.IFileExporter file exporter} based on the 
 * extension of the supplied file.
 * 
 * A typical export looks something like this:
 *  <blockquote><pre>
 * FileExporterFactory
 *     .createExporter(new File("path/to/the/file/file.ext"))
 *     .writeToFile(object.toCSG().toFacets());
 *	</pre></blockquote>
 * @author Ivan
 *
 */
public final class FileExporterFactory {
	private FileExporterFactory() {
		// prevent instantiating this class
	}
	
	/**
	 * Creates a {@link eu.printingin3d.javascad.vrl.export.IFileExporter file exporter} based on the 
	 * extension of the supplied file. The currently known extensions are {@code stl} and {@code ply}. 
	 * The result will be a {@link eu.printingin3d.javascad.vrl.export.StlBinaryFile binary STL exporter} 
	 * for the former one and a {@link eu.printingin3d.javascad.vrl.export.PolygonFile Polygon exporter} 
	 * for the latter.
	 * @param file the file to be used
	 * @return an exporter to export the model to the given file
	 * @throws IOException if an I/O error occurs during the close operation of the file 
	 * @throws FileNotFoundException if the file exists but is a directory rather than a regular file, 
	 * 		does not exist but cannot be created, or cannot be opened for any other reason
	 * @throws UnknownFileExtensionException if the extension of the given file is unknown
	 */
	@SuppressWarnings("resource")
	public static IFileExporter createExporter(File file) throws FileNotFoundException, IOException {
		String path = file.getPath().toLowerCase();
		
		OutputStream out = new FileOutputStream(file);
		if (path.endsWith(".stl")) {
			return new StlBinaryFile(out);
		}
		if (path.endsWith(".ply")) {
			return new PolygonFile(out);
		}
		
		throw new UnknownFileExtensionException("Unknown file extension: "+file.getPath());
	}
}
