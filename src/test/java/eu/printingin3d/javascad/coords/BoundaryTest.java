package eu.printingin3d.javascad.coords;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import eu.printingin3d.javascad.enums.AlignType;
import eu.printingin3d.javascad.exceptions.IllegalValueException;

public class BoundaryTest {
	private static final double DELTA = 9.8;
	private static final double MIN = 20.0;
	private static final double MAX = 50.0;
	
	public static void assertBoundaryEquals(Boundary expected, Boundary actual) {
		assertDoubleEquals("Min values mismatch", expected.getMin(), actual.getMin());
		assertDoubleEquals("Max values mismatch", expected.getMax(), actual.getMax());
		assertDoubleEquals("Middle values mismatch", expected.getMiddle(), actual.getMiddle());
	}

	@Test
	public void theResultShouldBeTheSameIrrelevantForTheOrder() {
		assertBoundaryEquals(new Boundary(MIN, MAX), new Boundary(MAX, MIN));
	}
	
	@Test
	public void shouldChooseMinAndMax() {
		Boundary b = new Boundary(MIN, MAX);
		assertDoubleEquals(MIN, b.getMin());
		assertDoubleEquals(MAX, b.getMax());
	}
	
	@Test
	public void shouldChooseMinAndMaxAnySort() {
		Boundary b = new Boundary(MAX, MIN);
		assertDoubleEquals(MIN, b.getMin());
		assertDoubleEquals(MAX, b.getMax());
	}
	
	@Test
	public void shouldChooseMinAndMaxFromListToo() {
		Boundary b = new Boundary(50.0, 0.0, 72.32, -32.8);
		assertDoubleEquals(-32.8, b.getMin());
		assertDoubleEquals(72.32, b.getMax());
	}

	@Test
	public void shouldWorkIfTheTwoValuesAreTheSame() {
		Boundary b = new Boundary(MAX, MAX);
		assertDoubleEquals(MAX, b.getMin());
		assertDoubleEquals(MAX, b.getMax());
	}
	
	@Test
	public void middleShouldReturnWithArithmeticMeanOfMinAndMax() {
		Boundary b = new Boundary(MIN, MAX);
		assertDoubleEquals((MIN+MAX)/2.0, b.getMiddle());
	}
	
	@Test
	public void testSize() {
		Boundary b = new Boundary(MIN, MAX);
		assertDoubleEquals(MAX-MIN, b.getSize());
	}
	
	@Test
	public void addShouldModifyTheValuesAccordingly() {
		Boundary b = new Boundary(MIN, MAX).add(DELTA);
		assertDoubleEquals(MIN+DELTA, b.getMin());
		assertDoubleEquals(MAX+DELTA, b.getMax());
	}
	
	@Test
	public void addBoundaryShouldModifyTheValuesAccordingly() {
		Boundary b = new Boundary(MIN, MAX).add(new Boundary(MIN, MAX));
		assertDoubleEquals(MIN*2, b.getMin());
		assertDoubleEquals(MAX*2, b.getMax());
	}
	
	@Test
	public void combineShouldReturnTheMinAndMaxValueOfAList() {
		Boundary b = Boundary.combine(Arrays.asList(new Boundary(12.5, 25.0), new Boundary(55.0, 20.0)));
		assertDoubleEquals(12.5, b.getMin());
		assertDoubleEquals(55.0, b.getMax());
	}
	
	@Test
	public void combineShouldReturnTheMinAndMaxValueOfAList2() {
		Boundary b = Boundary.combine(Arrays.asList(new Boundary(12.5, 25.0), new Boundary(55.0, 20.0), new Boundary(0.0, 111.11)));
		assertDoubleEquals(0.0, b.getMin());
		assertDoubleEquals(111.11, b.getMax());
	}
	
	@Test
	public void intersectionShouldReturnTheMinAndMaxValueOfAList() {
		Boundary b = Boundary.intersect(Arrays.asList(new Boundary(12.5, 25.0), new Boundary(55.0, 20.0)));
		assertDoubleEquals(20.0, b.getMin());
		assertDoubleEquals(25.0, b.getMax());
	}
	
	@Test(expected=IllegalValueException.class)
	public void intersectionShouldThrowExceptionIfTheBoundariesAreNotIntersecting() {
		Boundary.intersect(Arrays.asList(new Boundary(12.5, 15.0), new Boundary(55.0, 20.0), new Boundary(0.0, 111.11)));
	}
	
