package eu.printingin3d.javascad.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ScadSurroundingsTest {
	
	@Test
	public void emptySurroundingsShouldNotChangeScad() {
		SCAD scad = new SCAD("test scad");
		
		SCAD changed = ScadSurroundings.EMPTY.surroundScad(scad);
		
		assertSame(scad, changed);
	}

	@Test
	public void prefixShouldBePutBeforeScad() {
		SCAD scad = new SCAD("test scad");
		
		SCAD changed = ScadSurroundings.EMPTY.appendPrefix("test ").surroundScad(scad);
		
		assertEquals("test test scad", changed.getScad());
	}
	
	@Test
	public void prefixShouldNotDoAnythingIfTheGivenParameterIsEmpty() {
		assertSame(ScadSurroundings.EMPTY, ScadSurroundings.EMPTY.appendPrefix(""));
	}
	
	@Test
	public void postfixShouldBePutBeforeScad() {
		SCAD scad = new SCAD("test scad");
		
		SCAD changed = ScadSurroundings.EMPTY.appendPostfix(" test").surroundScad(scad);
		
		assertEquals("test scad test", changed.getScad());
	}
	
	@Test
	public void postfixShouldNotDoAnythingIfTheGivenParameterIsEmpty() {
		assertSame(ScadSurroundings.EMPTY, ScadSurroundings.EMPTY.appendPostfix(""));
	}
	
	@Test
	public void multiplePrefixesAndPostfixes() {
		SCAD scad = new SCAD("test scad");
		
		SCAD changed = ScadSurroundings.EMPTY
				.appendPrefix("{")
				.appendPostfix("}")
				.appendPrefix("(")
				.appendPostfix(")")
				.surroundScad(scad);
		
		assertEquals("{(test scad)}", changed.getScad());
	}
	
	@Test
	public void surroundScadShouldReturnEmptyIfTheGivenParameterIsEmpty() {
		SCAD changed = ScadSurroundings.EMPTY
				.appendPrefix("{")
				.appendPostfix("}")
				.appendPrefix("(")
				.appendPostfix(")")
				.surroundScad(SCAD.EMPTY);
		
		assertSame(SCAD.EMPTY, changed);
	}
	
	@Test
	public void includeShouldBeInsideOfTheOuter() {
		SCAD scad = new SCAD("test scad");
		
		ScadSurroundings outer = ScadSurroundings.EMPTY
				.appendPrefix("{")
				.appendPostfix("}");
		ScadSurroundings inner = ScadSurroundings.EMPTY
				.appendPrefix("(")
				.appendPostfix(")");
		
		SCAD changed = outer.include(inner).surroundScad(scad);
		
		assertEquals("{(test scad)}", changed.getScad());
	}
	
}
