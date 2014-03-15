package eu.printingin3d.javascad.coords2d;

import org.junit.Test;

import eu.printingin3d.javascad.exceptions.IllegalValueException;

public class Dims2dTest {

	@Test(expected = IllegalValueException.class)
	public void testX() {
		new Dims2d(-10.0, 20.0);
	}

	@Test(expected = IllegalValueException.class)
	public void testY() {
		new Dims2d(10.0, -20.0);
	}
}
