package eu.printingin3d.javascad.tranzitions.slicer;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.testutils.TestModel;
import eu.printingin3d.javascad.tranzitions.Direction;

public class CoverTest {
	private static final Abstract3dModel TEST_MODEL = new TestModel("(model)",
			new Boundaries3d(new Boundary(0.0, 24.0), new Boundary(-12.0, 36.0), new Boundary(6.0, 18.0)));

	@Test
	public void coverShouldBeAroundTheModelOnOtherSides_X() {
		Cover cover = new Cover(TEST_MODEL, Direction.X, 0.5, false);
		
		Assert.assertTrue(cover.getBoundaries().getY().getMin()<TEST_MODEL.getBoundaries().getY().getMin());
		Assert.assertTrue(cover.getBoundaries().getZ().getMax()>TEST_MODEL.getBoundaries().getZ().getMax());
	}
	
	@Test
	public void coverShouldBeBeforeTheModelIfLower_X() {
		Cover cover = new Cover(TEST_MODEL, Direction.X, 0.5, true);
		
		Assert.assertTrue(cover.getBoundaries().getX().getMin()<TEST_MODEL.getBoundaries().getX().getMin());
	}
	
	@Test
	public void coverShouldBeAfterTheModelIfUpper_X() {
		Cover cover = new Cover(TEST_MODEL, Direction.X, 0.5, false);
		
		Assert.assertTrue(cover.getBoundaries().getX().getMax()>TEST_MODEL.getBoundaries().getX().getMax());
	}
	
	@Test
	public void coverShouldBeAroundTheModelOnOtherSides_Y() {
		Cover cover = new Cover(TEST_MODEL, Direction.Y, 0.5, false);
		
		Assert.assertTrue(cover.getBoundaries().getX().getMin()<TEST_MODEL.getBoundaries().getX().getMin());
		Assert.assertTrue(cover.getBoundaries().getZ().getMax()>TEST_MODEL.getBoundaries().getZ().getMax());
	}
	
	
	@Test
	public void coverShouldBeBeforeTheModelIfLower_Y() {
		Cover cover = new Cover(TEST_MODEL, Direction.Y, 0.5, true);
		
		Assert.assertTrue(cover.getBoundaries().getY().getMin()<TEST_MODEL.getBoundaries().getY().getMin());
	}
	
	@Test
	public void coverShouldBeAfterTheModelIfUpper_Y() {
		Cover cover = new Cover(TEST_MODEL, Direction.Y, 0.5, false);
		
		Assert.assertTrue(cover.getBoundaries().getY().getMax()>TEST_MODEL.getBoundaries().getY().getMax());
	}
	
	@Test
	public void coverShouldBeAroundTheModelOnOtherSides_Z() {
		Cover cover = new Cover(TEST_MODEL, Direction.Z, 0.5, false);
		
		Assert.assertTrue(cover.getBoundaries().getX().getMax()>TEST_MODEL.getBoundaries().getX().getMax());
		Assert.assertTrue(cover.getBoundaries().getY().getMin()<TEST_MODEL.getBoundaries().getY().getMin());
	}
	
	
	@Test
	public void coverShouldBeBeforeTheModelIfLower_Z() {
		Cover cover = new Cover(TEST_MODEL, Direction.Z, 0.5, true);
		
		Assert.assertTrue(cover.getBoundaries().getZ().getMin()<TEST_MODEL.getBoundaries().getZ().getMin());
	}
	
	@Test
	public void coverShouldBeAfterTheModelIfUpper_Z() {
		Cover cover = new Cover(TEST_MODEL, Direction.Z, 0.5, false);
		
		Assert.assertTrue(cover.getBoundaries().getZ().getMax()>TEST_MODEL.getBoundaries().getZ().getMax());
	}
}
