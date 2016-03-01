package eu.printingin3d.javascad.utils;


/**
 * Simple store for two values.
 * @param <T> the type of the first value
 * @param <U> the type of the second value
 */
public class Pair<T, U> {
	private final T value1;
	private final U value2;
	
	/**
	 * Creates a new object with the two given values.
	 * @param value1 the first value
	 * @param value2 the second value
	 */
	public Pair(T value1, U value2) {
		this.value1 = value1;
		this.value2 = value2;
	}
	
	/**
	 * Gives the first value back.
	 * @return the first value
	 */
	public T getValue1() {
		return value1;
	}
	
	/**
	 * Gives the second value back.
	 * @return the second value
	 */
	public U getValue2() {
		return value2;
	}
	
	/**
	 * Creates a new instance of this class with the two values in reversed order.
	 * @return a new instance of this class with the two values in reversed order
	 */
	public Pair<U, T> reverse() {
		return new Pair<U, T>(value2, value1);
	}
}