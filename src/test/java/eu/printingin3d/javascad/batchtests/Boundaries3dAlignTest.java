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
import eu.printingin3d.javascad.testutils.RandomUtils;

@RunWith(Parameterized.class)
public class Boundaries3dAlignTest {
	private final AlignTestCase testCase;
	
	public Boundaries3dAlignTest(AlignTestCase testCase) {
		this.testCase = testCase;
	}

	@Parameterized.Parameters(name="{0}")
	public static Collection<Object[]> testCases() {
		List<Object[]> result = new ArrayList<Object[]>();
		for (AlignTestCase testCase : AlignTestCase.createTestSubjects()) {
			result.add(new Object[] {testCase});
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	private static void assertOneAxis(AlignType align, double coordinate, Boundary boundary) {
		switch (align) {
		case MIN:
		case MIN_IN:
		case MIN_OUT:
			assertDoubleEquals(boundary.getMin(), coordinate);
			break;
		case MAX:
		case MAX_IN:
		case MAX_OUT:
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
