package eu.printingin3d.javascad.coords2d;

import eu.printingin3d.javascad.utils.DoubleUtils;

/**
 * The base class of all 3D related coordinate classes such as dimensions, angles or coordinates.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Abstract2d {
	protected final double x;
	protected final double y;
	
	protected Abstract2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Checks if the X coordinate is zero.
	 * @return return true if and only if the X coordinate is zero
	 */
	public final boolean isXZero() {
		return DoubleUtils.equalsEps(x, 0.0);
	}
	
	/**
	 * Checks if the Y coordinate is zero.
	 * @return return true if and only if the Y coordinate is zero
	 */
	public final boolean isYZero() {
		return DoubleUtils.equalsEps(y, 0.0);
	}
	
	/**
	 * Checks if all three coordinates are zero, which means it is equals to 
	 * isXZero() && isYZero() && isZZero().
	 * @return return true if and only if all three coordinates are zero
	 */
	public final boolean isZero() {
		return isXZero() && isYZero();
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append('[').
				append(DoubleUtils.formatDouble(x)).append(',').
				append(DoubleUtils.formatDouble(y)).append(']').
				toString();
	}

	/**
	 * @return the X coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the Y coordinate
	 */
	public double getY() {
		return y;
	}

    /**
     * Returns the dot product of this vector and the specified vector.
     *
     * @param a the second vector
     *
     * @return the dot product of this vector and the specified vector
     */
    public double dot(Abstract2d a) {
        return this.x * a.x + this.y * a.y;
    }
    
    /**
     * Returns the square distance of the two point.
     * @param p the other point
     * @return the square distance of the two point
     */
    public double squareDist(Abstract2d p) {
		double dx = x-p.getX();
		double dy = y-p.getY();
		return dx*dx+dy*dy;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + DoubleUtils.hashCodeEps(x);
		result = prime * result + DoubleUtils.hashCodeEps(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Abstract2d other = (Abstract2d) obj;
		
		return DoubleUtils.equalsEps(x, other.x) &&
				DoubleUtils.equalsEps(y, other.y);
	}
}
