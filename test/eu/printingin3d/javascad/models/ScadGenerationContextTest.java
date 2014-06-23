package eu.printingin3d.javascad.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class ScadGenerationContextTest {
	@Test
	public void allTagsShouldBeIncludedByDefault() {
		assertTrue(ScadGenerationContext.DEFAULT.isTagIncluded(21));
	}
	
	@Test
	public void excludedTag() {
		ScadGenerationContext testSubject = ScadGenerationContext.excludeThese(Arrays.asList(11, 32));
		assertFalse(testSubject.isTagIncluded(11));
		assertTrue(testSubject.isTagIncluded(55));
	}
	
	@Test
	public void includeTag() {
		ScadGenerationContext testSubject = ScadGenerationContext.includeThese(Arrays.asList(21, 88));
		assertFalse(testSubject.isTagIncluded(5));
		assertFalse(testSubject.isTagIncluded(11));
		assertTrue(testSubject.isTagIncluded(21));
		assertTrue(testSubject.isTagIncluded(88));
		assertFalse(testSubject.isTagIncluded(111));
	}
}
