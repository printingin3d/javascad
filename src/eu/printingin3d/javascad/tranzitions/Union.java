package eu.printingin3d.javascad.tranzitions;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.utils.ListUtils;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * <p>Represents an union of models. It is a descendant of {@link Abstract3dModel}, which means you
 * can use the convenient methods on unions too.</p>
 * <p>You don't have to worry about the optimization either, because the generated OpenSCAD code will be 
 * the optimal one in every case. The parameters could even contain null elements, those will
 * be ignored during the model generation.</p>
 * <p>The object doesn't have any list modification method and although it could work if the passed list
 * is modified after the construction, the advised solution is to create the final list before creating
 * this object.</p>  
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Union extends Abstract3dModel {
	protected final List<Abstract3dModel> models;

	/**
	 * Construct the object.
	 * @param models list of models
	 */
	public Union(List<Abstract3dModel> models) {
		this.models = models==null ? Collections.<Abstract3dModel>emptyList() : ListUtils.removeNulls(models);
	}

	/**
	 * Construct the object.
	 * @param models array of models
	 */
	public Union(Abstract3dModel... models) {
		this(Arrays.asList(models));
	}

	@Override
	protected String innerToScad() {
		StringBuilder result = new StringBuilder();
		switch (models.size()) {
		case 0:
			break;
		case 1:
			result.append(models.get(0).toScad());
			break;
		default:
			result.append("union() {\n");
			for (Abstract3dModel model : models) {
				result.append(model.toScad());
			}
			result.append("}\n");
			break;
		}
		return result.toString();
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
		List<Abstract3dModel> cloneModels = new ArrayList<>();
		for (Abstract3dModel model : models) {
			cloneModels.add(model.cloneModel());
		}
		return new Union(cloneModels);
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		CSG csg = null;
		for (Abstract3dModel model : models) {
			if (csg==null) {
				csg = model.toCSG(context);
			}
			else {
				csg = csg.union(model.toCSG(context));
			}
		}
		return csg;
	}
}
