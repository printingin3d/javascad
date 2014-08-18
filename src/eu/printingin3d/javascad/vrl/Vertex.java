package eu.printingin3d.javascad.vrl;

import java.awt.Color;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.utils.DoubleUtils;

public class Vertex {
	public final Coords3d coords;
	public final Color color;
	
	public Vertex(Coords3d coords, Color color) {
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
		result = prime * result + ((coords == null) ? 0 : coords.hashCode());
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
		if (coords == null) {
			if (other.coords != null) {
				return false;
			}
		} else if (!coords.equals(other.coords)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return 
			DoubleUtils.formatDouble(coords.getX())+" "+
			DoubleUtils.formatDouble(coords.getY())+" "+
			DoubleUtils.formatDouble(coords.getZ())+" "+
			color.getRed()+" "+
			color.getGreen()+" "+
			color.getBlue();
	}
	
	
}
