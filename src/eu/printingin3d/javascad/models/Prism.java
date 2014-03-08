package eu.printingin3d.javascad.models;

import java.util.ArrayList;
import java.util.List;

import eu.printingin3d.javascad.coords.Coords2d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Triangle3d;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.exceptions.LanguageNotSupportedException;
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
	public Prism(double length, double r, int numberOfSides) throws IllegalValueException {
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
	public Prism(double length, double r1, double r2, int numberOfSides) throws IllegalValueException {
		super(length, r1, r2);
		AssertValue.isNotNegative(numberOfSides, 
				"The number of sides should be positive, but "+numberOfSides);
		this.numberOfSides = numberOfSides;
	}

	private List<Coords2d> getCirclePoints(double radius) {
		List<Coords2d> result = new ArrayList<>();
		for (int i=0;i<numberOfSides;i++) {
			double alpha = 2*Math.PI*i/numberOfSides;
			result.add(new Coords2d(radius*Math.sin(alpha), -radius*Math.cos(alpha)));
		}
		result.add(result.get(0));
		return result;
	}
	
	private static String listToString(List<? extends Object> list) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : list) {
			sb.append(obj);
		}
		return sb.toString();
	}
	
	private List<Triangle3d> getTrianglesOfBottomAndTop() {
		double z = length/2.0;
		List<Triangle3d> result = new ArrayList<>();
		Coords3d prevTop = null;
		Coords3d prevBottom = null;
		List<Coords2d> topCirclePoints = getCirclePoints(topRadius);
		List<Coords2d> bottomCirclePoints = getCirclePoints(bottomRadius);
		for (int i=0;i<topCirclePoints.size();i++) {
			Coords3d top = topCirclePoints.get(i).withZ(z);
			Coords3d bottom = bottomCirclePoints.get(i).withZ(-z);
			if (i>0) {
				result.add(new Triangle3d(Coords3d.zOnly(z), top, prevTop));
				result.add(new Triangle3d(Coords3d.zOnly(-z), bottom, prevBottom));
				
				result.add(new Triangle3d(prevBottom, prevTop, top));
				result.add(new Triangle3d(prevBottom, bottom, top));
			}
			
			prevTop = top;
			prevBottom = bottom;
		}
		return result;
	}
	
	@Override
	protected String innerToScad() {
		if (DoubleUtils.equalsEps(bottomRadius, topRadius)) {
			switch (Language.getCurrent()) {
			case OpenSCAD:
				return "cylinder(h="+DoubleUtils.formatDouble(length)+
						", r="+DoubleUtils.formatDouble(bottomRadius)+
						", $fn="+numberOfSides+", center=true);\n";
			case POVRay:
				return "prism { linear_spline "+
					DoubleUtils.formatDouble(-length/2)+","+DoubleUtils.formatDouble(length/2)+","+
					DoubleUtils.formatDouble(numberOfSides+1)+"," +
					listToString(getCirclePoints(bottomRadius))+Abstract3dModel.ATTRIBUTES_PLACEHOLDER+"}";
			default:
				throw new LanguageNotSupportedException();
			}
		}
		switch (Language.getCurrent()) {
		case OpenSCAD:
			return "cylinder(h="+DoubleUtils.formatDouble(length)+
					", r1="+DoubleUtils.formatDouble(bottomRadius)+
					", r2="+DoubleUtils.formatDouble(topRadius)+
					", $fn="+numberOfSides+", center=true);\n";
		case POVRay:
			return "mesh {"+
					Triangle3d.trianglesToString(getTrianglesOfBottomAndTop())+
					Abstract3dModel.ATTRIBUTES_PLACEHOLDER+"}";
		default:
			throw new LanguageNotSupportedException();
		}
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Prism(length, bottomRadius, topRadius, numberOfSides);
	}
}
