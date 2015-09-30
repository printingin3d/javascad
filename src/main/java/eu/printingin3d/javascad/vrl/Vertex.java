package eu.printingin3d.javascad.vrl;

import java.awt.Color;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.enums.OutputFormat;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.utils.AssertValue;

/**
 * Represents a vertex in the rendered CSG object, which consists of a coordinate and a color.
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Vertex {
	private final Coords3d coords;
	private final Color color;
	
	/**
	 * Creates a new vertex with the given coordinate and color.
	 * @param coords the coordinate of the vertex
	 * @param color the color of the vertex - can be null
	 * @throws eu.printingin3d.javascad.exceptions.IllegalValueException if the coords parameter is null
	 */
	public Vertex(Coords3d coords, Color color) {
		AssertValue.isNotNull(coords, "Coords must not be null for a vertex!");
		
		this.coords = coords;
		this.color = color;
	}

	/**
	 * Returns with a byte array representation of this vertex.
	 * @return a byte array representation of this vertex
	 */
	public byte[] toByteArray() {
		return coords.toByteArray();
	}

	/**
	 * Formats this vertex with the given output format. Only STL and POLYGON are supported yet.
	 * @param outputFormat the output format to be used
	 * @return the string representation of this vertex
	 */
	public String format(OutputFormat outputFormat) {
		switch (outputFormat) {
		case STL:
			return coords.format(outputFormat);
		case POLYGON:
			return coords.format(outputFormat)+
					color.getRed()+" "+
					color.getGreen()+" "+
					color.getBlue();
		default:
			throw new IllegalValueException("Unsupported format: "+outputFormat);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + coords.hashCode();
		return result;
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
		Vertex other = (Vertex) obj;
		if (color == null) {
			if (other.color != null) {
				return false;
			}
		} else if (!color.equals(other.color)) {
			return false;
		}
		return coords.equals(other.coords);
	}
}
