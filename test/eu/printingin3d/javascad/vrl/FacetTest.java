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
	public void testVertexes() {
		Triangle3d triangle = RandomUtils.getRandomTriangle();
		Coords3d normal = RandomUtils.getRandomCoords();
		Color color = RandomUtils.getRandomColor();
		Facet facet = new Facet(triangle, normal, color);
		
		assertEquals(normal, facet.getNormal());
		
		Set<Coords3d> facetCoords = new HashSet<>();
		for (Vertex v : facet.getVertexes()) {
			assertEquals(color, v.getColor());
			facetCoords.add(v.getCoords());
		}
		
		assertEquals(new HashSet<Coords3d>(triangle.getPoints()), facetCoords); 
		
	}
}
