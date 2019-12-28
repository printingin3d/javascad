package eu.printingin3d.javascad.tranzitions;

import java.util.Collections;
import java.util.List;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Complex3dModel;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.tranform.ITransformation;
import eu.printingin3d.javascad.tranform.TransformationFactory;
import eu.printingin3d.javascad.utils.AssertValue;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * Mirrors a model. The plane of the mirroring could only be the X, Y and Z plane, to make it easier 
 * to use and understand.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public final class Mirror extends Complex3dModel {
	/**
	 * Mirrors the given model using the X plane as a mirror.
	 * @param model the model to be mirrored
	 * @return the mirrored model
	 * @throws IllegalValueException if the model is null
	 */
	public static Mirror mirrorX(Abstract3dModel model) throws IllegalValueException {
		return new Mirror(model, Direction.X);
	}
	
	/**
	 * Mirrors the given model using the Y plane as a mirror.
	 * @param model the model to be mirrored
	 * @return the mirrored model
	 * @throws IllegalValueException if the model is null
	 */
	public static Mirror mirrorY(Abstract3dModel model) throws IllegalValueException {
		return new Mirror(model, Direction.Y);
	}
	
	/**
	 * Mirrors the given model using the Z plane as a mirror.
	 * @param model the model to be mirrored
	 * @return the mirrored model
	 * @throws IllegalValueException if the model is null
	 */
	public static Mirror mirrorZ(Abstract3dModel model) throws IllegalValueException {
		return new Mirror(model, Direction.Z);
	}
	
	private final Abstract3dModel model;
	private final Direction direction;

	private Mirror(Abstract3dModel model, Direction direction) throws IllegalValueException {
		AssertValue.isNotNull(model, "The model to be mirrored must not be null!");
		
		this.model = model;
		this.direction = direction;
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return new SCAD("mirror("+direction.getCoords()+")").append(model.toScad(context));
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		Boundaries3d boundaries = model.getBoundaries();
		return new Boundaries3d(
				boundaries.getX().negate(direction==Direction.X), 
				boundaries.getY().negate(direction==Direction.Y), 
				boundaries.getZ().negate(direction==Direction.Z));
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Mirror(model, direction);
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		ITransformation tr = TransformationFactory.getMirrorMatrix(direction);
		return model.toCSG(context).transformed(tr);
	}

	@Override
	protected Abstract3dModel innerSubModel(IScadGenerationContext context) {
		Abstract3dModel subModel = model.subModel(context);
		return subModel==null ? null : new Mirror(subModel, direction);
	}

    @Override
    protected List<Abstract3dModel> getChildrenModels() {
        return Collections.singletonList(model);
    }
}
