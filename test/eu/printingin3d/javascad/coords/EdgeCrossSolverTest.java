package eu.printingin3d.javascad.coords;

import org.junit.Assert;
import org.junit.Test;

public class EdgeCrossSolverTest {
	@Test
	public void testTrivialCrossing() {
		Coords3d c1 = new Coords3d(+1,0,0);
		Coords3d c2 = new Coords3d(-1.25,0,0);
		
		Coords3d d1 = new Coords3d(0,+1,0);
		Coords3d d2 = new Coords3d(0,-1,0);
		
		Coords3d intersection = EdgeCrossSolver.findIntersection(c1, c2, d1, d2);
		
		Assert.assertNotNull(intersection);
	}
	
	@Test
	public void testTrivialNonCrossing() {
		Coords3d c1 = new Coords3d(+1,0,-1);
		Coords3d c2 = new Coords3d(-1.25,0,1);
		
		Coords3d d1 = new Coords3d(0,+1,0);
		Coords3d d2 = new Coords3d(0,-1,0);
		
		Coords3d intersection = EdgeCrossSolver.findIntersection(c1, c2, d1, d2);
		
		Assert.assertNull(intersection);
	}
	
	@Test
	public void testRealCase() {
		Coords3d c1 = new Coords3d(-0.7092,1.87,  9.9);
		Coords3d c2 = new Coords3d(-0.7092,1.87, 10.1);
//		Coords3d c1 = new Coords3d(-0.7092,1.87,-10.5);
//		Coords3d c2 = new Coords3d(-0.7092,1.87, 10.5);
		
		Coords3d d1 = new Coords3d(-1.0554,1.828,10);
		Coords3d d2 = new Coords3d(-0.3377,1.9151,10);
		
		Coords3d intersection = EdgeCrossSolver.findIntersection(c1, c2, d1, d2);
		
		Assert.assertNotNull(intersection);
		
		Assert.assertEquals(new Coords3d(-0.7092,1.87,10), intersection);
	}
}
