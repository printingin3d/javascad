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
public class Cover extends Cube {

	/**
	 * Create this helper class.
	 * @param model model to be sliced
	 * @param direction the direction of the slicing
	 * @param sizeRate the rate of the part to be cut off
	 * @param lower which end should be cut off
	 */
	public Cover(Abstract3dModel model, Direction direction, double sizeRate, boolean lower) {
		super(calculateSize(model, direction, sizeRate));
		Boundaries3d b3d = model.getBoundaries();
		move(new Coords3d(
				b3d.getX().getMiddle(), 
				b3d.getY().getMiddle(),
				b3d.getZ().getMiddle()));
		if (lower) {
			moveLower(model, direction);
		} else {
			moveUpper(model, direction);
		}
	}

	private void moveLower(Abstract3dModel model, Direction direction) {
		switch (direction) {
		case X:
			align(Side.LEFT, model, true);
			move(Coords3d.xOnly(-1.0));
			break;
		case Y:
			align(Side.FRONT, model, true);
			move(Coords3d.yOnly(-1.0));
			break;
		case Z:
		default:
			align(Side.BOTTOM, model, true);
			move(Coords3d.zOnly(-1.0));
			break;
		}
	}
	
	private void moveUpper(Abstract3dModel model, Direction direction) {
		switch (direction) {
		case X:
			align(Side.RIGHT, model, true);
			move(Coords3d.xOnly(+1.0));
			break;
		case Y:
			align(Side.BACK, model, true);
			move(Coords3d.yOnly(+1.0));
			break;
		case Z:
		default:
			align(Side.TOP, model, true);
			move(Coords3d.zOnly(+1.0));
			break;
		}
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
