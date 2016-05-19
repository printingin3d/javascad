package eu.printingin3d.javascad.basic;

import static eu.printingin3d.javascad.utils.AssertValue.isNotNegative;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.utils.DoubleUtils;

/**
 * An immutable representation of a radius of a circle, cylinder or sphere.
 */
public final class Radius extends BasicFunc<Radius> {
	/**
	 * The ZERO Radius object.
	 */
	public static final Radius ZERO = new Radius(0);
	
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
	
	private Radius(double radius) {
		super(radius);
		isNotNegative(radius, "Radius should be positive, but bottom radius is "+radius);
	}

	/**
	 * Returns with the radius.
	 * @return the radius
	 */
	public double getRadius() {
		return value;
	}
	
	/**
	 * Returns with the diameter.
	 * @return the diameter
	 */
	public double getDiameter() {
		return value*2.0;
	}
	
	/**
	 * Calculates the coordinate for the given alpha angle for this radius.
	 * @param alpha the angle
	 * @return the coordinate
	 */
	public Coords2d toCoordinate(Angle alpha) {
		return new Coords2d(alpha.sin()*value, alpha.cos()*value);
	}
	
	/**
	 * Adds the given delta to the radius and returns with a new Radius object.
	 * @param delta the delta radius
	 * @return the new object
	 */
	public Radius plusRadius(double delta) {
		return new Radius(value+delta);
	}
	
	/**
	 * Adds the given delta to the diameter and returns with a new Radius object.
	 * @param delta the delta diameter
	 * @return the new object
	 */
	public Radius plusDiameter(double delta) {
		return new Radius(value+delta*0.5);
	}
	
	@Override
	public String toString() {
		return DoubleUtils.formatDouble(value);
	}

	@Override
	protected Radius create(double value) {
		return new Radius(value);
	}

	@Override
	protected Radius thisOne() {
		return this;
	}
}
