package eu.printingin3d.javascad.utils;

import eu.printingin3d.javascad.models.IModel;

/**
 * Represents a model and path pair - it tells where the model should be saved.
 * @author Ivan
 *
 */
public class ModelWithPath {
	private final IModel model;
	private final String relPath;
	/**
	 * Creates the object with the given parameters.
	 * @param model the model
	 * @param relPath the path
	 */
	public ModelWithPath(IModel model, String relPath) {
		this.model = model;
		this.relPath = relPath;
	}
	/**
	 * The model.
	 * @return the model
	 */
	public IModel getModel() {
		return model;
	}
	/**
	 * The path.
	 * @return the path
	 */
	public String getRelPath() {
		return relPath;
	}
}
