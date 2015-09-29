package eu.printingin3d.javascad.vrl.export;

import java.io.File;

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
	 * @throws UnknownFileExtensionException if the extension of the given file is unknown
	 */
	public static IFileExporter createExporter(File file) {
		String path = file.getPath().toLowerCase();
		
		if (path.endsWith(".stl")) {
			return new StlBinaryFile(file);
		}
		if (path.endsWith(".ply")) {
			return new PolygonFile(file);
		}
		
		throw new UnknownFileExtensionException("Unknown file extension: "+file.getPath());
	}
}
