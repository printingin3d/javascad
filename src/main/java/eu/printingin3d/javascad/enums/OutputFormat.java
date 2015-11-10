package eu.printingin3d.javascad.enums;

/**
 * The output format used by the toString method of the 
 * {@link eu.printingin3d.javascad.coords.Abstract3d Abstract3d} class.
 * @author ivivan <ivivan@printingin3d.eu>
 */
public enum OutputFormat {
	/**
	 * The SCAD output format. The format of a coordinate in SCAD is <code>[10,20,30]</code>.
	 */
	SCAD("[", "]", ","),
	/**
	 * The textual STL output format. The format of a coordinate in an STL file is <code>10 20 30</code>
	 */
	STL("", "", " "),
	/**
	 * The textual Polygon output format. The format of a coordinate in Polygon file is <code>10 20 30</code>
	 */
	POLYGON("", "", " ");
	
	private final String preText;
	private final String postText;
	private final String separator;
	
	OutputFormat(String preText, String postText, String separator) {
		this.preText = preText;
		this.postText = postText;
		this.separator = separator;
	}

	/**
	 * Returns with the string before the three numbers.
	 * @return the string before the three numbers
	 */
	public String getPreText() {
		return preText;
	}

	/**
	 * Returns with the string after the three numbers.
	 * @return the string after the three numbers
	 */
	public String getPostText() {
		return postText;
	}

	/**
	 * Returns with the string separating the three numbers.
	 * @return the string separating the three numbers
	 */
	public String getSeparator() {
		return separator;
	}
}
