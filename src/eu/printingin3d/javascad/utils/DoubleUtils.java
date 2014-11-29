package eu.printingin3d.javascad.utils;

/**
 * Miscellaneous helper methods regarding doubles.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public final class DoubleUtils {
	private static final double EPSILON = 0.0001;
	private static final double ZERO_EPSILON = 0.000001;
	
	private DoubleUtils() {
		// prevents creating this class
	}
	
	/**
	 * Checks the the given doubles if they are equals - which means they are closer than a 
	 * predefined epsilon value, because comparing two doubles directly is a bad practice.
	 * @param a one of the values to be checked
	 * @param b the other value to be checked
	 * @return true if the two values are close enough to call them equals
	 */
	public static boolean equalsEps(double a, double b) {
		return Math.abs(a-b)<EPSILON;
	}
	
	public static boolean isZero(double a) {
		return Math.abs(a)<ZERO_EPSILON;
	}
	
	/**
	 * Format the given double with four digits precision, but leave it without a decimal point.
	 * if it is a round value
	 * @param value the value to be formatted
	 * @return the formatted string
	 */
	public static String formatDouble(double value) {
		if (equalsEps(Math.round(value), value)) {
			return Long.toString(Math.round(value));
		}
		return Double.toString(Math.round(value*10000.0)/10000.0);
	}

	/**
	 * Returns true if and only if the given value is between value1 and value2. It doesn't matter 
	 * if value1 is bigger than value2 or vica-versa.
	 * 
	 * @param value the value to be checked
	 * @param value1 the border of the interval
	 * @param value2 the border of the interval.
	 * @return  true if and only if the given value is between value1 and value2
	 */
	public static boolean between(double value, double value1, double value2) {
		return (value1<=value && value<=value2) || (value2<=value && value<=value1);
	}
}
