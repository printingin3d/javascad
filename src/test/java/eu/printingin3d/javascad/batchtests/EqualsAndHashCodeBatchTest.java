package eu.printingin3d.javascad.batchtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

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
import eu.printingin3d.javascad.coords.Abstract3dTest.TestAbstract3d;
import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords2d.Abstract2dTest.TestAbstract2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.coords2d.LineSegment;
import eu.printingin3d.javascad.coords2d.LineSegment2d;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.vrl.Vertex;

@RunWith(Parameterized.class)
public class EqualsAndHashCodeBatchTest {
	private static class TestCase<T> {
		private final T testObject;
		private final T equalObject;
		private final List<T> nonEqualObjects;
		
		@SafeVarargs
		public TestCase(T testObject, T equalObject, T... nonEqualObjects) {
			this.testObject = testObject;
			this.equalObject = equalObject;
			this.nonEqualObjects = Arrays.asList(nonEqualObjects);
		}
		
		public void testTrivialNonEquals() {
			assertFalse(testObject.equals(null));
			assertFalse(testObject.equals("alma"));
		}
		
		public void testTrivialEquals() {
			assertEquals(testObject, testObject);
		}
		
		public void testEquals() {
			assertEquals(testObject, equalObject);
			assertEquals(equalObject, testObject);
			assertEquals(testObject.hashCode(), equalObject.hashCode());
		}
		
		public void testNonEquals() {
			for (T v : nonEqualObjects) {
				assertNotEquals(testObject, v);
				assertNotEquals(v, testObject);
				assertNotEquals(testObject.hashCode(), v.hashCode());
			}
		}
		
		@Override
		public String toString() {
			return testObject.getClass().getName();
		}
	}
	
	public static Collection<TestCase<?>> createTestSubjects() {
		Coords3d c = RandomUtils.getRandomCoords();
		
		return Arrays.<TestCase<?>>asList(
				new TestCase<TestAbstract3d>(
						new TestAbstract3d(0.1, 20.0, 30.0), 
						new TestAbstract3d(1.0/10.0, 20.0, 30.0), 
						new TestAbstract3d(5.0, 20.0, 30.0), new TestAbstract3d(0.1, -20.0, 30.0), new TestAbstract3d(0.1, 20.0, 30.1)),
				new TestCase<TestAbstract3d>(
						new TestAbstract3d(0.1, -(1-0.5*2.0), 30.0), 
						new TestAbstract3d(1.0/10.0, +(1-0.5*2.0), 30.0), 
						new TestAbstract3d(5.0, 20.0, 30.0)),
				new TestCase<Angles3d>(
						new Angles3d(0.1, 20.0, 30.0), 
						new Angles3d(1.0/10.0, 20.0, 30.0), 
						new Angles3d(5.0, 20.0, 30.0), new Angles3d(0.1, -20.0, 30.0), new Angles3d(0.1, 20.0, 30.1)),
				new TestCase<SCAD>(
						new SCAD("test"), 
						new SCAD("test"), 
						new SCAD("not test")),
				new TestCase<SCAD>(
						new SCAD(null), 
						new SCAD(null), 
						new SCAD("not test")),
				new TestCase<TestAbstract2d>(
						new TestAbstract2d(0.1, 20.0), 
						new TestAbstract2d(1.0/10.0, 20.0), 
						new TestAbstract2d(5.0, 20.0), new TestAbstract2d(0.1, -20.0)),
				new TestCase<TestAbstract2d>(
						new TestAbstract2d(-(1-0.5*2.0), 20.0), 
						new TestAbstract2d(+((1-0.5*2.0)), 20.0), 
						new TestAbstract2d(5.0, 20.0), new TestAbstract2d(0.1, -20.0)),
				new TestCase<Vertex>(
						new Vertex(c, null), 
						new Vertex(c, null), 
						new Vertex(c, Color.WHITE)),
				new TestCase<Vertex>(
						new Vertex(c, Color.BLACK), 
						new Vertex(c, Color.BLACK), 
						new Vertex(c, Color.WHITE), new Vertex(c, null)),
				new TestCase<LineSegment2d>(
						new LineSegment2d(new Coords2d(20, 30), new Coords2d(-10, 25)),
						new LineSegment2d(new Coords2d(-10, 25), new Coords2d(20, 30)),
							new LineSegment2d(new Coords2d(0, 25), new Coords2d(20, 30)), 
							new LineSegment2d(new Coords2d(2, 30), new Coords2d(-10, 25))
						),
				new TestCase<Radius>(
						Radius.fromDiameter(10),
						Radius.fromRadius(5),
						Radius.fromRadius(15), Radius.fromRadius(50)
						),
				new TestCase<Angle>(
						Angle.ofDegree(45),
						Angle.A45,
						Angle.ZERO, Angle.ofRadian(Math.PI)
						),
				new TestCase<LineSegment<Coords3d>>(
						new LineSegment<Coords3d>(new Coords3d(0.0, 0.0, 0.0), new Coords3d(10, 20, 30)),
						new LineSegment<Coords3d>(Coords3d.ZERO, new Coords3d(10, 20, 30)),
						new LineSegment<Coords3d>(null, new Coords3d(10, 20, 30)), new LineSegment<Coords3d>(Coords3d.ZERO, new Coords3d(10, 10, 10)), new LineSegment<Coords3d>(Coords3d.ZERO, null)
						),
				new TestCase<LineSegment2d>(
						new LineSegment2d(new Coords2d(0.0, 0.0), new Coords2d(10, 20)),
						new LineSegment2d(Coords2d.ZERO, new Coords2d(10, 20)),
						new LineSegment2d(null, new Coords2d(10, 20)), new LineSegment2d(Coords2d.ZERO, new Coords2d(10, 10)), new LineSegment2d(Coords2d.ZERO, null)
						)
			);
	}
	
	@Parameterized.Parameters(name="{0}")
	public static Collection<Object[]> testCases() {
		List<Object[]> result = new ArrayList<Object[]>();
		for (TestCase<?> testCase : createTestSubjects()) {
			result.add(new Object[] {testCase});
		}
		return result;
	}
	
	private final TestCase<?> testCase;
	
	public EqualsAndHashCodeBatchTest(TestCase<?> testCase) {
		this.testCase = testCase;
	}

	@Test
	public void testTrivialNonEquals() {
		testCase.testTrivialNonEquals();
	}
	
	@Test
	public void testTrivialEquals() {
		testCase.testTrivialEquals();
	}
	
	@Test
	public void testEquals() {
		testCase.testEquals();
	}
	
	@Test
	public void testNonEquals() {
		testCase.testNonEquals();
	}
}
