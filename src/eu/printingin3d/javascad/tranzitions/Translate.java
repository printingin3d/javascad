package eu.printingin3d.javascad.tranzitions;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.exceptions.LanguageNotSupportedException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.utils.AssertValue;

/**
 * This represents a move transition, but used rarely, because the convenient
 * methods of {@link Abstract3dModel} replace it most of the time.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Translate extends Abstract3dModel {
	private final Abstract3dModel model;
	private final Coords3d move;

	/**
	 * Creates a move operation.
	 * @param model the model to be moved
	 * @param move the coordinates used by the move operation
	 * @throws IllegalValueException if either of the two parameters is null
	 */
	public Translate(Abstract3dModel model, Coords3d move) throws IllegalValueException {
		AssertValue.isNotNull(model, "Model must not be null for translation!");
		AssertValue.isNotNull(move, "Move must not be null for translation!");
		
		this.model = model;
		this.move = move;
	}

	/**
	 * This method is used internally by the {@link Abstract3dModel} - do not use it!
	 * @param move the coordinates used by the move operation
	 * @return the string which represents the move in OpenSCAD
	 */
	public static String getTranslate(Coords3d move) {
		if (move.isZero()) {
			return "";
		}
		switch (Language.getCurrent()) {
		case OpenSCAD:
			return "translate("+move+")";
		case POVRay:
			return "translate "+move;
		default:
			throw new LanguageNotSupportedException();
		}
	}

	@Override
	protected String innerToScad() {
		switch (Language.getCurrent()) {
		case OpenSCAD:
			return getTranslate(move)+model.toScad();
		case POVRay:
			return "object {"+model.toScad()+getTranslate(move)+Abstract3dModel.ATTRIBUTES_PLACEHOLDER+"}";
		default:
			throw new LanguageNotSupportedException();			
		}
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return model.getBoundaries().move(move);
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Translate(model.cloneModel(), move);
	}
}
