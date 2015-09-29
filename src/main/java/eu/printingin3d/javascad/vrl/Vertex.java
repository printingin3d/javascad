package eu.printingin3d.javascad.vrl;

import java.awt.Color;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.utils.AssertValue;

public class Vertex {
	public final Coords3d coords;
	public final Color color;
	
	public Vertex(Coords3d coords, Color color) {
		AssertValue.isNotNull(coords, "Coords must not be null for a vertex!");
		
		this.coords = coords;
		this.color = color;
	}

	public Coords3d getCoords() {
		return coords;
	}

	public Color getColor() {
		return color;
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
