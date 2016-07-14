package eu.printingin3d.javascad.coords2d;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a line segment.
 * @param <T> the class of the line segment
 */
public class LineSegment<T> {
	protected final T start;
	protected final T end;
	
	/**
	 * Starts a series of line segments. You can get the next segment by calling the
	 * next method. You can use the {@link #lineSegmentSeries(List)} method to convert a
	 * list of coordinates to a list of line segments.
	 * @param p the starting point
	 * @param <T> the class of the line segment
	 * @return the starting of a line segment
	 */
	public static <T> LineSegment<T> startLineSegmentSeries(T p) {
		return new LineSegment<>(null, p);
	}

	/**
	 * Returns with the list of line segments which is denoted by this series of points.
	 * @param coords the list of points
	 * @param <T> the class of the line segment
	 * @return the list of segments
	 */
	public static <T> List<LineSegment<T>> lineSegmentSeries(List<T> coords) {
		List<LineSegment<T>> result = new ArrayList<>();
		LineSegment<T> current = new LineSegment<T>(null, coords.get(coords.size()-1));
		for (T c : coords) {
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
	public LineSegment(T start, T end) {
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Returns with the start point of the segment.
	 * @return the start point of the segment
	 */
	public T getStart() {
		return start;
	}
	
	/**
	 * Returns with the end point of the segment.
	 * @return the end point of the segment
	 */
	public T getEnd() {
		return end;
	}
	
	/**
	 * Creates the next segment of the line segment series.
	 * @param c the next point of the point series
	 * @return the next segment of the line segment series
	 */
	public LineSegment<T> next(T c) {
		return new LineSegment<>(end, c);
	}
	
	/**
	 * Checks if the given coordinate is equals with either end point of this segment.
	 * @param c the point to be checked
	 * @return true if and only if either of the two end points of this segment is equals with 
	 *     the given point 
	 */
	public boolean contains(T c) {
		return c==null ? false : c.equals(start) || c.equals(end);
	}
	
	/**
	 * Checks if this segment and the given segment has a common end point.
	 * @param ls the other segment to be checked
	 * @return true if and only if this segment and the given segment has a common end point
	 */
	public boolean hasCommon(LineSegment<T> ls) {
		return contains(ls.start) || contains(ls.end);
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
		@SuppressWarnings("unchecked")
		LineSegment<T> other = (LineSegment<T>) obj;
		
		return extendedEquals(start, other.start) && extendedEquals(end, other.end) ||
				extendedEquals(end, other.start) && extendedEquals(start, other.end);
	}
	
	private static boolean extendedEquals(Object o1, Object o2) {
		return o1==null ? o2==null : o1.equals(o2);
	}
}
