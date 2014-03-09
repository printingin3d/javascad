package eu.printingin3d.javascad.tranzitions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.models.Abstract3dModel;

/**
 * Intersection operation. The result of this operation is the common part of the child models.
 * If there is no common part the getBoundaries method call and all operations depending on that
 * (eg. align) will throw an exception.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Intersection extends Abstract3dModel {
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
		List<Abstract3dModel> cloneModels = new ArrayList<>();
		for (Abstract3dModel model : models) {
			cloneModels.add(model.cloneModel());
		}
		return new Intersection(cloneModels);
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
			result.append("intersection()");
			
			result.append("{\n");
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
		return Boundaries3d.intersect(boundaries);
	}
}
