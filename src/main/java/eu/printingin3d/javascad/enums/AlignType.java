package eu.printingin3d.javascad.enums;

import eu.printingin3d.javascad.coords.Boundary;

/**
 * Denotes the alignment on one axis.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public enum AlignType {
	/**
	 * Aligns to the lower parts on the axis, means the left, front and bottom.
	 */
	MIN,
	/**
	 * Aligns to the higher parts on the axis, means the right, back and top. 
	 */
	MAX,
	/**
	 * Aligns to the middle of the models.
	 */
	CENTER,
	/**
	 * Don't change anything.
	 */
	NONE;
	
	/**
	 * Used internally to calculate the coordinate on the given axis. The result is the necessary 
	 * movement on that axis to reach the wanted alignment.
	 * @param model the model we want to move
	 * @param relateTo the model we use as a reference
	 * @param inside controls which side of the aligned model will be aligned 
	 * @return the necessary movement on that axis to reach the wanted alignment
	 */
	public double calculateCoordinate(Boundary model, Boundary relateTo, boolean inside) {
		switch (this) {
		case MAX:
			return relateTo.getMax()- (inside ? model.getMax() : model.getMin());
		case MIN:
			return relateTo.getMin()- (inside ? model.getMin() : model.getMax());
		case CENTER:
			return relateTo.getMiddle() - model.getMiddle();
		default:
			return 0.0;
		}
	}
	
	/**
	 * Used internally to calculate the coordinate on the given axis. The result is the necessary 
	 * movement on that axis to reach the wanted alignment.
	 * @param model the model we want to move
	 * @param relateTo the coordinate we want to use as a reference in the alignment process
	 * @return the necessary movement on that axis to reach the wanted alignment
	 */
	public double calculateCoordinate(Boundary model, double relateTo) {
		switch (this) {
		case MAX:
			return relateTo - model.getMax();
		case MIN:
			return relateTo - model.getMin();
		case CENTER:
			return relateTo - model.getMiddle();
		default:
			return 0.0;
		}
	}
}
