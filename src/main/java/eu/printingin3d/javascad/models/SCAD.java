package eu.printingin3d.javascad.models;

/**
 * Immutable object for the SCAD output.
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class SCAD {
	/**
	 * An empty SCAD object.
	 */
	public static final SCAD EMPTY = new SCAD("");
	
	private final String scad;

	/**
	 * Creates a new object which holds the given string.
	 * @param scad the string to represent
	 */
	public SCAD(String scad) {
		this.scad = scad;
	}
	
	/**
	 * True if this result should be included into the output.
	 * @return true if this result should be included into the output
	 */
	public boolean isIncluded() {
		return !scad.isEmpty();
	}
	
	/**
	 * Returns the string representation of the SCAD output.
	 * @return the string representation of the SCAD output
	 */
	public String getScad() {
		return scad;
	}
	
	/**
	 * True if this object doesn't contain any output.
	 * @return true if this object doesn't contain any output
	 */
	public boolean isEmpty() {
		return scad.isEmpty();
	}
	
	/**
	 * Prepend the given text to this object.
	 * @param text the text to be prepend
	 * @return a new object containing the newly concatenated string
	 */
	public SCAD prepend(String text) {
		return new SCAD(text+this.scad);
	}
	
	/**
	 * Append the given text to this object.
	 * @param text the text to be appended
	 * @return a new object containing the newly concatenated string
	 */
	public SCAD append(String text) {
		return new SCAD(this.scad+text);
	}
	
	/**
	 * Append the given text to this object.
	 * @param scad the text to be appended
	 * @return a new object containing the newly concatenated string
	 */
	public SCAD append(SCAD scad) {
		return new SCAD(this.scad+scad.scad);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((scad == null) ? 0 : scad.hashCode());
		return result;
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
		SCAD other = (SCAD) obj;
		if (scad == null) {
			if (other.scad != null) {
				return false;
			}
		} else if (!scad.equals(other.scad)) {
			return false;
		}
		return true;
	}
}
