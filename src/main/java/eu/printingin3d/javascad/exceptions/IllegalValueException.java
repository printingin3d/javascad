package eu.printingin3d.javascad.exceptions;

/**
 * <p>Thrown when some parameter of the method or constructor is invalid.</p>
 * <p>It is a Runtime exception to make it easier to handle - these types of 
 * errors are not meant to be caught anyway.</p>
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class IllegalValueException extends RuntimeException {
	private static final long serialVersionUID = -4061356054143939648L;

	/**
	 * Constructs the exception with the given message.
	 * @param message the message of the new exception
	 */
	public IllegalValueException(String message) {
		super(message);
	}
	
	/**
	 * Constructs the exception with the given message and cause.
	 * @param message the message of the new exception
	 * @param cause the exception caused this exception
	 */
	public IllegalValueException(String message, Throwable cause) {
		super(message, cause);
	}
}
