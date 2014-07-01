package eu.printingin3d.javascad.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.IModel;
import eu.printingin3d.javascad.openscad.Consts;

/**
 * A helper class which helps you to save SCAD files and models.
 * 
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
		addModels(fileName, models, ScadGenerationContextFactory.DEFAULT);
		return this;
	}
	
	/**
	 * Adds a SCAD file with the given file name and the given model list.
	 * @param fileName the name of the file where the model will be saved
	 * @param models the models to be saved
	 * @param context the context used for the scad file generation
	 * @return return this object to make it possible to chain more method call
	 */
	public SaveScadFiles addModels(final String fileName, final Collection<IModel> models,
			final IScadGenerationContext context) {
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
			public IScadGenerationContext getContext() {
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
		return addModel(fileName, model, ScadGenerationContextFactory.DEFAULT);
	}
	
	/**
	 * Adds a SCAD file with the given file name and the given model. 
	 * A default {@link Consts} is added before the model.
	 * @param fileName the name of the file where the model will be saved
	 * @param model the model to be saved
	 * @param context the context used for the scad file generation
	 * @return return this object to make it possible to chain more method call
	 */
	public SaveScadFiles addModel(String fileName, IModel model, IScadGenerationContext context) {
		return addModels(fileName, Arrays.asList(new Consts(), model), context);
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
