package eu.printingin3d.javascad.utils;

/**
 * Helper methods with strings.
 * @author ivivan <ivivan@printingin3d.eu>
 */
public final class StringUtils {
	
	private StringUtils() {}

	/**
	 * Returns true only if the given string is not empty, false otherwise.
	 * @param value value to be tested
	 * @return true only if the given string is not empty, false otherwise
	 */
	public static boolean hasText(String value) {
		return value!=null && !value.isEmpty();
	}

	/**
	 * Appends the two given strings together. Creates a new object only if necessary. Handles null parameters.
	 * @param value1 the first string to be used
	 * @param value2 the second string to be used
	 * @return the two strings appended together
	 */
	public static String append(String value1, String value2) {
		if (!hasText(value1)) {
			return value2==null ? "" : value2;
		}
		if (!hasText(value2)) {
			return value1;
		}
		return value1+value2;
	}
}