	@Test
	public void combineEmptyListShouldReturnWithZeroBoundary() {
		Boundary b = Boundary.combine(Collections.<Boundary>emptyList());
		assertDoubleEquals(0.0, b.getMin());
		assertDoubleEquals(0.0, b.getMax());
	}
	
	@Test
	public void testScale() {
		Boundary b = new Boundary(MIN, MAX).scale(3.0);
		assertDoubleEquals(MIN*3.0, b.getMin());
		assertDoubleEquals(MAX*3.0, b.getMax());
	}
	
	@Test
	public void negateWithTrueShouldNegateTheObjectsValues() {
		Boundary b = new Boundary(MIN, MAX).negate(true);
		assertDoubleEquals(-MAX, b.getMin());
		assertDoubleEquals(-MIN, b.getMax());
	}
	
	@Test
	public void negateWithTrueShouldDoNothing() {
		Boundary b = new Boundary(MIN, MAX).negate(false);
		assertDoubleEquals(MIN, b.getMin());
		assertDoubleEquals(MAX, b.getMax());
	}
	
	@Test
	public void alignValueShouldReturnMinValueForMinAlignment() {
		Boundary b = new Boundary(MIN, MAX);
		assertDoubleEquals(MIN, b.getAlignedValue(AlignType.MIN));
	}
	
	@Test
	public void alignValueShouldReturnMaxValueForMaxAlignment() {
		Boundary b = new Boundary(MIN, MAX);
		assertDoubleEquals(MAX, b.getAlignedValue(AlignType.MAX));
	}
	
	@Test
	public void alignValueShouldReturnMiddleValueForCenterAlignment() {
		Boundary b = new Boundary(MIN, MAX);
		assertDoubleEquals((MIN+MAX)/2.0, b.getAlignedValue(AlignType.CENTER));
	}
	
	@Test
	public void alignValueShouldReturnZeroValueForNoneAlignment() {
		Boundary b = new Boundary(MIN, MAX);
		assertDoubleEquals(0.0, b.getAlignedValue(AlignType.NONE));
	}
	
	@Test
	public void isInsideOfShouldReturnTrueIfAndOnlyIfTheParameterContainsThisBoundary() {
		Boundary a = new Boundary(0.0, 5.5);
		Boundary b = new Boundary(5.0, 10.5);
		Boundary c = new Boundary(1.0, 3.5);
		
		assertFalse(a.isInsideOf(b));
		assertFalse(b.isInsideOf(c));
		assertTrue(c.isInsideOf(a));
	}
	
	@Test
	public void removeShouldDecreaseTheMaximumValueIfTheRemovedValuesAreBiggerThenTheMinimumButLessThenTheMaximum() {
		Boundary a = new Boundary(0.0, 5.5);
		Boundary b = new Boundary(5.0, 10.5);

		Boundary c = a.remove(b);
		
		assertDoubleEquals(0.0, c.getMin());
		assertDoubleEquals(5.0, c.getMax());
	}
	
	@Test
	public void removeShouldIncreaseTheMinimumValueIfTheRemovedValuesAreSmallerThenTheMaximumButGreaterThenTheMinimum() {
		Boundary a = new Boundary(0.0, 5.5);
		Boundary b = new Boundary(5.0, 10.5);
		
		Boundary c = b.remove(a);
		
		assertDoubleEquals(5.5, c.getMin());
		assertDoubleEquals(10.5, c.getMax());
	}
	
	@Test
	public void removeShouldDoNothingIfTheGivenBoundaryIsInsideOfIt() {
		Boundary a = new Boundary(0.0, 10.5);
		Boundary b = new Boundary(3.0, 5.5);
		
		Boundary c = a.remove(b);

		assertBoundaryEquals(a, c);
	}
	
	@Test
	public void removeShouldDoNothingIfTheGivenBoundaryHasNoIntersectionWithIt() {
		Boundary a = new Boundary(5.0, 10.5);
		Boundary b = new Boundary(1.0, 3.5);
		
		assertBoundaryEquals(a, a.remove(b));
		assertBoundaryEquals(b, b.remove(a));
	}
	
	@Test(expected=IllegalValueException.class)
	public void removeShouldThrowExceptionIfGivenBoundaryContainsIt() {
		Boundary a = new Boundary(0.0, 10.5);
		Boundary b = new Boundary(3.0, 5.5);
		
		b.remove(a);
	}
	
	@Test
	public void symmetricBoundaryShouldBeCreated() {
		Boundary testSubject = Boundary.createSymmetricBoundary(18.2);
		
		assertDoubleEquals(18.2, testSubject.getMax());
		assertDoubleEquals(0.0, testSubject.getMiddle());
	}
}
