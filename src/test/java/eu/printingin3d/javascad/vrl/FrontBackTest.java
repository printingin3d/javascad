package eu.printingin3d.javascad.vrl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Coords3d;

public class FrontBackTest {
	private FrontBack<Coords3d> testSubject;
	
	@Before
	public void init() {
		testSubject = new FrontBack<>();
	}
	
	@Test
	public void initiallyShouldBeEmpty() {
		assertTrue(testSubject.getFront().isEmpty());
		assertTrue(testSubject.getBack().isEmpty());
	}
	
	@Test
	public void testAddToFront() {
		testSubject.addToFront(Coords3d.ZERO);
		assertEquals(Arrays.asList(Coords3d.ZERO), testSubject.getFront());
		assertTrue(testSubject.getBack().isEmpty());
	}
	
	@Test
	public void testAddToBack() {
		testSubject.addToBack(Coords3d.ZERO);
		assertTrue(testSubject.getFront().isEmpty());
		assertEquals(Arrays.asList(Coords3d.ZERO), testSubject.getBack());
	}
	
	@Test
	public void testAddToBoth() {
		testSubject.addToBoth(Coords3d.ZERO);
		assertEquals(Arrays.asList(Coords3d.ZERO), testSubject.getFront());
		assertEquals(Arrays.asList(Coords3d.ZERO), testSubject.getBack());
	}
	
	@Test
	public void complexTest() {
		testSubject.addToFront(new Coords3d(10, 20, 30));
		testSubject.addToBoth(Coords3d.ZERO);
		testSubject.addToBack(new Coords3d(30, 20, 10));
		
		assertEquals(Arrays.asList(new Coords3d(10, 20, 30), Coords3d.ZERO), testSubject.getFront());
		assertEquals(Arrays.asList(Coords3d.ZERO, new Coords3d(30, 20, 10)), testSubject.getBack());
	}
	
	@Test
	public void testAddTo() {
		testSubject.addToFront(new Coords3d(10, 20, 30));
		testSubject.addToBoth(Coords3d.ZERO);
		testSubject.addToFront(new Coords3d(30, 30, 30));
		testSubject.addToBack(new Coords3d(30, 20, 10));
		testSubject.addToBack(new Coords3d(30, 30, 30));
		
		FrontBack<Polygon> dest = new FrontBack<>();
		
		testSubject.addTo(list -> Polygon.fromPolygons(list, Color.BLACK), dest);
		
		assertEquals(1, dest.getFront().size());
		assertEquals(1, dest.getBack().size());
		
		assertEquals(Arrays.asList(new Coords3d(10, 20, 30), Coords3d.ZERO, new Coords3d(30, 30, 30)), 
				dest.getFront().get(0).getVertices());
		assertEquals(Arrays.asList(Coords3d.ZERO, new Coords3d(30, 20, 10), new Coords3d(30, 30, 30)), 
				dest.getBack().get(0).getVertices());
	}
}
