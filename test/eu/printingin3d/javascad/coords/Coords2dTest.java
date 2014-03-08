package eu.printingin3d.javascad.coords;

import org.junit.Test;

public class Coords2dTest {

	@Test
	public void testWithZ() {
		Coords3dTest.assertCoords3dEquals(new Coords3d(10, 20, 30), new Coords2d(10, 20).withZ(30));
	}

}
