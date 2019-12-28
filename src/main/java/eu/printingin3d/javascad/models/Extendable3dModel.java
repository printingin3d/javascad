package eu.printingin3d.javascad.models;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * <p>Extendable model - use this as the base class for your own models.</p>
 * <p>To use it you have to set the value of the <code>baseModel</code> field in the constructor of your class.
 * And if there are more fields of your class other than the inherited <code>baseModel</code> than you have to implement
 * the <code>innerSubModel</code> method to make a copy of your model. Otherwise it is enough if your class has a
 * default constructor - the <code>innerSubModel</code> implementation of this class will handle 
 * everything from there.</p>
 * @author ivivan <ivivan@printingin3d.eu>
 */
public abstract class Extendable3dModel extends Complex3dModel {
	protected Abstract3dModel baseModel;

	@Override
	protected Abstract3dModel innerCloneModel() {
		return innerSubModel(ScadGenerationContextFactory.DEFAULT);
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return baseModel.toScad(context);
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return baseModel.getBoundaries();
	}


	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return baseModel.toCSG(context);
	}
	
	@Override
	protected Abstract3dModel innerSubModel(IScadGenerationContext context) {
		Extendable3dModel newInstance;
		try {
			Constructor<? extends Extendable3dModel> constructor = getClass().getDeclaredConstructor();
			constructor.setAccessible(true);
			newInstance = constructor.newInstance();
		} catch (ReflectiveOperationException | IllegalArgumentException | SecurityException e) {
			throw new IllegalValueException("Creating the new instance of this "
					+ "class ("+getClass()+") has failed! "+
					"Possibly there is no default constructor for this class.", e);
		}
		newInstance.baseModel = baseModel.subModel(context);
		return newInstance;
	}

    @Override
    protected List<Abstract3dModel> getChildrenModels() {
        return Collections.singletonList(baseModel);
    }
}
