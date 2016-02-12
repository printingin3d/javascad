package eu.printingin3d.javascad.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import eu.printingin3d.javascad.annotations.ModelPart;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.IModel;

/**
 * <p>An IModelProvider implementation based on annotations.</p>
 * <p>Use the {@link ModelPart} annotation to denote the methods to be used as parts for the export when extending 
 * this class. The {@link IModelProvider#getAssembledModel} method should be still implemented though.</p>
 * 
 * @author ivivan <ivivan@printingin3d.eu>
 */
public abstract class AnnotatedModelProvider implements IModelProvider {

	@Override
	public final List<ModelWithPath> getModelsAndPaths() {
		List<ModelWithPath> paths = new ArrayList<>();

		for (Method m : getClass().getDeclaredMethods()) {
			m.setAccessible(true);
			ModelPart a = m.getAnnotation(ModelPart.class);
			if (a!=null) {
				AssertValue.isTrue(IModel.class.isAssignableFrom(m.getReturnType()), 
							"The return type of the method annotated with ModelPart annotation "
							+ "must implement the IModel interface");
				
				String relPath = a.value();
				if (relPath.isEmpty()) {
					relPath = m.getName() + ".scad";
				}
				try {
					IModel model = (IModel)m.invoke(this);
					if (model!=null) {
						paths.add(new ModelWithPath(model, relPath));
					}
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					throw new IllegalValueException("Unknown exception has been thrown", e);
				}
			}
		}
		
		return paths;
	}

}
