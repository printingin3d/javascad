package eu.printingin3d.javascad.coords;

import org.junit.Test;

import eu.printingin3d.javascad.exceptions.IllegalValueException;

public class RadiusTest {
	
	@Test(expected = IllegalValueException.class)
	public void negativeRadiusShouldThrowException() {
		Radius.fromRadius(-5.0);
	}

}
