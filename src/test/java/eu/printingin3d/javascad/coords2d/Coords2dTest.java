package eu.printingin3d.javascad.coords2d;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Coords3dTest;
import eu.printingin3d.javascad.coords2d.Coords2d;

public class Coords2dTest {

	@Test
	public void testWithZ() {
		Coords3dTest.assertCoords3dEquals(new Coords3d(10, 20, 30), new Coords2d(10, 20).withZ(30));
	}

}
