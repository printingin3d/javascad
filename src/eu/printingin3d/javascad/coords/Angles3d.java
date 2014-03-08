package eu.printingin3d.javascad.coords;

import eu.printingin3d.javascad.utils.DoubleUtils;

/**
 * Denotes the 3D angles - 3 number which denotes the angles on the X, Y and Z planes
 * This class is used for the rotation operations.
 * The angle on each plane will be normalized, which means the result will be always between
 * -180 and 180 degrees.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Angles3d extends Abstract3d {
	/**
	 * the zero angle, which does nothing - usually used as a starting point.
	 */
	public static final Angles3d ZERO = new Angles3d(0.0, 0.0, 0.0);
	
	/**
	 * rotating on the X plane by +90 degrees.
	 */
	public static final Angles3d ROTATE_PLUS_X = xOnly(90.0);
	/**
	 * rotating on the X plane by -90 degrees.
	 */
	public static final Angles3d ROTATE_MINUS_X = xOnly(-90.0);
	/**
	 * rotating on the Y plane by +90 degrees.
	 */
	public static final Angles3d ROTATE_PLUS_Y = yOnly(90.0);
	/**
	 * rotating on the Y plane by -90 degrees.
	 */
	public static final Angles3d ROTATE_MINUS_Y = yOnly(-90.0);
	/**
	 * rotating on the Z plane by +90 degrees.
	 */
	public static final Angles3d ROTATE_PLUS_Z = zOnly(90.0);
	/**
	 * rotating on the Z plane by -90 degrees.
	 */
	public static final Angles3d ROTATE_MINUS_Z = zOnly(-90.0);
	
	/**
	 * Rotating only around the X axis.
	 * @param x the angle in degree
	 * @return the Angles3d object represents x degree rotation around the X axis
	 */
	public static Angles3d xOnly(double x) {
		return new Angles3d(x, 0.0, 0.0);
	}
	
	/**
	 * Rotating only around the Y axis.
	 * @param y the angle in degree
	 * @return the Angles3d object represents y degree rotation around the Y axis
	 */
	public static Angles3d yOnly(double y) {
		return new Angles3d(0.0, y, 0.0);
	}
	
	/**
	 * Rotating only around the Z axis.
	 * @param z the angle in degree
	 * @return the Angles3d object represents z degree rotation around the Z axis
	 */
	public static Angles3d zOnly(double z) {
		return new Angles3d(0.0, 0.0, z);
	}
	
	/**
	 * Create a new angle object with the given X, Y and Z value.
	 * @param x the angle on the X plane
	 * @param y the angle on the Y plane
	 * @param z the angle on the Z plane
	 */
	public Angles3d(double x, double y, double z) {
		super(normalize(x), normalize(y), normalize(z));
	}
	
	private double simplicity(Coords3d temp, Normal3d normalAngle, Normal3d normalVectorA, Normal3d normalVectorB) {
		double result = 0.0;
		double a = normalVectorA.normalValue(temp);
		double b = normalVectorB.normalValue(temp);
		if (!DoubleUtils.isZero(a)) {
			result = (a<0.0 ? -1.0 : 1.0) *toDeg(Math.acos(b/Math.sqrt(b*b+a*a)));
		}
		return result;
	}
	
	/**
	 * Rotates the current value by the given value - this object won't change, but
	 * a new object will be created.
	 * @param delta the angle of the rotation
	 * @return the new object with the new angles
	 */
	public Angles3d rotate(Angles3d delta) {
		Coords3d temp = Coords3d.xOnly(1.0).rotate(this).rotate(delta);
		double gamma = simplicity(temp, Normal3d.Z, Normal3d.Y, Normal3d.X);
		
		temp = Coords3d.zOnly(1.0).rotate(this).rotate(delta).rotate(zOnly(-gamma));
		double beta = simplicity(temp, Normal3d.Y, Normal3d.X, Normal3d.Z);
		
		temp = Coords3d.yOnly(1.0).rotate(this).rotate(delta).rotate(zOnly(-gamma)).rotate(yOnly(-beta));
		double alpha = simplicity(temp, Normal3d.X, Normal3d.Z, Normal3d.Y);

		if (Coords3d.xOnly(1.0).rotate(this).rotate(delta).rotate(zOnly(-gamma)).rotate(yOnly(-beta)).rotate(xOnly(-alpha))
				.equals(Coords3d.xOnly(-1.0))) {
			alpha = -(180.0+alpha);
			beta = -beta;
			gamma = 180.0+gamma;
		}
		if (Coords3d.yOnly(1.0).rotate(this).rotate(delta).rotate(zOnly(-gamma)).rotate(yOnly(-beta)).rotate(xOnly(-alpha))
				.equals(Coords3d.yOnly(-1.0))) {
			alpha = -(180.0+alpha);
		}
		
		return new Angles3d(alpha, beta, gamma);
	}
	
	protected double getXRad() {
		return toRad(x);
	}
	
	protected double getYRad() {
		return toRad(y);
	}
	
	protected double getZRad() {
		return toRad(z);
	}
	
	private static double normalize(double a) {
		return Math.IEEEremainder(a, 360.0);
	}
	
	private static double toRad(double deg) {
		return deg/180.0*Math.PI;
	}
	
	private static double toDeg(double rad) {
		return rad*180.0/Math.PI;
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
		Angles3d other = (Angles3d) obj;
		
		if (DoubleUtils.equalsEps(x, other.x) &&
				DoubleUtils.equalsEps(y, other.y) &&
				DoubleUtils.equalsEps(z, other.z)) {
			return true;
		}
		
		return Coords3d.xOnly(1.0).rotate(this).equals(Coords3d.xOnly(1.0).rotate(other)) &&
				Coords3d.yOnly(1.0).rotate(this).equals(Coords3d.yOnly(1.0).rotate(other)) &&
				Coords3d.zOnly(1.0).rotate(this).equals(Coords3d.zOnly(1.0).rotate(other));
	}
	
	@Override
	public int hashCode() {
		return -super.hashCode();
	}
}
