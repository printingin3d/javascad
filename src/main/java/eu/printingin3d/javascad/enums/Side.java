package eu.printingin3d.javascad.enums;

import static eu.printingin3d.javascad.enums.AlignType.MAX_IN;
import static eu.printingin3d.javascad.enums.AlignType.MAX_OUT;
import static eu.printingin3d.javascad.enums.AlignType.MIN_IN;
import static eu.printingin3d.javascad.enums.AlignType.MIN_OUT;
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
	 * <p>This behaves exactly the same as {@link #TOP_OUT} for backward compatibility reasons.</p>
	 * @deprecated Use {@link #TOP_IN} or {@link #TOP_OUT} instead.
	 */
	@Deprecated
	public static final Side TOP = new Side(AlignType.NONE, AlignType.NONE, AlignType.MAX);
	/**
	 * The top of the model inside. Only the Z coordinate will changed during the alignment.
	 */
	public static final Side TOP_IN = new Side(AlignType.NONE, AlignType.NONE, MAX_IN);
	/**
	 * The top of the model outside. Only the Z coordinate will changed during the alignment.
	 */
	public static final Side TOP_OUT = new Side(AlignType.NONE, AlignType.NONE, MAX_OUT);
	/**
	 * The inside top and center of the model. The model will be aligned to the center on the
	 * X and Y axis, and inside top on the Z axis.
	 */
	public static final Side TOP_IN_CENTER = new Side(AlignType.CENTER, AlignType.CENTER, MAX_IN);
	/**
	 * The outside top and center of the model. The model will be aligned to the center on the
	 * X and Y axis, and outside top on the Z axis.
	 */
	public static final Side TOP_OUT_CENTER = new Side(AlignType.CENTER, AlignType.CENTER, MAX_OUT);
	/**
	 * The bottom of the model.
	 * <p>This behaves exactly the same as {@link #BOTTOM_OUT} for backward compatibility reasons.</p>
	 * @deprecated Use {@link #BOTTOM_IN} or {@link #BOTTOM_OUT} instead.
	 */
	@Deprecated
	public static final Side BOTTOM = new Side(AlignType.NONE, AlignType.NONE, AlignType.MIN);
	/**
	 * The bottom of the model inside. Only the Z coordinate will changed during the alignment.
	 */
	public static final Side BOTTOM_IN = new Side(AlignType.NONE, AlignType.NONE, MIN_IN);
	/**
	 * The bottom of the model outside. Only the Z coordinate will changed during the alignment.
	 */
	public static final Side BOTTOM_OUT = new Side(AlignType.NONE, AlignType.NONE, MIN_OUT);
	/**
	 * The inside bottom and center of the model. The model will be aligned to the center on the
	 * X and Y axis, and inside bottom on the Z axis.
	 */
	public static final Side BOTTOM_IN_CENTER = new Side(AlignType.CENTER, AlignType.CENTER, MIN_IN);
	/**
	 * The outside bottom and center of the model. The model will be aligned to the center on the
	 * X and Y axis, and outside bottom on the Z axis.
	 */
	public static final Side BOTTOM_OUT_CENTER = new Side(AlignType.CENTER, AlignType.CENTER, MIN_OUT);
	/**
	 * The left side of the model.
	 * <p>This behaves exactly the same as {@link #LEFT_OUT} for backward compatibility reasons.</p>
	 * @deprecated Use {@link #LEFT_IN} or {@link #LEFT_OUT} instead.
	 */
	@Deprecated
	public static final Side LEFT = new Side(AlignType.MIN, AlignType.NONE, AlignType.NONE);
	/**
	 * The left side of the model. Only the X coordinate will changed during the alignment.
	 */
	public static final Side LEFT_IN = new Side(MIN_IN, AlignType.NONE, AlignType.NONE);
	/**
	 * The left side of the model. Only the X coordinate will changed during the alignment.
	 */
	public static final Side LEFT_OUT = new Side(MIN_OUT, AlignType.NONE, AlignType.NONE);
	/**
	 * The inside left and center of the model. The model will be aligned to the center on the
	 * Y and Z axis, and inside left on the X axis.
	 */
	public static final Side LEFT_IN_CENTER = new Side(MIN_IN, AlignType.CENTER, AlignType.CENTER);
	/**
	 * The outside left and center of the model. The model will be aligned to the center on the
	 * Y and Z axis, and outside left on the X axis.
	 */
	public static final Side LEFT_OUT_CENTER = new Side(MIN_OUT, AlignType.CENTER, AlignType.CENTER);
	/**
	 * The right side of the model.
	 * <p>This behaves exactly the same as {@link #RIGHT_OUT} for backward compatibility reasons.</p>
	 * @deprecated Use {@link #RIGHT_IN} or {@link #RIGHT_OUT} instead
	 */
	@Deprecated
	public static final Side RIGHT = new Side(AlignType.MAX, AlignType.NONE, AlignType.NONE);
	/**
	 * The right side of the model. Only the X coordinate will changed during the alignment.
	 */
	public static final Side RIGHT_IN = new Side(MAX_IN, AlignType.NONE, AlignType.NONE);
	/**
	 * The right side of the model. Only the X coordinate will changed during the alignment.
	 */
	public static final Side RIGHT_OUT = new Side(MAX_OUT, AlignType.NONE, AlignType.NONE);
	/**
	 * The inside right and center of the model. The model will be aligned to the center on the
	 * Y and Z axis, and inside right on the X axis.
	 */
	public static final Side RIGHT_IN_CENTER = new Side(MAX_IN, AlignType.CENTER, AlignType.CENTER);
	/**
	 * The outside right and center of the model. The model will be aligned to the center on the
	 * Y and Z axis, and outside right on the X axis.
	 */
	public static final Side RIGHT_OUT_CENTER = new Side(MAX_OUT, AlignType.CENTER, AlignType.CENTER);
	/**
	 * The front side of the model.
	 * <p>This behaves exactly the same as {@link #FRONT_OUT} for backward compatibility reasons.</p>
	 * @deprecated Use {@link #FRONT_IN} or {@link #FRONT_OUT} instead.
	 */
	@Deprecated
	public static final Side FRONT = new Side(AlignType.NONE, AlignType.MIN, AlignType.NONE);
	/**
	 * The front side of the model. Only the Y coordinate will changed during the alignment.
	 */
	public static final Side FRONT_IN = new Side(AlignType.NONE, MIN_IN, AlignType.NONE);
	/**
	 * The front side of the model. Only the Y coordinate will changed during the alignment.
	 */
	public static final Side FRONT_OUT = new Side(AlignType.NONE, MIN_OUT, AlignType.NONE);
	/**
	 * The inside front and center of the model. The model will be aligned to the center on the
	 * X and Z axis, and inside front on the Y axis.
	 */
	public static final Side FRONT_IN_CENTER = new Side(AlignType.CENTER, MIN_IN, AlignType.CENTER);
	/**
	 * The outside front and center of the model. The model will be aligned to the center on the
	 * X and Z axis, and outside front on the Y axis.
	 */
	public static final Side FRONT_OUT_CENTER = new Side(AlignType.CENTER, MIN_OUT, AlignType.CENTER);
	/**
	 * The back side of the model.
	 * <p>This behaves exactly the same as {@link #BACK_OUT} for backward compatibility reasons.</p>
	 * @deprecated Use {@link #BACK_IN} or {@link #BACK_OUT} instead.
	 */
	@Deprecated
	public static final Side BACK = new Side(AlignType.NONE, AlignType.MAX, AlignType.NONE);
	/**
	 * The back side of the model inside. Only the Y coordinate will changed during the alignment.
	 */
	public static final Side BACK_IN = new Side(AlignType.NONE, MAX_IN, AlignType.NONE);
	/**
	 * The back side of the model outside. Only the Y coordinate will changed during the alignment.
	 */
	public static final Side BACK_OUT = new Side(AlignType.NONE, MAX_OUT, AlignType.NONE);
	/**
	 * The inside back and center of the model. The model will be aligned to the center on the
	 * X and Z axis, and inside back on the Y axis.
	 */
	public static final Side BACK_IN_CENTER = new Side(AlignType.CENTER, MAX_IN, AlignType.CENTER);
	/**
	 * The outside back and center of the model. The model will be aligned to the center on the
	 * X and Z axis, and outside back on the Y axis.
	 */
	public static final Side BACK_OUT_CENTER = new Side(AlignType.CENTER, MAX_OUT, AlignType.CENTER);
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
	 * This method is used internally by the {@link eu.printingin3d.javascad.models.Abstract3dModel
	 * 		#align(Side, eu.printingin3d.javascad.models.Abstract3dModel, boolean) 
	 * 		Abstract3dModel.align(Side, Abstract3dModel, boolean)}.
	 * It calculates the coordinates of the position the first model should be placed to be
	 * in the desired position.
	 * @param model the model we want to move
	 * @param relateTo the model we use as a reference
	 * @param inside controls which side of the aligned model will be aligned 
	 * @return the coordinates we should use in a move operation
	 * @deprecated Use {@link #calculateCoords(Boundaries3d, Boundaries3d)} instead
	 */
	@Deprecated
	public Coords3d calculateCoords(Boundaries3d model, Boundaries3d relateTo, boolean inside) {
		return new Coords3d(
				alignX.convertTo(inside).calculateCoordinate(model.getX(), relateTo.getX()),
				alignY.convertTo(inside).calculateCoordinate(model.getY(), relateTo.getY()),
				alignZ.convertTo(inside).calculateCoordinate(model.getZ(), relateTo.getZ()));
	}
	
	/**
	 * This method is used internally by the {@link eu.printingin3d.javascad.models.Abstract3dModel
	 * 		#align(Side, eu.printingin3d.javascad.models.Abstract3dModel, boolean) 
	 * 		Abstract3dModel.align(Side, Abstract3dModel, boolean)}.
	 * It calculates the coordinates of the position the first model should be placed to be
	 * in the desired position.
	 * @param model the model we want to move
	 * @param relateTo the model we use as a reference
	 * @return the coordinates we should use in a move operation
	 */
	public Coords3d calculateCoords(Boundaries3d model, Boundaries3d relateTo) {
		return new Coords3d(
				alignX.calculateCoordinate(model.getX(), relateTo.getX()),
				alignY.calculateCoordinate(model.getY(), relateTo.getY()),
				alignZ.calculateCoordinate(model.getZ(), relateTo.getZ()));
	}
	
	/**
	 * This method is used internally by the 
	 * {@link eu.printingin3d.javascad.models.Abstract3dModel#align(Side, Coords3d) 
	 * 		Abstract3dModel.align(Side, Coords3d)}.
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
