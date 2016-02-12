package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class Abstract3dModelEmptyTest {
	private static final double MAX_BOUND = 15.0;
	private static final double MIN_BOUND = -10.0;
	
	private Abstract3dModel testSubject;
	
	@Before
	public void init() {
		testSubject = new Test3dModel("",
			new Boundaries3d(
					new Boundary(MIN_BOUND, MAX_BOUND), 
					new Boundary(MIN_BOUND, MAX_BOUND),
					new Boundary(MIN_BOUND, MAX_BOUND)));
	}
	
	@Test
	public void testDefault() {
		assertEqualsWithoutWhiteSpaces("", testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testMove() {
		testSubject.move(new Coords3d(10.0, 20.0, 30.0));
		assertEqualsWithoutWhiteSpaces("", testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testMoves() {
		testSubject.moves(new Coords3d(10.0, 20.0, 30.0), new Coords3d(30.0, 10.0, 20.0));
		assertEqualsWithoutWhiteSpaces("", testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}

	@Test
	public void testRotate() {
		testSubject.rotate(new Angles3d(10.0, 20.0, 30.0));
		assertEqualsWithoutWhiteSpaces("", testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testDefaultWithDebug() {
		testSubject.debug();
		assertEqualsWithoutWhiteSpaces("", testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testDefaultWithBackground() {
		testSubject.background();
		assertEqualsWithoutWhiteSpaces("", testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testDefaultWithDebugAndBackground() {
		testSubject.background().debug();
		assertEqualsWithoutWhiteSpaces("", testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testMoveAndRotate() {
		testSubject.rotate(new Angles3d(10.0, 20.0, 30.0));
		testSubject.move(new Coords3d(30.0, 10.0, 20.0));
		assertEqualsWithoutWhiteSpaces("", testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void testAlignLeft() {
		Cube cube1 = new Cube(30.0);
		testSubject.align(Side.LEFT, cube1, false);
		assertEqualsWithoutWhiteSpaces("", testSubject.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
}
