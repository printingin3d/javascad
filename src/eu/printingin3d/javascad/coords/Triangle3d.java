package eu.printingin3d.javascad.coords;

import java.util.Arrays;
import java.util.List;

import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.utils.AssertValue;

/**
 * An implementation of a 3D triangle. It is used by both the OpenSCAD and POVRay rendering, 
 * but in a completely different way. This class is also used by the Polyhedron class as input.  
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Triangle3d {
	private final Coords3d point1;
	private final Coords3d point2;
	private final Coords3d point3;
	
	/**
	 * Created the triangle by defining the three corner.
	 * @param point1 the first corner
	 * @param point2 the second corner
	 * @param point3 the third corner
	 * @throws IllegalValueException if any of the parameters is null
	 */
	public Triangle3d(Coords3d point1, Coords3d point2, Coords3d point3) throws IllegalValueException {
		AssertValue.isNotNull(point1, "point1 should not be null");
		AssertValue.isNotNull(point2, "point2 should not be null");
		AssertValue.isNotNull(point3, "point3 should not be null");

		this.point1 = point1;
		this.point2 = point2;
		this.point3 = point3;
	}

	/**
	 * Returns with the corners of the triangle as a list. It always contains three points 
	 * in an order as it was given in the constructor.
	 * @return the list of the corners
	 */
	public List<Coords3d> getPoints() {
		return Arrays.asList(point1, point2, point3);
	}
	
	/**
	 * Renders the triangle as is used by OpenSCAD: three index of a list of points - 
	 * that's why this method requires a coordinate list as a parameter.
	 * @param coords the list of coordinates which is used to determine the indexes
	 * @return the OpenSCAD version of the triangle
	 * @throws IllegalValueException if any of the three corners of the triangle cannot be 
	 * found in the given list
	 */
	public String toTriangleString(List<Coords3d> coords) throws IllegalValueException {
		int p1 = coords.indexOf(point1);
		int p2 = coords.indexOf(point2);
		int p3 = coords.indexOf(point3);
		
		AssertValue.isNotNegative(p1, "point1 ("+point1+") cannot be found in the coordinate list!");
		AssertValue.isNotNegative(p2, "point2 ("+point2+") cannot be found in the coordinate list!");
		AssertValue.isNotNegative(p3, "point3 ("+point3+") cannot be found in the coordinate list!");
		
		return "[" + p1 + ',' + p2 + ',' + p3 + ']';
	}
}
