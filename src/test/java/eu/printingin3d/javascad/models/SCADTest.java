package eu.printingin3d.javascad.models;


import static org.junit.Assert.assertSame;

import org.junit.Test;

public class SCADTest {
	@Test
	public void prependShouldNotCreateNewObjectIfEmptyParameterIsGiven() {
		assertSame(SCAD.EMPTY, SCAD.EMPTY.prepend(""));
	}
	
	@Test
	public void appendShouldNotCreateNewObjectIfEmptyParameterIsGiven() {
		assertSame(SCAD.EMPTY, SCAD.EMPTY.append(""));
	}
}
