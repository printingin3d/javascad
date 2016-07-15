package eu.printingin3d.javascad.models2d;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import eu.printingin3d.javascad.basic.Angle;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.EdgeCrossSolver;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.coords2d.LineSegment2d;
import eu.printingin3d.javascad.enums.PointRelation;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.utils.AssertValue;
import eu.printingin3d.javascad.utils.DoubleUtils;
import eu.printingin3d.javascad.utils.Pair;

/**
 * Represents a list of 2D points.
 */
public class Area2d extends AbstractCollection<Coords2d> {
	private final List<Coords2d> coords;

	/**
	 * Instantiate a new object with the given list of coordinates.
	 * @param coords the coordinates to be used
	 */
	public Area2d(List<Coords2d> coords) {
		this.coords = compact(coords);
	}
	
	/**
	 * Moves this list of coordinates with the given coordinate. Creates a new object, this
	 * object remains untouched.
	 * @param move the coordinate move with
	 * @return a new object with the new coordinates
	 */
	public Area2d move(Coords2d move) {
		return new Area2d(coords.stream().map(c -> c.move(move)).collect(Collectors.toList()));
	}
	
	/**
	 * Rotates this list of coordinates with the given degree. Creates a new object, this
	 * object remains untouched.
	 * @param angle the angle in degree to rotate with 
	 * @return a new object with the new coordinates
	 */
	public Area2d rotate(Angle angle) {
		return new Area2d(
				coords.stream()
					.map(c -> new Coords2d(
							angle.cos()*c.getX()-angle.sin()*c.getY(), 
							angle.sin()*c.getX()+angle.cos()*c.getY()))
					.collect(Collectors.toList()));
	}
	
	/**
	 * Extends this list of 2D coordinates to a 3D list of coordinates.
	 * @param z the Z value to be used for the transformation
	 * @return a list of 3D coordinates
	 */
	public List<Coords3d> withZ(double z) {
		return coords.stream().map(c -> c.withZ(z)).collect(Collectors.toList());
	}

	@Override
	public Iterator<Coords2d> iterator() {
		return coords.iterator();
	}
	
	/**
	 * Iterates over a list of 2D coordinates. Used internally by the union function.
	 */
	private static class Area2dIterator {
		private final List<Coords2d> coords;
		private int current;
		private int returned = 0;
		
		Area2dIterator(int start, List<Coords2d> coords) {
			this.current = start;
			this.coords = coords;
		}
		public boolean hasNext() {
			return returned<coords.size();
		}
		public Coords2d peekPrevious() {
			return coords.get(Math.floorMod(current-1, coords.size()));
		}
		public Coords2d peekNext() {
			return coords.get(Math.floorMod(current, coords.size()));
		}
		public void reuseLast() {
			current = Math.floorMod(current-1, coords.size());
			returned--;
		}
		public Coords2d next() {
			Coords2d result = coords.get(current);
			current = (current+1) % coords.size();
			returned++;
			return result;
		}
		public Coords2d skipTillOutside(Area2d c1) {
			LineSegment2d curr = LineSegment2d.startLineSegmentSeries(coords.get(current));
			for (int i=coords.size();i>0;i--) {
				Coords2d c = next();
				curr = curr.next(c);
				if (c1.calculatePointRelation(c)!=PointRelation.INSIDE) {
					return findClosest(c1.findCrossing(curr, true), curr.getStart());
				}
			}
			throw new IllegalValueException("There is no point outside of the given area in this area.");
		}
	}
	
	private Area2dIterator getIteratorOutside(Area2d other) {
		for (int i=0;i<coords.size();i++) {
			if (other.calculatePointRelation(coords.get(i))==PointRelation.OUTSIDE) {
				return new Area2dIterator(i, coords);
			}
		}
		throw new RuntimeException("Unexpected");
	}
	
