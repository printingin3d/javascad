package eu.printingin3d.javascad.tranzitions;

import eu.printingin3d.javascad.context.IColorGenerationContext;
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
 * This represents a move transition, but used rarely, because the convenient
 * methods of {@link Abstract3dModel} replace it most of the time.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Translate extends Complex3dModel {
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
		return "translate("+move+")";
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return new SCAD(getTranslate(move)).append(model.toScad(context));
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return model.getBoundaries().move(move);
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Translate(model, move);
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return model.toCSG(context).transformed(TransformationFactory.getTranlationMatrix(move));
	}

	@Override
	protected Abstract3dModel innerSubModel(IScadGenerationContext context) {
		Abstract3dModel subModel = model.subModel(context);
		return subModel==null ? null : new Translate(subModel, move);
	}
}
