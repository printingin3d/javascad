package eu.printingin3d.javascad.batchtests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.coords2d.Dims2d;
import eu.printingin3d.javascad.models2d.Abstract2dModel;
import eu.printingin3d.javascad.models2d.Circle;
import eu.printingin3d.javascad.models2d.Polygon;
import eu.printingin3d.javascad.models2d.RoundedSquare;
import eu.printingin3d.javascad.models2d.Square;
import eu.printingin3d.javascad.tranzitions2d.Translate;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

@RunWith(Parameterized.class)
public class Abstract2dModelBatchTest {
	private static final Coords3d EXPECTED_NORM = new Coords3d(0, 0, -1);
	private static final double EPSILON = 0.0001;
	private final Abstract2dModel testSubject;

	public Abstract2dModelBatchTest(Abstract2dModel testSubject) {
		this.testSubject = testSubject;
	}
	public static Collection<Abstract2dModel> createTestSubjects() {
		return Arrays.<Abstract2dModel>asList(
				new Circle(5.0),
				new Square(new Dims2d(5, 10)),
				new RoundedSquare(new Dims2d(3, 2), 1),
				new Polygon(Arrays.asList(new Coords2d(10, 10), new Coords2d(5, 5), new Coords2d(10, 5))),
				new Translate(new Circle(2.5), new Coords2d(10, 12))
			);
	}
	
	@Parameterized.Parameters(name="{0}")
	public static Collection<Object[]> testCases() {
		List<Object[]> result = new ArrayList<Object[]>();
		for (Abstract2dModel testCase : createTestSubjects()) {
			result.add(new Object[] {testCase});
		}
		return result;
	}

	@Test
	public void moveShouldReturnWithNewObject() {
		Assert.assertNotSame(testSubject, testSubject.move(new Coords2d(3, 4)));
	}
	
	@Test
	public void moveShouldMoveMiddleOfBoundaries() {
		Boundaries2d before = testSubject.getBoundaries2d();
		Boundaries2d after = testSubject.move(new Coords2d(7, 10)).getBoundaries2d();
		
		Assert.assertEquals(before.getX().getMiddle()+7, after.getX().getMiddle(), EPSILON);
		Assert.assertEquals(before.getY().getMiddle()+10, after.getY().getMiddle(), EPSILON);
	}
	
	@Test
	public void moveShouldNotChangeSizeOfBoundaries() {
		Boundaries2d before = testSubject.getBoundaries2d();
		Boundaries2d after = testSubject.move(new Coords2d(7, 10)).getBoundaries2d();
		
		Assert.assertEquals(before.getX().getSize(), after.getX().getSize(), EPSILON);
		Assert.assertEquals(before.getY().getSize(), after.getY().getSize(), EPSILON);
	}
	
	@Test
	public void testPointsNorm() {
		Coords3d a = null;
    	Coords3d b = null;
    	Coords3d c = null;
		
		for (Coords2d coords : testSubject.getPointCircle(FacetGenerationContext.DEFAULT)) {
			c = b;
			b = a;
			a = coords.withZ(0);
			
			if (a!=null && b!=null && c!=null) {
		    	Coords3d n = b.add(a.inverse()).cross(c.add(a.inverse())).unit();
		    	
		    	Assert.assertEquals(EXPECTED_NORM, n);
			}
		}
	}
	
	@Test
	public void testPointsWithinBoundaries() {
		Boundaries2d boundaries2d = testSubject.getBoundaries2d();
		
		for (Coords2d coords : testSubject.getPointCircle(FacetGenerationContext.DEFAULT)) {
			Assert.assertTrue(boundaries2d.getX().isInside(coords.getX()));
			Assert.assertTrue(boundaries2d.getY().isInside(coords.getY()));
		}
	}
}
