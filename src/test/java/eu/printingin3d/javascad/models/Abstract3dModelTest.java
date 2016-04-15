package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.testutils.Test3dModel;
import eu.printingin3d.javascad.tranzitions.Union;

public class Abstract3dModelTest {
	private static final double MAX_BOUND = 15.0;
	private static final double MIN_BOUND = -10.0;
	private static final Boundaries3d BOUNDARIES = new Boundaries3d(
			new Coords3d(MIN_BOUND, MIN_BOUND, MIN_BOUND), 
			new Coords3d(MAX_BOUND, MAX_BOUND, MAX_BOUND));
	
	private Abstract3dModel testSubject;
	
	@Before
	public void init() {
		testSubject = new Test3dModel("(empty)", BOUNDARIES);
	}
	
	@Test
	public void testDefault() {
		assertEqualsWithoutWhiteSpaces("(empty)", testSubject);
	}
	
	@Test
	public void testDefaultWithDebug() {
		Abstract3dModel ts = testSubject.debug();
		assertEqualsWithoutWhiteSpaces("# (empty)", ts);
	}
	
	@Test
	public void testDefaultWithBackground() {
		Abstract3dModel ts = testSubject.background();
		assertEqualsWithoutWhiteSpaces("% (empty)", ts);
	}
	
	@Test
	public void testDefaultWithDebugAndBackground() {
		Abstract3dModel ts = testSubject.background().debug();
		assertEqualsWithoutWhiteSpaces("# % (empty)", ts);
	}
	
	@Test
	public void testMove() {
		Abstract3dModel ts = testSubject.move(new Coords3d(10.0, 20.0, 30.0));
		assertEqualsWithoutWhiteSpaces("translate([10,20,30])(empty)", ts);
	}

	@Test
	public void testMoves() {
		Abstract3dModel moved = testSubject.moves(
				new Coords3d(10.0, 20.0, 30.0), new Coords3d(30.0, 10.0, 20.0));
		assertEqualsWithoutWhiteSpaces("union(){translate([10,20,30])(empty)translate([30,10,20])(empty)}", moved);
	}
	
	@Test
	public void testMovesWithSingleMoveShouldNotCreateUnion() {
		Abstract3dModel moved = testSubject.moves(new Coords3d(10.0, 20.0, 30.0));
		Assert.assertNotEquals(Union.class, moved.getClass());
	}
	
	@Test
	public void movesWithEmptyListDoesNothing() {
		Abstract3dModel moved = testSubject.moves(Collections.<Coords3d>emptyList());
		assertEqualsWithoutWhiteSpaces("(empty)", moved);
	}
	
	@Test
	public void testMovesWithDebug() {
		Abstract3dModel moved = testSubject
				.moves(new Coords3d(10.0, 20.0, 30.0), new Coords3d(30.0, 10.0, 20.0))
				.debug();
		assertEqualsWithoutWhiteSpaces("# union(){translate([10,20,30])(empty)translate([30,10,20])(empty)}", 
				moved);
	}

	@Test
	public void testRotate() {
		Abstract3dModel ts = testSubject.rotate(new Angles3d(10.0, 20.0, 30.0));
		assertEqualsWithoutWhiteSpaces("rotate([10,20,30]) (empty)", ts);
	}
	
	@Test
	public void testRotates() {
		Abstract3dModel ts = testSubject.rotates(new Angles3d(10.0, 20.0, 30.0), new Angles3d(30.0, 20.0, 10.0));
		assertEqualsWithoutWhiteSpaces("union(){rotate([10,20,30]) (empty) rotate([30,20,10]) (empty)}", ts);
	}
	
	@Test
	public void testRotatesWithEmpty() {
		Abstract3dModel ts = testSubject.rotates(Collections.<Angles3d>emptyList());
		Assert.assertSame(testSubject, ts);
	}
	
	@Test
	public void testMoveAndRotate() {
		Abstract3dModel ts = testSubject
				.rotate(new Angles3d(10.0, 20.0, 30.0))
				.move(new Coords3d(30.0, 10.0, 20.0));
		assertEqualsWithoutWhiteSpaces("translate([30,10,20]) rotate([10,20,30]) (empty)", ts);
	}
	
	@Test
	public void testRotateAndMove() {
		Abstract3dModel ts = testSubject
				.move(Coords3d.yOnly(30.0))
				.rotate(Angles3d.ROTATE_PLUS_X);
		assertEqualsWithoutWhiteSpaces("translate([0,0,30]) rotate([90,0,0]) (empty)", ts);
	}
	
