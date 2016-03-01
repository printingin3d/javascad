package eu.printingin3d.javascad.coords2d;

import java.util.ArrayList;
import java.util.List;

import eu.printingin3d.javascad.utils.DoubleUtils;

/**
 * Represents a 2D line segment.
 */
public class LineSegment2d {
	private final Coords2d start;
	private final Coords2d end;
	
	/**
	 * Starts a series of line segments. You can get the next segment by calling the
	 * next method. You can use the {@link #lineSegmentSeries(List)} method to convert a
	 * list of coordinates to a list of line segments.
	 * @param p the starting point
	 * @return the starting of a line segment
	 */
	public static LineSegment2d startLineSegmentSeries(Coords2d p) {
		return new LineSegment2d(null, p);
	}

	/**
	 * Returns with the list of line segments which is denoted by this series of points.
	 * @param coords the list of points
	 * @return the list of segments
	 */
	public static List<LineSegment2d> lineSegmentSeries(List<Coords2d> coords) {
		List<LineSegment2d> result = new ArrayList<>();
		LineSegment2d current = new LineSegment2d(null, coords.get(coords.size()-1));
		for (Coords2d c : coords) {
			current = current.next(c);
			result.add(current);
		}
		return result;
	}
	
	/**
	 * Creates a new line segment using the two given end points.
	 * @param start the starting point of the segment
	 * @param end the end point of the segment
	 */
	public LineSegment2d(Coords2d start, Coords2d end) {
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Returns with the start point of the segment.
	 * @return the start point of the segment
	 */
	public Coords2d getStart() {
		return start;
	}
	
	/**
	 * Returns with the end point of the segment.
	 * @return the end point of the segment
	 */
	public Coords2d getEnd() {
		return end;
	}
	
	/**
	 * Creates the next segment of the line segment series.
	 * @param c the next point of the point series
	 * @return the next segment of the line segment series
	 */
	public LineSegment2d next(Coords2d c) {
		return new LineSegment2d(end, c);
	}
	
	/**
	 * Checks if the given coordinate is equals with either end point of this segment.
	 * @param c the point to be checked
	 * @return true if and only if either of the two end points of this segment is equals with 
	 *     the given point 
	 */
	public boolean contains(Coords2d c) {
		return c==null ? false : c.equals(start) || c.equals(end);
	}
	
	/**
	 * Checks if this segment and the given segment has a common end point.
	 * @param ls the other segment to be checked
	 * @return true if and only if this segment and the given segment has a common end point
	 */
	public boolean hasCommon(LineSegment2d ls) {
		return contains(ls.start) || contains(ls.end);
	}
	
	/**
	 * Checks if the two line segments are parallel.
	 * @param ls the other line segment
	 * @return true if and only if the two line segments are parallel
	 */
	public boolean isParallel(LineSegment2d ls) {
		return DoubleUtils.equalsEps(
				(start.getY()-end.getY())*(ls.start.getX()-ls.end.getX()), 
				(ls.start.getY()-ls.end.getY())*(start.getX()-end.getX()));
	}
	
	/**
	 * Return true if the given point is on the segment.
	 * @param c the point to be checked
	 * @return true if and only if the point is on the line segment
	 */
	public boolean isOnLineSegment(Coords2d c) {
		if (DoubleUtils.between(c.getX(), start.getX(), end.getX()) && 
				DoubleUtils.between(c.getY(), start.getY(), end.getY())) {
			double left = (end.getY()-start.getY())*(c.getX()-start.getX());
			double right = (end.getX()-start.getX())*(c.getY()-start.getY());
			return DoubleUtils.equalsEps(left, right);
		}
		return false;
	}
	
	/**
	 * Calculates the common part of the two line segments if there are any. Returns null if the two line
	 * segments are distinct.
	 * @param lc the other line segment
	 * @return the common part or null if they are distinct segments
	 */
	public LineSegment2d common(LineSegment2d lc) {
		boolean lcStartOn = isOnLineSegment(lc.start);
		boolean lcEndOn = isOnLineSegment(lc.end);
		boolean thisStartOn = lc.isOnLineSegment(start);
		boolean thisEndOn = lc.isOnLineSegment(end);
		
		Coords2d lcSide = null;
		Coords2d thisSide = null;
		
		if (lcStartOn) {
			if (lcEndOn) {
				return lc;
			}
			lcSide = lc.start;
		} else if (lcEndOn) {
			lcSide = lc.end;
		}
		
		if (thisStartOn) {
			if (thisEndOn) {
				return this;
			}
			thisSide = start;
		} else if (thisEndOn) {
			thisSide = end;
		}
		
		return lcSide==null || thisSide==null || lcSide.equals(thisSide)  ? null : new LineSegment2d(lcSide, thisSide);
	}

	@Override
	public String toString() {
		return start + " - " + end;
	}

	@Override
	public int hashCode() {
		return 31 + ((end == null) ? 0 : end.hashCode()) +
					((start == null) ? 0 : start.hashCode());
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
		LineSegment2d other = (LineSegment2d) obj;
		
		return extendedEquals(start, other.start) && extendedEquals(end, other.end) ||
				extendedEquals(end, other.start) && extendedEquals(start, other.end);
	}
	
	private static boolean extendedEquals(Object o1, Object o2) {
		return o1==null ? o2==null : o1.equals(o2);
	}
}
