package eu.printingin3d.javascad.batchtests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.coords2d.LineSegment2d;

@RunWith(Parameterized.class)
public class LineSegment2dCommonTest {
	private static class TestCase {
		private final String name;
		private final LineSegment2d subject1;
		private final LineSegment2d subject2;
		private final LineSegment2d expected;
		
		public TestCase(String name, LineSegment2d subject1, LineSegment2d subject2, LineSegment2d expected) {
			this.name = name;
			this.subject1 = subject1;
			this.subject2 = subject2;
			this.expected = expected;
		}

		private static LineSegment2d reverse(LineSegment2d ls) {
			return new LineSegment2d(ls.getEnd(), ls.getStart());
		}
		
		public List<TestCase> allPermutation() {
			return Arrays.asList(
					this,
					new TestCase(name + " (first segment reversed)", reverse(subject1), subject2, expected),
					new TestCase(name + " (second segment reversed)", subject1, reverse(subject2), expected),
					new TestCase(name + " (both segments reversed)", reverse(subject1), reverse(subject2), expected),
					new TestCase(name + " (reversed)", subject2, subject1, expected),
					new TestCase(name + " (segments and order reversed 1)", subject2, reverse(subject1), expected),
					new TestCase(name + " (segments and order reversed 2)", reverse(subject2), subject1, expected),
					new TestCase(name + " (segments and order reversed 3)", reverse(subject2), reverse(subject1), expected)
				);
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	@Parameterized.Parameters(name="{0}")
	public static Collection<Object[]> testCases() {
		List<Object[]> result = new ArrayList<Object[]>();
		for (TestCase testCase : createTestSubjects()) {
			for (TestCase tc : testCase.allPermutation()) {
				result.add(new Object[] {tc});
			}
		}
		return result;
	}

	private static List<TestCase> createTestSubjects() {
		return Arrays.asList(
				new TestCase("distinct segments", new LineSegment2d(new Coords2d(10, 20), new Coords2d(0, 10)), 
						new LineSegment2d(new Coords2d(10, 30), new Coords2d(3, 0)), 
							null),
				new TestCase("after each other", new LineSegment2d(new Coords2d(10, 10), new Coords2d(20, 10)), 
						new LineSegment2d(new Coords2d(20, 10), new Coords2d(30, 10)), 
							null),
				new TestCase("after each other with gap", new LineSegment2d(new Coords2d(10, 10), new Coords2d(20, 10)), 
						new LineSegment2d(new Coords2d(30, 10), new Coords2d(40, 10)), 
							null),
				new TestCase("one contains other", new LineSegment2d(new Coords2d(20, 20), new Coords2d(30, 30)),
						new LineSegment2d(new Coords2d(10, 10), new Coords2d(40, 40)),
							new LineSegment2d(new Coords2d(20, 20), new Coords2d(30, 30))),
				new TestCase("overlap", new LineSegment2d(new Coords2d(10, 10), new Coords2d(30, 30)),
						new LineSegment2d(new Coords2d(20, 20), new Coords2d(40, 40)),
							new LineSegment2d(new Coords2d(20, 20), new Coords2d(30, 30))),
				new TestCase("non paralel touching", new LineSegment2d(new Coords2d(0, 0), new Coords2d(20, 20)),
						new LineSegment2d(new Coords2d(10, 10), new Coords2d(20, 10)),
							null)
			);
	}
	
	private final TestCase testCase;
	
	public LineSegment2dCommonTest(TestCase testCase) {
		this.testCase = testCase;
	}

	@Test
	public void test() {
		Assert.assertEquals(testCase.expected, testCase.subject1.common(testCase.subject2));
	}
}
