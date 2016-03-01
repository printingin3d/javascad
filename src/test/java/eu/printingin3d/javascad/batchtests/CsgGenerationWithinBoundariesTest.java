package eu.printingin3d.javascad.batchtests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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

@RunWith(Parameterized.class)
public class CsgGenerationWithinBoundariesTest {
	private final Abstract3dModel testSubject;
	
	public CsgGenerationWithinBoundariesTest(Abstract3dModel testSubject) {
		this.testSubject = testSubject;
	}

	@Parameterized.Parameters(name="{0}")
	public static Collection<Object[]> testCases() {
		List<Object[]> result = new ArrayList<Object[]>();
		for (Abstract3dModel testCase : createTestSubjects()) {
			result.add(new Object[] {testCase});
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
		triangles.add(new Triangle3d(c0,c1,c4));
		triangles.add(new Triangle3d(c1,c2,c4));
		triangles.add(new Triangle3d(c2,c3,c4));
		triangles.add(new Triangle3d(c3,c0,c4));
		triangles.add(new Triangle3d(c1,c0,c3));
		triangles.add(new Triangle3d(c2,c1,c3));
		
		List<Abstract3dModel> result = new ArrayList<>(Arrays.<Abstract3dModel>asList(
					new Cube(new Dims3d(10, 20, Math.PI)),
					new Cylinder(12, 54),
					new Cylinder(3, 500).move(new Coords3d(10, 20, 50)),
					new Sphere(12),
					new Support(new Dims3d(50, 23, 3), 0.1),
					new Support(new Dims3d(12, 33, 3), 0.1).move(new Coords3d(34, 3, 10)),
					new Polyhedron(triangles),
					new LinearExtrude(new Circle(5), 5, 0),
					new LinearExtrude(new Union(Arrays.asList(new Circle(3), new Square(new Dims2d(3, 5)))), 10, 0)
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
}