	@Test
	public void testMoveRotateAndMove() {
		Abstract3dModel ts = testSubject
				.move(Coords3d.yOnly(30.0))
				.rotate(Angles3d.ROTATE_PLUS_X)
				.move(new Coords3d(30.0, 10.0, 20.0));
		assertEqualsWithoutWhiteSpaces("translate([30,10,50]) rotate([90,0,0]) (empty)", ts);
	}
	
	@Test
	public void testMovesAndRotate() {
		Abstract3dModel moved = testSubject
				.rotate(new Angles3d(10.0, 20.0, 30.0))
				.moves(new Coords3d(10.0, 20.0, 30.0), new Coords3d(30.0, 10.0, 20.0));
		assertEqualsWithoutWhiteSpaces("union(){translate([10,20,30]) rotate([10,20,30]) (empty) translate([30,10,20]) rotate([10,20,30]) (empty)}", 
				moved);
	}
	
	@Test
	public void testAlignTop() {
		Cube cube1 = new Cube(30.0);
		Abstract3dModel ts = testSubject.align(Side.TOP_OUT, cube1);
		// moves up with half of the cube -> 30/2=15 + 10, because the testSubject's bottom is at that point
		assertEqualsWithoutWhiteSpaces("translate([0, 0, 25]) (empty)", ts);
	}
	
	@Test
	public void testAlignBottom() {
		Cube cube1 = new Cube(30.0);
		Abstract3dModel ts = testSubject.align(Side.BOTTOM_OUT, cube1);
		// moves down with half of the cube -> -30/2=-15 - 15, because the testSubject's top is at that point
		assertEqualsWithoutWhiteSpaces("translate([0, 0, -30]) (empty)", ts);
	}
	
	@Test
	public void testAlignRight() {
		Cube cube1 = new Cube(30.0);
		Abstract3dModel ts = testSubject.align(Side.RIGHT_OUT, cube1);
		// moves right with half of the cube -> 30/2=15 + 10, because the testSubject's left side is at that point
		assertEqualsWithoutWhiteSpaces("translate([25, 0, 0]) (empty)", ts);
	}
	
	@Test
	public void testAlignLeft() {
		Cube cube1 = new Cube(30.0);
		Abstract3dModel ts = testSubject.align(Side.LEFT_OUT, cube1);
		// moves right with half of the cube -> -30/2=-15 - 15, because the testSubject's top is at that point
		assertEqualsWithoutWhiteSpaces("translate([-30, 0, 0]) (empty)", ts);
	}
	
	@Test
	public void testAlignBack() {
		Cube cube1 = new Cube(30.0);
		Abstract3dModel ts = testSubject.align(Side.BACK_OUT, cube1);
		// moves back with half of the cube -> 30/2=15 + 10, because the testSubject's front side is at that point
		assertEqualsWithoutWhiteSpaces("translate([0, 25, 0]) (empty)", ts);
	}
	
	@Test
	public void testAlignFront() {
		Cube cube1 = new Cube(30.0);
		Abstract3dModel ts = testSubject.align(Side.FRONT_OUT, cube1);
		// moves front with half of the cube -> -30/2=-15 - 15, because the testSubject's back is at that point
		assertEqualsWithoutWhiteSpaces("translate([0, -30, 0]) (empty)", ts);
	}
	
	@Test
	public void addModelShouldResultUnion() {
		assertEqualsWithoutWhiteSpaces("union() {(empty) (added)}",
				testSubject.addModel(new Test3dModel("(added)")));
	}
	
	@Test
	public void addModelShouldReturnNewObject() {
		Assert.assertNotSame(testSubject, testSubject.addModel(new Test3dModel("(added)")));
	}
	
	@Test
	public void addModelToShouldResultUnion() {
		assertEqualsWithoutWhiteSpaces("union() {(empty) translate("+Coords3d.zOnly(MAX_BOUND-MIN_BOUND)+")(added)}",
				testSubject.addModelTo(Side.TOP_OUT, new Test3dModel("(added)", BOUNDARIES)));
	}
	
	@Test
	public void addModelToShouldReturnNewObject() {
		Abstract3dModel newUnion = testSubject.addModelTo(Side.TOP_OUT, new Test3dModel("(added)", 
				new Boundaries3d(new Coords3d(-5, -10, -20), new Coords3d(5, 10, 20))));
		Assert.assertNotSame(testSubject, newUnion);
	}
	
	@Test
	public void subtractModelShouldResultDifference() {
		assertEqualsWithoutWhiteSpaces("difference() {(empty) (added)}",
				testSubject.subtractModel(new Test3dModel("(added)")));
	}
	
	@Test
	public void subtrackModelShouldReturnNewObject() {
		Assert.assertNotSame(testSubject, testSubject.subtractModel(new Test3dModel("(added)")));
	}
}
