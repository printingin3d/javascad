package eu.printingin3d.javascad.tranzitions;

import java.util.Collections;
import java.util.List;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Complex3dModel;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.tranform.TransformationFactory;
import eu.printingin3d.javascad.utils.AssertValue;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * This represents a rotate transition, but used rarely, because the convenient
 * methods of {@link Abstract3dModel} replace it most of the time.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Rotate extends Complex3dModel {
	private final Abstract3dModel model;
	private final Angles3d angles;

	/**
	 * Creates a rotation operation.
	 * @param model the model to be rotated
	 * @param angles the angles used by the rotation operation
	 * @throws IllegalValueException if either of the two parameters is null
	 */
	public Rotate(Abstract3dModel model, Angles3d angles) throws IllegalValueException {
		AssertValue.isNotNull(model, "The model should not be null for a rotation!");
		AssertValue.isNotNull(angles, "The angles should not be null for a rotation!");
		
		this.model = model;
		this.angles = angles;
	}

	/**
	 * This method is used internally by the {@link Abstract3dModel} - do not use it!
	 * @param angles the angles used by the rotate operation
	 * @return the string which represents the rotation in OpenSCAD
	 */
	public static String getRotate(Angles3d angles) {
		return "rotate("+angles+")";
	}
	
	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return new SCAD(getRotate(angles)).append(model.toScad(context));
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return model.getBoundaries().rotate(angles);
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Rotate(model, angles);
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return model.toCSG(context).transformed(TransformationFactory.getRotationMatrix(angles));
	}

	@Override
	protected Abstract3dModel innerSubModel(IScadGenerationContext context) {
		Abstract3dModel subModel = model.subModel(context);
		return subModel==null ? null : new Rotate(subModel, angles);
	}

    @Override
    protected List<Abstract3dModel> getChildrenModels() {
        return Collections.singletonList(model);
    }
}
