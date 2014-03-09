package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.utils.AssertValue.isNotNegative;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.utils.DoubleUtils;

/**
 * Represents a cylinder, a truncated cone or a cone. It is a descendant of {@link Abstract3dModel}, 
 * which means you can use the convenient methods on cylinders too.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Cylinder extends Abstract3dModel {
	protected final double length;
	protected final double bottomRadius;
	protected final double topRadius;
	
	/**
	 * Creates a truncated cone. If one of the two radiuses is zero the result is a cone. 
	 * If the two radiuses are the same the result is a cylinder.
	 * @param length the length of the cylinder
	 * @param bottomRadius the bottom radius of the cylinder
	 * @param topRadius the top radius of the cylinder
	 * @throws IllegalValueException if the length or any any of the two radius parameter is negative 
	 */
	public Cylinder(double length, double bottomRadius, double topRadius) throws IllegalValueException {
		super();
		isNotNegative(length, "The length should be positive, but "+length);
		isNotNegative(bottomRadius, "Both radius should be positive, but bottom radius is "+bottomRadius);
		isNotNegative(topRadius, "Both radius should be positive, but top radius is "+topRadius);
		
		this.length = length;
		this.bottomRadius = bottomRadius;
		this.topRadius = topRadius;
	}
	
	/**
	 * Creates a cylinder with a given length and radius.
	 * @param length the length of the cylinder
	 * @param r the radius of the cylinder
	 * @throws IllegalValueException if the length or the radius parameter is negative 
	 */
	public Cylinder(double length, double r) throws IllegalValueException {
		super();
		isNotNegative(length, "The length should be positive, but "+length);
		isNotNegative(r, "The radius should be positive, but r1 is "+r);
		
		this.length = length;
		this.bottomRadius = r;
		this.topRadius = r;
	}

	@Override
	protected String innerToScad() {
		if (DoubleUtils.equalsEps(bottomRadius, topRadius)) {
			return "cylinder(h="+DoubleUtils.formatDouble(length)+
						", r="+DoubleUtils.formatDouble(bottomRadius)+", center=true);\n";
		}
		return "cylinder(h="+DoubleUtils.formatDouble(length)+
					", r1="+DoubleUtils.formatDouble(bottomRadius)+
					", r2="+DoubleUtils.formatDouble(topRadius)+", center=true);\n";
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		double r = Math.max(bottomRadius, topRadius);
		double z = length/2.0;
		return new Boundaries3d(
				new Boundary(-r, r), 
				new Boundary(-r, r),
				new Boundary(-z, z));
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Cylinder(length, bottomRadius, topRadius);
	}
}
