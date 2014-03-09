package eu.printingin3d.javascad.tranzitions;

/**
 * Denotes the direction. It is used internally by the {@link Mirror} operation, and used by the 
 * {@link Slicer} as well.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public enum Direction {
	/**
	 * X direction.
	 */
	X("1,0,0"), 
	
	/**
	 * Y direction.
	 */
	Y("0,1,0"),
	
	/**
	 * z direction.
	 */
	Z("0,0,1");
	
	private final String mirrorParams;

	private Direction(String mirrorParams) {
		this.mirrorParams = mirrorParams;
	}

	protected String getOpenScadMirrorParams() {
		return mirrorParams;
	}
}
