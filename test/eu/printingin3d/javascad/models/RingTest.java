package eu.printingin3d.javascad.models;

import org.junit.Test;

import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.BoundaryTest;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.testutils.AssertEx;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.testutils.Test2dModel;

public class RingTest {

	@Test
	public void testNormalRing() {
		Abstract3dModel testSubject = new Ring(5, new Test2dModel("(model)"));
		AssertEx.assertEqualsWithoutWhiteSpaces("rotate_extrude() translate([5, 0, 0]) (model);", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testBoundaries() {
		Boundary testSubjectYBoundary = RandomUtils.getRandomBoundary();
		Abstract3dModel testSubject = new Ring(15, new Test2dModel("(model)", 
				new Boundaries2d(
						Boundary.createSymmetricBoundary(5), 
						testSubjectYBoundary)));
		
		Boundaries3d boundaries = testSubject.getBoundaries();
		
		BoundaryTest.assertBoundaryEquals(testSubjectYBoundary, boundaries.getZ());
		
		AssertEx.assertDoubleEquals(20.0, boundaries.getX().getMax());
		AssertEx.assertDoubleEquals( 0.0, boundaries.getX().getMiddle());
		
		AssertEx.assertDoubleEquals(20.0, boundaries.getY().getMax());
		AssertEx.assertDoubleEquals( 0.0, boundaries.getY().getMiddle());
	}
}
