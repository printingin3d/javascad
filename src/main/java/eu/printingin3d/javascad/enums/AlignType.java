package eu.printingin3d.javascad.enums;

import java.util.EnumMap;
import java.util.Map;

import eu.printingin3d.javascad.coords.Boundary;

/**
 * Denotes the alignment on one axis.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public enum AlignType {
	/**
	 * Aligns to the lower parts on the axis, means the left, front and bottom.
	 * If we align to another object we align to be inside of it.
	 */
	MIN_IN(1.0, 0.0, 1.0, 0.0),
	/**
	 * Aligns to the lower parts on the axis, means the left, front and bottom.
	 * If we align to another object we align to be outside of it.
	 */
	MIN_OUT(1.0, 0.0, 0.0, 1.0),
	/**
	 * <p>Aligns to the lower parts on the axis, means the left, front and bottom.</p>
	 * <p>This behaves exactly the same as {@link #MIN_OUT} for backward compatibility reasons.</p>
	 * @deprecated use {@link MIN_IN} or {@link MIN_OUT} instead
	 */
	MIN(1.0, 0.0, 0.0, 1.0),
	/**
	 * Aligns to the higher parts on the axis, means the right, back and top.
	 * If we align to another object we align to be inside of it.
	 */
	MAX_IN(0.0, 1.0, 0.0, 1.0),
	/**
	 * Aligns to the higher parts on the axis, means the right, back and top.
	 * If we align to another object we align to be outside of it.
	 */
	MAX_OUT(0.0, 1.0, 1.0, 0.0),
	/**
	 * <p>Aligns to the higher parts on the axis, means the right, back and top.</p>
	 * <p>This behaves exactly the same as {@link #MAX_OUT} for backward compatibility reasons.</p>
	 * @deprecated use {@link #MAX_IN} or {@link #MAX_OUT} instead
	 */
	MAX(0.0, 1.0, 1.0, 0.0),
	/**
	 * Aligns to the middle of the models.
	 */
	CENTER(0.5, 0.5, 0.5, 0.5),
	/**
	 * Don't change anything.
	 */
	NONE(0.0, 0.0, 0.0, 0.0);
	
	private static final Map<AlignType, AlignType> INSIDE_MAP = new EnumMap<>(AlignType.class);
	private static final Map<AlignType, AlignType> OUTSIDE_MAP = new EnumMap<>(AlignType.class);
	static {
		INSIDE_MAP.put(MIN, MIN_IN);
		INSIDE_MAP.put(MIN_IN, MIN_IN);
		INSIDE_MAP.put(MIN_OUT, MIN_IN);
		
		INSIDE_MAP.put(MAX, MAX_IN);
		INSIDE_MAP.put(MAX_IN, MAX_IN);
		INSIDE_MAP.put(MAX_OUT, MAX_IN);
		
		INSIDE_MAP.put(CENTER, CENTER);
		INSIDE_MAP.put(NONE, NONE);
		
		OUTSIDE_MAP.put(MIN, MIN_OUT);
		OUTSIDE_MAP.put(MIN_IN, MIN_OUT);
		OUTSIDE_MAP.put(MIN_OUT, MIN_OUT);
		
		OUTSIDE_MAP.put(MAX, MAX_OUT);
		OUTSIDE_MAP.put(MAX_IN, MAX_OUT);
		OUTSIDE_MAP.put(MAX_OUT, MAX_OUT);
		
		OUTSIDE_MAP.put(CENTER, CENTER);
		OUTSIDE_MAP.put(NONE, NONE);
	}
	
	private final double relateMin;
	private final double relateMax;
	private final double modelMin;
	private final double modelMax;
	
	AlignType(double relateMin, double relateMax, double modelMin, double modelMax) {
		this.relateMin = relateMin;
		this.relateMax = relateMax;
		this.modelMin = modelMin;
		this.modelMax = modelMax;
	}

	/**
	 * Return with the corresponding inside alignment of this alignment type. 
	 * @return the corresponding inside alignment of this alignment type
	 */
	public AlignType convertToInside() {
		return INSIDE_MAP.get(this);
	}
	
	/**
	 * Return with the corresponding outside alignment of this alignment type. 
	 * @return the corresponding outside alignment of this alignment type
	 */
	public AlignType convertToOutside() {
		return OUTSIDE_MAP.get(this);
	}
	
	/**
	 * Return with the corresponding inside or outside alignment of this alignment type based on
	 * the given parameter.
	 * @param inside determine if we want to get the inside or outside version of this aligment 
	 * @return the corresponding outside alignment of this alignment type
	 */
	public AlignType convertTo(boolean inside) {
		return inside ? convertToInside() : convertToOutside();
	}

	/**
	 * Returns with the side of the given boundary denoted by this align.
	 * @param boundary the boundary we want to get
	 * @return the minimum, maximum or middle value of the given boundary - or zero if the alignment is NONE
	 */
	public double getAlignedValue(Boundary boundary) {
		return relateMin*boundary.getMin() + relateMax*boundary.getMax();
	}

	/**
	 * Used internally to calculate the coordinate on the given axis. The result is the necessary 
	 * movement on that axis to reach the wanted alignment.
	 * @param model the model we want to move
	 * @param relateTo the model we use as a reference
	 * @param inside controls which side of the aligned model will be aligned 
	 * @return the necessary movement on that axis to reach the wanted alignment
	 */
	protected double calculateCoordinate(Boundary model, Boundary relateTo) {
		return relateMin*relateTo.getMin()+relateMax*relateTo.getMax()
				- modelMin*model.getMin()-modelMax*model.getMax();
	}
	
	/**
	 * Used internally to calculate the coordinate on the given axis. The result is the necessary 
	 * movement on that axis to reach the wanted alignment.
	 * @param model the model we want to move
	 * @param relateTo the coordinate we want to use as a reference in the alignment process
	 * @return the necessary movement on that axis to reach the wanted alignment
	 */
	protected double calculateCoordinate(Boundary model, double relateTo) {
		switch (this) {
		case MAX:
		case MAX_IN:
		case MAX_OUT:
			return relateTo - model.getMax();
		case MIN:
		case MIN_IN:
		case MIN_OUT:
			return relateTo - model.getMin();
		case CENTER:
			return relateTo - model.getMiddle();
		default:
			return 0.0;
		}
	}
}
