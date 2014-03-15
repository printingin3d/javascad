package eu.printingin3d.javascad.coords2d;

import eu.printingin3d.javascad.utils.AssertValue;

/**
 * Denotes the size of a 2D object, thus negative values are forbidden.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Dims2d extends Abstract2d {

	/**
	 * Creates a new object with the given values. 
	 * @param x the x value
	 * @param y the y value
	 * @throws IllegalValueException if any parameter is negative
	 */
	public Dims2d(double x, double y) {
		super(x, y);
		AssertValue.isNotNegative(x, "All dimensions should be positive, but x is "+x);
		AssertValue.isNotNegative(y, "All dimensions should be positive, but y is "+y);
	}

}
