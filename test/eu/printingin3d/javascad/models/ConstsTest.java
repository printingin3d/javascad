package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.openscad.Consts;

public class ConstsTest {
	public static final String DEFAULT_CONSTS = "$fs=0.25;$fa=6;";

	@Before
	public void init() {
		Language.OpenSCAD.setCurrent();
	}
	
	@Test
	public void testDefaultContructor() {
		Consts consts = new Consts();
		assertEqualsWithoutWhiteSpaces(DEFAULT_CONSTS, consts.toScad());
	}
	
	@Test
	public void testContructor() {
		Consts consts = new Consts(5.0, 10);
		assertEqualsWithoutWhiteSpaces("$fs=5.0;$fa=10;", consts.toScad());
	}
	
	@Test
	public void forPovRayTheResultShouldBeEmpty() {
		Language.POVRay.setCurrent();

		Consts consts = new Consts();
		assertEqualsWithoutWhiteSpaces("", consts.toScad());
	}

}
