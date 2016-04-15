package eu.printingin3d.javascad.coords;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import eu.printingin3d.javascad.basic.Angle;

/**
 * Immutable representation of a 3D coordinate with useful helper methods.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Coords3d extends Basic3dFunc<Coords3d> {
	/**
	 * Represents the (1,0,0) coordinate.
	 */
	public static final Coords3d X = xOnly(1.0);
	/**
	 * Represents the (0,1,0) coordinate.
	 */
	public static final Coords3d Y = yOnly(1.0);
	/**
	 * Represents the (0,0,1) coordinate.
	 */
	public static final Coords3d Z = zOnly(1.0);
	
	/**
	 * Represents the (0,0,0) coordinate.
	 */
	public static final Coords3d ZERO = new Coords3d(0.0, 0.0, 0.0);
	
	/**
	 * Can be used to move the object that tiny bit that will solve the different annoying manifold issues
	 * and CSG rendering problems. 
	 */
	public static final Coords3d TINY = new Coords3d(0.001, 0.001, 0.001);
	/**
	 * Can be used to move the object that tiny bit that will solve the different annoying manifold issues
	 * and CSG rendering problems. 
	 */
	public static final Coords3d MINUS_TINY = TINY.inverse();
	
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
	 * Rotating this coordinate by the given angle, but this object will be unchanged and 
	 * this method will create a new object with the new coordinates.
	 * @param angles the angles used by the rotation
	 * @return a new coordinate instance which points to the new location
	 */
	public Coords3d rotate(Angles3d angles) {
		Coords3d result = this;
		if (!angles.isXZero()) {
			Angle xAngle = angles.getXAngle();
			result = new Coords3d(
					result.x,
					xAngle.cos()*result.y-xAngle.sin()*result.z, 
					xAngle.sin()*result.y+xAngle.cos()*result.z);
		}
		if (!angles.isYZero()) {
			Angle yAngle = angles.getYAngle();
			result = new Coords3d(
					yAngle.cos()*result.x+yAngle.sin()*result.z, 
					result.y, 
					-yAngle.sin()*result.x +yAngle.cos()*result.z);
		}
		if (!angles.isZZero()) {
			Angle zAngle = angles.getZAngle();
			result = new Coords3d(
					zAngle.cos()*result.x -zAngle.sin()*result.y, 
					zAngle.sin()*result.x +zAngle.cos()*result.y, 
					result.z);
		}
		return result;
	}

    /**
     * Returns the cross product of this vector and the specified vector.
     *
     * @param a the vector
     *
     * @return the cross product of this vector and the specified vector.
     */
    public Coords3d cross(Coords3d a) {
        return new Coords3d(
                this.y * a.z - this.z * a.y,
                this.z * a.x - this.x * a.z,
                this.x * a.y - this.y * a.x
        );
    }
    
    /**
     * Creates a collection of coordinates which contains all variances of this coordinates and 
     * their negated pair. The result contains at least one element (for ZERO) and up to eight elements.
     * @return a collection of coordinates which contains all variances
     */
    public Collection<Coords3d> createVariances() {
    	Set<Coords3d> result = new HashSet<>();
    	for (int i=0;i<8;i++) {
    		result.add(new Coords3d(
    				x*((i&0b001)==0 ? -1 : +1), 
    				y*((i&0b010)==0 ? -1 : +1), 
    				z*((i&0b100)==0 ? -1 : +1)));
    	}
    	return result;
    }

    /**
     * Returns a normalized copy of this vector with {@code length}.
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return a normalized copy of this vector with {@code length}
     */
    public Coords3d unit() {
        return this.mul(1.0/this.magnitude());
    }

    /**
     * Linearly interpolates between this and the specified vector.
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @param a vector
     * @param t interpolation value
     *
     * @return copy of this vector if {@code t = 0}; copy of {@code a} if {@code t = 1};
     * the point midway between this and the specified vector if {@code t = 0.5}
     */
    public Coords3d lerp(Coords3d a, double t) {
        return this.add(a.add(this.inverse()).mul(t));
    }

	@Override
	protected Coords3d create(double x, double y, double z) {
		return new Coords3d(x, y, z);
	}
}
