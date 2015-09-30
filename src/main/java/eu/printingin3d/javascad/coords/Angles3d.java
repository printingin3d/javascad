package eu.printingin3d.javascad.coords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.printingin3d.javascad.exceptions.IllegalValueException;
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
	
	private static double simplicity(Coords3d temp, Normal3d normalVectorA, Normal3d normalVectorB) {
		double result = 0.0;
		double a = normalVectorA.normalValue(temp);
		double b = normalVectorB.normalValue(temp);
		if (!DoubleUtils.isZero(a)) {
			result = (a<0.0 ? -1.0 : 1.0) *toDeg(Math.acos(b/Math.sqrt(b*b+a*a)));
		}
		return result;
	}
	
	/**
	 * Internal use only.
	 * @author ivivan <ivivan@printingin3d.eu>
	 */
	private static class Config {
		private final Coords3d coord1;
		private final Coords3d coord2;
		private final Coords3d coord3;
		
		private final Normal3d norm1;
		private final Normal3d norm2;
		private final Normal3d norm3;
		
		public Config(Coords3d coord1, Coords3d coord2, Coords3d coord3,
				Normal3d norm1, Normal3d norm2, Normal3d norm3) {
			this.coord1 = coord1;
			this.coord2 = coord2;
			this.coord3 = coord3;
			this.norm1 = norm1;
			this.norm2 = norm2;
			this.norm3 = norm3;
		}

		@Override
		public String toString() {
			return "Config [coord1=" + coord1 + ", coord2=" + coord2
					+ ", coord3=" + coord3 + ", norm1=" + norm1 + ", norm2="
					+ norm2 + ", norm3=" + norm3 + "]";
		}
	}
	
	private static final List<Config> CONFIGS; 
	
	static {
		List<Coords3d> coords = Arrays.asList(Coords3d.X, Coords3d.Y, Coords3d.Z);
		
		List<Config> results = new ArrayList<>();
		for (Coords3d c1 : coords) {
			for (Coords3d c2 : coords) {
				for (Coords3d c3 : coords) {
					for (List<Normal3d> norms : Arrays.asList(
							Arrays.asList(Normal3d.X, Normal3d.Y, Normal3d.Z),
							Arrays.asList(Normal3d.X, Normal3d.Z, Normal3d.Y),
							Arrays.asList(Normal3d.Y, Normal3d.X, Normal3d.Z),
							Arrays.asList(Normal3d.Y, Normal3d.Z, Normal3d.X),
							Arrays.asList(Normal3d.Z, Normal3d.X, Normal3d.Y),
							Arrays.asList(Normal3d.Z, Normal3d.Y, Normal3d.X)
						)) {
						results.add(new Config(c1, c2, c3, norms.get(0), norms.get(1), norms.get(2)));
					}
				}
			}
		}
		CONFIGS = Collections.unmodifiableList(results);
	}
	
	/**
	 * Rotates the current value by the given value - this object won't change, but
	 * a new object will be created.
	 * @param delta the angle of the rotation
	 * @return the new object with the new angles
	 */
	public Angles3d rotate(Angles3d delta) {
		for (Config conf : CONFIGS) {
			Coords3d temp = conf.coord1.rotate(this).rotate(delta);
			double gamma = simplicity(temp, conf.norm1, conf.norm2);
			
			temp = conf.coord2.rotate(this).rotate(delta).rotate(zOnly(-gamma));
			double beta = simplicity(temp, conf.norm2, conf.norm3);
			
			temp = conf.coord3.rotate(this).rotate(delta).rotate(zOnly(-gamma)).rotate(yOnly(-beta));
			double alpha = simplicity(temp, conf.norm3, conf.norm1);
			
			if (Coords3d.xOnly(1.0).rotate(this).rotate(delta)
					.rotate(zOnly(-gamma)).rotate(yOnly(-beta)).rotate(xOnly(-alpha))
					.equals(Coords3d.xOnly(-1.0))) {
				alpha = -(180.0+alpha);
				beta = -beta;
				gamma = 180.0+gamma;
			}
			if (Coords3d.yOnly(1.0).rotate(this).rotate(delta)
					.rotate(zOnly(-gamma)).rotate(yOnly(-beta)).rotate(xOnly(-alpha))
					.equals(Coords3d.yOnly(-1.0))) {
				alpha = -(180.0+alpha);
			}
			
			Angles3d result = new Angles3d(alpha, beta, gamma);
			
			if (Abstract3d.closeEquals(Coords3d.X.rotate(this).rotate(delta), Coords3d.X.rotate(result)) &&
				Abstract3d.closeEquals(Coords3d.Y.rotate(this).rotate(delta), Coords3d.Y.rotate(result)) &&
				Abstract3d.closeEquals(Coords3d.Z.rotate(this).rotate(delta), Coords3d.Z.rotate(result))) {
				return result;
			}
		}

		throw new IllegalValueException("No solution found for adding "+this+" to "+delta+
				". Please send a bug report to the developer.");
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
		
		return Coords3d.X.rotate(this).equals(Coords3d.X.rotate(other)) &&
				Coords3d.Y.rotate(this).equals(Coords3d.Y.rotate(other)) &&
				Coords3d.Z.rotate(this).equals(Coords3d.Z.rotate(other));
	}
	
	@Override
	public int hashCode() {
		return -super.hashCode();
	}
}
