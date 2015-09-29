package eu.printingin3d.javascad.utils;

import java.io.File;
import java.util.Collection;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.models.IModel;

/**
 * Represents a OpenSCAD source file. It can be implemented directly or used through {@link SaveScadFiles}.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public interface IScadFile {
	/**
	 * Get the file path relative to the given root directory.
	 * @param root the root directory - the returned file must be under this directory.
	 * @return the file path
	 */
	File getFile(File root);
	/**
	 * The models which should be included in this OpenSCAD file.
	 * @return the models which should be included in this OpenSCAD file.
	 */
	Collection<IModel> getModels();
	/**
	 * The context which will be used during the generation process.
	 * @return the context
	 */
	IColorGenerationContext getContext();
}
