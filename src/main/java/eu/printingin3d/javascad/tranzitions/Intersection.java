package eu.printingin3d.javascad.tranzitions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Complex3dModel;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.utils.ListUtils;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * Intersection operation. The result of this operation is the common part of the child models.
 * If there is no common part the getBoundaries method call and all operations depending on that
 * (eg. align) will throw an exception.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Intersection extends Complex3dModel {
	private final List<Abstract3dModel> models;

	/**
	 * Creates the object with the models given.
	 * @param models the models used to create the intersection
	 */
	public Intersection(List<Abstract3dModel> models) {
		this.models = models;
	}

	/**
	 * Creates the object with the models given.
	 * @param models the models used to create the intersection
	 */
	public Intersection(Abstract3dModel... models) {
		this(Arrays.asList(models));
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Intersection(new ArrayList<Abstract3dModel>(models));
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		switch (models.size()) {
		case 0:
			return SCAD.EMPTY;
		case 1:
			return models.get(0).toScad(context);
		default:
			SCAD result = new SCAD("intersection()");
			
			result = result.append("{\n");
			for (Abstract3dModel model : models) {
				result = result.append(model.toScad(context));
			}
			return result.append("}\n");
		}
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		List<Boundaries3d> boundaries = new ArrayList<>();
		for (Abstract3dModel model : models) {
			boundaries.add(model.getBoundaries());
		}
		return boundaries.isEmpty() ? Boundaries3d.EMPTY : Boundaries3d.intersect(boundaries);
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		CSG csg = null;
		for (Abstract3dModel model : models) {
			if (csg==null) {
				csg = model.toCSG(context);
			}
			else {
				csg = csg.intersect(model.toCSG(context));
			}
		}
		return csg;
	}

	@Override
	protected Abstract3dModel innerSubModel(IScadGenerationContext context) {
		List<Abstract3dModel> subModels = new ArrayList<>();
		for (Abstract3dModel model : models) {
			subModels.add(model.subModel(context));
		}
		return new Intersection(ListUtils.removeNulls(subModels));
	}

    @Override
    protected List<Abstract3dModel> getChildrenModels() {
        return models;
    }
}
