package eu.printingin3d.javascad.coords;


/**
 * Immutable representation of a 3D coordinate with useful helper methods.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Coords3d extends Abstract3d {
	/**
	 * Represents the (0,0,0) coordinate.
	 */
	public static final Coords3d ZERO = new Coords3d(0.0, 0.0, 0.0);
	
	/**
	 * Creates a coordinate where the Y and Z fields are zero.
	 * @param x the X coordinate
	 * @return the newly create coordinate which points to (x,0,0)
	 */
	public static Coords3d xOnly(double x) {
		return new Coords3d(x, 0.0, 0.0);
	}
	
	/**
	 * Creates a coordinate where the X and Z fields are zero.
	 * @param y the Y coordinate
	 * @return the newly create coordinate which points to (0,y,0)
	 */
	public static Coords3d yOnly(double y) {
		return new Coords3d(0.0, y, 0.0);
	}
	
	/**
	 * Creates a coordinate where the X and Y fields are zero.
	 * @param z the Z coordinate
	 * @return the newly create coordinate which points to (0,0,z)
	 */
	public static Coords3d zOnly(double z) {
		return new Coords3d(0.0, 0.0, z);
	}
	
	/**
	 * Calculates the midpoint of the ab segment.
	 * @param a first point
	 * @param b second point
	 * @return the coordinate of the midpoint of the ab segment
	 */
	public static Coords3d midPoint(Coords3d a, Coords3d b) {
		return new Coords3d((a.x+b.x)/2.0, (a.y+b.y)/2.0, (a.z+b.z)/2.0);
	}
	
	/**
	 * Instantiating a new coordinate with the given coordinates.
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 */
	public Coords3d(double x, double y, double z) {
		super(x,y,z);
	}
	
	/**
	 * Moving this coordinate by the given vector, but this object will be unchanged and 
	 * this method will create a new object with the new coordinates.
	 * @param move the vector used by the move
	 * @return a new coordinate instance which points to the new location
	 */
	public Coords3d move(Coords3d move) {
		return new Coords3d(x+move.x, y+move.y, z+move.z);
	}

	/**
	 * Rotating this coordinate by the given angle, but this object will be unchanged and 
	 * this method will create a new object with the new coordinates.
	 * @param angles the angles used by the rotation
	 * @return a new coordinate instance which points to the new location
	 */
	public Coords3d rotate(Angles3d angles) {
		Coords3d result = this;
		if (!angles.isXZero()) {
			result = new Coords3d(
					result.x, 
					Math.cos(angles.getXRad())*result.y-Math.sin(angles.getXRad())*result.z, 
					Math.sin(angles.getXRad())*result.y+Math.cos(angles.getXRad())*result.z);
		}
		if (!angles.isYZero()) {
			result = new Coords3d(
					Math.cos(angles.getYRad())*result.x+Math.sin(angles.getYRad())*result.z, 
					result.y, 
					-Math.sin(angles.getYRad())*result.x +Math.cos(angles.getYRad())*result.z);
		}
		if (!angles.isZZero()) {
			result = new Coords3d(
					Math.cos(angles.getZRad())*result.x -Math.sin(angles.getZRad())*result.y, 
					Math.sin(angles.getZRad())*result.x+Math.cos(angles.getZRad())*result.y, 
					result.z);
		}
		return result;
	}
	
	/**
	 * Return the inverse of this coordinate which means every coordinates will be negated.
	 * @return the inverse of this coordinate
	 */
	public Coords3d inverse() {
		return new Coords3d(-x, -y, -z);
	}
}
