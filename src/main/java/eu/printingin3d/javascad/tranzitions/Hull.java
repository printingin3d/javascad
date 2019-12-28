package eu.printingin3d.javascad.tranzitions;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.exceptions.NotImplementedException;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Complex3dModel;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.utils.ListUtils;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * <p>Represents a hull of models. It is a descendant of {@link Abstract3dModel}, which means you
 * can use the convenient methods on unions too.</p>
 * <p>You don't have to worry about the optimization either, because the generated OpenSCAD code will be 
 * the optimal one in every case. The parameters could even contain null elements, those will
 * be ignored during the model generation.</p>
 */
public class Hull extends Complex3dModel {
	protected final List<Abstract3dModel> models;

	/**
	 * Construct the object.
	 * @param models list of models
	 */
	public Hull(List<Abstract3dModel> models) {
		this.models = models==null ? Collections.<Abstract3dModel>emptyList() : ListUtils.removeNulls(models);
	}

	/**
	 * Construct the object.
	 * @param models array of models
	 */
	public Hull(Abstract3dModel... models) {
		this(Arrays.asList(models));
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		List<SCAD> scads = new ArrayList<>();
		for (Abstract3dModel model : models) {
			SCAD scad = model.toScad(context);
			if (!scad.isEmpty()) {
				scads.add(scad);
			}
		}
		
		switch (scads.size()) {
		case 0:
			return SCAD.EMPTY;
		case 1:
			return scads.get(0);
		default:
			SCAD result = new SCAD("hull() {\n");
			for (SCAD scad : scads) {
				result = result.append(scad);
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
		return Boundaries3d.combine(boundaries);
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Hull(new ArrayList<Abstract3dModel>(models));
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		// It is very hard to implement hull with CSG
		throw new NotImplementedException();
	}

	@Override
	protected Abstract3dModel innerSubModel(IScadGenerationContext context) {
		List<Abstract3dModel> subModels = new ArrayList<>();
		for (Abstract3dModel m : models) {
			subModels.add(m.subModel(context));
		}
		
		return new Hull(subModels);
	}

    @Override
    protected List<Abstract3dModel> getChildrenModels() {
        return models;
    }
}
