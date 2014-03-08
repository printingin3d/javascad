package eu.printingin3d.javascad.models;

import eu.printingin3d.javascad.coords.Boundaries3d;

/**
 * <p>There are cases, when we want to have an object, which boundaries differ from the calculated ones.
 * An example could be a wardrobe, when we don't want to include the handles into the boundary calculation, when we align the object.</p>
 * <p>This object could be used to achieve that goal.</p>
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class BoundedModel extends Abstract3dModel {
	private final Abstract3dModel baseModel;
	private final Boundaries3d boundaries3d;

	/**
	 * Creates the object.
	 * @param baseModel the object used to generate the output
	 * @param boundaries3d the boundary used by the alignment methods
	 */
	public BoundedModel(Abstract3dModel baseModel, Boundaries3d boundaries3d) {
		this.baseModel = baseModel;
		this.boundaries3d = boundaries3d;
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new BoundedModel(baseModel.cloneModel(), boundaries3d);
	}

	@Override
	protected String innerToScad() {
		return baseModel==null ? "" : baseModel.toScad();
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return boundaries3d;
	}
	
}
