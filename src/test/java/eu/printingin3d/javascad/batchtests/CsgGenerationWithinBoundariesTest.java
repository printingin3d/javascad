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
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Cube;
import eu.printingin3d.javascad.models.Cylinder;
import eu.printingin3d.javascad.models.LinearExtrude;
import eu.printingin3d.javascad.models.Polyhedron;
import eu.printingin3d.javascad.models.Sphere;
import eu.printingin3d.javascad.models.Support;
import eu.printingin3d.javascad.models2d.Circle;
import eu.printingin3d.javascad.models2d.Square;
import eu.printingin3d.javascad.tranzitions.Intersection;
import eu.printingin3d.javascad.tranzitions2d.Union;
import eu.printingin3d.javascad.vrl.Facet;
import eu.printingin3d.javascad.vrl.VertexPosition;

@RunWith(Parameterized.class)
public class CsgGenerationWithinBoundariesTest {
	private final Abstract3dModel testSubject;
	private final boolean checkFacets;
	
	public CsgGenerationWithinBoundariesTest(Abstract3dModel testSubject, boolean checkFacets) {
		this.testSubject = testSubject;
		this.checkFacets = checkFacets;
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
					new Cylinder(10, Radius.fromRadius(12), Radius.fromRadius(20))
							.addModelTo(Side.TOP_OUT, new Cylinder(10, Radius.fromRadius(20), Radius.fromRadius(12))),
					new Intersection(
							new Sphere(Radius.fromRadius(1)).move(Coords3d.xOnly(-0.2)),
							new Sphere(Radius.fromRadius(1)).move(Coords3d.xOnly(+0.2))
						),
					new Support(new Dims3d(50, 23, 3), 0.1),
					new Support(new Dims3d(12, 33, 3), 0.1).move(new Coords3d(34, 3, 10)),
					new Polyhedron(triangles),
					new LinearExtrude(new Circle(Radius.fromRadius(5)), 5, Angle.ZERO),
					new LinearExtrude(new Union(Arrays.asList(
							new Circle(Radius.fromRadius(3)), new Square(new Dims2d(3, 5)))), 10, Angle.ZERO)
				));
		return result;
	}
	
	@Test
	public void allPointsShouldBeWithinBoundaries() {
		Boundaries3d boundaries = testSubject.getBoundaries();
		for (Coords3d c : testSubject.toCSG().getPoints()) {
			Assert.assertTrue(boundaries.getX().isInside(c.getX()));
			Assert.assertTrue(boundaries.getY().isInside(c.getY()));
			Assert.assertTrue(boundaries.getZ().isInside(c.getZ()));
		}
	}
	
	@Test
	public void allPolygonShouldFaceFromOrigin() {
		if (checkFacets) {
			Boundaries3d boundaries = testSubject.getBoundaries();

			Coords3d mid = new Coords3d(boundaries.getX().getMiddle(), boundaries.getY().getMiddle(), boundaries.getZ().getMiddle());

			for (Facet f : testSubject.toCSG().toFacets()) {
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
