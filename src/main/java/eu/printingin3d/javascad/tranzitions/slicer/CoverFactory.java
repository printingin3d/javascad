package eu.printingin3d.javascad.tranzitions.slicer;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Dims3d;
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Cube;
import eu.printingin3d.javascad.tranzitions.Direction;

/**
 * <p>Helper class to support the slicing functionality by Slicer. It is only used internally and 
 * shouldn't be created outside of the Slicer class!</p>
 * <p>It represents the cover cube(s) which is used to slicing the unnecessary parts of the model 
 * to be sliced.</p>
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public final class CoverFactory {

	private CoverFactory() {}
	
	/**
	 * Create a helper object to be used.
	 * @param model model to be sliced
	 * @param direction the direction of the slicing
	 * @param sizeRate the rate of the part to be cut off
	 * @param lower which end should be cut off
	 * @return the cover object which hides the proper part of the given model
	 */
	public static Abstract3dModel createCover(Abstract3dModel model, Direction direction, 
			double sizeRate, boolean lower) {
		Abstract3dModel result = new Cube(calculateSize(model, direction, sizeRate));
		Boundaries3d b3d = model.getBoundaries();
		result = result.move(new Coords3d(
				b3d.getX().getMiddle(), 
				b3d.getY().getMiddle(),
				b3d.getZ().getMiddle()));
		if (lower) {
			return moveLower(result, model, direction);
		} else {
			return moveUpper(result, model, direction);
		}
	}

	private static Abstract3dModel moveLower(Abstract3dModel result, Abstract3dModel model, Direction direction) {
		switch (direction) {
		case X:
			result = result
					.align(Side.LEFT, model, true)
					.move(Coords3d.xOnly(-1.0));
			break;
		case Y:
			result = result
					.align(Side.FRONT, model, true)
					.move(Coords3d.yOnly(-1.0));
			break;
		case Z:
		default:
			result = result
					.align(Side.BOTTOM, model, true)
					.move(Coords3d.zOnly(-1.0));
			break;
		}
		return result;
	}
	
	private static Abstract3dModel moveUpper(Abstract3dModel result, Abstract3dModel model, Direction direction) {
		switch (direction) {
		case X:
			result = result
					.align(Side.RIGHT, model, true)
					.move(Coords3d.xOnly(+1.0));
			break;
		case Y:
			result = result
					.align(Side.BACK, model, true)
					.move(Coords3d.yOnly(+1.0));
			break;
		case Z:
		default:
			result = result
					.align(Side.TOP, model, true)
					.move(Coords3d.zOnly(+1.0));
			break;
		}
		return result;
	}

	private static Dims3d calculateSize(Abstract3dModel model, Direction direction, double sizeRate) {
		Boundaries3d b3d = model.getBoundaries();
		switch (direction) {
		case X:
			return new Dims3d(
					b3d.getX().getSize()*sizeRate+1, 
					b3d.getY().getSize()+1, 
					b3d.getZ().getSize()+1);
		case Y:
			return new Dims3d(
					b3d.getX().getSize()+1, 
					b3d.getY().getSize()*sizeRate+1, 
					b3d.getZ().getSize()+1);
		case Z:
			return new Dims3d(
					b3d.getX().getSize()+1, 
					b3d.getY().getSize()+1, 
					b3d.getZ().getSize()*sizeRate+1);
		default:
			throw new IllegalValueException("Unknown direction: "+direction);
		}
	}

}
