package eu.printingin3d.javascad.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.context.ITagColors;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.IModel;
import eu.printingin3d.javascad.openscad.Consts;
/**
 * <p>Helper class to save several models to a directory.<p>
 * <p>A typical usage looks like this:<p>
 * <blockquote><pre>
 *new SaveScadFiles(new File("c:/temp"))
 *  .addModel("test.scad", new TestModel())
 *  .saveScadFiles();
 * </pre></blockquote>

 * @author ivivan <ivivan@printingin3d.eu>
 */
public class SaveScadFiles {
	private final List<IScadFile> scadFiles = new ArrayList<>();
	private final File root;
	
	/**
	 * Creates the object with the given root directory.
	 * @param root the root directory where the files will be saved
	 * @throws IllegalValueException if the root is null or exists as a file
	 */
	public SaveScadFiles(File root) throws IllegalValueException {
		AssertValue.isNotNull(root, "The root directory must not be null!");
		AssertValue.isFalse(root.isFile(), "The root must not be an existing file - " +
				"if it is exists, it should be a directory!");
		this.root = root;
	}

	/**
	 * Adds a SCAD file.
	 * @param scadFile the SCAD file to be saved
	 * @return return this object to make it possible to chain more method call
	 */
	public SaveScadFiles addScadFile(IScadFile scadFile) {
		scadFiles.add(scadFile);
		return this;
	}
	
	/**
	 * Adds a collection of SCAD files.
	 * @param scadFile the SCAD files to be saved
	 * @return return this object to make it possible to chain more method call
	 */
	public SaveScadFiles addScadFiles(Collection<IScadFile> scadFile) {
		scadFiles.addAll(scadFiles);
		return this;
	}
	
	/**
	 * Adds a SCAD file with the given file name and the given model list.
	 * @param fileName the name of the file where the model will be saved
	 * @param models the models to be saved
	 * @return return this object to make it possible to chain more method call
	 */
	public SaveScadFiles addModels(String fileName, Collection<IModel> models) {
		return addModels(fileName, models, ColorHandlingContext.DEFAULT);
	}
	
	/**
	 * Adds a SCAD file with the given file name and the given model list.
	 * @param fileName the name of the file where the model will be saved
	 * @param models the models to be saved
	 * @param context the context used for the scad file generation
	 * @return return this object to make it possible to chain more method call
	 */
	public SaveScadFiles addModels(final String fileName, final Collection<IModel> models,
			final IColorGenerationContext context) {
		addScadFile(new IScadFile() {
			@Override
			public File getFile(File root) {
				return new File(root.getAbsolutePath()+"/"+fileName);
			}
			
			@Override
			public Collection<IModel> getModels() {
				return models;
			}

			@Override
			public IColorGenerationContext getContext() {
				return context;
			}
		});
		return this;
	}

	/**
	 * Adds a SCAD file with the given file name and the given model using the default generation context. 
	 * A default {@link Consts} is added before the model.
	 * @param fileName the name of the file where the model will be saved
	 * @param model the model to be saved
	 * @return return this object to make it possible to chain more method call
	 */
	public SaveScadFiles addModel(String fileName, IModel model) {
		return addModel(fileName, model, ColorHandlingContext.DEFAULT);
	}
	
	/**
	 * Adds a SCAD file with the given file name and the given model. 
	 * A default {@link Consts} is added before the model.
	 * @param fileName the name of the file where the model will be saved
	 * @param model the model to be saved
	 * @param tagColors the colors used for the scad file generation
	 * @return return this object to make it possible to chain more method call
	 */
	public SaveScadFiles addModel(String fileName, IModel model, ITagColors tagColors) {
		return addModel(fileName, model, new ColorHandlingContext(tagColors));
	}
	
	/**
	 * Adds a SCAD file with the given file name and the given model. 
	 * A default {@link Consts} is added before the model.
	 * @param fileName the name of the file where the model will be saved
	 * @param model the model to be saved
	 * @param context the color context used for the scad file generation
	 * @return return this object to make it possible to chain more method call
	 */
	public SaveScadFiles addModel(String fileName, IModel model, IColorGenerationContext context) {
		return addModels(fileName, Arrays.asList(new Consts(), model), context);
	}
	
	/**
	 * <p>Adds a SCAD file with the given file name and the given model. 
	 * A default {@link Consts} is added before the model.</p>
	 * <p>A method for backward compatibility only - use 
	 * <code>addModel(fileName, model.subModel(context))</code> instead.</p>
	 * @param fileName the name of the file where the model will be saved
	 * @param model the model to be saved
	 * @param scadContext the context used for the scad file generation
	 * @return return this object to make it possible to chain more method call
	 */
	@Deprecated
	public SaveScadFiles addModel(String fileName, Abstract3dModel model, 
			IScadGenerationContext scadContext) {
		return addModel(fileName, model.subModel(scadContext));
	}
	
	/**
	 * Adds a model provider with the default generation context.
	 * A default {@link Consts} is added before the model.
	 * @param provider the provider to be added
	 * @return return this object to make it possible to chain more method call
	 */
	public SaveScadFiles addModelProvider(IModelProvider provider) {
		return addModelProvider(provider, ColorHandlingContext.DEFAULT);
	}
	
	/**
	 * Adds a model provider with the given tag colors.
	 * A default {@link Consts} is added before the model.
	 * @param provider the provider to be added
	 * @param tagColors the tag-color pairs to be used for the SCAD file generation
	 * @return return this object to make it possible to chain more method call
	 */
	public SaveScadFiles addModelProvider(IModelProvider provider, ITagColors tagColors) {
		return addModelProvider(provider, new ColorHandlingContext(tagColors));
	}
	
	/**
	 * Adds a model provider with the given color context.
	 * A default {@link Consts} is added before the model.
	 * @param provider the provider to be added
	 * @param context the context used for the SCAD file generation
	 * @return return this object to make it possible to chain more method call
	 */
	public SaveScadFiles addModelProvider(IModelProvider provider, IColorGenerationContext context) {
		for (ModelWithPath mp : provider.getModelsAndPaths()) {
			addModel(mp.getRelPath(), mp.getModel(), context);
		}
		return this;
	}
	
	/**
	 * Save the added SCAD files into the corresponding files. 
	 * @throws IOException if any IO error happens during the file write
	 */
	public void saveScadFiles() throws IOException {
		for (IScadFile scadFile : scadFiles) {
			File file = scadFile.getFile(root);
			file.getParentFile().mkdirs();
			new ModelToFile(file).addModels(scadFile.getModels()).saveToFile(scadFile.getContext());
		}
	}
}
