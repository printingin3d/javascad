package eu.printingin3d.javascad.tranform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.tranzitions.Direction;

public class TransformationFactoryTest {
	@Test
	public void testIdent() {
		ITransformation testSubject = TransformationFactory.getIdentityMatrix();
		
		assertFalse(testSubject.isMirror());
		assertEquals(new Coords3d(20, 30, 40), testSubject.transform(new Coords3d(20, 30, 40)));
	}
	
	@Test
	public void testMirrorX() {
		ITransformation testSubject = TransformationFactory.getMirrorMatrix(Direction.X);
		
		assertTrue(testSubject.isMirror());
		assertEquals(new Coords3d(-20, 30, 40), testSubject.transform(new Coords3d(20, 30, 40)));
	}
	
	@Test
	public void testMirrorY() {
		ITransformation testSubject = TransformationFactory.getMirrorMatrix(Direction.Y);
		
		assertTrue(testSubject.isMirror());
		assertEquals(new Coords3d(20, -30, 40), testSubject.transform(new Coords3d(20, 30, 40)));
	}
	
	@Test
	public void testMirrorZ() {
		ITransformation testSubject = TransformationFactory.getMirrorMatrix(Direction.Z);
		
		assertTrue(testSubject.isMirror());
		assertEquals(new Coords3d(20, 30, -40), testSubject.transform(new Coords3d(20, 30, 40)));
	}
	
	@Test
	public void testTranslate() {
		ITransformation testSubject = TransformationFactory.getTranlationMatrix(new Coords3d(20, 30, 40));
		
		assertFalse(testSubject.isMirror());
		assertEquals(new Coords3d(30, 50, 70), testSubject.transform(new Coords3d(10, 20, 30)));
	}
	
	@Test
	public void testScale1() {
		ITransformation testSubject = TransformationFactory.getScaleMatrix(5, 10, 20);
		
		assertFalse(testSubject.isMirror());
		assertEquals(new Coords3d(50, 200, 600), testSubject.transform(new Coords3d(10, 20, 30)));
	}
	
	@Test
	public void testScale2() {
		ITransformation testSubject = TransformationFactory.getScaleMatrix(new Coords3d(10, 5, 2));
		
		assertFalse(testSubject.isMirror());
		assertEquals(new Coords3d(100, 100, 60), testSubject.transform(new Coords3d(10, 20, 30)));
	}
}
