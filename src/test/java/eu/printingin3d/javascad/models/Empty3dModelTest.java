package eu.printingin3d.javascad.models;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Empty3dModelTest {

	@Test
	public void csgShouldBeEmpty() {
		assertTrue(new Empty3dModel().toCSG().getPolygons().isEmpty());
	}
	
	@Test
	public void addModelShouldGiveBackTheAddedObject() {
		Abstract3dModel added = new Cube(10.0);
		assertSame(added, new Empty3dModel().addModel(added));
	}
	
	@Test
	public void subtractingSomethingFromAnEmptyObjectShouldBeEmpty() {
		Abstract3dModel empty = new Empty3dModel();;
		assertSame(empty, empty.subtractModel(new Cube(10.0)));
	}
}
