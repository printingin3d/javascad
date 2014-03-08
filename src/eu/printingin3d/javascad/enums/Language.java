package eu.printingin3d.javascad.enums;

/**
 * <p>This enumeration controls the language the result will be. Currently there are
 * two supported language: the default OpenSCAD and the experimental POVRay.</p>
 * <p>Switching between the languages is easy: you just need to call the
 * setCurrent() method of the wanted language's enum.</p>
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public enum Language {
	/**
	 * Enum value for OpenSCAD.
	 */
	OpenSCAD('[', ']'),
	/**
	 * Enum value for POVRay.
	 */
	POVRay('<', '>');
	
	private static Language current = Language.OpenSCAD;
	
	private final char vectorStartChar;
	private final char vectorEndChar;

	private Language(char vectorStartChar, char vectorEndChar) {
		this.vectorStartChar = vectorStartChar;
		this.vectorEndChar = vectorEndChar;
	}

	/**
	 * The starting character for the vectors. This is '[' for 
	 * OpenSCAD and '<' for POVRay. 
	 * @return the starting character for the vectors 
	 */
	public char getVectorStartChar() {
		return vectorStartChar;
	}

	/**
	 * The ending character for the vectors. This is ']' for 
	 * OpenSCAD and '>' for POVRay. 
	 * @return the ending character for the vectors
	 */
	public char getVectorEndChar() {
		return vectorEndChar;
	}
	
	/**
	 * Checks if this is the active language.
	 * @return true if and only if this is the active language
	 */
	public boolean isActive() {
		return this == getCurrent();
	}

	/**
	 * Gets the current active language.
	 * @return the current active language
	 */
	public static Language getCurrent() {
		return current;
	}

	/**
	 * Sets this as the current language.
	 */
	public void setCurrent() {
		Language.current = this;
	}
}
