package eu.printingin3d.javascad.testutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.hamcrest.CustomTypeSafeMatcher;

public class AssertEx {
	private static final double EPSILON = 0.0001;
	
	public static void assertEqualsWithoutWhiteSpaces(final String expected, String actual) {
		assertThat(actual, new CustomTypeSafeMatcher<String>(expected) {
			
			@Override
			protected boolean matchesSafely(String item) {
				return item.replaceAll("\\s", "").equals(expected.replaceAll("\\s", ""));
			}
		});
	}
	
	public static void assertMatchToExpressionWithoutWhiteSpaces(final String regex, String actual) {
		assertThat(actual, new CustomTypeSafeMatcher<String>(regex) {
			
			@Override
			protected boolean matchesSafely(String item) {
				return item.replaceAll("\\s", "").matches(regex.replaceAll("\\s", ""));
			}
		});
	}
	
	public static void assertDoubleEquals(double expected, double actual) {
		assertEquals(expected, actual, EPSILON);
	}
	
	public static void assertDoubleEquals(String message, double expected, double actual) {
		assertEquals(message, expected, actual, EPSILON);
	}
}
