package eu.printingin3d.javascad.enums;

public enum OutputFormat {
	SCAD("[", "]", ","),
	STL("", "", " ");
	
	private final String preText;
	private final String postText;
	private final String separator;
	
	private OutputFormat(String preText, String postText, String separator) {
		this.preText = preText;
		this.postText = postText;
		this.separator = separator;
	}

	public String getPreText() {
		return preText;
	}

	public String getPostText() {
		return postText;
	}

	public String getSeparator() {
		return separator;
	}
}
