package eu.printingin3d.javascad.models;

import eu.printingin3d.javascad.utils.StringUtils;

/**
 * Immutable representation of prefix and suffix of the generated SCAD output.
 * @author ivivan <ivivan@printingin3d.eu>
 */
public final class ScadSurroundings {
	/**
	 * An empty object to start from.
	 */
	public static final ScadSurroundings EMPTY = new ScadSurroundings("", ""); 
	
	private final String prefix;
	private final String postfix;
	
	private ScadSurroundings(String prefix, String postfix) {
		this.prefix = prefix;
		this.postfix = postfix;
	}

	/**
	 * Appends the given text to the prefix in this object and returns with a new object representing the new
	 * prefix and suffix.
	 * @param prefix the text to be appended to the prefix
	 * @return object representing the new prefix and suffix
	 */
	public ScadSurroundings appendPrefix(String prefix) {
		return StringUtils.hasText(prefix) 
				? new ScadSurroundings(StringUtils.append(this.prefix, prefix), this.postfix)
		        : this;
	}
	
	/**
	 * Prepends the given text to the postfix in this object and returns with a new object representing the new
	 * prefix and suffix.
	 * @param postfix the text to be prepended to the postfix
	 * @return object representing the new prefix and suffix
	 */
	public ScadSurroundings appendPostfix(String postfix) {
		return StringUtils.hasText(postfix)
				? new ScadSurroundings(this.prefix, StringUtils.append(postfix, this.postfix))
				: this;
	}
	
	/**
	 * Inserts the given surrounding inside of this object.
	 * @param inner the surroundings to be inserted
	 * @return object representing the new prefix and suffix
	 */
	public ScadSurroundings include(ScadSurroundings inner) {
		return new ScadSurroundings(prefix + inner.prefix, inner.postfix + postfix);
	}
	
	/**
	 * Creates the full SCAD file by prepending the prefix and appending the postfix
	 * in this object to the given SCAD output.
	 * @param source the SCAD source to be used
	 * @return a new SCAD file representing the surrounded source 
	 */
	public SCAD surroundScad(SCAD source) {
		return source.isEmpty() ? source :
				source
				.prepend(prefix)
				.append(postfix);
	}
}
