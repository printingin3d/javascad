package eu.printingin3d.javascad.models2d;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.enums.PointRelation;
import eu.printingin3d.javascad.tranzitions2d.Union;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

public class Area2dTest {
	@Test
	public void twoSeparateCirclesShouldBeDistinct() {
		for (Area2d lc1 : new Circle(4).getPointCircle(FacetGenerationContext.DEFAULT)) {
			for (Area2d lc2 : new Circle(4).move(Coords2d.xOnly(10)).getPointCircle(FacetGenerationContext.DEFAULT)) {
				Assert.assertTrue(lc1.isDistinct(lc2));
			}
		}
	}
	
	@Test
	public void touchingCirclesShouldNotBeDistinct() {
		for (Area2d lc1 : new Circle(5).getPointCircle(FacetGenerationContext.DEFAULT)) {
			for (Area2d lc2 : new Circle(5).move(Coords2d.xOnly(10)).getPointCircle(FacetGenerationContext.DEFAULT)) {
				Assert.assertFalse(lc1.isDistinct(lc2));
			}
		}
	}
	
	@Test
	public void trivialIsInsideTest() {
		for (Area2d lc : new Circle(5).getPointCircle(FacetGenerationContext.DEFAULT)) {
			Assert.assertEquals(PointRelation.INSIDE, lc.calculatePointRelation(Coords2d.ZERO));
		}
	}

	@Test
	public void trivialIsInsideTest2() {
		Abstract2dModel union = new Union(Arrays.asList(
				new Circle(5).move(Coords2d.xOnly(-5)),
				new Circle(5), 
				new Circle(5).move(Coords2d.xOnly(5))));

		FacetGenerationContext context = new FacetGenerationContext(null, null, 0);
		context.setFsAndFa(2, 30);
		
		for (Area2d lc : union.getPointCircle(context)) {
			Assert.assertEquals(PointRelation.INSIDE, lc.calculatePointRelation(new Coords2d(4.99999999999999, -8.881784197001252E-16)));
		}
	}
	
	@Test
	public void testIsInsideWithYEqualsBorder() {
		Area2d area = new Area2d(Arrays.asList(
				new Coords2d(-1.5, -1.5),
				new Coords2d(+1.5, -1.5),
				new Coords2d(+1.5, +8.5),
				new Coords2d(-1.5, +8.5)
			));
		
		Assert.assertEquals(PointRelation.OUTSIDE, area.calculatePointRelation(new Coords2d(5, -1.5)));
	}
	
	@Test
	public void testIsInsideWithOnBorderPoint() {
		Area2d area = new Area2d(Arrays.asList(
				new Coords2d(-1.5, -1.5),
				new Coords2d(+1.5, -1.5),
				new Coords2d(+1.5, +8.5),
				new Coords2d(-1.5, +8.5)
				));
		
		Assert.assertEquals(PointRelation.BORDER, area.calculatePointRelation(new Coords2d(1, -1.5)));
	}
	
	@Test
	public void ifFirstAndLastPointsAreTheSameLastShouldBeOmitted() {
		Area2d area = new Area2d(Arrays.asList(
				new Coords2d(-1.5, -1.5),
				new Coords2d(+1.5, -1.5),
				new Coords2d(+1.5, +8.5),
				new Coords2d(-1.5, +8.5),
				new Coords2d(-1.5, -1.5)
				));
		Assert.assertEquals(4, area.size());
	}
	
	@Test
	public void inLinePointsShouldBeOmitted() {
		Area2d area = new Area2d(Arrays.asList(
				new Coords2d(-1.5, -1.5),
				new Coords2d(+1.5, -1.5),
				new Coords2d(+1.5, +1.5),
				new Coords2d(+1.5, +5.5),
				new Coords2d(+1.5, +8.5),
				new Coords2d(-1.5, +8.5)
				));
		Assert.assertEquals(4, area.size());
	}
}
