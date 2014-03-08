package eu.printingin3d.javascad.batchtests;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.enums.AlignType;
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.testutils.RandomUtils;

@RunWith(Parameterized.class)
public class Boundaries3dAlignTest {

	private static class TestCase {
		private final AlignType x;
		private final AlignType y;
		private final AlignType z;
		@Override
		public String toString() {
			return x + ", " + y + ", " + z;
		}
		public TestCase(AlignType x, AlignType y, AlignType z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		public AlignType getX() {
			return x;
		}
		public AlignType getY() {
			return y;
		}
		public AlignType getZ() {
			return z;
		}
		public Side getSide() {
			return new Side(x, y, z);
		}
	}

	private final TestCase testCase;
	
	public Boundaries3dAlignTest(TestCase testCase) {
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

	public static Collection<TestCase> createTestSubjects() {
		List<TestCase> result = new ArrayList<>();
		
		for (AlignType x : AlignType.values()) {
			for (AlignType y : AlignType.values()) {
				for (AlignType z : AlignType.values()) {
					result.add(new TestCase(x, y, z));
				}
			}
		}
		return result;
	}

	private static void assertOneAxis(AlignType align, double coordinate, Boundary boundary) {
		switch (align) {
		case MIN:
			assertDoubleEquals(boundary.getMin(), coordinate);
			break;
		case MAX:
			assertDoubleEquals(boundary.getMax(), coordinate);
			break;
		case CENTER:
			assertDoubleEquals(boundary.getMiddle(), coordinate);
			break;
		case NONE:
			assertDoubleEquals(0.0, coordinate);
			break;
		}
	}
	
	@Test
	public void shouldReturnProperValue() {
		Boundaries3d boundaries = RandomUtils.getRandomBoundaries();
		
		Coords3d alignedValue = boundaries.getAlignedValue(testCase.getSide());
		
		assertOneAxis(testCase.getX(), alignedValue.getX(), boundaries.getX());
		assertOneAxis(testCase.getY(), alignedValue.getY(), boundaries.getY());
		assertOneAxis(testCase.getZ(), alignedValue.getZ(), boundaries.getZ());
	}
}
