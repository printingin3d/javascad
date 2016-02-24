package eu.printingin3d.javascad.models2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.EdgeCrossSolver;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.enums.PointRelation;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.utils.DoubleUtils;

/**
 * Represents a list of 2D points.
 */
public class Area2d implements Iterable<Coords2d> {
	private final List<Coords2d> coords;

	/**
	 * Instantiate a new object with the given list of coordinates.
	 * @param coords the coordinates to be used
	 */
	public Area2d(List<Coords2d> coords) {
		if (coords.get(0).equals(coords.get(coords.size()-1))) {
			this.coords = new ArrayList<>(coords.subList(0, coords.size()-1));
		}
		else {
			this.coords = new ArrayList<>(coords);
		}
	}
	
	/**
	 * Moves this list of coordinates with the given coordinate. Creates a new object, this
	 * object remains untouched.
	 * @param move the coordinate move with
	 * @return a new object with the new coordinates
	 */
	public Area2d move(Coords2d move) {
		List<Coords2d> result = new ArrayList<>();
		for (Coords2d c : coords) {
			result.add(c.move(move));
		}
		return new Area2d(result);
	}
	
	/**
	 * Rotates this list of coordinates with the given degree. Creates a new object, this
	 * object remains untouched.
	 * @param angle the angle in degree to rotate with 
	 * @return a new object with the new coordinates
	 */
	public Area2d rotate(double angle) {
		double rad = Math.toRadians(angle);
		
		List<Coords2d> result = new ArrayList<>();
		for (Coords2d c : coords) {
			result.add(new Coords2d(
					Math.cos(rad)*c.getX()-Math.sin(rad)*c.getY(), 
					Math.sin(rad)*c.getX()+Math.cos(rad)*c.getY()));
		}

		return new Area2d(result);
	}
	
	/**
	 * Extends this list of 2D coordinates to a 3D list of coordinates.
	 * @param z the Z value to be used for the transformation
	 * @return a list of 3D coordinates
	 */
	public List<Coords3d> withZ(double z) {
		List<Coords3d> result = new ArrayList<>();
		for (Coords2d c : coords) {
			result.add(c.withZ(z));
		}
		return result;
	}

	@Override
	public Iterator<Coords2d> iterator() {
		return coords.iterator();
	}
	
	/**
	 * Iterates over a list of 2D coordinates. Used internally by the union function.
	 */
	private static class LCIterator {
		private final List<Coords2d> coords;
		private int current;
		private int returned = 0;
		
		public LCIterator(int start, List<Coords2d> coords) {
			this.current = start;
			this.coords = coords;
		}
		public boolean hasNext() {
			return returned<coords.size();
		}
		public Coords2d next() {
			Coords2d result = coords.get(current);
			current = (current+1) % coords.size();
			returned++;
			return result;
		}
		public Coords2d skipTillOutside(Area2d c1) {
			Coords2d c, prev = coords.get(current);
			while (null!=(c = next())) {
				if (c1.calculatePointRelation(c)!=PointRelation.INSIDE) {
					return c1.findCrossing(c, prev, true);
				}
				prev = c;
			}
			return null;
		}
	}
	
	private LCIterator getIteratorOutside(Area2d other) {
		boolean foundInside = false;
		
		for (int i=0;i<coords.size();i++) {
			if (other.calculatePointRelation(coords.get(i))!=PointRelation.OUTSIDE) {
				foundInside = true;
			} else {
				if (foundInside) {
					return new LCIterator(i, coords);
				}
			}
		}
		throw new RuntimeException("Unexpected");
	}
	
	/**
	 * Simple store for two values.
	 */
	private static class Pair<T, U> {
		private final T value1;
		private final U value2;
		public Pair(T value1, U value2) {
			this.value1 = value1;
			this.value2 = value2;
		}
		public T getValue1() {
			return value1;
		}
		public U getValue2() {
			return value2;
		}
	}
	
