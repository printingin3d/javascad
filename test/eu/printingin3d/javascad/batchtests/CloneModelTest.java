package eu.printingin3d.javascad.batchtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Dims3d;
import eu.printingin3d.javascad.coords2d.Dims2d;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Abstract3dModelReader;
import eu.printingin3d.javascad.models.BoundedModel;
import eu.printingin3d.javascad.models.Cube;
import eu.printingin3d.javascad.models.Cylinder;
import eu.printingin3d.javascad.models.Polyhedron;
import eu.printingin3d.javascad.models.Prism;
import eu.printingin3d.javascad.models.Ring;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.models.Sphere;
import eu.printingin3d.javascad.models2d.Square;
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
		private TestCase(Abstract3dModel model) {
			this.model = model;
		}
		public Abstract3dModel getModel() {
			return model;
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
				new TestCase(cube.cloneModel()),
				new TestCase(cylinder.cloneModel()),
				new TestCase(new Prism(78.2, 10.1, 32.2, 5)),
				new TestCase(new Prism(78.2, 10.1, 5)),
				new TestCase(new Intersection()),
				new TestCase(new Intersection(cube.cloneModel())),
				new TestCase(new Intersection(cube.cloneModel(), cylinder.cloneModel())),
				new TestCase(new Difference(cube.cloneModel(), cylinder.cloneModel())),
				new TestCase(new Difference(cube.cloneModel())),
				new TestCase(Mirror.mirrorX(cube.cloneModel())),
				new TestCase(new Rotate(cylinder, new Angles3d(-21, 32.1, 331.4))),
				new TestCase(new Scale(cylinder, new Coords3d(4.10, 1.0, 3.21))),
				new TestCase(new Slicer(cube, Direction.X, 3, 0)),
				new TestCase(new Colorize(Color.BLACK, cube)),
				new TestCase(new Sphere(3.1)),
				new TestCase(new Translate(cube, new Coords3d(-23, 33.2, 7.3))),
				new TestCase(new BoundedModel(cube, RandomUtils.getRandomBoundaries())),
				new TestCase(new Union()),
				new TestCase(new Union(cube, cylinder)),
				new TestCase(cube.cloneModel().background()),
				new TestCase(cube.cloneModel().debug()),
				new TestCase(new Polyhedron(Arrays.asList(RandomUtils.getRandomTriangle(), RandomUtils.getRandomTriangle()))),
				new TestCase(new Ring(12.2, new Square(new Dims2d(15.0, 12.2))))
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
	public void subModelWithDefaultContextShouldClone() {
		assertEquals(original.toScad(ScadGenerationContextFactory.DEFAULT), 
				original.subModel(ScadGenerationContextFactory.DEFAULT).toScad(ScadGenerationContextFactory.DEFAULT));
	}
	
	@Test
	public void shouldRepresentsTheSameOpenScadObject() {
		assertEquals(original.toScad(ScadGenerationContextFactory.DEFAULT), original.cloneModel().toScad(ScadGenerationContextFactory.DEFAULT));
	}
	
	@Test
	public void shouldBeIndependent() {
		Abstract3dModel clone = original.cloneModel();
		SCAD scad = clone.toScad(ScadGenerationContextFactory.DEFAULT);
		if (!scad.isEmpty()) {
			original.move(RandomUtils.getRandomCoords());
			assertFalse(scad.equals(original.toScad(ScadGenerationContextFactory.DEFAULT)));
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
