package eu.printingin3d.javascad.batchtests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import eu.printingin3d.javascad.basic.Angle;
import eu.printingin3d.javascad.basic.Radius;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Dims3d;
import eu.printingin3d.javascad.coords.Triangle3d;
import eu.printingin3d.javascad.coords2d.Dims2d;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Cube;
import eu.printingin3d.javascad.models.Cylinder;
import eu.printingin3d.javascad.models.LinearExtrude;
import eu.printingin3d.javascad.models.Polyhedron;
import eu.printingin3d.javascad.models.Sphere;
import eu.printingin3d.javascad.models.Support;
import eu.printingin3d.javascad.models2d.Circle;
import eu.printingin3d.javascad.models2d.Square;
import eu.printingin3d.javascad.tranzitions2d.Union;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.Facet;
import eu.printingin3d.javascad.vrl.VertexPosition;

@RunWith(Parameterized.class)
public class CsgGenerationWithinBoundariesTest {
	private final Boundaries3d testSubjectBoundaries3d;
	private final boolean checkFacets;
	private final CSG testSubjectCSG;
	
	public CsgGenerationWithinBoundariesTest(Abstract3dModel testSubject, boolean checkFacets) {
		this.testSubjectBoundaries3d = testSubject.getBoundaries();
		this.checkFacets = checkFacets;
		this.testSubjectCSG = testSubject.toCSG();
	}

	@Parameterized.Parameters(name="{0}")
	public static Collection<Object[]> testCases() {
		List<Object[]> result = new ArrayList<Object[]>();
		for (Abstract3dModel testCase : createTestSubjects()) {
			result.add(new Object[] {testCase, Boolean.valueOf(!(testCase instanceof Support))});
		}
		return result;
	}
	
	private static Collection<Abstract3dModel> createTestSubjects() {
		Coords3d c0 = new Coords3d(10,10,0);
		Coords3d c1 = new Coords3d(10,-10,0);
		Coords3d c2 = new Coords3d(-10,-10,0);
		Coords3d c3 = new Coords3d(-10,10,0);
		Coords3d c4 = new Coords3d(0,0,10);

		List<Triangle3d> triangles=new ArrayList<Triangle3d>();
		triangles.add(new Triangle3d(c0,c4,c1));
		triangles.add(new Triangle3d(c1,c4,c2));
		triangles.add(new Triangle3d(c2,c4,c3));
		triangles.add(new Triangle3d(c3,c4,c0));
		triangles.add(new Triangle3d(c1,c3,c0));
		triangles.add(new Triangle3d(c2,c3,c1));
		
		List<Abstract3dModel> result = new ArrayList<>(Arrays.<Abstract3dModel>asList(
					new Cube(new Dims3d(10, 20, Math.PI)),
					new Cylinder(12, Radius.fromRadius(54)),
					new Cylinder(3, Radius.fromRadius(500)).move(new Coords3d(10, 20, 50)),
					new Sphere(Radius.fromRadius(12)),
					new Support(new Dims3d(50, 23, 3), 0.1),
					new Support(new Dims3d(12, 33, 3), 0.1).move(new Coords3d(34, 3, 10)),
					new Polyhedron(triangles),
					new Cube(10).addModel(new Cube(15)),
					new LinearExtrude(new Circle(Radius.fromRadius(5)), 5, Angle.ZERO),
					new LinearExtrude(new Union(Arrays.asList(
							new Circle(Radius.fromRadius(3)), new Square(new Dims2d(3, 5)))), 10, Angle.ZERO)
				));
		return result;
	}
	
	@Test
	public void allPointsShouldBeWithinBoundaries() {
		for (Coords3d c : testSubjectCSG.getPoints()) {
			Assert.assertTrue(testSubjectBoundaries3d.getX().isInside(c.getX()));
			Assert.assertTrue(testSubjectBoundaries3d.getY().isInside(c.getY()));
			Assert.assertTrue(testSubjectBoundaries3d.getZ().isInside(c.getZ()));
		}
	}
	
	@Test
	public void allPolygonShouldFaceFromOrigin() {
		if (checkFacets) {
			Coords3d mid = new Coords3d(
					testSubjectBoundaries3d.getX().getMiddle(), 
					testSubjectBoundaries3d.getY().getMiddle(), 
					testSubjectBoundaries3d.getZ().getMiddle());

			for (Facet f : testSubjectCSG.toFacets()) {
				Coords3d a = f.getTriangle().getPoints().get(0);
				Coords3d n = f.getNormal();

				double dist = n.dot(a);

				double t = n.dot(mid) - dist;
				VertexPosition vp = VertexPosition.fromSquareDistance(t);

				Assert.assertEquals(VertexPosition.BACK, vp);
			}
		}
	}
}
