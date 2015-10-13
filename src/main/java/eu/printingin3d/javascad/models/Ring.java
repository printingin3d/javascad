package eu.printingin3d.javascad.models;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.exceptions.NotImplementedException;
import eu.printingin3d.javascad.models2d.Abstract2dModel;
import eu.printingin3d.javascad.tranzitions.Translate;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * A ring 3D object based on a 2D object.
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Ring extends Atomic3dModel {
	private final Abstract2dModel model;
	private final double radius;
	
	/**
	 * Creates the ring with the given parameters. The ready object will be the 2D model extruded into a ring
	 * with the given radius between the origin and the origin of the 2D model.
	 * @param radius the radius of the extrusion
	 * @param model the model to be rotated
	 */
	public Ring(double radius, Abstract2dModel model) {
		this.model = model;
		this.radius = radius;
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Ring(radius, model);
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return new SCAD("rotate_extrude() "+
					Translate.getTranslate(Coords3d.xOnly(radius))).append(model.toScad(context)).append(";");
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		Boundaries2d modelBoundaries = model.getBoundaries2d();
		Boundary z = modelBoundaries.getY();
		Boundary xy = Boundary.createSymmetricBoundary(modelBoundaries.getX().getMax()+radius);
		return new Boundaries3d(xy, xy, z);
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		throw new NotImplementedException();
	}

}
