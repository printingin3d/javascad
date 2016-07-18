package eu.printingin3d.javascad.utils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Miscellaneous helper methods regarding lists.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public final class ListUtils {
	private ListUtils() {
		// prevents creating this object
	}
	
	/**
	 * Creates a new list which contains all the non-null elements of the given list.
	 * @param <T> the type of the parameter list and the resulting list 
	 * @param values the list to be processed
	 * @return the same values except nulls
	 */
	public static <T> List<T> removeNulls(List<T> values) {
		if (values==null) {
			return Collections.emptyList();
		}
		return values.stream().filter(Objects::nonNull).collect(Collectors.toList());
	}
}
