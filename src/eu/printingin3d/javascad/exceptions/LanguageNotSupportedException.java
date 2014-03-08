package eu.printingin3d.javascad.exceptions;

import eu.printingin3d.javascad.enums.Language;

/**
 * <p>Thrown when the requested operation does not support the current language.</p>
 * <p>It is a Runtime exception to make it easier to handle - these types of 
 * errors are not meant to be caught anyway.</p>
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class LanguageNotSupportedException extends UnsupportedOperationException {
	private static final long serialVersionUID = -5610339347870031153L;

	/**
	 * Constructs the exception with a useful message which contains
	 * the current language.
	 */
	public LanguageNotSupportedException() {
		super("Language "+Language.getCurrent()+" is not supported!");
	}
}
