package eu.printingin3d.javascad.enums;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundary;

public class AlignTypeTest {
	private static final double MIN = 20.0;
	private static final double MAX = 50.0;
	private static final Boundary TEST_BOUNDARY = new Boundary(MIN, MAX);

	@SuppressWarnings("deprecation")
	@Test
	public void alignValueShouldReturnMinValueForMinAlignment() {
		assertDoubleEquals(MIN, AlignType.MIN.getAlignedValue(TEST_BOUNDARY));
		assertDoubleEquals(MIN, AlignType.MIN_IN.getAlignedValue(TEST_BOUNDARY));
		assertDoubleEquals(MIN, AlignType.MIN_OUT.getAlignedValue(TEST_BOUNDARY));
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void alignValueShouldReturnMaxValueForMaxAlignment() {
		assertDoubleEquals(MAX, AlignType.MAX.getAlignedValue(TEST_BOUNDARY));
		assertDoubleEquals(MAX, AlignType.MAX_IN.getAlignedValue(TEST_BOUNDARY));
		assertDoubleEquals(MAX, AlignType.MAX_OUT.getAlignedValue(TEST_BOUNDARY));
	}
	
	@Test
	public void alignValueShouldReturnMiddleValueForCenterAlignment() {
		assertDoubleEquals((MIN+MAX)/2, AlignType.CENTER.getAlignedValue(TEST_BOUNDARY));
	}
	
	@Test
	public void alignValueShouldReturnZeroValueForNoneAlignment() {
		assertDoubleEquals(0.0, AlignType.NONE.getAlignedValue(TEST_BOUNDARY));
	}

}
