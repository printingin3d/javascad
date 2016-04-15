package eu.printingin3d.javascad.models;

import eu.printingin3d.javascad.basic.Radius;
import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.utils.AssertValue;
import eu.printingin3d.javascad.utils.DoubleUtils;

/**
 * Represents a prism or a pyramid.
 * It is a descendant of Abstract3dModel, 
 * which means you can use the convenient methods on prisms too.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Prism extends Cylinder {
	private final int numberOfSides;
	
	/**
	 * Creates a prism with a given length, radius and the number of sides.
	 * @param length the length of the prism
	 * @param r the radius of the prism
	 * @param numberOfSides the side number of the prism
	 * @throws IllegalValueException if any of its parameters is negative 
	 */
	public Prism(double length, Radius r, int numberOfSides) throws IllegalValueException {
		super(length, r);
		AssertValue.isNotNegative(numberOfSides, 
				"The number of sides should be positive, but "+numberOfSides);
		this.numberOfSides = numberOfSides;
	}
	
	/**
	 * Creates a prism, which base and top have different radius.
	 * If one of the two radiuses is zero the result is a pyramid. 
	 * If the two radiuses are the same the result is the same as {@link #Prism(double, double, int)}.
	 * @param length the length of the prism
	 * @param r1 the bottom radius of the prism
	 * @param r2 the top radius of the prism
	 * @param numberOfSides the side number of the prism
	 * @throws IllegalValueException if any of its parameters is negative 
	 */
	public Prism(double length, Radius r1, Radius r2, int numberOfSides) throws IllegalValueException {
		super(length, r1, r2);
		AssertValue.isNotNegative(numberOfSides, 
				"The number of sides should be positive, but "+numberOfSides);
		this.numberOfSides = numberOfSides;
	}
	
	/**
	 * Creates a prism with a given length, radius and the number of sides.
	 * @param length the length of the prism
	 * @param r the radius of the prism
	 * @param numberOfSides the side number of the prism
	 * @throws IllegalValueException if any of its parameters is negative
	 * @deprecated use the constructor with Radius parameters instead of doubles 
	 */
	@Deprecated
	public Prism(double length, double r, int numberOfSides) throws IllegalValueException {
		this(length, Radius.fromRadius(r), numberOfSides);
	}
	
	/**
	 * Creates a prism, which base and top have different radius.
	 * If one of the two radiuses is zero the result is a pyramid. 
	 * If the two radiuses are the same the result is the same as {@link #Prism(double, double, int)}.
	 * @param length the length of the prism
	 * @param r1 the bottom radius of the prism
	 * @param r2 the top radius of the prism
	 * @param numberOfSides the side number of the prism
	 * @throws IllegalValueException if any of its parameters is negative 
	 * @deprecated use the constructor with Radius parameters instead of doubles 
	 */
	@Deprecated
	public Prism(double length, double r1, double r2, int numberOfSides) throws IllegalValueException {
		this(length, Radius.fromRadius(r1), Radius.fromRadius(r2), numberOfSides);
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		if (bottomRadius.equals(topRadius)) {
			return new SCAD("cylinder(h="+DoubleUtils.formatDouble(length)+
					", r="+bottomRadius+
					", $fn="+numberOfSides+", center=true);\n");
		}
		return new SCAD("cylinder(h="+DoubleUtils.formatDouble(length)+
				", r1="+bottomRadius+
				", r2="+topRadius+
				", $fn="+numberOfSides+", center=true);\n");
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Prism(length, bottomRadius, topRadius, numberOfSides);
	}
}
