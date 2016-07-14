package eu.printingin3d.javascad.vrl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Just a test.
 * @param <T> just a test
 */
public class FrontBack<T> {
	private final List<T> front = new ArrayList<>();
	private final List<T> back = new ArrayList<>();

	/**
	 * Just a test.
	 * @param value just a test
	 */
	public void addToFront(T value) {
		front.add(value);
	}
	
	/**
	 * Just a test.
	 * @param value just a test
	 */
	public void addToBack(T value) {
		back.add(value);
	}
	
	/**
	 * Just a test.
	 * @param value just a test
	 */
	public void addToBoth(T value) {
		front.add(value);
		back.add(value);
	}
	
	/**
	 * Just a test.
	 * @param func just a test
	 * @param fb just a test
	 * @param <U> just a test
	 */
	public <U> void addTo(Function<List<T>, U> func, FrontBack<U> fb) {
		fb.addToBack(func.apply(front));
		fb.addToFront(func.apply(back));
	}

	/**
	 * Just a test.
	 * @return just a test
	 */
	public List<T> getFront() {
		return front;
	}

	/**
	 * Just a test.
	 * @return just a test
	 */
	public List<T> getBack() {
		return back;
	}
}
