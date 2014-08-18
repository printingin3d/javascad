package eu.printingin3d.javascad.tranform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Coords3d;

public class TransformationMatrixTest {
	@Test
	public void testIdent() {
		ITransformation testSubject = new TransformationMatrix(new double[]{
				1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
		});
		
		assertFalse(testSubject.isMirror());
		assertEquals(new Coords3d(20, 30, 40), testSubject.transform(new Coords3d(20, 30, 40)));
	}
	
}
