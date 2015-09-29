package eu.printingin3d.javascad.coords2d;

import eu.printingin3d.javascad.coords.Boundary;


/**
 * Represents two intervals interpreting as an including square which could contain the 
 * actual 2D object or objects.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Boundaries2d {
	private final Boundary x;
	private final Boundary y;
	
	/**
	 * Constructs a new object with the given X, Y and Z boundary.
	 * @param x the boundary on the X plane
	 * @param y the boundary on the Y plane
	 */
	public Boundaries2d(Boundary x, Boundary y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns with the boundary on the X plane.
	 * @return the boundary on the X plane
	 */
	public Boundary getX() {
		return x;
	}

	/**
	 * Returns with the boundary on the Y plane.
	 * @return the boundary on the Y plane
	 */
	public Boundary getY() {
		return y;
	}
	
	/**
	 * Moves the interval with the given delta.
	 * @param delta the value used by the move
	 * @return a new object with the calculated new boundaries
	 */
	public Boundaries2d move(Coords2d delta) {
		return new Boundaries2d(x.add(delta.getX()), y.add(delta.getY()));
	}
}
