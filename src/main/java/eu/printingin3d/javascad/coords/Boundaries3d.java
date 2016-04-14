package eu.printingin3d.javascad.coords;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.exceptions.IllegalValueException;

/**
 * Represents three intervals interpreting as an including cuboid which could contains the 
 * actual 3D object or objects.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Boundaries3d {
	/**
	 * Represents an empty 3D boundary object.
	 */
	public static final Boundaries3d EMPTY = new Boundaries3d(Boundary.EMPTY, Boundary.EMPTY, Boundary.EMPTY);
	
	private final Boundary x;
	private final Boundary y;
	private final Boundary z;

	/**
	 * Calculate the including cuboid so the result of this method is an interval representing a cuboid
	 * that all of the parameter cuboids are inside in this cuboid.
	 * @param boundaries the intervals considered during the calculation
	 * @return a new object representing the including cuboid
	 */
	public static Boundaries3d combine(Collection<Boundaries3d> boundaries) {
		List<Boundary> collectX = new ArrayList<>();
		List<Boundary> collectY = new ArrayList<>();
		List<Boundary> collectZ = new ArrayList<>();
		for (Boundaries3d b : boundaries) {
			collectX.add(b.x);
			collectY.add(b.y);
			collectZ.add(b.z);
		}
		return new Boundaries3d(
				Boundary.combine(collectX), 
				Boundary.combine(collectY),
				Boundary.combine(collectZ));
	}
	
	/**
	 * Calculate the inner cuboid so the result of this method is an interval representing a cuboid
	 * that is inside all of the parameter cuboids.
	 * @param boundaries the intervals considered during the calculation
	 * @return a new object representing the including cuboid
	 * @throws IllegalValueException if the given boundaries have no common part
	 */
	public static Boundaries3d intersect(Collection<Boundaries3d> boundaries) throws IllegalValueException {
		List<Boundary> collectX = new ArrayList<>();
		List<Boundary> collectY = new ArrayList<>();
		List<Boundary> collectZ = new ArrayList<>();
		for (Boundaries3d b : boundaries) {
			collectX.add(b.x);
			collectY.add(b.y);
			collectZ.add(b.z);
		}
		return new Boundaries3d(
				Boundary.intersect(collectX), 
				Boundary.intersect(collectY),
				Boundary.intersect(collectZ));
	}
	
	/**
	 * Constructs a new object with the given X, Y and Z boundary.
	 * @param x the boundary on the X plane
	 * @param y the boundary on the Y plane
	 * @param z the boundary on the Z plane
	 */
	public Boundaries3d(Boundary x, Boundary y, Boundary z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Constructs a new object with the given two corner. Despite of the
	 * parameters names any two opposite corners work.
	 * @param minCorner the first corner
	 * @param maxCorner the second corner
	 */
	public Boundaries3d(Coords3d minCorner, Coords3d maxCorner) {
		this.x = new Boundary(minCorner.getX(), maxCorner.getX());
		this.y = new Boundary(minCorner.getY(), maxCorner.getY());
		this.z = new Boundary(minCorner.getZ(), maxCorner.getZ());
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
	 * Returns with the boundary on the Z plane.
	 * @return the boundary on the Z plane
	 */
	public Boundary getZ() {
		return z;
	}
	
	/**
	 * Returns with the size of this boundaries. The result will be a {@link Dims3d} object which means
	 * it can be used to create a cube with the same size as this model. 
	 * @return the size of this boundaries
	 */
	public Dims3d getSize() {
		return new Dims3d(x.getSize(), y.getSize(), z.getSize());
	}
	
	/**
	 * Returns with the front-left-bottom corner's coordinates.
	 * @return the front-left-bottom corner's coordinates
	 */
	public Coords3d getMinCorner() {
		return new Coords3d(getX().getMin(),getY().getMin(), getZ().getMin());
	}
	
	/**
	 * Returns with the back-right-top corner's coordinates.
	 * @return the back-right-top corner's coordinates
	 */
	public Coords3d getMaxCorner() {
		return new Coords3d(getX().getMax(),getY().getMax(), getZ().getMax());
	}
	
	/**
	 * Moves the interval with the given delta.
	 * @param delta the value used by the move
	 * @return a new object with the calculated new boundaries
	 */
	public Boundaries3d move(Coords3d delta) {
		return new Boundaries3d(x.add(delta.getX()), y.add(delta.getY()), z.add(delta.getZ()));
	}
	
	/**
	 * Adds the interval with the given delta - this operation always increase the size of the boundaries.
	 * @param delta the value used by the add operation
	 * @return a new object with the calculated new boundaries
	 */
	public Boundaries3d add(Boundaries3d delta) {
		return new Boundaries3d(x.add(delta.getX()), y.add(delta.getY()), z.add(delta.getZ()));
	}
	
	/**
	 * Scales the interval with the given delta.
	 * @param delta the value used by the scale
	 * @return a new object with the calculated new boundaries
	 */
	public Boundaries3d scale(Coords3d delta) {
		return new Boundaries3d(x.scale(delta.getX()), y.scale(delta.getY()), z.scale(delta.getZ()));
	}
	
	/**
	 * Rotates the interval with the given angle. The rotation takes the cuboid represented
	 * by this object, rotates that and then returns with the cuboids parameter which can contain
	 * this result. 
	 * @param angle the value used by the rotation
	 * @return a new object with the calculated new boundaries
	 */
	public Boundaries3d rotate(Angles3d angle) {
		Coords3d minCorner = getMinCorner();
		Coords3d maxCorner = getMaxCorner();
		return new Boundaries3d(minCorner.rotate(angle), maxCorner.rotate(angle));
	}
	/**
	 * Returns with the coordinate of this boundary queried by the parameter.
	 * @param side the side we want to get
	 * @return the point we asked for
	 */
	public Coords3d getAlignedValue(Side side) {
		return new Coords3d(
				side.getAlignX().getAlignedValue(x),
				side.getAlignY().getAlignedValue(y),
				side.getAlignZ().getAlignedValue(z));
	}
}
