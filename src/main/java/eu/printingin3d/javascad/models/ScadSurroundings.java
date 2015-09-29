package eu.printingin3d.javascad.models;

/**
 * 
 * @author Ivan
 *
 */
public class ScadSurroundings {
	public static final ScadSurroundings EMPTY = new ScadSurroundings("", ""); 
	
	private final String prefix;
	private final String postfix;
	
	private ScadSurroundings(String prefix, String postfix) {
		this.prefix = prefix;
		this.postfix = postfix;
	}

	public ScadSurroundings appendPrefix(String prefix) {
		return new ScadSurroundings(this.prefix + prefix, this.postfix);
	}
	
	public ScadSurroundings appendPostfix(String postfix) {
		return new ScadSurroundings(this.prefix, postfix + this.postfix);
	}
	
	public ScadSurroundings include(ScadSurroundings inner) {
		return new ScadSurroundings(prefix + inner.prefix, inner.postfix + postfix);
	}
	
	public SCAD surroundScad(SCAD source) {
		return source
				.prepend(prefix)
				.append(postfix);
	}
}
