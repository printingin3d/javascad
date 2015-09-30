package eu.printingin3d.javascad.utils;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.IModel;

/**
 * This class can be used to write models to file. It can be used directly, but it is
 * advised to use it through the SaveScadFiles class.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class ModelToFile {
	private final File file;
	private final List<IModel> models = new ArrayList<>();

	/**
	 * Creates the object.
	 * @param file the file where the model(s) will be saved
	 * @throws IllegalValueException if the file is null
	 */
	public ModelToFile(File file) throws IllegalValueException {
		AssertValue.isNotNull(file, "The file must not be null!");
		this.file = file;
	}
	
	/**
	 * Adds a model to the list of models this class will save.
	 * @param model the model to be added
	 * @return this object to make it possible to chain more method call
	 */
	public ModelToFile addModel(IModel model) {
		models.add(model);
		return this;
	}
	
	/**
	 * Adds models to the list of models this class will save.
	 * @param models the models to be added
	 * @return this object to make it possible to chain more method call
	 */
	public ModelToFile addModels(Collection<IModel> models) {
		this.models.addAll(models);
		return this;
	}
	
	/**
	 * Saves the models to the file.
	 * @param context the color context to be used for the generation
	 * @throws IOException if any IO error happens during opening, writing or closing the file
	 */
	public void saveToFile(IColorGenerationContext context) throws IOException {
		Writer writer = new FileWriter(file);
		try {
			for (IModel model : models) {
				writer.append(model.toScad(context).getScad());
			}
		}
		finally {
			writer.close();
		}
	}
}
