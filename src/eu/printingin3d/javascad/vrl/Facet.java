package eu.printingin3d.javascad.vrl;

import java.awt.Color;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
	
	public String toStlString() {
		List<Coords3d> points = triangle.getPoints();
		
		return new StringBuilder().
        append("  facet normal ").append(normal.toStlString()).append('\n').
        append("    outer loop\n").
        append("    vertex ").append(points.get(0).toStlString()).append('\n').
        append("    vertex ").append(points.get(1).toStlString()).append('\n').
        append("    vertex ").append(points.get(2).toStlString()).append('\n').
        append("    endloop\n").
        append("  endfacet\n").toString();
	}
	
	public byte[] toBinaryStl() throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.allocate(50).order(ByteOrder.LITTLE_ENDIAN);
		
		byteBuffer.put(normal.toByteArray());
		
		byteBuffer.put(triangle.getPoints().get(0).toByteArray());
		byteBuffer.put(triangle.getPoints().get(1).toByteArray());
		byteBuffer.put(triangle.getPoints().get(2).toByteArray());
		
		byteBuffer.putShort((short)0);
		
		return byteBuffer.array();
	}
	
	public List<Vertex> getVertexes() {
		List<Vertex> vertexes = new ArrayList<>();
		for (Coords3d c : triangle.getPoints()) {
			vertexes.add(new Vertex(c, color));
		}
		return vertexes;
	}
}
