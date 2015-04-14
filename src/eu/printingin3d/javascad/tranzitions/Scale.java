package eu.printingin3d.javascad.tranzitions;

import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Complex3dModel;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.tranform.TransformationFactory;
import eu.printingin3d.javascad.utils.AssertValue;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * Scales a model by the given value on X, Y and Z plane. It is a descendant of {@link Abstract3dModel}, 
 * which means you can use the convenient methods on scales too.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Scale extends Complex3dModel {
	private final Abstract3dModel model;
	private final Coords3d scale;

	/**
	 * Creates the scale operation with the given model and scale value. If the scale equals to (1,1,1)
	 * this object does nothing, just gives back the underlying model.
	 * @param model the model to be scaled
	 * @param scale the scale values to be used
	 * @throws IllegalValueException if either of the parameters is null
	 */
	public Scale(Abstract3dModel model, Coords3d scale) throws IllegalValueException {
		AssertValue.isNotNull(model, "Model should not be null for scale operation!");
		AssertValue.isNotNull(scale, "Scale should not be null for scale operation!");
		
		this.model = model;
		this.scale = scale;
	}

	@Override
	protected SCAD innerToScad(IScadGenerationContext context) {
		if (scale.isIdent()) {
			return model.toScad(context);
		}
		return new SCAD("scale("+scale+")").append(model.toScad(context));	
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		Boundaries3d result = model.getBoundaries();
		if (!scale.isIdent()) {
			result = result.scale(scale);
		}
		return result;
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Scale(model.cloneModel(), scale);
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return model.toCSG(context).transformed(TransformationFactory.getScaleMatrix(scale));
	}

	@Override
	protected Abstract3dModel innerSubModel(IScadGenerationContext context) {
		Abstract3dModel subModel = model.subModel(context);
		return subModel==null ? null : new Scale(subModel, scale);
	}

}
