package eu.printingin3d.javascad.basic;

import eu.printingin3d.javascad.utils.DoubleUtils;

/**
 * Basic functions.
 * 
 * @param <T> just a test
 */
public abstract class BasicFunc <T extends BasicFunc<T>> {
	protected final double value;

	protected BasicFunc(double value) {
		this.value = value;
	}
	
	protected abstract T create(double value);
	protected abstract T thisOne();
	
	/**
	 * Checks if this object represents the zero angle.
	 * @return true if and only if this object represents the zero angle
	 */
	public boolean isZero() {
		return DoubleUtils.isZero(value);
	}
	
	/**
	 * Multiplies this object with the given value and returns with a new angle 
	 * representing the new value.
	 * @param value the value to be used
	 * @return a new object which represents the multiplied value
	 */
	public T mul(double value) {
		return create(this.value*value);
	}
	
	/**
	 * Divides this object with the given value and returns with a new object 
	 * representing the new value.
	 * @param value the value to be used
	 * @return a new object which represents the divided value
	 */
	public T divide(double value) {
		return create(this.value/value);
	}
	
	/**
	 * Calculates the difference of this and the given value.
	 * @param value the other angle to be used
	 * @return a new object representing the difference of the two angles 
	 */
	public T substract(T value) {
		return create(this.value - value.value);
	}
	
	/**
	 * Calculates the sum of this and the given value.
	 * @param value the other angle to be used
	 * @return a new object representing the sum of the two angles 
	 */
	public T add(T value) {
		return create(this.value + value.value);
	}
	
	/**
	 * Creates the inverse of this object.
	 * @return a new object representing the inverse of this object
	 */
	public T inverse() {
		return create(-value);
	}
	
	/**
	 * Returns with the bigger object of this object and the given object.
	 * @param b second object to compare
	 * @return the bigger object of the two objects 
	 */
	public T max(T b) {
		return this.value>b.value ? thisOne() : b;
	}
	
	/**
	 * Returns with the smaller object of this object and the given object.
	 * @param b second object to compare
	 * @return the smaller object of the two objects 
	 */
	public T min(T b) {
		return this.value<b.value ? thisOne() : b;
	}

	@Override
	public int hashCode() {
		return DoubleUtils.hashCodeEps(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BasicFunc<?> other = (BasicFunc<?>) obj;
		return DoubleUtils.equalsEps(value, other.value);
	}
}
