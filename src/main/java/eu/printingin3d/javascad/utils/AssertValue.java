package eu.printingin3d.javascad.utils;

import java.util.Collection;

import eu.printingin3d.javascad.exceptions.IllegalValueException;

/**
 * Helper class with convenience method to check assertions.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public final class AssertValue {
	
	private AssertValue() {
		// prevents instantiating this object
	}
	
	/**
	 * Checks if the value is null - if it is, throws an IllegalValueException with the given message.
	 * @param value the object to be checked
	 * @param message the message used in the exception
	 * @throws IllegalValueException if the value parameter is null
	 */
	public static void isNotNull(Object value, String message) throws IllegalValueException {
		isFalse(value==null, message);
	}
	
	/**
	 * Checks if the value is null and not empty - if it is, throws an IllegalValueException 
	 * with the given message.
	 * @param value the object to be checked
	 * @param message the message used in the exception
	 * @param <T> the type of the collection
	 * @throws IllegalValueException if the value parameter is null or an empty list
	 */
	public static <T> void isNotEmpty(Collection<T> value, String message) throws IllegalValueException {
		isFalse(value==null || value.isEmpty(), message);
	}

	/**
	 * Checks if the value is false - if it isn't, throws an IllegalValueException with the given message.
	 * @param value the value to be checked
	 * @param message the message used in the exception
	 * @throws IllegalValueException if the value parameter is null
	 */
	public static void isFalse(boolean value, String message) throws IllegalValueException {
		if (value) {
			throw new IllegalValueException(message);
		}
	}
	
	/**
	 * Checks if the value is true - if it isn't, throws an IllegalValueException with the given message.
	 * @param value the value to be checked
	 * @param message the message used in the exception
	 * @throws IllegalValueException if the value parameter is null
	 */
	public static void isTrue(boolean value, String message) throws IllegalValueException {
		if (!value) {
			throw new IllegalValueException(message);
		}
	}
	
	/**
	 * Checks if the value is not negative - if it is, throws an IllegalValueException with the given message.
	 * @param value the value to be checked
	 * @param message the message used in the exception
	 * @throws IllegalValueException if the value parameter is null
	 */
	public static void isNotNegative(double value, String message) throws IllegalValueException {
		isFalse(value<0.0, message);
	}
	
	/**
	 * Checks if the value is not negative - if it is, throws an IllegalValueException with the given message.
	 * @param value the value to be checked
	 * @param message the message used in the exception
	 * @throws IllegalValueException if the value parameter is null
	 */
	public static void isNotNegative(int value, String message) throws IllegalValueException {
		isFalse(value<0, message);
	}
}
