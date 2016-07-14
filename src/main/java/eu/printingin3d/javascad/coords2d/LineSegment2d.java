package eu.printingin3d.javascad.coords2d;

import java.util.ArrayList;
import java.util.List;

import eu.printingin3d.javascad.utils.DoubleUtils;

/**
 * Represents a 2D line segment.
 */
public class LineSegment2d extends LineSegment<Coords2d> {
	
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
	public static List<LineSegment2d> lineSegmentSeries2d(List<Coords2d> coords) {
		List<LineSegment2d> result = new ArrayList<>();
		LineSegment2d current = startLineSegmentSeries(coords.get(coords.size()-1));
		for (Coords2d c : coords) {
			current = current.next(c);
			result.add(current);
		}
		return result;
	}
	
	@Override
	public LineSegment2d next(Coords2d c) {
		return new LineSegment2d(end, c);
	}

	/**
	 * Creates a new line segment using the two given end points.
	 * @param start the starting point of the segment
	 * @param end the end point of the segment
	 */
	public LineSegment2d(Coords2d start, Coords2d end) {
		super(start, end);
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
}
