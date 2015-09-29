package eu.printingin3d.javascad.vrl;

import static org.junit.Assert.assertFalse;

import java.awt.Color;

import org.junit.Test;

import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.testutils.RandomUtils;

public class VertexTest {
	
	@Test(expected = IllegalValueException.class)
	public void coordsShouldNotBeNull() {
		new Vertex(null, Color.BLACK);
	}
	
	@Test
	public void shouldNotEqualNull() {
		Vertex v = new Vertex(RandomUtils.getRandomCoords(), Color.BLACK);
		
		assertFalse(v.equals(null));
	}
	
	@Test
	public void shouldNotEqualOtherTypesOfObject() {
		Vertex v = new Vertex(RandomUtils.getRandomCoords(), Color.BLACK);
		
		assertFalse(v.equals("asd"));
	}
}

