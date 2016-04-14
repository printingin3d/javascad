package eu.printingin3d.javascad.coords;

import java.util.Collection;

import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.utils.AssertValue;

/**
 * This object calculates the boundary of a series of values. It represents an interval which
 * includes all of the values and all methods will use this interval for any later calculation. 
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Boundary {
	/**
	 * Represents an empty 1D boundary object.
	 */
	public static final Boundary EMPTY = new Boundary(0);
	
	private final double min;
	private final double max;

	/**
	 * Creates a Boundary object which will hold the minimum and maximum values
	 * from the given collection.
	 * @param boundaries the collections which should be considered
	 * @return the Boundary object which will represent the collection
	 */
	public static Boundary combine(Collection<Boundary> boundaries) {
		double[] values = new double[boundaries.size()*2];
		int i=0;
		for (Boundary b : boundaries) {
			values[i++] = b.getMin();
			values[i++] = b.getMax();
		}
		return new Boundary(values);
	}
	
	/**
	 * Creates a Boundary object which will hold the minimum and maximum values
	 * from the given collection.
	 * @param boundaries the collections which should be considered
	 * @return the Boundary object which will represent the collection
	 * @throws IllegalValueException if the given boundaries does not have a common interval or if
	 * 	the given list is empty
	 */
	public static Boundary intersect(Collection<Boundary> boundaries) throws IllegalValueException {
		AssertValue.isNotEmpty(boundaries, "The parameter should not be null or empty");
		 
		double minValue = -Double.MAX_VALUE;
		double maxValue = +Double.MAX_VALUE;
		
		for (Boundary b : boundaries) {
			minValue = Math.max(b.getMin(), minValue);
			maxValue = Math.min(b.getMax(), maxValue);
			AssertValue.isTrue(minValue<=b.getMax(), 
					"The boundaries should intersect with each other, " +
							"but there is a gap between "+minValue+" and "+b.getMax());
			AssertValue.isTrue(maxValue>=b.getMin(), 
					"The boundaries should intersect with each other, " +
							"but there is a gap between "+maxValue+" and "+b.getMin());
		}
		return new Boundary(minValue, maxValue);
	}
	
	/**
	 * Creates a Boundary object which has the given maximum value and the middle is zero.
	 * @param value the maximum value of the Boundary
	 * @return the new symmetric Boundary object
	 */
	public static Boundary createSymmetricBoundary(double value) {
		return new Boundary(-value, value);
	}
	
	/**
	 * Constructs an object using given values to calculate the minimum and maximum values.
	 * @param values the values used by the calculation
	 */
	public Boundary(double... values) {
		double min = 0.0;
		double max = 0.0;
		boolean first = true;
		for (double value : values) {
			if (first) {
				min = max = value;
				first = false;
			}
			else {
				if (min>value) {
					min = value;
				}
				if (max<value) {
					max = value;
				}
			}
		}
		this.min = min;
		this.max = max;
	}
	
	/**
	 * <p>Negate the current object if the parameter is true, but this object 
	 * will be unchanged: a new one will be created with the new values. 
	 * This means the new objects maximum value will be equals this object's minimum value 
	 * negated - the same is true for the minimum value.</p>
	 * <p>If the parameter is false this method returns with this object and does nothing.</p> 
	 * @param doNegate this is the controlling parameter
	 * @return this object negated if the parameter true, the object unchanged otherwise
	 */
	public Boundary negate(boolean doNegate) {
		return doNegate ? new Boundary(-max, -min) : this;
	}

	/**
	 * Returns with the calculated minimum value.
	 * @return the calculated minimum value
	 */
	public double getMin() {
		return min;
	}

	/**
	 * Returns with the calculated maximum value.
	 * @return the calculated maximum value
	 */
	public double getMax() {
		return max;
	}

	/**
	 * Returns the middle point of the interval.
	 * @return the arithmetic mean of the minimum and maximum value
	 */
	public double getMiddle() {
		return (min+max)/2.0;
	}
	
	/**
	 * Returns with the size of the interval.
	 * @return the difference of the maximum and minimum value
	 */
	public double getSize() {
		return max-min;
	}

	/**
	 * Adding the given value to the interval and creates a new Boundary object with the new values.
	 * @param delta the value to move this interval
	 * @return a new object representing the new calculated interval
	 */
	public Boundary add(double delta) {
		return new Boundary(min+delta, max+delta);
	}
	
	/**
	 * Adding the given value to the interval and creates a new Boundary object with the new values.
	 * @param delta the value to add to this interval
	 * @return a new object representing the new calculated interval
	 */
	public Boundary add(Boundary delta) {
		return new Boundary(min+delta.getMin(), max+delta.getMax());
	}
	
	/**
	 * Removes the values of the given boundary from this boundary and returns with the remaining values.
	 * @param b the boundary to be removed
	 * @return the remaining values
	 * @throws IllegalValueException if there is nothing left
	 */
	public Boundary remove(Boundary b) throws IllegalValueException {
		if (this.max<=b.min || this.min>=b.max) {
			return this;
		}
		if (this.min<b.min && this.max>b.min) {
			if (this.max<b.max) {
				return new Boundary(this.min, b.min);
			}
			return this;
		}
		if (this.max>b.max && this.min<b.max) {
			return new Boundary(b.max, this.max);
		}
		throw new IllegalValueException("The given boundary contains this boundary!");
	}
	
	/**
	 * Multiplying the interval with the given value, basically scaling it. The method will create
	 * a new object and the current object will be unchanged.
	 * @param delta the value used by the multiplication
	 * @return a new object representing the new calculated interval
	 */
	public Boundary scale(double delta) {
		return new Boundary(min*delta, max*delta);
	}
	
	/**
	 * Returns true if and only if this boundary is inside of the given boundary. It means
	 * it gives true if the given boundary's minimum value is not greater then this boundary's minimum 
	 * value and the given boundary's maximum value is not less then this boundary's maximum value.
	 * @param b the given boundary
	 * @return true if and only if this boundary is inside of the given boundary
	 */
	public boolean isInsideOf(Boundary b) {
		return b.min<=this.min && b.max>=this.max;
	}
	
	/**
	 * Returns true if and only if the given value is within this boundaries limits.
	 * @param value the given value
	 * @return true if and only if the given value is inside of this boundary
	 */
	public boolean isInside(double value) {
		return value>=this.min && value<=this.max;
	}
}
