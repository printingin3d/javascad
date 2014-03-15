package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.coords.Boundaries3dTest.assertBoundariesEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.coords.BoundaryTest;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class Abstract3dModelBoundariesTest {
	private Boundaries3d testSubjectsBoundaries;
	private Abstract3dModel testSubject;

	@Before
	public void init() {
		testSubjectsBoundaries = RandomUtils.getRandomBoundaries();
		testSubject = new Test3dModel("(subject)", testSubjectsBoundaries);
	}
	
	@Test
	public void boundariesShouldReturnWithTheModelsBoundariesIfNoMovesAdded() {
		assertBoundariesEquals(testSubjectsBoundaries, testSubject.getBoundaries());
	}
	
	@Test
	public void verticalBoundariesShouldChangeIfZMovesAdded() {
		double delta = RandomUtils.getRandomDouble(-1000, 1000);
		Abstract3dModel testSubject = this.testSubject.move(Coords3d.zOnly(delta));
		Boundaries3d boundaries = testSubject.getBoundaries();
		
		BoundaryTest.assertBoundaryEquals(testSubjectsBoundaries.getX(), boundaries.getX());
		BoundaryTest.assertBoundaryEquals(testSubjectsBoundaries.getY(), boundaries.getY());
		
		assertDoubleEquals(testSubjectsBoundaries.getZ().getMin()+delta, boundaries.getZ().getMin());
		assertDoubleEquals(testSubjectsBoundaries.getZ().getMax()+delta, boundaries.getZ().getMax());
	}
	
	@Test
	public void boundariesShouldNotThrowExceptionIfRotationHasAdded() {
		Abstract3dModel testSubject = this.testSubject.rotate(Angles3d.ROTATE_MINUS_X);
		testSubject.getBoundaries();
	}
	
	@Test
	public void boundariesShouldUseTheRotationFromBoundaries() {
		Angles3d angle = RandomUtils.getRandomAngle();
		Boundaries3dTest.assertBoundariesEquals(
				testSubjectsBoundaries.rotate(angle), this.testSubject.rotate(angle).getBoundaries());
	}
	
	@Test
	public void boundariesShouldCalculateBoundariesForMultiModels() {
		Abstract3dModel testSubject = this.testSubject.moves(Arrays.asList(new Coords3d(10.0, 20.0, 30.0), new Coords3d(30.0, 10.0, 20.0)));
		Boundaries3d boundaries = testSubject.getBoundaries();
		
		assertDoubleEquals(testSubjectsBoundaries.getX().getMin()+10.0, boundaries.getX().getMin());
		assertDoubleEquals(testSubjectsBoundaries.getX().getMax()+30.0, boundaries.getX().getMax());
		
		assertDoubleEquals(testSubjectsBoundaries.getY().getMin()+10.0, boundaries.getY().getMin());
		assertDoubleEquals(testSubjectsBoundaries.getY().getMax()+20.0, boundaries.getY().getMax());
		
		assertDoubleEquals(testSubjectsBoundaries.getZ().getMin()+20.0, boundaries.getZ().getMin());
		assertDoubleEquals(testSubjectsBoundaries.getZ().getMax()+30.0, boundaries.getZ().getMax());
	}	
}
