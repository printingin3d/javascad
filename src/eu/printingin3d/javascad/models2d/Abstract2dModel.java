package eu.printingin3d.javascad.models2d;

import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.models.IModel;

/**
 * <p>Implements IModel interface and adds convenient methods to make it easier to move or rotate
 * the 2D models. Every primitive 2D object and 2D transition extend this class.</p>
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public abstract class Abstract2dModel implements IModel {
	
	/**
	 * Generates the OpenSCAD representation of the model without.
	 * and moves or rotations 
	 * @return the representation of the model
	 */
	protected abstract String innerToScad();

	protected abstract Boundaries2d getModelBoundaries();
	
	/**
	 * Calculate the including cuboid for the current model. Rotation is not yet supported.
	 * @return the calculated boundaries
	 */
	public Boundaries2d getBoundaries2d() {
		return getModelBoundaries();
	}

	@Override
	public String toScad() {
		return innerToScad();
	}
}
