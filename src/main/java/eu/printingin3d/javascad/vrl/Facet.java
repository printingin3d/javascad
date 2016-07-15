package eu.printingin3d.javascad.vrl;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Triangle3d;

/**
 * <p>Immutable representation of one triangle in the mesh with a normal pointing outward from the object.</p>
 * <p>It is used internally by the STL output generation, you don't really have to use it directly.</p>
 * 
 * @author ivivan <ivivan@printingin3d.eu>
 *
 */
public class Facet {
	private final Triangle3d triangle;
	private final Coords3d normal;
	private final Color color;
	
	/**
	 * Creates a facet based on a triangle and a normal vector.
	 * @param triangle the triangle
	 * @param normal the normal vector
	 * @param color the color of the facet
	 */
	public Facet(Triangle3d triangle, Coords3d normal, Color color) {
		this.triangle = triangle;
		this.normal = normal;
		this.color = color;
	}
	
	/**
	 * Returns all the vertices this facet holds. The result will always contain exactly three vertices.
	 * @return all the vertices this facet holds
	 */
	public List<Vertex> getVertexes() {
		return triangle.getPoints().stream()
				.map(c -> new Vertex(c, color))
				.collect(Collectors.toList());
	}
	
	/**
	 * Returns the triangle within this facet. Added for testing purposes.
	 * @return the triangle within this facet.
	 */
	public Triangle3d getTriangle() {
		return triangle;
	}

	/**
	 * Returns the normal of this facet.
	 * @return the normal of this facet
	 */
	public Coords3d getNormal() {
		return normal;
	}
	
}
