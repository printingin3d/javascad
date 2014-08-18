package eu.printingin3d.javascad.vrl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.testutils.RandomUtils;

public class VertexMapTest {
	
	@Test
	public void multipleValuesShouldBeInTheResultOnce() {
		Coords3d c = RandomUtils.getRandomCoords();
		Vertex v = new Vertex(c, Color.BLACK);
		
		VertexMap vertexMap = new VertexMap(Arrays.asList(v, v));
		
		assertEquals(Arrays.asList(new Vertex(c, Color.BLACK)), vertexMap.getVertexList());
	}
	
	@Test
	public void findVertexShouldFindTheCorrectIndex() {
		Vertex v = RandomUtils.getRandomVertex();
		
		List<Vertex> list = new ArrayList<>();
		list.add(v);
		
		for (int i=0;i<20;i++) {
			list.add(RandomUtils.getRandomVertex());
		}
		
		VertexMap vertexMap = new VertexMap(list);
		
		int i = vertexMap.findIndex(v);
		
		assertEquals(v, vertexMap.getVertexList().get(i));
	}
	
	@Test
	public void emptyInputShouldBeAccepted() {
		VertexMap vertexMap = new VertexMap(Collections.<Vertex>emptyList());
		
		assertTrue(vertexMap.getVertexList().isEmpty());
	}
}
