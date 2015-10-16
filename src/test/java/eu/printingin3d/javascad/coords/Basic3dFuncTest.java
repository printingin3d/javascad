package eu.printingin3d.javascad.coords;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static eu.printingin3d.javascad.testutils.RandomUtils.getRandomDouble;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import eu.printingin3d.javascad.testutils.AssertEx;

public class Basic3dFuncTest {
	
	public static class TestBasic3d extends Basic3dFunc<TestBasic3d> {
		public TestBasic3d(double x, double y, double z) {
			super(x, y, z);
		}

		@Override
		protected TestBasic3d create(double x, double y, double z) {
			return new TestBasic3d(x, y, z);
		}
	}

	@Test
	public void testInverse() {
		TestBasic3d start = new TestBasic3d(
				getRandomDouble(-1000, +1000), 
				getRandomDouble(-1000, +1000), 
				getRandomDouble(-1000, +1000));
		TestBasic3d result = start.inverse();
		assertDoubleEquals(-start.x, result.x);
		assertDoubleEquals(-start.y, result.y);
		assertDoubleEquals(-start.z, result.z);
	}
	
	@Test
	public void testAdd() {
		TestBasic3d a = new TestBasic3d(
				getRandomDouble(-1000, +1000), 
				getRandomDouble(-1000, +1000), 
				getRandomDouble(-1000, +1000));
		TestBasic3d b = new TestBasic3d(
				getRandomDouble(-1000, +1000), 
				getRandomDouble(-1000, +1000), 
				getRandomDouble(-1000, +1000));
		Basic3dFunc<?> result = a.add(b);

		assertDoubleEquals(a.x+b.x, result.x);
		assertDoubleEquals(a.y+b.y, result.y);
		assertDoubleEquals(a.z+b.z, result.z);
		
		// should create a new object
		assertNotSame(a, result);
		assertNotSame(b, result);
	}

	@Test
	public void testBasicDistances() {
		AssertEx.assertDoubleEquals(1, Coords3d.xOnly(1).distance(Coords3d.ZERO));
		AssertEx.assertDoubleEquals(1, Coords3d.yOnly(1).distance(Coords3d.ZERO));
		AssertEx.assertDoubleEquals(1, Coords3d.zOnly(1).distance(Coords3d.ZERO));
	}
	
	@Test
	public void testComplexDistances() {
		AssertEx.assertDoubleEquals(5, new Coords3d(3, 4, 0).distance(Coords3d.ZERO));
		AssertEx.assertDoubleEquals(5, new Coords3d(3, 0, 4).distance(Coords3d.ZERO));
		AssertEx.assertDoubleEquals(5, new Coords3d(0, 3, 4).distance(Coords3d.ZERO));
	}
}
