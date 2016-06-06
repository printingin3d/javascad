package eu.printingin3d.javascad.utils;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.enums.Plane;
import eu.printingin3d.javascad.exceptions.IllegalValueException;

/**
 * <p>Internal class used by the Abstract3dModel used to represent the properties 
 * of the rounding operation.</p>
 * <p>This class is immutable.</p>
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class RoundProperties {
	private final Plane plane;
	private final double radius;
	
	/**
	 * Creates the properties with the given radius on the given plane. 
	 * @param plane the plane used by the rounding
	 * @param radius the radius of the rounding
	 */
	public RoundProperties(Plane plane, double radius) {
		this.plane = plane;
		this.radius = radius;
	}
	
	/**
	 * Generates the OpenSCAD representation of the cylinder used by the rounding operation
	 * with the necessary rotation if needed.
	 * @return the OpenSCAD code of the cylinder
	 */
	public String getRounding() {
		String rotation;
		switch (plane) {
		case ALL:
			return "sphere(r="+DoubleUtils.formatDouble(radius)+");";
		case YZ: rotation = "rotate([0,90,0])"; break;
		case XZ: rotation = "rotate([90,0,0])"; break;
		default: rotation = ""; break;
		}
		return rotation+"cylinder(r="+DoubleUtils.formatDouble(radius)+", h=0.00001, center=true);";
	}
	
	/**
	 * Calculates the size increase of the rounding operation. 
	 * This is always equals to the radius on those planes where the rounding takes effect. 
	 * @return the calculated increase of the rounding
	 */
	public Boundaries3d getRoundingSize() {
		Boundary b = Boundary.createSymmetricBoundary(radius);
		switch (plane) {
		case XY:
			return new Boundaries3d(b, b, Boundary.EMPTY);
		case YZ:
			return new Boundaries3d(Boundary.EMPTY, b, b);
		case XZ:
			return new Boundaries3d(b, Boundary.EMPTY, b);
		case ALL:
			return new Boundaries3d(b, b, b);
		default:
			throw new IllegalValueException("Unknown plane type: "+plane);
		}
	}
}