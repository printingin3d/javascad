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
import eu.printingin3d.javascad.testutils.RandomUtils;

@RunWith(Parameterized.class)
public class Angle3dRotateBatchTest {
	private static class TestCase {
		private final Angles3d start;
		private final Angles3d delta;
		private final Angles3d expected;
		public TestCase(Angles3d start, Angles3d delta, Angles3d expected) {
			this.start = start;
			this.delta = delta;
			this.expected = expected;
		}
		public Angles3d getStart() {
			return start;
		}
		public Angles3d getDelta() {
			return delta;
		}
		public Angles3d getExpected() {
			return expected;
		}
		@Override
		public String toString() {
			return start + " + " + delta + " = " + expected;
		}
	}
	
	private final TestCase testCase;
	
	public Angle3dRotateBatchTest(TestCase testCase) {
		this.testCase = testCase;
	}

	@Parameterized.Parameters(name="{0}")
	public static Collection<Object[]> testCases() {
		List<Object[]> result = new ArrayList<Object[]>();
		for (TestCase testCase : createTestSubjects()) {
			result.add(new Object[] {testCase});
		}
		return result;
	}

	private static Collection<TestCase> createTestSubjects() {
		List<TestCase> result = new ArrayList<>(Arrays.asList(
					new TestCase(Angles3d.xOnly(45.0), Angles3d.yOnly(45.0), new Angles3d(45.0, 45.0, 0.0)),
					new TestCase(Angles3d.yOnly(90.0), Angles3d.xOnly(90.0), new Angles3d(90.0, 0.0, 90.0)),
					new TestCase(Angles3d.xOnly(90.0), Angles3d.zOnly(90.0), new Angles3d(90.0, 0.0, 90.0)),
					new TestCase(Angles3d.ZERO, Angles3d.ZERO, Angles3d.ZERO),
					new TestCase(new Angles3d(25.0, 63.0, 0.0), new Angles3d(55.0, 72.3, 0.0), new Angles3d(7.0692,143.9934,-64.4547)),
					new TestCase(new Angles3d(0.0, 45.0, 0.0), new Angles3d(45.0, 0.0, 0.0), new Angles3d(54.7356,30,35.2644)),
					new TestCase(new Angles3d(116.9089,-176.0843,-102.7228), new Angles3d(106.7371,-88.1627,-139.8541), new Angles3d(25.3536,-165.5902,-118.9456)),
					new TestCase(new Angles3d(0,161.1918,153.7864), Angles3d.ZERO, new Angles3d(0,161.1918,153.7864))
				));
		
		for (int i=0;i<10;i++) {
			Angles3d randomAngle = RandomUtils.getRandomAngle();
			result.add(new TestCase(randomAngle, Angles3d.ZERO, randomAngle));
		}
		return result;
	}
	
	@Test
	public void testCase() {
		Angles3d newOne = testCase.getStart().rotate(testCase.getDelta());
		
		assertEquals(testCase.getExpected(), newOne);
		
		assertNotSame(testCase.getStart(), newOne);
	}
}
