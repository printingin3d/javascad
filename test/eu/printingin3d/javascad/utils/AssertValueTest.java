package eu.printingin3d.javascad.utils;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import eu.printingin3d.javascad.exceptions.IllegalValueException;

public class AssertValueTest {

	@Test(expected=IllegalValueException.class)
	public void isNotNullShouldThrowExceptionIfValueIsNull() {
		AssertValue.isNotNull(null, "test");
	}
	
	@Test
	public void isNotNullShouldNotThrowExceptionIfValueIsNotNull() {
		AssertValue.isNotNull("value", "test");
	}
	
	@Test(expected=IllegalValueException.class)
	public void isNotEmptyShouldThrowExceptionIfValueIsNull() {
		AssertValue.isNotEmpty(null, "test");
	}
	
	@Test(expected=IllegalValueException.class)
	public void isNotEmptyShouldThrowExceptionIfValueIsAnEmptyList() {
		AssertValue.isNotEmpty(Collections.emptyList(), "test");
	}
	
	@Test
	public void isNotEmptyShouldNotThrowExceptionIfValueIsANotEmptyList() {
		AssertValue.isNotEmpty(Arrays.asList("asd"), "test");
	}
	
	@Test(expected=IllegalValueException.class)
	public void isFalseShouldThrowExceptionIfValueIsTrue() {
		AssertValue.isFalse(true, "test");
	}
	
	@Test
	public void isFalseShouldNotThrowExceptionIfValueIsFalse() {
		AssertValue.isFalse(false, "test");
	}
	
	@Test(expected=IllegalValueException.class)
	public void isTrueShouldThrowExceptionIfValueIsFalse() {
		AssertValue.isTrue(false, "test");
	}
	
	@Test
	public void isTrueShouldNotThrowExceptionIfValueIsTrue() {
		AssertValue.isTrue(true, "test");
	}
	
	@Test(expected=IllegalValueException.class)
	public void isNotNegativeDoubleShouldThrowExceptionIfValueIsNegative() {
		AssertValue.isNotNegative(-1.2, "test");
	}
	
	@Test
	public void isNotNegativeDoubleShouldNotThrowExceptionIfValueIsPositive() {
		AssertValue.isNotNegative(321.1, "test");
	}
	
	@Test(expected=IllegalValueException.class)
	public void isNotNegativeIntShouldThrowExceptionIfValueIsNegative() {
		AssertValue.isNotNegative(-1, "test");
	}
	
	@Test
	public void isNotNegativeIntShouldNotThrowExceptionIfValueIsPositive() {
		AssertValue.isNotNegative(321, "test");
	}

}
