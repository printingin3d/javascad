package eu.printingin3d.javascad.utils;

import org.junit.Test;

import eu.printingin3d.javascad.exceptions.IllegalValueException;

public class ModelToFileTest {

	@Test(expected=IllegalValueException.class)
	public void shouldCheckIfTheFileIsNull() {
		new ModelToFile(null);
	}

}
