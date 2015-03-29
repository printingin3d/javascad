package eu.printingin3d.javascad.models2d;

import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.models.IModel;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.tranzitions2d.Translate;

/**
 * <p>Implements IModel interface and adds convenient methods to make it easier to move or rotate
 * the 2D models. Every primitive 2D object and 2D transition extend this class.</p>
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public abstract class Abstract2dModel implements IModel {
	protected final Coords2d move;
	
	protected Abstract2dModel(Coords2d move) {
		this.move = move;
	}

	/**
	 * Generates the OpenSCAD representation of the model without.
	 * and moves or rotations 
	 * @return the representation of the model
	 */
	protected abstract SCAD innerToScad(IScadGenerationContext context);

	protected abstract Boundaries2d getModelBoundaries();
	
	/**
	 * Calculate the including cuboid for the current model. Rotation is not yet supported.
	 * @return the calculated boundaries
	 */
	public Boundaries2d getBoundaries2d() {
		return getModelBoundaries().move(move);
	}

	@Override
	public SCAD toScad(IScadGenerationContext context) {
		return innerToScad(context).prepend(Translate.getTranslate(move));
	}

	/**
	 * Moves this object by the given coordinates.
	 * @param delta the coordinates used by the move
	 * @return a new object which has been moved by the given coordinates - the original object left unmodified
	 */
	public abstract Abstract2dModel move(Coords2d delta);
}
