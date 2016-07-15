package eu.printingin3d.javascad.tranzitions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
		this.models = ListUtils.removeNulls(models);
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
			return new SCAD("intersection()")
				.append("{\n")
				.append(			
					models.stream()
						.map(m -> m.toScad(context))
						.reduce(SCAD::append)
						.get()
					)
				.append("}\n");
		}
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		List<Boundaries3d> boundaries = models.stream()
				.map(Abstract3dModel::getBoundaries)
				.collect(Collectors.toList());
		return boundaries.isEmpty() ? Boundaries3d.EMPTY : Boundaries3d.intersect(boundaries);
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return models.stream()
			.map(m -> m.toCSG(context))
			.reduce(CSG::intersect)
			.orElse(null);
	}

	@Override
	protected Abstract3dModel innerSubModel(IScadGenerationContext context) {
		return new Intersection(
				models.stream().map(m -> m.subModel(context)).filter(Objects::nonNull).collect(Collectors.toList())
			);
	}
}
