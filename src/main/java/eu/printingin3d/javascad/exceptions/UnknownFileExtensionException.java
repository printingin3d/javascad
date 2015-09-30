package eu.printingin3d.javascad.exceptions;

/**
 * Unknown file extension exception.
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class UnknownFileExtensionException extends RuntimeException {
	private static final long serialVersionUID = -1427465032184073037L;

	/**
	 * Creates a new exception object with the given message.
	 * @param messasge the message of the exception
	 */
	public UnknownFileExtensionException(String messasge) {
		super(messasge);
	}

}