	private Pair<Coords2d, Area2dIterator> getIteratorCrossing(final LineSegment2d segment) {
		Set<Pair<Coords2d, Area2dIterator>> closest = new TreeSet<>(new Comparator<Pair<Coords2d, Area2dIterator>>() {
			@Override
			public int compare(Pair<Coords2d, Area2dIterator> o1,
					Pair<Coords2d, Area2dIterator> o2) {
				return Double.compare(
						o1.getValue1().squareDist(segment.getEnd()), 
						o2.getValue1().squareDist(segment.getEnd()));
			}
		});
		
		int i=0;
		for (LineSegment2d current : LineSegment2d.lineSegmentSeries2d(coords)) {
			if (current.hasCommon(segment)) {
				closest.add(new Pair<Coords2d, Area2dIterator>(current.getEnd(), new Area2dIterator(i, coords)));
			} else {
				Coords2d cross = EdgeCrossSolver.findCross(segment, current);
				if (cross!=null) {
					closest.add(new Pair<Coords2d, Area2dIterator>(cross, new Area2dIterator(i, coords)));
				}
			}
			i++;
		}
		if (closest.isEmpty()) {
			throw new RuntimeException("There is no cross for "+segment+" in this area.");
		}
		return closest.iterator().next();
	}
	
	private static Coords2d findClosest(List<Coords2d> list, Coords2d p) {
		Coords2d closest = null;
		double minDist = Double.MAX_VALUE;
		for (Coords2d c : list) {
			double d = c.squareDist(p);
			if (d<minDist) {
				minDist = d;
				closest = c;
			}
		}
		return closest;
	}
	
	/**
	 * Creates a new object which covers the union of this and the given area.
	 * @param other the other area to be used
	 * @return a new object which holds the union of the two areas
	 */
	public Area2d union(Area2d other) {
		AssertValue.isFalse(isDistinct(other), "Cannot create a union of two distinct areas!");
		if (isAllInside(other)) {
			return this;
		}
		if (other.isAllInside(this)) {
			return other;
		}
		
		List<Coords2d> result = new ArrayList<>();
		Pair<Area2dIterator, Area2dIterator> iterators = new Pair<>(getIteratorOutside(other), null);
		Pair<Area2d, Area2d> areas = new Pair<>(this, other);
		
		Coords2d prev = iterators.getValue1().peekPrevious();
		Coords2d c = iterators.getValue1().next();
		List<Coords2d> crosses = areas.getValue2().findCrossing(new LineSegment2d(c, prev), false);
		if (!crosses.isEmpty()) {
			result.add(findClosest(crosses, c));
		}
		prev = c;
		result.add(c);
		
		while (iterators.getValue1().hasNext() || iterators.getValue2().hasNext()) {
			c = iterators.getValue1().next();
			LineSegment2d current = new LineSegment2d(c, prev);
			
			crosses = areas.getValue2().findCrossing(current, false);
			crosses.remove(result.get(result.size()-1));
			if (!crosses.isEmpty()) {
				Coords2d cross = findClosest(crosses, prev);
				result.add(cross);
				prev = cross;
				if (areas.getValue2().calculatePointRelation(c)==PointRelation.OUTSIDE) {
					iterators.getValue1().reuseLast();
				}
				if (iterators.getValue2()==null) {
					Pair<Coords2d, Area2dIterator> pair = 
							areas.getValue2().getIteratorCrossing(current);
					iterators = new Pair<>(pair.getValue2(), iterators.getValue1());
				} else {
					iterators = iterators.reverse();
				}
				areas = areas.reverse();
			} else if (areas.getValue2().calculatePointRelation(c)!=PointRelation.INSIDE) {
				result.add(c);
				prev = c;
			} else {
				if (areas.getValue2().calculatePointRelation(c)==PointRelation.OUTSIDE) {
					iterators.getValue1().reuseLast();
				}
				Coords2d cross;
				if (iterators.getValue2()==null) {
					Pair<Coords2d, Area2dIterator> pair = 
							areas.getValue2().getIteratorCrossing(current);
					cross = pair.getValue1();
					iterators = new Pair<>(pair.getValue2(), iterators.getValue1());
				} else {
					cross = iterators.getValue2().skipTillOutside(areas.getValue1());
					iterators = iterators.reverse();
				}
				if (cross!=null) {
					result.add(cross);
				}
				areas = areas.reverse();
			}
		}
		return new Area2d(result);
	}
	
