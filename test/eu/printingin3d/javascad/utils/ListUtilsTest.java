package eu.printingin3d.javascad.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ListUtilsTest {

	@Test
	public void removeNullsShouldReturnEmptyListIfTheGivenListIsEmpty() {
		assertTrue(ListUtils.removeNulls(Collections.emptyList()).isEmpty());
	}
	
	@Test
	public void removeNullsShouldReturnEmptyListIfTheGivenListContainsOnlyNullValues() {
		assertTrue(ListUtils.removeNulls(Arrays.asList(null, null)).isEmpty());
	}
	
	@Test
	public void removeNullsShouldReturnTheSameListIfItDoesntContainNullElement() {
		List<String> param = Arrays.asList("apple", "peas");
		assertEquals(param, ListUtils.removeNulls(param));
	}
	
	@Test
	public void removeNullsShouldReturnWithAListWithoutTheNulls() {
		List<String> param = Arrays.asList("apple", null, "peas");
		List<String> expected = Arrays.asList("apple", "peas");
		assertEquals(expected, ListUtils.removeNulls(param));
	}

}
