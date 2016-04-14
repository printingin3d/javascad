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
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.testutils.Test3dModel;

@RunWith(Parameterized.class)
public class Abstract3dModelAlignBatchTest {

	private final AlignTestCase testCase;
	
	public Abstract3dModelAlignBatchTest(AlignTestCase testCase) {
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
	private static void assertOneAxis(AlignType align, double coordinate, Boundary newBoundary, Boundary oldBoundary) {
		switch (align) {
		case MIN:
		case MIN_IN:
		case MIN_OUT:
			assertDoubleEquals(coordinate, newBoundary.getMin());
			break;
		case MAX:
		case MAX_IN:
		case MAX_OUT:
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
	public void shouldPositionToCoordinateAccordingly() {
		Boundaries3d boundaries = RandomUtils.getRandomBoundaries();
		Abstract3dModel testSubject = new Test3dModel("(base)", boundaries);
		Coords3d coords = RandomUtils.getRandomCoords();
		
		Abstract3dModel ts = testSubject.align(testCase.getSide(), coords);
		
		assertOneAxis(testCase.getX(), coords.getX(), ts.getBoundaries().getX(), boundaries.getX());
		assertOneAxis(testCase.getY(), coords.getY(), ts.getBoundaries().getY(), boundaries.getY());
		assertOneAxis(testCase.getZ(), coords.getZ(), ts.getBoundaries().getZ(), boundaries.getZ());
	}
	
	@Test
	public void shouldPositionToObjectAccordingly() {
		Boundaries3d boundaries = RandomUtils.getRandomBoundaries();
		Abstract3dModel testSubject = new Test3dModel("(base)", boundaries);
		Coords3d coords = RandomUtils.getRandomCoords();
		
		Abstract3dModel ts = testSubject.align(testCase.getSide(), coords);
		
		assertOneAxis(testCase.getX(), coords.getX(), ts.getBoundaries().getX(), boundaries.getX());
		assertOneAxis(testCase.getY(), coords.getY(), ts.getBoundaries().getY(), boundaries.getY());
		assertOneAxis(testCase.getZ(), coords.getZ(), ts.getBoundaries().getZ(), boundaries.getZ());
	}
}
