package eu.printingin3d.javascad.vrl;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Triangle3d;
import eu.printingin3d.javascad.testutils.RandomUtils;

public class FacetTest {

	@Test
	public void testVertices() {
		Triangle3d triangle = RandomUtils.getRandomTriangle();
		Coords3d normal = RandomUtils.getRandomCoords();
		Color color = RandomUtils.getRandomColor();
		Facet facet = new Facet(triangle, normal, color);
		
		assertEquals(normal, facet.getNormal());
		
		Set<Vertex> expectedVertices = new HashSet<>();
		for (Coords3d c : triangle.getPoints()) {
			expectedVertices.add(new Vertex(c, color));
		}
		
		assertEquals(expectedVertices, new HashSet<Vertex>(facet.getVertexes())); 
	}

}
