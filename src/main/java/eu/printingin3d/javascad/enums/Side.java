package eu.printingin3d.javascad.enums;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Coords3d;

/**
 * This class is used by the {@link eu.printingin3d.javascad.models.Abstract3dModel
 * 		#align(Side, eu.printingin3d.javascad.models.Abstract3dModel, boolean)
 * 	    Abstract3dModel.align(Side, Abstract3dModel, boolean)} method to 
 * set the side where the other model should be aligned.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Side {
	/**
	 * The top of the model.
	 */
	public static final Side TOP = new Side(AlignType.NONE, AlignType.NONE, AlignType.MAX);
	/**
	 * The bottom of the model.
	 */
	public static final Side BOTTOM = new Side(AlignType.NONE, AlignType.NONE, AlignType.MIN);
	/**
	 * The left side of the model.
	 */
	public static final Side LEFT = new Side(AlignType.MIN, AlignType.NONE, AlignType.NONE);
	/**
	 * The right side of the model.
	 */
	public static final Side RIGHT = new Side(AlignType.MAX, AlignType.NONE, AlignType.NONE);
	/**
	 * The front side of the model.
	 */
	public static final Side FRONT = new Side(AlignType.NONE, AlignType.MIN, AlignType.NONE);
	/**
	 * The back side of the model.
	 */
	public static final Side BACK = new Side(AlignType.NONE, AlignType.MAX, AlignType.NONE);
	/**
	 * The center of the model.
	 */
	public static final Side CENTER = new Side(AlignType.CENTER, AlignType.CENTER, AlignType.CENTER);
	
	private final AlignType alignX;
	private final AlignType alignY;
	private final AlignType alignZ;
	
	/**
	 * Constructs a side object with the given aligns on the X, Y and Z axis. 
	 * @param alignX the alignment on the X axis
	 * @param alignY the alignment on the Y axis
	 * @param alignZ the alignment on the Z axis
	 */
	public Side(AlignType alignX, AlignType alignY, AlignType alignZ) {
		this.alignX = alignX;
		this.alignY = alignY;
		this.alignZ = alignZ;
	}
	
	/**
	 * This method is used internally by the {@link Abstract3dModel#align(Side, Abstract3dModel, boolean)}.
	 * It calculates the coordinates of the position the first model should be placed to be
	 * in the desired position.
	 * @param model the model we want to move
	 * @param relateTo the model we use as a reference
	 * @param inside controls which side of the aligned model will be aligned 
	 * @return the coordinates we should use in a move operation
	 */
	public Coords3d calculateCoords(Boundaries3d model, Boundaries3d relateTo, boolean inside) {
		return new Coords3d(
				alignX.calculateCoordinate(model.getX(), relateTo.getX(), inside),
				alignY.calculateCoordinate(model.getY(), relateTo.getY(), inside),
				alignZ.calculateCoordinate(model.getZ(), relateTo.getZ(), inside));
	}
	
	/**
	 * This method is used internally by the {@link Abstract3dModel#align(Side, Abstract3dModel, boolean)}.
	 * It calculates the coordinates of the position the first model should be placed to be
	 * in the desired position.
	 * @param model the model we want to move
	 * @param relateTo the coordinate we use as a reference
	 * @return the coordinates we should use in a move operation
	 */
	public Coords3d calculateCoords(Boundaries3d model, Coords3d relateTo) {
		return new Coords3d(
				alignX.calculateCoordinate(model.getX(), relateTo.getX()),
				alignY.calculateCoordinate(model.getY(), relateTo.getY()),
				alignZ.calculateCoordinate(model.getZ(), relateTo.getZ()));
	}

	/**
	 * Returns with the alignment on the x axis.
	 * @return the alignment on the x axis
	 */
	public AlignType getAlignX() {
		return alignX;
	}

	/**
	 * Returns with the alignment on the y axis.
	 * @return the alignment on the y axis
	 */
	public AlignType getAlignY() {
		return alignY;
	}

	/**
	 * Returns with the alignment on the z axis.
	 * @return the alignment on the z axis
	 */
	public AlignType getAlignZ() {
		return alignZ;
	}
}
