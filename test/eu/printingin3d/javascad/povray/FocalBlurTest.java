package eu.printingin3d.javascad.povray;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import org.junit.Test;

public class FocalBlurTest {

	@Test
	public void offTest() {
		assertEqualsWithoutWhiteSpaces("aperture 0", FocalBlur.OFF.toString());
	}
	
	@Test
	public void fastTest() {
		assertEqualsWithoutWhiteSpaces("aperture 2.25 focal_point <0, 0, 0> blur_samples 7 confidence 0.5 variance 1/64", FocalBlur.FAST.toString());
	}
	
	@Test
	public void normalTest() {
		assertEqualsWithoutWhiteSpaces("aperture 2.25 focal_point <0, 0, 0> blur_samples 19 confidence 0.9 variance 1/128", FocalBlur.NORMAL.toString());
	}
	
	@Test
	public void highTest() {
		assertEqualsWithoutWhiteSpaces("aperture 2.25 focal_point <0, 0, 0> blur_samples 37 confidence 0.975 variance 1/255", FocalBlur.HIGH.toString());
	}
}
