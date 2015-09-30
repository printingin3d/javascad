package eu.printingin3d.javascad.coords;

/**
 * Represents a normal vector. Only the X, Y and Z vectors are available.
 * @author Ivan
 */
public final class Normal3d extends Abstract3d {
	/**
	 * The normal X vector.
	 */
	public static final Normal3d X = new Normal3d(1.0, 0.0, 0.0);
	/**
	 * The normal Y vector.
	 */
	public static final Normal3d Y = new Normal3d(0.0, 1.0, 0.0);
	/**
	 * The normal Z vector.
	 */
	public static final Normal3d Z = new Normal3d(0.0, 0.0, 1.0);
	
	private Normal3d(double x, double y, double z) {
		super(x, y, z);
	}
	
	/**
	 * Calculates the normal value of the two vectors. Because there are only the 3 axis vector
	 * this will return with the x, y or z coordinate of the given vector. 
	 * @param vector the vector to be used
	 * @return the normal value
	 */
	public double normalValue(Abstract3d vector) {
		return x*vector.x+y*vector.y+z*vector.z;
	}
}
