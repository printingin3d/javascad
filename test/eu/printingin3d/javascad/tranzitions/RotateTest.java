package eu.printingin3d.javascad.tranzitions;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.testutils.TestModel;

public class RotateTest {
	
	@Before
	public void init() {
		Language.OpenSCAD.setCurrent();
	}
	
	@Test
	public void testGetRotate() {
		assertEqualsWithoutWhiteSpaces("rotate([10,20,30])", Rotate.getRotate(new Angles3d(10, 20, 30)));
	}
	
	@Test
	public void testGetRotatePovRay() {
		Language.POVRay.setCurrent();
		
		assertEqualsWithoutWhiteSpaces("rotate <10,20,30>", Rotate.getRotate(new Angles3d(10, 20, 30)));
	}
	
	@Test
	public void testToScad() {
		assertEqualsWithoutWhiteSpaces("rotate([5,100,50]) (model)", new Rotate(new TestModel("(model)"), new Angles3d(5, 100, 50)).toScad());
	}
	
	@Test
	public void testToPovRay() {
		Language.POVRay.setCurrent();
		
		assertEqualsWithoutWhiteSpaces("object { (model) rotate <5, 100, 50> #attributes}", new Rotate(new TestModel("(model)"), new Angles3d(5, 100, 50)).innerToScad());
	}
	
	@Test
	public void testBoundary() {
		Angles3d angle = RandomUtils.getRandomAngle();
		Boundaries3d boundaries = RandomUtils.getRandomBoundaries();
		Rotate rotate = new Rotate(new TestModel("(model)", boundaries), angle);
		Boundaries3dTest.assertBoundariesEquals(boundaries.rotate(angle), rotate.getBoundaries());
	}
	
	@Test(expected = IllegalValueException.class)
	public void shouldThrowExceptionIfModelIsNull() {
		new Rotate(null, new Angles3d(5, 100, 50));
	}
	
	@Test(expected = IllegalValueException.class)
	public void shouldThrowExceptionIfAnglesIsNull() {
		new Rotate(new TestModel("(model)"), null);
	}
}
