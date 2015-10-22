package eu.printingin3d.javascad.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import eu.printingin3d.javascad.annotations.ModelPart;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Cube;
import eu.printingin3d.javascad.models.Cylinder;
import eu.printingin3d.javascad.models.IModel;

public class AnnotatedModelProviderTest {
	@Test
	public void testDefaultNames() {
		IModelProvider testSubject = new AnnotatedModelProvider() {
			@ModelPart
			public Abstract3dModel cube() {
				return new Cube(10);
			}
			
			@ModelPart
			public Abstract3dModel cylinder() {
				return new Cylinder(10, 10);
			}
			
			@Override
			public Abstract3dModel getAssembledModel() {
				// we don't use this method in this test
				return null;
			}
		};
		
		List<ModelWithPath> modelsAndPaths = testSubject.getModelsAndPaths();
		
		assertEquals(2, modelsAndPaths.size());
		
		Map<String, IModel> paths = new HashMap<>();
		for (ModelWithPath mwp : modelsAndPaths) {
			paths.put(mwp.getRelPath(), mwp.getModel());
		}
		
		assertEquals(new HashSet<>(Arrays.asList("cube.scad", "cylinder.scad")), paths.keySet());
		
		assertEquals(Cube.class, paths.get("cube.scad").getClass());
		assertEquals(Cylinder.class, paths.get("cylinder.scad").getClass());
	}

	@Test
	public void testGivenNames() {
		IModelProvider testSubject = new AnnotatedModelProvider() {
			@ModelPart("cube2.scad")
			public Abstract3dModel cube() {
				return new Cube(10);
			}
			
			@Override
			public Abstract3dModel getAssembledModel() {
				// we don't use this method in this test
				return null;
			}
		};
		
		List<ModelWithPath> modelsAndPaths = testSubject.getModelsAndPaths();
		
		assertEquals(1, modelsAndPaths.size());
		assertEquals("cube2.scad", modelsAndPaths.get(0).getRelPath());
		
		assertEquals(Cube.class, modelsAndPaths.get(0).getModel().getClass());
	}
	
	@Test
	public void testEmptyClass() {
		IModelProvider testSubject = new AnnotatedModelProvider() {
			@Override
			public Abstract3dModel getAssembledModel() {
				// we don't use this method in this test
				return null;
			}
		};
		
		List<ModelWithPath> modelsAndPaths = testSubject.getModelsAndPaths();
		
		assertTrue(modelsAndPaths.isEmpty());
	}
	
	@Test(expected=IllegalValueException.class)
	public void illegalValueExceptionShouldBeThrownIfReturnTypeIsNotIModel() {
		IModelProvider testSubject = new AnnotatedModelProvider() {
			@ModelPart
			public String cube() {
				return "something";
			}
			
			@Override
			public Abstract3dModel getAssembledModel() {
				// we don't use this method in this test
				return null;
			}
		};
		
		testSubject.getModelsAndPaths();
	}
	
	@Test(expected=IllegalValueException.class)
	public void illegalValueExceptionShouldBeThrownIfParametersExpected() {
		IModelProvider testSubject = new AnnotatedModelProvider() {
			@ModelPart
			public IModel cube(String param) {
				return null;
			}
			
			@Override
			public Abstract3dModel getAssembledModel() {
				// we don't use this method in this test
				return null;
			}
		};
		
		testSubject.getModelsAndPaths();
	}
}
