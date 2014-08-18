package eu.printingin3d.javascad.vrl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.testutils.RandomUtils;

public class VertexTest {
	
	@Test(expected = IllegalValueException.class)
	public void coordsShouldNotBeNull() {
		new Vertex(null, Color.BLACK);
	}
	
	@Test
	public void shouldEqualItself() {
		Vertex v = new Vertex(RandomUtils.getRandomCoords(), Color.BLACK);
	
		shouldEqual(v, v);
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
	
	@Test
	public void shouldNotEqualOtherColor() {
		Coords3d c = RandomUtils.getRandomCoords();
		Vertex v1 = new Vertex(c, Color.BLACK);
		Vertex v2 = new Vertex(c, Color.WHITE);
		Vertex v3 = new Vertex(c, null);
		Vertex v4 = new Vertex(c, null);
		
		shouldNotEqual(v1, v2);
		shouldNotEqual(v2, v1);
		shouldNotEqual(v2, v3);
		shouldNotEqual(v3, v2);
		shouldNotEqual(v3, v1);
		shouldNotEqual(v1, v3);
		
		shouldEqual(v3, v4);
	}
	
	@Test
	public void shouldEqualSameCoordsAndColor() {
		Coords3d c = RandomUtils.getRandomCoords();
		Vertex v1 = new Vertex(c, Color.BLACK);
		Vertex v2 = new Vertex(c, Color.BLACK);
		
		shouldEqual(v1, v2);
	}
	
	@Test
	public void testToString() {
		Coords3d c = new Coords3d(55, 67.2, Math.PI);
		Vertex v = new Vertex(c, new Color(211, 32, 44));
		
		assertEquals("55 67.2 3.1416 211 32 44", v.toString());
	}

	private static void shouldEqual(Vertex v1, Vertex v2) {
		assertTrue(v1.equals(v2));
		assertEquals(v1.hashCode(), v2.hashCode());
	}
	
	private static void shouldNotEqual(Vertex v1, Vertex v2) {
		assertFalse(v1.equals(v2));
		assertNotEquals(v1.hashCode(), v2.hashCode());
	}
}
