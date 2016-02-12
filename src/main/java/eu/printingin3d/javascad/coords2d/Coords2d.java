package eu.printingin3d.javascad.coords2d;

import eu.printingin3d.javascad.coords.Coords3d;

/**
 * Immutable representation of a 2D coordinate.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Coords2d extends Abstract2d {
	/**
	 * The coordinate of the origin.
	 */
	public static final Coords2d ZERO = new Coords2d(0, 0); 

	/**
	 * Instantiating a new coordinate with the given coordinates.
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public Coords2d(double x, double y) {
		super(x, y);
	}
	
	/**
	 * Creating a 3D coordinate by defining the Z value.
	 * @param z Z coordinate
	 * @return the new 3D coordinate
	 */
	public Coords3d withZ(double z) {
		return new Coords3d(x, y, z);
	}
	
	/**
	 * Moving this coordinate by the given vector, but this object will be unchanged and 
	 * this method will create a new object with the new coordinates.
	 * @param move the vector used by the move
	 * @return a new coordinate instance which points to the new location
	 */
	public Coords2d move(Coords2d move) {
		return new Coords2d(x+move.x, y+move.y);
	}
	
	/**
	 * Creates a coordinate where the Y and Z fields are zero.
	 * @param x the X coordinate
	 * @return the newly create coordinate which points to (x,0,0)
	 */
	public static Coords2d xOnly(double x) {
		return new Coords2d(x, 0.0);
	}
	
	/**
	 * Creates a coordinate where the X and Z fields are zero.
	 * @param y the Y coordinate
	 * @return the newly create coordinate which points to (0,y,0)
	 */
	public static Coords2d yOnly(double y) {
		return new Coords2d(0.0, y);
	}
}
