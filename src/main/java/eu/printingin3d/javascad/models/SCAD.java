package eu.printingin3d.javascad.models;

import eu.printingin3d.javascad.utils.StringUtils;

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
		return StringUtils.hasText(text)
				? new SCAD(StringUtils.append(text, this.scad))
				: this;
	}
	
	/**
	 * Append the given text to this object.
	 * @param text the text to be appended
	 * @return a new object containing the newly concatenated string
	 */
	public SCAD append(String text) {
		return StringUtils.hasText(text)
				? new SCAD(StringUtils.append(this.scad, text))
				: this;
	}
	
	/**
	 * Append the given text to this object.
	 * @param scad the text to be appended
	 * @return a new object containing the newly concatenated string
	 */
	public SCAD append(SCAD scad) {
		return new SCAD(StringUtils.append(this.scad, scad.scad));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + ((scad == null) ? 0 : scad.hashCode());
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
	
	@Override
	public String toString() {
		return scad;
	}
}
