package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.coords.BoundaryTest.assertBoundaryEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.enums.AlignType;
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.testutils.TestModel;

public class Abstract3dModelAlignTest {
	private Abstract3dModel base;
	private Boundaries3d testSubjectsBoundaries;
	private Abstract3dModel testSubject;
	
	@Before
	public void init() {
		testSubjectsBoundaries = RandomUtils.getRandomBoundaries();
		base = new TestModel("(base)", RandomUtils.getRandomBoundaries());
		testSubject = new TestModel("(subject)", testSubjectsBoundaries);
	}
	
	private void assertXDidNotChanged() {
		assertBoundaryEquals(testSubjectsBoundaries.getX(), testSubject.getBoundaries().getX());
	}
	
	private void assertYDidNotChanged() {
		assertBoundaryEquals(testSubjectsBoundaries.getY(), testSubject.getBoundaries().getY());
	}
	
	private void assertZDidNotChanged() {
		assertBoundaryEquals(testSubjectsBoundaries.getZ(), testSubject.getBoundaries().getZ());
	}
	
	@Test
	public void testAlignTopOutside() {
		testSubject.align(Side.TOP, base, false);
		
		assertXDidNotChanged();
		assertYDidNotChanged();
		assertDoubleEquals(base.getBoundaries().getZ().getMax(), testSubject.getBoundaries().getZ().getMin());
	}
	
	@Test
	public void testAlignTopInside() {
		testSubject.align(Side.TOP, base, true);

		assertXDidNotChanged();
		assertYDidNotChanged();
		assertDoubleEquals(base.getBoundaries().getZ().getMax(), testSubject.getBoundaries().getZ().getMax());
	}
	
	@Test
	public void testAlignBottomOutside() {
		testSubject.align(Side.BOTTOM, base, false);
		
		assertXDidNotChanged();
		assertYDidNotChanged();
		assertDoubleEquals(base.getBoundaries().getZ().getMin(), testSubject.getBoundaries().getZ().getMax());
	}
	
	@Test
	public void testAlignBottomInside() {
		testSubject.align(Side.BOTTOM, base, true);
		
		assertXDidNotChanged();
		assertYDidNotChanged();
		assertDoubleEquals(base.getBoundaries().getZ().getMin(), testSubject.getBoundaries().getZ().getMin());
	}
	
	@Test
	public void testAlignLeftOutside() {
		testSubject.align(Side.LEFT, base, false);
		
		assertDoubleEquals(base.getBoundaries().getX().getMin(), testSubject.getBoundaries().getX().getMax());
		assertYDidNotChanged();
		assertZDidNotChanged();
	}
	
	@Test
	public void testAlignLeftInside() {
		testSubject.align(Side.LEFT, base, true);
		
		assertDoubleEquals(base.getBoundaries().getX().getMin(), testSubject.getBoundaries().getX().getMin());
		assertYDidNotChanged();
		assertZDidNotChanged();
	}
	
	@Test
	public void testAlignRightOutside() {
		testSubject.align(Side.RIGHT, base, false);
		
		assertDoubleEquals(base.getBoundaries().getX().getMax(), testSubject.getBoundaries().getX().getMin());
		assertYDidNotChanged();
		assertZDidNotChanged();
	}
	
	@Test
	public void testAlignRightInside() {
		testSubject.align(Side.RIGHT, base, true);
		
		assertDoubleEquals(base.getBoundaries().getX().getMax(), testSubject.getBoundaries().getX().getMax());
		assertYDidNotChanged();
		assertZDidNotChanged();
	}
	
	@Test
	public void testAlignFrontOutside() {
		testSubject.align(Side.FRONT, base, false);
		
		assertXDidNotChanged();
		assertDoubleEquals(base.getBoundaries().getY().getMin(), testSubject.getBoundaries().getY().getMax());
		assertZDidNotChanged();
	}
	
	@Test
	public void testAlignFrontInside() {
		testSubject.align(Side.FRONT, base, true);
		
		assertXDidNotChanged();
		assertDoubleEquals(base.getBoundaries().getY().getMin(), testSubject.getBoundaries().getY().getMin());
		assertZDidNotChanged();
	}
	
	@Test
	public void testAlignBackOutside() {
		testSubject.align(Side.BACK, base, false);
		
		assertXDidNotChanged();
		assertDoubleEquals(base.getBoundaries().getY().getMax(), testSubject.getBoundaries().getY().getMin());
		assertZDidNotChanged();
	}
	
	@Test
	public void testAlignBackInside() {
		testSubject.align(Side.BACK, base, true);
		
		assertXDidNotChanged();
		assertDoubleEquals(base.getBoundaries().getY().getMax(), testSubject.getBoundaries().getY().getMax());
		assertZDidNotChanged();
	}
	
	@Test
	public void testAlignCenterInside() {
		testSubject.align(new Side(AlignType.CENTER, AlignType.CENTER, AlignType.CENTER), base, true);
		
		assertDoubleEquals(base.getBoundaries().getX().getMiddle(), testSubject.getBoundaries().getX().getMiddle());
		assertDoubleEquals(base.getBoundaries().getY().getMiddle(), testSubject.getBoundaries().getY().getMiddle());
		assertDoubleEquals(base.getBoundaries().getZ().getMiddle(), testSubject.getBoundaries().getZ().getMiddle());
	}
	
	@Test
	public void testAlignCenterOutside() {
		testSubject.align(new Side(AlignType.CENTER, AlignType.CENTER, AlignType.CENTER), base, false);
		
		assertDoubleEquals(base.getBoundaries().getX().getMiddle(), testSubject.getBoundaries().getX().getMiddle());
		assertDoubleEquals(base.getBoundaries().getY().getMiddle(), testSubject.getBoundaries().getY().getMiddle());
		assertDoubleEquals(base.getBoundaries().getZ().getMiddle(), testSubject.getBoundaries().getZ().getMiddle());
	}
	
}
