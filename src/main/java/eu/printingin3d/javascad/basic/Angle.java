package eu.printingin3d.javascad.basic;

import eu.printingin3d.javascad.utils.DoubleUtils;

/**
 * Immutable representation of an angle. Has convenient methods to handle the angles.
 */
public final class Angle extends BasicFunc<Angle> {
	/**
	 * Constant for the zero angle.
	 */
	public static final Angle ZERO = new Angle(0.0);
	/**
	 * Constant for 45 degrees.
	 */
	public static final Angle A45 = new Angle(Math.PI*0.25);
	/**
	 * Constant for the quarter of the full circle angle - 90 degrees.
	 */
	public static final Angle A90 = new Angle(Math.PI*0.5);
	/**
	 * Constant for the half of the full circle angle - 180 degrees.
	 */
	public static final Angle A180 = new Angle(Math.PI);
	/**
	 * Constant for the full circle angle - 360 degrees.
	 */
	public static final Angle A360 = new Angle(Math.PI*2.0);

	private Angle(double radian) {
		super(radian);
	}
	
	/**
	 * Creates an angle object based on the given radian value.
	 * @param radian the radian value to be used
	 * @return an Angle object which represents the given angle
	 */
	public static Angle ofRadian(double radian) {
		return DoubleUtils.isZero(radian) ? ZERO : new Angle(radian);
	}
	
	/**
	 * Creates an angle object based on the given degree value.
	 * @param degree the degree value to be used
	 * @return an Angle object which represents the given angle
	 */
	public static Angle ofDegree(double degree) {
		return ofRadian(Math.toRadians(degree));
	}
	
	/**
	 * Creates an angle object which corresponds the given coordinates. The order of the
	 * parameters are the same as the Math.atan2 method. 
	 * @param y the y coordinate 
	 * @param x the x coordinate
	 * @return an Angle object which represents the calculated angle
	 */
	public static Angle atan2(double y, double x) {
		return new Angle(Math.atan2(y, x));
	}
	
	/**
	 * Returns the radian value for this angle.
	 * @return the radian value for this angle.
	 */
	public double asRadian() {
		return value;
	}
	
	/**
	 * Returns the degree value for this angle.
	 * @return the degree value for this angle.
	 */
	public double asDegree() {
		return Math.toDegrees(value);
	}
	
	/**
	 * The sine value of this angle.
	 * @return sine value of this angle
	 */
	public double sin() {
		return Math.sin(value);
	}
	
	/**
	 * The cosine value of this angle.
	 * @return cosine value of this angle
	 */
	public double cos() {
		return Math.cos(value);
	}
	
	/**
	 * Normalize this angle to be within -180 and the 180 degree.
	 * @return a new object representing the normalized angle.
	 */
	public Angle normalize() {
		return new Angle(Math.IEEEremainder(value, Math.PI*2.0));
	}

	@Override
	public String toString() {
		return DoubleUtils.formatDouble(asDegree());
	}

	@Override
	protected Angle create(double value) {
		return ofRadian(value);
	}

	@Override
	protected Angle thisOne() {
		return this;
	}
}
