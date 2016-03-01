package eu.printingin3d.javascad.coords2d;

import static org.junit.Assert.assertNull;

import org.junit.Assert;
import org.junit.Test;

public class LineSegment2dTest {
	@Test
	public void distinctSegments() {
		assertNull(new LineSegment2d(new Coords2d(10, 20), new Coords2d(0, 10))
				.common(new LineSegment2d(new Coords2d(10, 30), new Coords2d(3, 0))));
	}
	
	@Test
	public void afterEachOther() {
		assertNull(new LineSegment2d(new Coords2d(10, 10), new Coords2d(20, 10))
				.common(new LineSegment2d(new Coords2d(20, 10), new Coords2d(30, 10))));
	}
	
	@Test
	public void afterEachOtherWithGap() {
		assertNull(new LineSegment2d(new Coords2d(10, 10), new Coords2d(20, 10))
				.common(new LineSegment2d(new Coords2d(30, 10), new Coords2d(40, 10))));
	}
	
	@Test
	public void thisContainsOther() {
		Assert.assertEquals(new LineSegment2d(new Coords2d(20, 20), new Coords2d(30, 30)),
				new LineSegment2d(new Coords2d(10, 10), new Coords2d(40, 40))
					.common(new LineSegment2d(new Coords2d(20, 20), new Coords2d(30, 30))));
	}
	
	@Test
	public void otherContainsThis() {
		Assert.assertEquals(new LineSegment2d(new Coords2d(20, 20), new Coords2d(30, 30)),
				new LineSegment2d(new Coords2d(20, 20), new Coords2d(30, 30))
					.common(new LineSegment2d(new Coords2d(10, 10), new Coords2d(40, 40))));
	}
}
