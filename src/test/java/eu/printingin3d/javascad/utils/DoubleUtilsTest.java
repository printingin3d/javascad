package eu.printingin3d.javascad.utils;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DoubleUtilsTest {

	@Test
	public void betweenShouldReturnFalseIfValueIsLowerThanBothValues() {
		assertFalse(DoubleUtils.between(5.5, 7.5, 10.5));
		assertFalse(DoubleUtils.between(5.5, 10.5, 7.5));
	}
	
	@Test
	public void betweenShouldReturnFalseIfValueIsHigherThanBothValues() {
		assertFalse(DoubleUtils.between(50.5, 7.5, 10.5));
		assertFalse(DoubleUtils.between(50.5, 10.5, 7.5));
	}
	
	@Test
	public void betweenShouldReturnTrueIfValueIsBetweenTheTwoValues() {
		assertTrue(DoubleUtils.between(8.5, 7.5, 10.5));
		assertTrue(DoubleUtils.between(8.5, 10.5, 7.5));
	}
}
