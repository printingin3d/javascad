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
	X("1,0,0", "-1,1,1"), 
	
	/**
	 * Y direction.
	 */
	Y("0,1,0", "1,-1,1"),
	
	/**
	 * z direction.
	 */
	Z("0,0,1", "1,1,-1");
	
	private final String openScadMirrorParams;
	private final String povRayMirrorParams;

	private Direction(String openScadMirrorParams, String povRayMirrorParams) {
		this.openScadMirrorParams = openScadMirrorParams;
		this.povRayMirrorParams = povRayMirrorParams;
	}

	protected String getOpenScadMirrorParams() {
		return openScadMirrorParams;
	}

	protected String getPovRayMirrorParams() {
		return povRayMirrorParams;
	}
}
