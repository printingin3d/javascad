package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.coords.BoundaryTest.assertBoundaryEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.enums.AlignType;
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.testutils.RandomUtils;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class Abstract3dModelAlignTest {
	private Abstract3dModel base;
	private Boundaries3d testSubjectsBoundaries;
	private Abstract3dModel testSubject;
	
	@Before
	public void init() {
		testSubjectsBoundaries = RandomUtils.getRandomBoundaries();
		base = new Test3dModel("(base)", RandomUtils.getRandomBoundaries());
		testSubject = new Test3dModel("(subject)", testSubjectsBoundaries);
	}
	
	private void assertXDidNotChanged(Abstract3dModel ts) {
		assertBoundaryEquals(testSubjectsBoundaries.getX(), ts.getBoundaries().getX());
	}
	
	private void assertYDidNotChanged(Abstract3dModel ts) {
		assertBoundaryEquals(testSubjectsBoundaries.getY(), ts.getBoundaries().getY());
	}
	
	private void assertZDidNotChanged(Abstract3dModel ts) {
		assertBoundaryEquals(testSubjectsBoundaries.getZ(), ts.getBoundaries().getZ());
	}
	
	@Test
	public void testAlignTopOutside() {
		Abstract3dModel ts = testSubject.align(Side.TOP, base, false);
		
		assertXDidNotChanged(ts);
		assertYDidNotChanged(ts);
		assertDoubleEquals(base.getBoundaries().getZ().getMax(), ts.getBoundaries().getZ().getMin());
	}
	
	@Test
	public void testAlignTopInside() {
		Abstract3dModel ts = testSubject.align(Side.TOP, base, true);

		assertXDidNotChanged(ts);
		assertYDidNotChanged(ts);
		assertDoubleEquals(base.getBoundaries().getZ().getMax(), ts.getBoundaries().getZ().getMax());
	}
	
	@Test
	public void testAlignBottomOutside() {
		Abstract3dModel ts = testSubject.align(Side.BOTTOM, base, false);
		
		assertXDidNotChanged(ts);
		assertYDidNotChanged(ts);
		assertDoubleEquals(base.getBoundaries().getZ().getMin(), ts.getBoundaries().getZ().getMax());
	}
	
	@Test
	public void testAlignBottomInside() {
		Abstract3dModel ts = testSubject.align(Side.BOTTOM, base, true);
		
		assertXDidNotChanged(ts);
		assertYDidNotChanged(ts);
		assertDoubleEquals(base.getBoundaries().getZ().getMin(), ts.getBoundaries().getZ().getMin());
	}
	
	@Test
	public void testAlignLeftOutside() {
		Abstract3dModel ts = testSubject.align(Side.LEFT, base, false);
		
		assertDoubleEquals(base.getBoundaries().getX().getMin(), ts.getBoundaries().getX().getMax());
		assertYDidNotChanged(ts);
		assertZDidNotChanged(ts);
	}
	
	@Test
	public void testAlignLeftInside() {
		Abstract3dModel ts = testSubject.align(Side.LEFT, base, true);
		
		assertDoubleEquals(base.getBoundaries().getX().getMin(), ts.getBoundaries().getX().getMin());
		assertYDidNotChanged(ts);
		assertZDidNotChanged(ts);
	}
	
	@Test
	public void testAlignRightOutside() {
		Abstract3dModel ts = testSubject.align(Side.RIGHT, base, false);
		
		assertDoubleEquals(base.getBoundaries().getX().getMax(), ts.getBoundaries().getX().getMin());
		assertYDidNotChanged(ts);
		assertZDidNotChanged(ts);
	}
	
	@Test
	public void testAlignRightInside() {
		Abstract3dModel ts = testSubject.align(Side.RIGHT, base, true);
		
		assertDoubleEquals(base.getBoundaries().getX().getMax(), ts.getBoundaries().getX().getMax());
		assertYDidNotChanged(ts);
		assertZDidNotChanged(ts);
	}
	
	@Test
	public void testAlignFrontOutside() {
		Abstract3dModel ts = testSubject.align(Side.FRONT, base, false);
		
		assertXDidNotChanged(ts);
		assertDoubleEquals(base.getBoundaries().getY().getMin(), ts.getBoundaries().getY().getMax());
		assertZDidNotChanged(ts);
	}
	
	@Test
	public void testAlignFrontInside() {
		Abstract3dModel ts = testSubject.align(Side.FRONT, base, true);
		
		assertXDidNotChanged(ts);
		assertDoubleEquals(base.getBoundaries().getY().getMin(), ts.getBoundaries().getY().getMin());
		assertZDidNotChanged(ts);
	}
	
	@Test
	public void testAlignBackOutside() {
		Abstract3dModel ts = testSubject.align(Side.BACK, base, false);
		
		assertXDidNotChanged(ts);
		assertDoubleEquals(base.getBoundaries().getY().getMax(), ts.getBoundaries().getY().getMin());
		assertZDidNotChanged(ts);
	}
	
	@Test
	public void testAlignBackInside() {
		Abstract3dModel ts = testSubject.align(Side.BACK, base, true);
		
		assertXDidNotChanged(ts);
		assertDoubleEquals(base.getBoundaries().getY().getMax(), ts.getBoundaries().getY().getMax());
		assertZDidNotChanged(ts);
	}
	
	@Test
	public void testAlignCenterInside() {
		Abstract3dModel ts = testSubject.align(new Side(AlignType.CENTER, AlignType.CENTER, AlignType.CENTER), base, true);
		
		assertDoubleEquals(base.getBoundaries().getX().getMiddle(), ts.getBoundaries().getX().getMiddle());
		assertDoubleEquals(base.getBoundaries().getY().getMiddle(), ts.getBoundaries().getY().getMiddle());
		assertDoubleEquals(base.getBoundaries().getZ().getMiddle(), ts.getBoundaries().getZ().getMiddle());
	}
	
	@Test
	public void testAlignCenterOutside() {
		Abstract3dModel ts = testSubject.align(new Side(AlignType.CENTER, AlignType.CENTER, AlignType.CENTER), base, false);
		
		assertDoubleEquals(base.getBoundaries().getX().getMiddle(), ts.getBoundaries().getX().getMiddle());
		assertDoubleEquals(base.getBoundaries().getY().getMiddle(), ts.getBoundaries().getY().getMiddle());
		assertDoubleEquals(base.getBoundaries().getZ().getMiddle(), ts.getBoundaries().getZ().getMiddle());
	}
	
}
