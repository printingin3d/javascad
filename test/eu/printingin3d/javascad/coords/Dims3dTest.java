package eu.printingin3d.javascad.coords;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Dims3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;

public class Dims3dTest {

	@Test(expected = IllegalValueException.class)
	public void testX() {
		new Dims3d(-10.0, 20.0, 30.0);
	}

	@Test(expected = IllegalValueException.class)
	public void testY() {
		new Dims3d(10.0, -20.0, 30.0);
	}
	
	@Test(expected = IllegalValueException.class)
	public void testZ() {
		new Dims3d(10.0, 20.0, -30.0);
	}
	
	@Test
	public void increaseShouldIncreaseTheSizeByOne() {
		Dims3d dims = new Dims3d(10.0, 20.0, 30.0).increase();
		
		assertDoubleEquals(11.0, dims.getX());
		assertDoubleEquals(21.0, dims.getY());
		assertDoubleEquals(31.0, dims.getZ());
	}
}
