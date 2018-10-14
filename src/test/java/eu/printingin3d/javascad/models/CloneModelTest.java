package eu.printingin3d.javascad.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import eu.printingin3d.javascad.basic.Angle;
import eu.printingin3d.javascad.basic.Radius;
import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Dims3d;
import eu.printingin3d.javascad.coords2d.Dims2d;
import eu.printingin3d.javascad.models2d.Circle;
import eu.printingin3d.javascad.models2d.Square;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.tranzitions.Colorize;
import eu.printingin3d.javascad.tranzitions.Difference;
import eu.printingin3d.javascad.tranzitions.Direction;
import eu.printingin3d.javascad.tranzitions.Hull;
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
		private final String name;
		private TestCase(Abstract3dModel model, String name) {
			this.model = model;
			this.name = name;
		}
		private TestCase(Abstract3dModel model) {
			this(model, model.getClass().getSimpleName());
		}
		public Abstract3dModel getModel() {
			return model;
		}
		@Override
		public String toString() {
			return name;
		}
	}
	
	private final Abstract3dModel original;
	
	public CloneModelTest(TestCase original) {
		this.original = original.getModel();
	}

	public static Collection<TestCase> createTestSubjects() {
		Abstract3dModel cube = new Cube(new Dims3d(67.0, 32.3, 98.3));
		Abstract3dModel cylinder = new Cylinder(32.2, Radius.fromRadius(9.3), Radius.fromRadius(11.1));
		return Arrays.<TestCase>asList(
				new TestCase(cube.cloneModel()),
				new TestCase(cylinder.cloneModel()),
				new TestCase(new Prism(78.2, Radius.fromRadius(10.1), Radius.fromRadius(32.2), 5), "PrismTwoRadiuses"),
				new TestCase(new Prism(78.2, Radius.fromRadius(10.1), 5), "PrismOneRadius"),
				new TestCase(new Intersection(), "IntersectionEmpty"),
				new TestCase(new Intersection(cube.cloneModel()), "IntersectionOneModel"),
				new TestCase(new Intersection(cube.cloneModel(), cylinder.cloneModel()), "IntersectionTwoModels"),
				new TestCase(new Difference(cube.cloneModel(), cylinder.cloneModel()), "DifferenceTwoModels"),
				new TestCase(new Difference(cube.cloneModel()), "DifferenceOneModel"),
				new TestCase(new Difference(cube.cloneModel()).move(new Coords3d(10, 20, 30)), "DifferenceMoved"),
				new TestCase(Mirror.mirrorX(cube.cloneModel())),
				new TestCase(new Rotate(cylinder, new Angles3d(-21, 32.1, 331.4))),
				new TestCase(new Scale(cylinder, new Coords3d(4.10, 1.0, 3.21))),
				new TestCase(new Slicer(cube, Direction.X, 3, 0)),
				new TestCase(new Colorize(Color.BLACK, cube)),
				new TestCase(new Sphere(Radius.fromRadius(3.1))),
				new TestCase(new Translate(cube, new Coords3d(-23, 33.2, 7.3))),
				new TestCase(new BoundedModel(cube, RandomUtils.getRandomBoundaries())),
				new TestCase(new Union()),
				new TestCase(new Hull()),
				new TestCase(new LinearExtrude(new Circle(Radius.fromRadius(10)), 30, Angle.ofDegree(55))),
				new TestCase(new LinearExtrude(new Circle(Radius.fromRadius(10)), 30, Angle.ofDegree(55), 2.5), "LinearExtrudeScaled"),
				new TestCase(new Union(cube, cylinder).rotate(new Angles3d(40, 42, -55))),
				new TestCase(new Hull(cube, cylinder).rotate(new Angles3d(40, 42, -55))),
				new TestCase(cube.cloneModel().background(), "Background"),
				new TestCase(cube.cloneModel().debug(), "Debug"),
				new TestCase(new Polyhedron(Arrays.asList(RandomUtils.getRandomTriangle(), RandomUtils.getRandomTriangle()))),
				new TestCase(new Ring(Radius.fromRadius(12.2), new Square(new Dims2d(15.0, 12.2)))),
				new TestCase(new Empty3dModel())
			);
	}
	
	@Parameterized.Parameters(name="{0}")
	public static Collection<Object[]> testCases() {
		List<Object[]> result = new ArrayList<Object[]>();
		for (TestCase testCase : createTestSubjects()) {
			result.add(new Object[] {testCase});
		}
		return result;
	}
	
	@Test
	public void shouldBeTheSameClass() {
		assertEquals(original.getClass(), original.cloneModel().getClass());
	}
	
	@Test
	public void subModelWithDefaultContextShouldClone() {
		assertEquals(original.toScad(ColorHandlingContext.DEFAULT), 
				original.subModel(ScadGenerationContextFactory.DEFAULT).toScad(ColorHandlingContext.DEFAULT));
	}
	
	@Test
	public void shouldRepresentsTheSameOpenScadObject() {
		assertEquals(original.toScad(ColorHandlingContext.DEFAULT), original.cloneModel().toScad(ColorHandlingContext.DEFAULT));
	}
	
	@Test
	public void shouldBeIndependent() {
		Abstract3dModel clone = original.cloneModel();
		SCAD scad = clone.toScad(ColorHandlingContext.DEFAULT);
		if (!scad.isEmpty()) {
			assertFalse(scad.equals(original.move(RandomUtils.getRandomCoords()).toScad(ColorHandlingContext.DEFAULT)));
		}
	}
	
	@Test
	public void shouldBeIndependent2() {
		if (!original.isBackground()) {
			Abstract3dModel newOne = original.background();
			assertNotSame(original, newOne);
			assertTrue(newOne.isBackground());
			assertFalse(original.isBackground());
		} 
		else if (!original.isDebug()) {
			Abstract3dModel newOne = original.debug();
			assertNotSame(original, newOne);
			assertTrue(newOne.isDebug());
			assertFalse(original.isDebug());
		}
	}
	
	@Test
	public void cloneModelShouldCopyFields() {
		assertEquals(111, Abstract3dModelReader.getTag(original.cloneModel()
				.withTag(111).cloneModel()));
		assertTrue(Abstract3dModelReader.isDebug(original.cloneModel()
				.debug().cloneModel()));
		assertTrue(Abstract3dModelReader.isBackground(original.cloneModel()
				.background().cloneModel()));
	}
	
	@Test
	public void subModelShouldCopyFields() {
		assertEquals(111, Abstract3dModelReader.getTag(original.cloneModel()
				.withTag(111).subModel(ScadGenerationContextFactory.DEFAULT)));
		assertTrue(Abstract3dModelReader.isDebug(original.cloneModel()
				.debug().subModel(ScadGenerationContextFactory.DEFAULT)));
		assertTrue(Abstract3dModelReader.isBackground(original.cloneModel()
					.background().subModel(ScadGenerationContextFactory.DEFAULT)));
	}
	
	@Test
	public void boundariesShouldBeIndependent() {
		Boundaries3d originalBoundaries = original.getBoundaries();
		Abstract3dModel clone = original.cloneModel();
		clone.move(RandomUtils.getRandomCoords());
		Boundaries3dTest.assertBoundariesEquals(originalBoundaries, original.getBoundaries());
	}
}
