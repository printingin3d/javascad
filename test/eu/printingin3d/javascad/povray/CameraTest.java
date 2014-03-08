package eu.printingin3d.javascad.povray;

import static eu.printingin3d.javascad.testutils.AssertEx.assertMatchToExpressionWithoutWhiteSpaces;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.testutils.RandomUtils;

public class CameraTest {
	@Before
	public void init() {
		Language.POVRay.setCurrent();
	}
	
	@Test
	public void cameraDeclarationShouldStartWithCameraKeyWord() {
		Camera camera = new Camera(RandomUtils.getRandomCoords(), RandomUtils.getRandomCoords());
		
		assertMatchToExpressionWithoutWhiteSpaces("camera \\{.*\\}", camera.toScad());
	}
	
	@Test(expected = IllegalValueException.class)
	public void nullLocationShouldBeForbidden() {
		new Camera(null, RandomUtils.getRandomCoords());
	}
	
	@Test(expected = IllegalValueException.class)
	public void nullLookAtValueShouldBeForbidden() {
		new Camera(RandomUtils.getRandomCoords(), null);
	}

	@Test
	public void nonPovRayLanguageShouldResultEmptyString() {
		Language.OpenSCAD.setCurrent();
		
		Camera camera = new Camera(RandomUtils.getRandomCoords(), RandomUtils.getRandomCoords());
		
		assertMatchToExpressionWithoutWhiteSpaces("", camera.toScad());
	}
	
	@Test
	public void cameraDeclarationShouldContainLocation() {
		Camera camera = new Camera(new Coords3d(10.0, 15.5, 20.0), RandomUtils.getRandomCoords());
		
		assertMatchToExpressionWithoutWhiteSpaces("camera \\{.*location <10, 15.5, 20>.*\\}", camera.toScad());
	}
	
	@Test
	public void cameraDeclarationShouldContainLookAt() {
		Camera camera = new Camera(RandomUtils.getRandomCoords(), new Coords3d(10.0, 15.5, 20.0));
		
		assertMatchToExpressionWithoutWhiteSpaces("camera \\{.*look_at <10, 15.5, 20>.*\\}", camera.toScad());
	}
	
	@Test
	public void defaultCameraDeclarationShouldContainZeroAperture() {
		Camera camera = new Camera(RandomUtils.getRandomCoords(), new Coords3d(10.0, 15.5, 20.0));
		
		assertMatchToExpressionWithoutWhiteSpaces("camera \\{.*aperture 0.*\\}", camera.toScad());
	}
	
	@Test
	public void fastFocalBlurShouldSetAperture() {
		Camera camera = new Camera(RandomUtils.getRandomCoords(), new Coords3d(10.0, 15.5, 20.0));
		camera.setFocalBlur(FocalBlur.FAST);
		
		assertMatchToExpressionWithoutWhiteSpaces("camera \\{.*aperture 2\\.25.*\\}", camera.toScad());
	}
	
}
