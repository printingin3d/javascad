package eu.printingin3d.javascad.batchtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Dims3d;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.BoundedModel;
import eu.printingin3d.javascad.models.Cube;
import eu.printingin3d.javascad.models.Cylinder;
import eu.printingin3d.javascad.models.Polyhedron;
import eu.printingin3d.javascad.models.Prism;
import eu.printingin3d.javascad.models.Sphere;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.tranzitions.Colorize;
import eu.printingin3d.javascad.tranzitions.Difference;
import eu.printingin3d.javascad.tranzitions.Direction;
import eu.printingin3d.javascad.tranzitions.Intersection;
import eu.printingin3d.javascad.tranzitions.Mirror;
import eu.printingin3d.javascad.tranzitions.Rotate;
import eu.printingin3d.javascad.tranzitions.Scale;
import eu.printingin3d.javascad.tranzitions.Slicer;
import eu.printingin3d.javascad.tranzitions.Translate;
import eu.printingin3d.javascad.tranzitions.Union;

@RunWith(Parameterized.class)
public class CloneModelTest {
	
	public static class TestCase {
		private final Abstract3dModel model;
		private final boolean expectSimpleResult;
		private TestCase(Abstract3dModel model, boolean expectSimpleResult) {
			this.model = model;
			this.expectSimpleResult = expectSimpleResult;
		}
		public Abstract3dModel getModel() {
			return model;
		}
		public boolean isExpectSimpleResult() {
			return expectSimpleResult;
		}
	}
	
	private final Abstract3dModel original;
	
	public CloneModelTest(Abstract3dModel original) {
		this.original = original;
	}

	public static Collection<TestCase> createTestSubjects() {
		Abstract3dModel cube = new Cube(new Dims3d(67.0, 32.3, 98.3));
		Abstract3dModel cylinder = new Cylinder(32.2, 9.3, 11.1);
		return Arrays.<TestCase>asList(
				new TestCase(cube.cloneModel(), false),
				new TestCase(cylinder.cloneModel(), false),
				new TestCase(new Prism(78.2, 10.1, 32.2, 5), false),
				new TestCase(new Prism(78.2, 10.1, 5), false),
				new TestCase(new Intersection(cube.cloneModel()), true),
				new TestCase(new Intersection(cube.cloneModel(), cylinder.cloneModel()), false),
				new TestCase(new Difference(cube.cloneModel(), cylinder.cloneModel()), false),
				new TestCase(new Difference(cube.cloneModel()), true),
				new TestCase(Mirror.mirrorX(cube.cloneModel()), false),
				new TestCase(new Rotate(cylinder, new Angles3d(-21, 32.1, 331.4)), false),
				new TestCase(new Scale(cylinder, new Coords3d(4.10, 1.0, 3.21)), false),
				new TestCase(new Slicer(cube, Direction.X, 3, 0), true),
				new TestCase(new Colorize(Color.black, cube), true),
				new TestCase(new Sphere(3.1), false),
				new TestCase(new Translate(cube, new Coords3d(-23, 33.2, 7.3)), false),
				new TestCase(new BoundedModel(cube, RandomUtils.getRandomBoundaries()), true),
				new TestCase(new Union(cube, cylinder), false),
				new TestCase(cube.cloneModel().background(), false),
				new TestCase(cube.cloneModel().debug(), false),
				new TestCase(new Polyhedron(Arrays.asList(RandomUtils.getRandomTriangle(), RandomUtils.getRandomTriangle())), false)
			);
	}
	
	@Parameterized.Parameters(name="{0}")
	public static Collection<Object[]> testCases() {
		List<Object[]> result = new ArrayList<Object[]>();
		for (TestCase testCase : createTestSubjects()) {
			result.add(new Object[] {testCase.getModel()});
		}
		return result;
	}
	
	@Test
	public void shouldBeTheSameClass() {
		assertEquals(original.getClass(), original.cloneModel().getClass());
	}
	
	@Test
	public void shouldRepresentsTheSameOpenScadObject() {
		assertEquals(original.toScad(), original.cloneModel().toScad());
	}
	
	@Test
	public void shouldBeIndependent() {
		Abstract3dModel clone = original.cloneModel();
		original.move(RandomUtils.getRandomCoords());
		assertFalse(clone.toScad().equals(original.toScad()));
	}
	
	@Test
	public void boundariesShouldBeIndependent() {
		Boundaries3d originalBoundaries = original.getBoundaries();
		Abstract3dModel clone = original.cloneModel();
		clone.move(RandomUtils.getRandomCoords());
		Boundaries3dTest.assertBoundariesEquals(originalBoundaries, original.getBoundaries());
	}
}
