package eu.printingin3d.javascad.tranzitions2d;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.models2d.Abstract2dModel;
import eu.printingin3d.javascad.testutils.AssertEx;
import eu.printingin3d.javascad.testutils.Test2dModel;

public class UnionTest {
	@Test
	public void shouldBeEmptyIfEmptyListWasGiven() {
		assertEquals(SCAD.EMPTY, new Union(Collections.<Abstract2dModel>emptyList()).toScad(ColorHandlingContext.DEFAULT));
	}

	@Test
	public void shouldBeEmptyIfListOfEmptiesWasGiven() {
		Abstract2dModel testSubject = new Union(Arrays.<Abstract2dModel>asList(new Test2dModel("", null)))
						.move(Coords2d.xOnly(10));
		assertEquals(SCAD.EMPTY, testSubject
				.toScad(ColorHandlingContext.DEFAULT));
	}
	
	@Test
	public void shouldReturnTheModelIfOnlyOneIsGiven() {
		Abstract2dModel testSubject = new Union(Arrays.<Abstract2dModel>asList(new Test2dModel("(model)", null)));
		assertEquals("(model)", testSubject
				.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
	
	@Test
	public void shouldReturnUnionIfListOfModelsWasGiven() {
		Abstract2dModel testSubject = new Union(
				Arrays.<Abstract2dModel>asList(new Test2dModel("(model1)", null), new Test2dModel("(model2)", null)));
		AssertEx.assertEqualsWithoutWhiteSpaces("union() {(model1)(model2)}", testSubject
				.toScad(ColorHandlingContext.DEFAULT).getScad());
	}
}
