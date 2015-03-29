package eu.printingin3d.javascad.models2d;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.testutils.AssertEx;
import eu.printingin3d.javascad.testutils.Test2dModel;

public class Abstract2dModelTest {
	private Abstract2dModel testSubject;
	
	@Before
	public void init() {
		testSubject = new Test2dModel("(model)", new Boundaries2d(Boundary.createSymmetricBoundary(5), Boundary.createSymmetricBoundary(10)));
	}
	
	@Test
	public void shouldReturnWithTheInnerScadValue() {
		AssertEx.assertEqualsWithoutWhiteSpaces("(model)", testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void moveShouldCreateNewObject() {
		Assert.assertNotSame(testSubject.move(new Coords2d(1,1)), testSubject);
	}
	
	@Test
	public void moveShouldMoveBoundaries() {
		Boundaries2d boundaries2d = testSubject.move(new Coords2d(1,2)).getBoundaries2d();
		AssertEx.assertDoubleEquals(1, boundaries2d.getX().getMiddle());
		AssertEx.assertDoubleEquals(2, boundaries2d.getY().getMiddle());
	}
}
