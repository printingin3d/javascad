package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Triangle3d;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.testutils.AssertEx;

public class PolyhedronTest {
	
	@Test(expected = IllegalValueException.class)
	public void shouldThrowExceptioIfParameterIsNull() {
		new Polyhedron(null);
	}

	@Test(expected = IllegalValueException.class)
	public void shouldThrowExceptioIfParameterIsEmptyList() {
		new Polyhedron(Collections.<Triangle3d>emptyList());
	}
	
	@Test
	public void boundariesShouldBeDefinedByTheLowestAndHighestPoints() {
		Coords3d c1 = new Coords3d(0.0, 10.0, 20.0);
		Coords3d c2 = new Coords3d(15.5, 7.0, 30.0);
		Coords3d c3 = new Coords3d(20.0, 3.14, 40.0);
		Coords3d c4 = new Coords3d(25.0, 5, 60.0);
		
		Triangle3d t1 = new Triangle3d(c1, c2, c3);
		Triangle3d t2 = new Triangle3d(c2, c3, c4);
		
		Polyhedron polyhedron = new Polyhedron(Arrays.asList(t1, t2));
		
		Boundaries3d boundaries = polyhedron.getBoundaries();
		
		assertDoubleEquals(0.0, boundaries.getX().getMin());
		assertDoubleEquals(25.0, boundaries.getX().getMax());
		
		assertDoubleEquals(3.14, boundaries.getY().getMin());
		assertDoubleEquals(10.0, boundaries.getY().getMax());
		
		assertDoubleEquals(20.0, boundaries.getZ().getMin());
		assertDoubleEquals(60.0, boundaries.getZ().getMax());
	}
	
	private static class PositionAndIndex implements Comparable<PositionAndIndex> {
		private final int position;
		private final int index;
		public PositionAndIndex(int position, int index) {
			assertTrue(position>=0);
			
			this.position = position;
			this.index = index;
		}
		public int getIndex() {
			return index;
		}
		@Override
		public int compareTo(PositionAndIndex arg0) {
			return Integer.compare(position, arg0.position);
		}
	}
	
	@Test
	public void testScad() {
		Language.OpenSCAD.setCurrent();
		
		Coords3d c0 = new Coords3d(10,10,0);
		Coords3d c1 = new Coords3d(10,-10,0);
		Coords3d c2 = new Coords3d(-10,-10,0);
		Coords3d c3 = new Coords3d(-10,10,0);
		Coords3d c4 = new Coords3d(0,0,10);

		List<Triangle3d> triangles=new ArrayList<Triangle3d>();
		triangles.add(new Triangle3d(c0,c1,c4));
		triangles.add(new Triangle3d(c1,c2,c4));
		triangles.add(new Triangle3d(c2,c3,c4));
		triangles.add(new Triangle3d(c3,c0,c4));
		triangles.add(new Triangle3d(c1,c0,c3));
		triangles.add(new Triangle3d(c2,c1,c3));
		Polyhedron p=new Polyhedron(triangles);
		
		String scad = p.toScad();
		
		List<PositionAndIndex> posList = new ArrayList<>();
		posList.add(new PositionAndIndex(scad.indexOf("[10,10,0]"), 0));
		posList.add(new PositionAndIndex(scad.indexOf("[10,-10,0]"), 1));
		posList.add(new PositionAndIndex(scad.indexOf("[-10,-10,0]"), 2));
		posList.add(new PositionAndIndex(scad.indexOf("[-10,10,0]"), 3));
		posList.add(new PositionAndIndex(scad.indexOf("[0,0,10]"), 4));
		
		Collections.sort(posList);
		
		assertTrue(scad.contains(replacePositions("[[%0,%1,%4], [%1,%2,%4], [%2,%3,%4], [%3,%0,%4], [%1,%0,%3], [%2,%1,%3]]", posList)));
	}
	
	@Test
	public void testPovRay() {
		Language.POVRay.setCurrent();

		Coords3d c0 = new Coords3d(10,10,0);
		Coords3d c1 = new Coords3d(10,-10,0);
		Coords3d c2 = new Coords3d(-10,-10,0);
		Coords3d c3 = new Coords3d(-10,10,0);
		Coords3d c4 = new Coords3d(0,0,10);

		List<Triangle3d> triangles=new ArrayList<Triangle3d>();
		triangles.add(new Triangle3d(c0,c1,c4));
		triangles.add(new Triangle3d(c1,c2,c4));
		triangles.add(new Triangle3d(c2,c3,c4));
		triangles.add(new Triangle3d(c3,c0,c4));
		triangles.add(new Triangle3d(c1,c0,c3));
		triangles.add(new Triangle3d(c2,c1,c3));
		Polyhedron p=new Polyhedron(triangles);

		String pov = p.innerToScad();
		
		AssertEx.assertEqualsWithoutWhiteSpaces("mesh { " +
				"triangle {< 10, 10,0>, < 10,-10,0>, <0,0,10>} " +
				"triangle {< 10,-10,0>, <-10,-10,0>, <0,0,10>} " +
				"triangle {<-10,-10,0>, <-10, 10,0>, <0,0,10>} " +
				"triangle {<-10, 10,0>, < 10, 10,0>, <0,0,10>} " +
				"triangle {< 10,-10,0>, < 10, 10,0>, <-10, 10,0>} " +
				"triangle {<-10,-10,0>, < 10,-10,0>, <-10, 10,0>} " +
				"#attributes}", pov);
	}
	
	private static String replacePositions(String origPos, List<PositionAndIndex> posList) {
		String result = origPos;
		int i = 0;
		for (PositionAndIndex pos : posList) {
			result = result.replace("%"+i, String.valueOf(pos.getIndex()));
			i++;
		}
		return result;
	}
}
