package eu.printingin3d.javascad.batchtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.tranform.ITransformation;
import eu.printingin3d.javascad.tranform.TransformationFactory;

@RunWith(Parameterized.class)
public class TransformationFactoryRotationTest {

	private static class TestCase {
		private final Angles3d start;
		private final Angles3d delta;

		public TestCase(Angles3d start, Angles3d delta) {
			this.start = start;
			this.delta = delta;
		}

		public Angles3d getStart() {
			return start;
		}

		public Angles3d getDelta() {
			return delta;
		}

		@Override
		public String toString() {
			return start + " + " + delta + " = " + start.rotate(delta);
		}
	}

	private final TestCase testCase;

	public TransformationFactoryRotationTest(TestCase testCase) {
		this.testCase = testCase;
	}

	@Parameterized.Parameters(name = "{0}")
	public static Collection<Object[]> testCases() {
		List<Object[]> result = new ArrayList<Object[]>();
		for (TestCase testCase : createTestSubjects()) {
			result.add(new Object[] { testCase });
		}
		return result;
	}

	private static Collection<TestCase> createTestSubjects() {
		List<TestCase> result = new ArrayList<>(Arrays.asList(
				new TestCase(Angles3d.xOnly(45.0), Angles3d.yOnly(45.0)), 
				new TestCase(Angles3d.yOnly(90.0), Angles3d.xOnly(90.0)),
				new TestCase(Angles3d.xOnly(90.0), Angles3d.zOnly(90.0)), 
				new TestCase(Angles3d.ZERO, Angles3d.ZERO), 
				new TestCase(new Angles3d(25.0, 63.0, 0.0), new Angles3d(55.0, 72.3, 0.0)),
				new TestCase(new Angles3d(0.0, 45.0, 0.0), new Angles3d(45.0, 0.0, 0.0)),
				new TestCase(new Angles3d(116.9089, -176.0843, -102.7228), new Angles3d(106.7371, -88.1627, -139.8541)),
				new TestCase(new Angles3d(0, 161.1918, 153.7864), Angles3d.ZERO)));
		return result;
	}

	@Test
	public void testRotation() {
		Angles3d newOne = testCase.getStart().rotate(testCase.getDelta());

		Coords3d coord = RandomUtils.getRandomCoords();
		
		ITransformation trans2 = TransformationFactory.getRotationMatrix(newOne);
		
		Coords3d coord1 = coord.rotate(newOne);
		Coords3d coord2 = trans2.transform(coord);
		
		assertEquals(coord1, coord2);

		assertNotSame(coord1, coord2);
	}
	
	@Test
	public void testMulRotation() {
		Angles3d newOne = testCase.getStart().rotate(testCase.getDelta());
		
		Coords3d coord = RandomUtils.getRandomCoords();
		
		ITransformation trans2 = TransformationFactory.getRotationMatrix(newOne);
		
		Coords3d coord1 = coord.rotate(newOne);
		Coords3d coord2 = trans2.transform(coord);
		
		assertEquals(coord1, coord2);
		
		assertNotSame(coord1, coord2);
	}
}
