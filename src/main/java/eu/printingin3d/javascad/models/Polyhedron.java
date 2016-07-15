package eu.printingin3d.javascad.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Triangle3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.utils.AssertValue;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;
import eu.printingin3d.javascad.vrl.Polygon;

/**
 * <p>
 * Represents a set of triangles. It is good to know that some operations - such
 * as difference or intersection - are not always works perfectly with this
 * object.
 * </p>
 * 
 * @author Rob van der Veer
 */
public class Polyhedron extends Atomic3dModel {

	private final List<Triangle3d> triangles;

	/**
	 * Constructs the object with the given triangles.
	 * 
	 * @param triangles
	 *            the triangles used to create this object
	 * @throws IllegalValueException
	 *             thrown if the given list is empty
	 */
	public Polyhedron(List<Triangle3d> triangles) throws IllegalValueException {
		AssertValue.isNotEmpty(triangles,
				"The triangle list should not be empty!");

		this.triangles = triangles;
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		List<Coords3d> points = getPoints();
		Coords3d minCorner = points.stream()
			.reduce((a, b) -> new Coords3d(
					Double.min(a.getX(), b.getX()), 
					Double.min(a.getY(), b.getY()), 
					Double.min(a.getZ(), b.getZ()))).get();
		Coords3d maxCorner = points.stream()
				.reduce((a, b) -> new Coords3d(
						Double.max(a.getX(), b.getX()), 
						Double.max(a.getY(), b.getY()), 
						Double.max(a.getZ(), b.getZ()))).get();
		return new Boundaries3d(minCorner, maxCorner);
	}

	private List<Coords3d> getPoints() {
		return new ArrayList<>(triangles.stream().flatMap(t -> t.getPoints().stream())
			.collect(Collectors.toSet()));
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Polyhedron(new ArrayList<>(triangles));
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		List<Coords3d> points = getPoints();

		SCAD b = new SCAD("polyhedron(");
		b = addPoints(b, points)
				.append(",");
		b = addTriangles(b, points)
				.append("\n);");
		return b;
	}

	private SCAD addPoints(SCAD b, List<Coords3d> points) {
		return b.append("\n  points=[")
				.append(points.stream()
						.map(Coords3d::toString)
						.reduce((u, v) -> u + ", " + v).get())
				.append("]");
	}

	private SCAD addTriangles(SCAD b, List<Coords3d> points) {
		return b.append("\n  faces=[")
				.append(triangles.stream()
					.map(c -> c.toTriangleString(points))
					.reduce((u, v) -> u + ", " + v).get())
				.append("]");
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return new CSG(triangles.stream()
			.map(c -> Polygon.fromPolygons(c.getPoints(), context.getColor()))
			.collect(Collectors.toList()));
	}
}