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
import eu.printingin3d.javascad.coords.BoundaryTest;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.enums.AlignType;
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.testutils.TestModel;

@RunWith(Parameterized.class)
public class Abstract3dModelAlignBatchTest {

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
		public Side getSize() {
			return new Side(x, y, z);
		}
	}

	private final TestCase testCase;
	
	public Abstract3dModelAlignBatchTest(TestCase testCase) {
		this.testCase = testCase;
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
	
	@Parameterized.Parameters(name="{0}")
	public static Collection<Object[]> testCases() {
		List<Object[]> result = new ArrayList<Object[]>();
		for (TestCase testCase : createTestSubjects()) {
			result.add(new Object[] {testCase});
		}
		return result;
	}

	private static void assertOneAxis(AlignType align, double coordinate, Boundary newBoundary, Boundary oldBoundary) {
		switch (align) {
		case MIN:
			assertDoubleEquals(coordinate, newBoundary.getMin());
			break;
		case MAX:
			assertDoubleEquals(coordinate, newBoundary.getMax());
			break;
		case CENTER:
			assertDoubleEquals(coordinate, newBoundary.getMiddle());
			break;
		case NONE:
			BoundaryTest.assertBoundaryEquals(oldBoundary, newBoundary);
			break;
		}
	}
	
	@Test
	public void shouldPositionAccordingly() {
		Boundaries3d boundaries = RandomUtils.getRandomBoundaries();
		Abstract3dModel testSubject = new TestModel("(base)", boundaries);
		Coords3d coords = RandomUtils.getRandomCoords();
		
		testSubject.align(testCase.getSize(), coords);
		
		assertOneAxis(testCase.getX(), coords.getX(), testSubject.getBoundaries().getX(), boundaries.getX());
		assertOneAxis(testCase.getY(), coords.getY(), testSubject.getBoundaries().getY(), boundaries.getY());
		assertOneAxis(testCase.getZ(), coords.getZ(), testSubject.getBoundaries().getZ(), boundaries.getZ());
	}
}
