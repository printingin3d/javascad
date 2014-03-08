package eu.printingin3d.javascad.enums;

/**
 * This enum is used to set the plane of the rounding. A plane is denoted by the two coordinate it contains -
 * for example the XY plane is where you can have x any y coordinates, but the z is always the same.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public enum Plane {
	/**
	 * The X-Y plane.
	 */
	XY,
	/**
	 * The Y-Z plane.
	 */
	YZ,
	/**
	 * The X-Z plane.
	 */
	XZ,
	/**
	 * Denoting the "all planes" case.
	 */
	ALL
}
