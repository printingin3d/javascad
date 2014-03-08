package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.testutils.TestModel;

public class Abstract3dModelPovRayTest {

	private Abstract3dModel testSubject;

	@Before
	public void init() {
		Language.POVRay.setCurrent();
		
		testSubject = new TestModel("no { #attributes }");
	}
	
	@Test
	public void defaultShouldSetColorToRed() {
		assertEqualsWithoutWhiteSpaces("no { pigment {color rgb <1, 0, 0>} }", testSubject.toScad());
	}
	
	@Test
	public void debugDoesNotDoAnything() {
		assertEqualsWithoutWhiteSpaces("no { pigment {color rgb <1, 0, 0>} }", testSubject.debug().toScad());
	}
	
	@Test
	public void backgroundDoesNotDoAnything() {
		assertEqualsWithoutWhiteSpaces("no { pigment {color rgb <1, 0, 0>} }", testSubject.background().toScad());
	}
	
	@Test
	public void translationShouldBeAddedAfterColor() {
		assertEqualsWithoutWhiteSpaces("no { pigment {color rgb <1, 0, 0>} translate <100, 20, 1.1> }", testSubject.move(new Coords3d(100.0, 20.0, 1.1)).toScad());
	}
	
	@Test
	public void rotationShouldBeAddedAfterColor() {
		assertEqualsWithoutWhiteSpaces("no { pigment {color rgb <1, 0, 0>} rotate <1, 20, 3> }", testSubject.rotate(new Angles3d(1.0, 20.0, 3)).toScad());
	}

	@Test
	public void testMoves() {
		testSubject.moves(Arrays.asList(new Coords3d(10.0, 20.0, 30.0), new Coords3d(30.0, 10.0, 20.0)));
		assertEqualsWithoutWhiteSpaces("union { no { pigment {color rgb <1, 0, 0>} translate <10, 20, 30> } no { pigment {color rgb <1, 0, 0>} translate <30, 10, 20> }}", testSubject.toScad());
	}
}
