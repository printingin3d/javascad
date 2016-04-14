package eu.printingin3d.javascad.coords;

import static eu.printingin3d.javascad.utils.AssertValue.isNotNegative;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.utils.DoubleUtils;

/**
 * An immutable representation of a radius of a circle, cylinder or sphere.
 */
public final class Radius {
	/**
	 * Creates a Radius object based on the given radius.
	 * @param radius the radius to be used
	 * @return a Radius object based on the given radius
	 */
	public static Radius fromRadius(double radius) {
		return new Radius(radius);
	}
	
	/**
	 * Creates a Radius object based on the given diameter.
	 * @param diameter the diameter to be used
	 * @return a Radius object based on the given diameter
	 */
	public static Radius fromDiameter(double diameter) {
		return new Radius(diameter*0.5);
	}
	
	private final double radius;

	private Radius(double radius) {
		isNotNegative(radius, "Radius should be positive, but bottom radius is "+radius);
		this.radius = radius;
	}

	/**
	 * Returns with the radius.
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}
	
	/**
	 * Returns with the diameter.
	 * @return the diameter
	 */
	public double getDiameter() {
		return radius*2.0;
	}
	
	/**
	 * Calculates the coordinate for the given alpha angle for this radius.
	 * @param alpha the angle
	 * @return the coordinate
	 */
	public Coords2d toCoordinate(double alpha) {
		return new Coords2d(Math.sin(alpha)*radius, Math.cos(alpha)*radius);
	}
	
	/**
	 * Adds the given delta to the radius and returns with a new Radius object.
	 * @param delta the delta radius
	 * @return the new object
	 */
	public Radius plusRadius(double delta) {
		return new Radius(radius+delta);
	}
	
	/**
	 * Adds the given delta to the diameter and returns with a new Radius object.
	 * @param delta the delta diameter
	 * @return the new object
	 */
	public Radius plusDiameter(double delta) {
		return new Radius(radius+delta*0.5);
	}
	
	@Override
	public String toString() {
		return DoubleUtils.formatDouble(radius);
	}

	@Override
	public int hashCode() {
		return DoubleUtils.hashCodeEps(radius);
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
		Radius other = (Radius) obj;
		return DoubleUtils.equalsEps(radius, other.radius);
	}
}
