package eu.printingin3d.javascad.vrl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.tranform.TransformationFactory;
import eu.printingin3d.javascad.tranzitions.Direction;

public class PolygonTest {
	private static final Coords3d POINT_1 = Coords3d.ZERO;
	private static final Coords3d POINT_2 = new Coords3d(100,   0, 0);
	private static final Coords3d POINT_3 = new Coords3d(100, 100, 0);
	private static final List<Coords3d> POINTS = Arrays.asList(POINT_1, POINT_2, POINT_3);
	
	@Test
	public void testToFacets() {
		Color color = RandomUtils.getRandomColor();
		Polygon p = Polygon.fromPolygons(POINTS, color);
		
		List<Facet> facetList = p.toFacets();
		
		assertEquals(1, facetList.size());
		
		Facet f = facetList.get(0);
		
		assertEquals(Arrays.asList(
				new Vertex(POINT_1, color), 
				new Vertex(POINT_2, color), 
				new Vertex(POINT_3, color)), f.getVertexes());
		
		assertEquals(new Coords3d(0, 0, 1), f.getNormal());
	}
	
	@Test
	public void testFlip() {
		Polygon p = Polygon.fromPolygons(POINTS, Color.BLACK);
		
		List<Facet> facetList = p.flip().toFacets();
		
		assertEquals(1, facetList.size());
		
		Facet f = facetList.get(0);
		
		assertEquals(new Coords3d(0, 0, -1), f.getNormal());
	}
	
	@Test
	public void testTranslate() {
		Color color = RandomUtils.getRandomColor();
		Polygon p = Polygon.fromPolygons(POINTS, color);
		
		Coords3d delta = new Coords3d(10, 20, 30);
		List<Facet> facetList = p.transformed(TransformationFactory.getTranlationMatrix(delta)).toFacets();
		
		assertEquals(1, facetList.size());
		
		Facet f = facetList.get(0);
		
		assertEquals(Arrays.asList(
				new Vertex(POINT_1.add(delta), color), 
				new Vertex(POINT_2.add(delta), color), 
				new Vertex(POINT_3.add(delta), color)), f.getVertexes());
		
		assertEquals(new Coords3d(0, 0, 1), f.getNormal());
	}
	
	@Test
	public void testMirrorTransform() {
		Color color = RandomUtils.getRandomColor();
		Polygon p = Polygon.fromPolygons(POINTS, color);
		
		List<Facet> facetList = p.transformed(TransformationFactory.getMirrorMatrix(Direction.Z)).toFacets();
		
		assertEquals(1, facetList.size());
		
		Facet f = facetList.get(0);
		
		assertEquals(new Coords3d(0, 0, -1), f.getNormal());
	}
	
	@Test
	public void splitFrontPolygon() {
		Polygon p = Polygon.fromPolygons(POINTS, Color.BLACK);
		Polygon p1 = Polygon.fromPolygons(Arrays.asList(
				new Coords3d(0, 0, 10), 
				new Coords3d(0, 100, 10), 
				new Coords3d(100, 100, 10)), Color.BLACK);

		List<Polygon> coplanarFront = Collections.emptyList();
		List<Polygon> coplanarBack = Collections.emptyList();
		List<Polygon> front = new ArrayList<>();
		List<Polygon> back = Collections.emptyList();
		p.splitPolygon(p1, coplanarFront, coplanarBack, front, back);
		
		// the list returned by Collections.emptyList() throws exception when
		// an add operation has been call, so there's no need for asserting that
		
		assertEquals(Collections.singletonList(p1), front);
	}
	
	@Test
	public void splitBackPolygon() {
		Polygon p = Polygon.fromPolygons(POINTS, Color.BLACK);
		Polygon p1 = Polygon.fromPolygons(Arrays.asList(
				new Coords3d(0, 0, -10), 
				new Coords3d(0, 100, -5), 
				new Coords3d(100, 100, -1)), Color.BLACK);
		
		List<Polygon> coplanarFront = Collections.emptyList();
		List<Polygon> coplanarBack = Collections.emptyList();
		List<Polygon> front = Collections.emptyList();
		List<Polygon> back = new ArrayList<>();
		p.splitPolygon(p1, coplanarFront, coplanarBack, front, back);
		
		// the list returned by Collections.emptyList() throws exception when
		// an add operation has been call, so there's no need for asserting that
		
		assertEquals(Collections.singletonList(p1), back);
	}
	
