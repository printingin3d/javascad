package eu.printingin3d.javascad.vrl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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
	 */
	public Facet(Triangle3d triangle, Coords3d normal, Color color) {
		this.triangle = triangle;
		this.normal = normal;
		this.color = color;
	}
	
	public List<Vertex> getVertexes() {
		List<Vertex> vertexes = new ArrayList<>();
		for (Coords3d c : triangle.getPoints()) {
			vertexes.add(new Vertex(c, color));
		}
		return vertexes;
	}

	public Coords3d getNormal() {
		return normal;
	}
	
}