	private Pair<Coords2d, LCIterator> getIteratorCrossing(Coords2d c1, Coords2d c2) {
		Coords2d prev = coords.get(coords.size()-1);
		if (c1.equals(prev) || c2.equals(prev)) {
			return new Pair<Coords2d, LCIterator>(prev, new LCIterator(0, coords));
		}
		int i=0;
		for (Coords2d c : coords) {
			if (c1.equals(c) || c2.equals(c)) {
				return new Pair<Coords2d, LCIterator>(c, new LCIterator(i, coords));
			} else {
				Coords2d cross = EdgeCrossSolver.findCross(c1, c2, c, prev);
				if (cross!=null) {
					return new Pair<Coords2d, LCIterator>(cross, new LCIterator(i, coords));
				}
			}
			prev = c;
			i++;
		}
		throw new RuntimeException("There is no cross for "+c1+" and "+c2+" in this area.");
	}
	
	/**
	 * Creates a new object which covers the union of this and the given area.
	 * @param other the other area to be used
	 * @return a new object which holds the union of the two areas
	 */
	public Area2d union(Area2d other) {
		if (isDistinct(other)) {
			throw new IllegalValueException("Cannot create a union of two distinct area!");
		}
		
		if (isAllInside(other)) {
			return this;
		}
		if (other.isAllInside(this)) {
			return other;
		}
		
		List<Coords2d> result = new ArrayList<>();
		
		LCIterator i1 = getIteratorOutside(other);
		LCIterator i2 = null;
		
		Area2d c1 = this;
		Area2d c2 = other;
		
		Coords2d prev = null;
		boolean include = false;
		while (i1.hasNext()) {
			Coords2d c = i1.next();
			if (include || c2.calculatePointRelation(c)!=PointRelation.INSIDE) {
				result.add(c);
				prev = c;
				include = false;
			} else if (prev!=null) {
				Coords2d cross;
				if (i2==null) {
					i2 = i1;
					Pair<Coords2d, LCIterator> pair = c2.getIteratorCrossing(c, prev);
					i1 = pair.getValue2();
					cross = pair.getValue1();
				} else {
					cross = i2.skipTillOutside(c1);

					LCIterator tmp = i2;
					i2 = i1;
					i1 = tmp;
				}
				if (cross!=null) {
					result.add(cross);
				}
				include = true;
				Area2d tmp = c2;
				c2 = c1;
				c1 = tmp;
			}
		}
		
		return new Area2d(result);
	}
	
	/**
	 * Checks if the given line segment has crossing with this geometric form.
	 * @param c1 the begin of the line segment
	 * @param c2 the end of the line segment
	 * @param includeEndPoints if the cross point is one of the two given end points this method 
	 * 				will return null if this parameter is false
	 * @return true if and only if the given line segment crosses any line segment of area.
	 */
	public Coords2d findCrossing(Coords2d c1, Coords2d c2, boolean includeEndPoints) {
		Coords2d prev = coords.get(coords.size()-1);
		for (Coords2d c : coords) {
			if (c1.equals(c) || c2.equals(c) || c1.equals(prev) || c2.equals(prev)) {
				if (includeEndPoints) {
					return c;
				}
			} else {
				Coords2d cross = EdgeCrossSolver.findCross(c1, c2, c, prev);
				if (cross!=null) {
					return cross;
				}
			}
			prev = c;
		}
		return null;
	}

	/**
	 * Checks if the two list of coordinates are distinct.
	 * @param other the other list of coordinates
	 * @return true if and only if the two list of coordinates are distinct
	 */
	public boolean isDistinct(Area2d other) {
    	for (Coords2d p : other.coords) {
    		if (calculatePointRelation(p)!=PointRelation.OUTSIDE) {
				return false;
			}
		}
    	return true;
	}
	
