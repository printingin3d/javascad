package eu.printingin3d.javascad.tranzitions;

import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.exceptions.LanguageNotSupportedException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.utils.AssertValue;

/**
 * This represents a rotate transition, but used rarely, because the convenient
 * methods of {@link Abstract3dModel} replace it most of the time.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Rotate extends Abstract3dModel {
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
		switch (Language.getCurrent()) {
		case OpenSCAD:
			return "rotate("+angles+")";
		case POVRay:
			return "rotate"+angles;
		default:
			throw new LanguageNotSupportedException();			
		}
	}
	
	@Override
	protected String innerToScad() {
		switch (Language.getCurrent()) {
		case OpenSCAD:
			return getRotate(angles)+model.toScad();
		case POVRay:
			return "object {"+model.toScad()+getRotate(angles)+Abstract3dModel.ATTRIBUTES_PLACEHOLDER+"}";
		default:
			throw new LanguageNotSupportedException();			
		}
		
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return model.getBoundaries().rotate(angles);
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Rotate(model.cloneModel(), angles);
	}
}
