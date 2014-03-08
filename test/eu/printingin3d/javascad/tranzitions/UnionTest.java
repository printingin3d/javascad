package eu.printingin3d.javascad.tranzitions;

import static eu.printingin3d.javascad.testutils.AssertEx.assertDoubleEquals;
import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.testutils.TestModel;

public class UnionTest {
	
	@Before
	public void init() {
		Language.OpenSCAD.setCurrent();
	}

	@Test
	public void testUnion1() {
		Union union = new Union(new TestModel("(model1)"), new TestModel("(model2)"));
		assertEqualsWithoutWhiteSpaces("union() {(model1) (model2)}", union.toScad());
	}
	
	@Test
	public void testUnion2() {
		Union union = new Union(Arrays.<Abstract3dModel>asList(new TestModel("(model1)"), new TestModel("(model2)")));
		assertEqualsWithoutWhiteSpaces("union() {(model1) (model2)}", union.toScad());
	}
	
	@Test
	public void testUnion1PovRay() {
		Language.POVRay.setCurrent();
		
		Union union = new Union(new TestModel("(model1)"), new TestModel("(model2)"));
		assertEqualsWithoutWhiteSpaces("union {(model1) (model2) #attributes }", union.innerToScad());
	}
	
	@Test
	public void testUnion2PovRay() {
		Language.POVRay.setCurrent();
		
		Union union = new Union(Arrays.<Abstract3dModel>asList(new TestModel("(model1)"), new TestModel("(model2)")));
		assertEqualsWithoutWhiteSpaces("union {(model1) (model2) #attributes }", union.innerToScad());
	}
	
	@Test
	public void testUnionWithNullList() {
		List<Abstract3dModel> models = null;
		Union union = new Union(models);
		assertEqualsWithoutWhiteSpaces("", union.toScad());
	}
	
	@Test
	public void testUnionWithNullListPovRay() {
		Language.POVRay.setCurrent();
		
		List<Abstract3dModel> models = null;
		Union union = new Union(models);
		assertEqualsWithoutWhiteSpaces("", union.toScad());
	}
	
	@Test
	public void unionShouldHandleNullElementsInTheList() {
		Union union = new Union(Arrays.<Abstract3dModel>asList(
				new TestModel("(model1)"), 
				null, 
				new TestModel("(model2)")));
		assertEqualsWithoutWhiteSpaces("union() {(model1) (model2)}", union.toScad());
	}
	
	@Test
	public void unionShouldHandleNullElementsInTheListPovRay() {
		Language.POVRay.setCurrent();
		
		Union union = new Union(Arrays.<Abstract3dModel>asList(
				new TestModel("(model1)"), 
				null, 
				new TestModel("(model2)")));
		assertEqualsWithoutWhiteSpaces("union {(model1) (model2) #attributes}", union.innerToScad());
	}
	
	@Test
	public void testUnionWithOnlyOneModel() {
		Union union = new Union(Arrays.<Abstract3dModel>asList(new TestModel("(model1)")));
		assertEqualsWithoutWhiteSpaces("(model1)", union.toScad());
	}
	
	@Test
	public void testUnionWithEmptyList() {
		Union union = new Union(Collections.<Abstract3dModel>emptyList());
		assertEqualsWithoutWhiteSpaces("", union.toScad());
	}

	@Test
	public void boundariesShouldReturnZeroBoundary() {
		Union union = new Union(Collections.<Abstract3dModel>emptyList());
		Boundary b = union.getBoundaries().getZ();
		assertDoubleEquals(0.0, b.getMin());
		assertDoubleEquals(0.0, b.getMax());
	}
	
	@Test
	public void boundariesShouldBeTheLowestAndHighestValueOfTheList() {
		TestModel model1 = new TestModel("(model1)", new Boundaries3d(
				new Boundary(10.2, 50.8), 
				new Boundary(10.2, 50.8), 
				new Boundary(10.2, 50.8)));
		TestModel model2 = new TestModel("(model2)", new Boundaries3d(
				new Boundary(20.9, 51.1), 
				new Boundary(20.9, 51.1), 
				new Boundary(20.9, 51.1)));
		Union union = new Union(Arrays.<Abstract3dModel>asList(model1, model2));
		Boundaries3d boundaries = union.getBoundaries();
		
		assertDoubleEquals(10.2, boundaries.getX().getMin());
		assertDoubleEquals(51.1, boundaries.getX().getMax());
		assertDoubleEquals(10.2, boundaries.getY().getMin());
		assertDoubleEquals(51.1, boundaries.getY().getMax());
		assertDoubleEquals(10.2, boundaries.getZ().getMin());
		assertDoubleEquals(51.1, boundaries.getZ().getMax());
	}
}
