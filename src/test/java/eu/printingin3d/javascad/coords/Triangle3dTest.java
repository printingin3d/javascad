package eu.printingin3d.javascad.coords;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Triangle3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.testutils.RandomUtils;

public class Triangle3dTest {

	@Test(expected = IllegalValueException.class)
	public void point1ShouldNotBeNull1() {
		new Triangle3d(null, RandomUtils.getRandomCoords(), RandomUtils.getRandomCoords());
	}
	
	@Test(expected = IllegalValueException.class)
	public void point1ShouldNotBeNull2() {
		new Triangle3d(RandomUtils.getRandomCoords(), null, RandomUtils.getRandomCoords());
	}
	
	@Test(expected = IllegalValueException.class)
	public void point1ShouldNotBeNull3() {
		new Triangle3d(RandomUtils.getRandomCoords(), RandomUtils.getRandomCoords(), null);
	}

	@Test
	public void getPointsShouldContainAllThreePoints() {
		Coords3d c1 = RandomUtils.getRandomCoords();
		Coords3d c2 = RandomUtils.getRandomCoords();
		Coords3d c3 = RandomUtils.getRandomCoords();
		List<Coords3d> points = new Triangle3d(c1, c2, c3).getPoints();
		
		assertTrue(points.contains(c1));
		assertTrue(points.contains(c2));
		assertTrue(points.contains(c3));
	}
	
	@Test(expected = IllegalValueException.class)
	public void toTriangleStringShouldThrowExceptionIfAPointDoesNotExistsInTheList() {
		List<Coords3d> points = Arrays.asList(
				RandomUtils.getRandomCoords(),
				RandomUtils.getRandomCoords(),
				RandomUtils.getRandomCoords());
		
		new Triangle3d(RandomUtils.getRandomCoords(), RandomUtils.getRandomCoords(), RandomUtils.getRandomCoords()).toTriangleString(points);
	}	
	
	@Test
	public void toTriangleStringShouldResultProperStringIfEverythingIsOkay() {
		Coords3d c1 = RandomUtils.getRandomCoords();
		Coords3d c2 = RandomUtils.getRandomCoords();
		Coords3d c3 = RandomUtils.getRandomCoords();
		
		List<Coords3d> points = Arrays.asList(
				c1,
				RandomUtils.getRandomCoords(),
				c2,
				RandomUtils.getRandomCoords(),
				RandomUtils.getRandomCoords(),
				c3);
		
		assertEqualsWithoutWhiteSpaces("[0, 2, 5]", new Triangle3d(c1, c2, c3).toTriangleString(points));
	}	
}
