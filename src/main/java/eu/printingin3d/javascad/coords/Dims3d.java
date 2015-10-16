package eu.printingin3d.javascad.coords;

import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.utils.AssertValue;

/**
 * Denotes the size of a 3D object, thus negative values are forbidden.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Dims3d extends Basic3dFunc<Dims3d> {

	/**
	 * Creates a new object with the given values. 
	 * @param x the x value
	 * @param y the y value
	 * @param z the z value
	 * @throws IllegalValueException if any parameter is negative
	 */
	public Dims3d(double x, double y, double z) throws IllegalValueException {
		super(x, y, z);
		AssertValue.isNotNegative(x, "All dimensions should be positive, but x is "+x);
		AssertValue.isNotNegative(y, "All dimensions should be positive, but y is "+y);
		AssertValue.isNotNegative(z, "All dimensions should be positive, but z is "+z);
	}

	/**
	 * Increases the dimension by 1 in every direction. 
	 * @return a new dimension object which is bigger than this by 1.
	 */
	public Dims3d increase() {
		return new Dims3d(x+1.0, y+1.0, z+1.0);
	}

	@Override
	protected Dims3d create(double x, double y, double z) {
		return new Dims3d(x, y, z);
	}
}