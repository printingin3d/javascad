package eu.printingin3d.javascad.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.context.ScadGenerationContextFactory;

public class ScadGenerationContextTest {
	@Test
	public void allTagsShouldBeIncludedByDefault() {
		assertTrue(ScadGenerationContextFactory.DEFAULT.isTagIncluded());
	}
	
	@Test
	public void excludedTag() {
		IScadGenerationContext testSubject = ScadGenerationContextFactory.excludeThese(Arrays.asList(11, 32));
		assertFalse(testSubject.applyTag(11).isTagIncluded());
		assertTrue(testSubject.applyTag(55).isTagIncluded());
	}
	
	@Test
	public void includeTag() {
		IScadGenerationContext testSubject = ScadGenerationContextFactory.includeThese(Arrays.asList(21, 88));
		assertFalse(testSubject.applyTag(5).isTagIncluded());
		assertFalse(testSubject.applyTag(11).isTagIncluded());
		assertTrue(testSubject.applyTag(21).isTagIncluded());
		assertTrue(testSubject.applyTag(88).isTagIncluded());
		assertFalse(testSubject.applyTag(111).isTagIncluded());
	}
}