	@Test
	public void splitCoplanarFrontPolygon() {
		Polygon p = Polygon.fromPolygons(POINTS, Color.BLACK);
		Polygon p1 = Polygon.fromPolygons(Arrays.asList(
				new Coords3d(0, 0, 0), 
				new Coords3d(100, 100, 0), 
				new Coords3d(0, 100, 0)), Color.BLACK);
		
		List<Polygon> coplanarFront = new ArrayList<>();
		List<Polygon> coplanarBack = Collections.emptyList();
		List<Polygon> front = Collections.emptyList();
		List<Polygon> back = Collections.emptyList();
		p.splitPolygon(p1, coplanarFront, coplanarBack, front, back);
		
		// the list returned by Collections.emptyList() throws exception when
		// an add operation has been call, so there's no need for asserting that
		
		assertEquals(Collections.singletonList(p1), coplanarFront);
	}
	
	@Test
	public void splitCoplanarBackPolygon() {
		Polygon p = Polygon.fromPolygons(POINTS, Color.BLACK);
		Polygon p1 = Polygon.fromPolygons(Arrays.asList(
				new Coords3d(0, 0, 0), 
				new Coords3d(0, 100, 0), 
				new Coords3d(100, 100, 0)), Color.BLACK);
		
		List<Polygon> coplanarFront = Collections.emptyList();
		List<Polygon> coplanarBack = new ArrayList<>();
		List<Polygon> front = Collections.emptyList();
		List<Polygon> back = Collections.emptyList();
		p.splitPolygon(p1, coplanarFront, coplanarBack, front, back);
		
		// the list returned by Collections.emptyList() throws exception when
		// an add operation has been call, so there's no need for asserting that

		assertEquals(Collections.singletonList(p1), coplanarBack);
	}
	
	@Test
	public void splitSpinningPolygon() {
		Polygon p = Polygon.fromPolygons(POINTS, Color.BLACK);
		Polygon p1 = Polygon.fromPolygons(Arrays.asList(
				new Coords3d(0,   0,  10), 
				new Coords3d(0,   0, -10), 
				new Coords3d(0, 100, -10)), Color.BLACK);
		
		List<Polygon> coplanarFront = Collections.emptyList();
		List<Polygon> coplanarBack = Collections.emptyList();
		List<Polygon> front = new ArrayList<>();
		List<Polygon> back = new ArrayList<>();
		p.splitPolygon(p1, coplanarFront, coplanarBack, front, back);
		
		// the list returned by Collections.emptyList() throws exception when
		// an add operation has been call, so there's no need for asserting that

		assertFalse(front.isEmpty());
		assertFalse(back.isEmpty());
	}
	
	@Test
	public void splitSpinningPolygonWithCoplanarVertex() {
		Polygon p = Polygon.fromPolygons(POINTS, Color.BLACK);
		Polygon p1 = Polygon.fromPolygons(Arrays.asList(
				new Coords3d(0,   0,  10),
				new Coords3d(0,   0,   0), 
				new Coords3d(0, 100, -10)), Color.BLACK);
		
		List<Polygon> coplanarFront = Collections.emptyList();
		List<Polygon> coplanarBack = Collections.emptyList();
		List<Polygon> front = new ArrayList<>();
		List<Polygon> back = new ArrayList<>();
		p.splitPolygon(p1, coplanarFront, coplanarBack, front, back);
		
		// the list returned by Collections.emptyList() throws exception when
		// an add operation has been call, so there's no need for asserting that
		
		assertFalse(front.isEmpty());
		assertFalse(back.isEmpty());
	}

	@Test
	public void testStream() {
		Coords3d c0 = new Coords3d(1, 2, 3);
		Coords3d c1 = new Coords3d(4, 5, 6);
		Coords3d c2 = new Coords3d(7, 8, 9);

		Polygon p = Polygon.fromPolygons(Arrays.asList(c0, c1, c2), Color.BLACK);

		List<Coords3d> l = p.stream().collect(Collectors.toList());

		assertEquals("c0", c0, l.get(0));
		assertEquals("c1", c1, l.get(1));
		assertEquals("c2", c2, l.get(2));
	}
}