	private boolean isAllInside(Area2d other) {
		for (Coords2d p : other.coords) {
			if (calculatePointRelation(p)==PointRelation.OUTSIDE) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Calculates the relation of the given point and this area.
	 * @param p the point to be checked
	 * @return returns with <b>INSIDE</b> if the point is inside of this area;<br/>
	 *         returns with <b>OUTSIDE</b> if the point is outside of this area;<br/>
	 *         returns with <b>BORDER</b> if the point is on the border of this area.
	 */
	public PointRelation calculatePointRelation(Coords2d p) {
		Coords2d c1 = new Coords2d(-Double.MAX_VALUE, p.getY());
		Coords2d c2 = new Coords2d(+Double.MAX_VALUE, p.getY());
		
		Coords2d prev = coords.get(coords.size()-1);
		Set<Coords2d> crosses = new TreeSet<>(new Comparator<Coords2d>() {
			@Override
			public int compare(Coords2d o1, Coords2d o2) {
				return DoubleUtils.compareEps(o1.getX(), o2.getX());
			}
		});
		int i = 0;
		for (Coords2d c : coords) {
			Coords2d cross = EdgeCrossSolver.findCross(c1, c2, c, prev);
			if (cross!=null) {
				if (cross.equals(p)) {
					return PointRelation.BORDER;
				}
				
				if (cross.equals(prev)) {
					Coords2d prevv = coords.get(Math.floorMod(i-2, coords.size()));
					if (EdgeCrossSolver.findCross(c1, c2, c, prevv)!=null) {
						crosses.add(cross);
					}
					
				} else if (cross.equals(c)) {
					Coords2d next = coords.get(Math.floorMod(i+1, coords.size()));
					if (EdgeCrossSolver.findCross(c1, c2, next, prev)!=null) {
						crosses.add(cross);
					}
				} else {
					crosses.add(cross);
				}
			}
			prev = c;
			i++;
		}
		
		boolean inside = false;
		for (Coords2d c : crosses) {
			if (c.getX()>p.getX()) {
				return inside ? PointRelation.INSIDE : PointRelation.OUTSIDE;
			}
			inside = !inside;
		}
		return PointRelation.OUTSIDE;
	}
	
	/**
	 * Creates a new object where the order of the points are reversed.
	 * @return new object with reversed point order
	 */
	public Area2d reverse() {
		List<Coords2d> rev = new ArrayList<>(coords);
		Collections.reverse(rev);
		return new Area2d(rev);
	}
	
	/**
	 * Gives access to the points of this area.
	 * @param index the index of the point we want to get.
	 * @return the indexth point of this area.
	 */
	public Coords2d get(int index) {
		return coords.get(index);
	}
	
	/**
	 * Returns with the number of points this area consists.
	 * @return the number of points
	 */
	public int size() {
		return coords.size();
	}
	
	/**
	 * Returns with a list of coordinates which contains the coordinates indexed from start to end inclusive. 
	 * The end index can be smaller then start index in which case the values are start over after the last
	 * item and start from zero. 
	 * @param start the first index to include
	 * @param end the last index to include
	 * @return a new list object
	 */
	public Area2d subList(int start, int end) {
		if (end<start) {
			List<Coords2d> result = new ArrayList<>(coords.subList(start, coords.size()));
			result.addAll(coords.subList(0, end+1));
			return new Area2d(result);
		}
		return new Area2d(coords.subList(start, end+1));
	}

	private int findCoords(Coords2d c) {
		for (int i=0;i<coords.size();i++) {
			if (coords.get(i).equals(c)) {
				return i;
			}
		}
		throw new IllegalValueException("The given value is not part of this list: "+c);
	}
	
	/**
	 * Returns with a list of coordinates which contains the coordinates from <b>first</b> to <b>last</b> inclusive. 
	 * The last item can be before then first in which case the values are start over after the last
	 * item and start from first.
	 * @param first the first item to include
	 * @param last the last item to include
	 * @return a new list object
	 */
	public Area2d subList(Coords2d first, Coords2d last) {
		return subList(findCoords(first), findCoords(last));
	}
/*	
	private static boolean isInsideTriangle(Coords2d a, Coords2d b, Coords2d c, Coords2d p) {
		// Compute vectors
		Coords2d v0 = new Coords2d(c.getX()-a.getX(), c.getY()-a.getY());
		Coords2d v1 = new Coords2d(b.getX()-a.getX(), b.getY()-a.getY());
		Coords2d v2 = new Coords2d(p.getX()-a.getX(), p.getY()-a.getY());

		// Compute dot products
		double dot00 = v0.dot(v0);
		double dot01 = v0.dot(v1);
		double dot02 = v0.dot(v2);
		double dot11 = v1.dot(v1);
		double dot12 = v1.dot(v2);

		// Compute barycentric coordinates
		double invDenom = 1.0 / (dot00 * dot11 - dot01 * dot01);
		double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
		double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

		// Check if point is in triangle
		return u>=0 && v>=0 && u + v<=1;
	}*/
	
	@Override
	public String toString() {
		return coords.toString();
	}
}
