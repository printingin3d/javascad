package eu.printingin3d.javascad.models2d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.tranzitions2d.Translate;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * <p>Implements IModel interface and adds convenient methods to make it easier to move or rotate
 * the 2D models. Every primitive 2D object and 2D transition extend this class.</p>
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public abstract class Abstract2dModel {
	protected final Coords2d move;
	
	protected Abstract2dModel(Coords2d move) {
		this.move = move;
	}

	/**
	 * Generates the OpenSCAD representation of the model without.
	 * and moves or rotations 
	 * @return the representation of the model
	 */
	protected abstract SCAD innerToScad(IColorGenerationContext context);

	protected abstract Boundaries2d getModelBoundaries();
	
	/**
	 * Calculate the including cuboid for the current model. Rotation is not yet supported.
	 * @return the calculated boundaries
	 */
	public Boundaries2d getBoundaries2d() {
		return getModelBoundaries().move(move);
	}

	/**
	 * Renders this model and returns with the generated OpenSCAD code.
	 * @param context the color context to be used for the generation
	 * @return the generated OpenSCAD code
	 */
	public SCAD toScad(IColorGenerationContext context) {
		SCAD scad = innerToScad(context);
		return scad.isEmpty() ? scad : scad.prepend(Translate.getTranslate(move));
	}

	/**
	 * Moves this object by the given coordinates.
	 * @param delta the coordinates used by the move
	 * @return a new object which has been moved by the given coordinates - 
	 * the original object left unmodified
	 */
	public abstract Abstract2dModel move(Coords2d delta);

	protected abstract Collection<Area2d> getInnerPointCircle(FacetGenerationContext context);
	
	/**
	 * Returns with a list of points which forms the shape.
	 * @param context the context to be used for the generation
	 * @return the list of point to form this shape
	 */
	public final Collection<Area2d> getPointCircle(FacetGenerationContext context) {
		List<Area2d> result = new ArrayList<>();
		for (Area2d lc : getInnerPointCircle(context)) {
			result.add(lc.move(move));
		}
		return result;
	}
}