	private static List<Coords2d> compact(List<Coords2d> coords) {
		List<Coords2d> result = new ArrayList<>();
		Area2dIterator iterator = new Area2dIterator(0, coords);
		while (iterator.hasNext()) {
			if (result.isEmpty()) {
				Coords2d current = iterator.next();
				result.add(current);
			} else {
				Coords2d prev = result.get(result.size()-1);
				Coords2d current = iterator.next();
				Coords2d next = iterator.peekNext();
				
				if (!new LineSegment2d(prev, next).isOnLineSegment(current)) {
					result.add(current);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Checks if the given line segment has crossing with this geometric form.
	 * @param segment the line segment
	 * @param includeEndPoints if the cross point is one of the two given end points this method 
	 * 				will return null if this parameter is false
	 * @return true if and only if the given line segment crosses any line segment of area.
	 */
	public List<Coords2d> findCrossing(LineSegment2d segment, boolean includeEndPoints) {
		List<Coords2d> result = new ArrayList<>();
		for (LineSegment2d current : LineSegment2d.lineSegmentSeries2d(coords)) {
			if (current.hasCommon(segment)) {
				if (includeEndPoints) {
					result.add(current.getEnd());
				}
			} else {
				LineSegment2d common = current.common(segment);
				if (common!=null) {
					result.add(common.getStart());
					result.add(common.getEnd());
				} else {
					Coords2d cross = EdgeCrossSolver.findCross(segment, current);
					if (cross!=null && (includeEndPoints || !segment.contains(cross))) {
						result.add(cross);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Checks if the two list of coordinates are distinct.
	 * @param other the other list of coordinates
	 * @return true if and only if the two list of coordinates are distinct
	 */
	public boolean isDistinct(Area2d other) {
		return LineSegment2d.lineSegmentSeries2d(other.coords).stream()
			.allMatch(c -> findCrossing(c, true).isEmpty());
	}
	
	private boolean isAllInside(Area2d other) {
		return other.coords.stream()
				.allMatch(c -> calculatePointRelation(c)!=PointRelation.OUTSIDE);
	}
	
	/**
	 * Calculates the relation of the given point and this area.
	 * @param p the point to be checked
	 * @return returns with <b>INSIDE</b> if the point is inside of this area;<br/>
	 *         returns with <b>OUTSIDE</b> if the point is outside of this area;<br/>
	 *         returns with <b>BORDER</b> if the point is on the border of this area.
	 */
	public PointRelation calculatePointRelation(Coords2d p) {
		LineSegment2d horizontal = new LineSegment2d(
					new Coords2d(-1E10, p.getY()),
					new Coords2d(+1E10, p.getY())
				);
		
		LineSegment2d current = new LineSegment2d(null, coords.get(coords.size()-1));
		Set<Coords2d> crosses = new TreeSet<>(new Comparator<Coords2d>() {
			@Override
			public int compare(Coords2d o1, Coords2d o2) {
				return DoubleUtils.compareEps(o1.getX(), o2.getX());
			}
		});
		int i = 0;
		for (Coords2d c : coords) {
			current = current.next(c);

			if (current.isOnLineSegment(p)) {
				return PointRelation.BORDER;
			}
			
			LineSegment2d common = horizontal.common(current);
			if (common!=null) {
				crosses.add(common.getStart());
				crosses.add(common.getEnd());
			} else {
				Coords2d cross = EdgeCrossSolver.findCross(horizontal, current);
				if (cross!=null) {
					if (cross.equals(current.getStart())) {
						Coords2d prevv = coords.get(Math.floorMod(i-2, coords.size()));
						if (EdgeCrossSolver.findCross(horizontal, new LineSegment2d(c, prevv))!=null) {
							crosses.add(cross);
						}
						
					} else if (cross.equals(c)) {
						Coords2d next = coords.get(Math.floorMod(i+1, coords.size()));
						if (EdgeCrossSolver.findCross(horizontal, new LineSegment2d(next, current.getStart()))!=null) {
							crosses.add(cross);
						}
					} else {
						crosses.add(cross);
					}
				}
			}
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
	@Override
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
