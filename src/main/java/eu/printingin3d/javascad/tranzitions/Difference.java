package eu.printingin3d.javascad.tranzitions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Complex3dModel;
import eu.printingin3d.javascad.models.Cube;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.utils.AssertValue;
import eu.printingin3d.javascad.utils.ListUtils;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * Difference operation. It subtracts from the first model all the others.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Difference extends Complex3dModel {
	private final Abstract3dModel model1;
	private final List<Abstract3dModel> model2;
	
	/**
	 * <p>Creates the object with the models given. The first parameter will be the model
	 * and the second list of models will be subtracted by it.</p>
	 * <p>If the first parameter is null it throws an IllegalValueException.
	 * If the second parameter is null or empty the operation do nothing,
	 * just gives back the first model.</p> 
	 * @param model1 the model subtracted from
	 * @param model2 the model to be subtracted
	 * @throws IllegalValueException if the first model is null
	 */
	public Difference(Abstract3dModel model1, List<Abstract3dModel> model2) throws IllegalValueException {
		AssertValue.isNotNull(model1, "The first parameter of the difference operation should not be null!");
		
		this.model1 = model1;
		this.model2 = model2==null ? Collections.<Abstract3dModel>emptyList() : ListUtils.removeNulls(model2);
	}
	
	/**
	 * <p>Creates the object with the list of models given. The first parameter is used as a model
	 * and all subsequent models will be subtracted from it.</p>
	 * <p>If the first parameter is null it throws an IllegalValueException.
	 * If any subsequent parameter is null that parameter is ignored. If only one parameter is
	 * given this operation does nothing, just gives back the first model.</p> 
	 * @param model1 the model subtracted from
	 * @param model2 the model to be subtracted
	 * @throws IllegalValueException if the first model is null
	 */
	public Difference(Abstract3dModel model1, Abstract3dModel... model2) throws IllegalValueException {
		this(model1, Arrays.asList(model2));
	}
	
	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		SCAD baseModel = model1.toScad(context);
		if (baseModel.isEmpty()) {
			return SCAD.EMPTY;
		}
		
		if (model2.isEmpty()) {
			return baseModel;
		}
		
		SCAD result = new SCAD("difference()");
		result = result.append("{").append(baseModel);
		for (Abstract3dModel model : model2) {
			result = result.append(model.toScad(context));
		}
		return result.append("}");
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		Boundaries3d boundaries = model1.getBoundaries();
		Boundary x = boundaries.getX();
		Boundary y = boundaries.getY();
		Boundary z = boundaries.getZ();
		
		for (Abstract3dModel model : model2) {
			if (model instanceof Cube && !model.isRotated()) {
				Boundaries3d b = model.getBoundaries();
				if (x.isInsideOf(b.getX())) {
					if (y.isInsideOf(b.getY())) {
						z = z.remove(b.getZ());
					}
					else if (z.isInsideOf(b.getZ())) {
						y = y.remove(b.getY());
					}
				}
				else if (y.isInsideOf(b.getY()) && 
						z.isInsideOf(b.getZ())) {
					x = x.remove(b.getX());
				}
			}
		}
		return new Boundaries3d(x, y, z);
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Difference(model1, new ArrayList<Abstract3dModel>(model2));
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		CSG csg = model1.toCSG(context);
		
		for (Abstract3dModel model : model2) {
			csg = csg.difference(model.toCSG(context));
		}
		
		return csg;
	}
	
	@Override
	public Abstract3dModel subtractModel(Abstract3dModel model) {
		if (isMoved() || isRotated()) {
			return super.subtractModel(model);
		}
		
		List<Abstract3dModel> newModel2 = new ArrayList<>(model2);
		newModel2.add(model);
		return new Difference(model1, newModel2);
	}

	@Override
	protected Abstract3dModel innerSubModel(IScadGenerationContext context) {
		Abstract3dModel subModel = model1.subModel(context);
		if (subModel==null) {
			return null;
		}
		List<Abstract3dModel> subModels = new ArrayList<>();
		for (Abstract3dModel model : model2) {
			subModels.add(model.subModel(context));
		}
		return new Difference(subModel, subModels);
	}

    @Override
    protected List<Abstract3dModel> getChildrenModels() {
        List<Abstract3dModel> result = new ArrayList<>(model2);
        result.add(model1);
        return result;
    }
}
