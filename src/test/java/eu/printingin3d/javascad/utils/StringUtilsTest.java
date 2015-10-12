package eu.printingin3d.javascad.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class StringUtilsTest {
	private static final String SOMETHING = "something";

	@Test
	public void testIsEmptyWithNull() {
		assertFalse(StringUtils.hasText(null));
	}
	
	@Test
	public void testIsEmptyWithEmpty() {
		assertFalse(StringUtils.hasText(""));
	}
	
	@Test
	public void testIsEmptyWithNonEmpty() {
		assertTrue(StringUtils.hasText(SOMETHING));
	}
	
	@Test
	public void testAppendSimple() {
		assertEquals("something new", StringUtils.append(SOMETHING, " new"));
	}
	
	@Test
	public void testAppendWithNull() {
		assertSame(SOMETHING, StringUtils.append(SOMETHING, null));
		assertSame(SOMETHING, StringUtils.append(null, SOMETHING));
		assertEquals("", StringUtils.append(null, null));
	}
	
	@Test
	public void testAppendWithEmpty() {
		assertSame(SOMETHING, StringUtils.append(SOMETHING, ""));
		assertSame(SOMETHING, StringUtils.append("", SOMETHING));
	}
}
