package eu.printingin3d.javascad.tranzitions2d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.models2d.Abstract2dModel;
import eu.printingin3d.javascad.models2d.Area2d;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * <p>Represents an union of models. It is a descendant of {@link Abstract2dModel}, which means you
 * can use the convenient methods on unions too.</p>
 * <p>You don't have to worry about the optimization either, because the generated OpenSCAD code will be 
 * the optimal one in every case. The parameters could even contain null elements, those will
 * be ignored during the model generation.</p>
 * <p>The object doesn't have any list modification method and although it could work if the passed list
 * is modified after the construction, the advised solution is to create the final list before creating
 * this object.</p>  
 */
public class Union extends Abstract2dModel {
	private final List<Abstract2dModel> models;

	private Union(List<Abstract2dModel> models, Coords2d move) {
		super(move);
		this.models = new ArrayList<>(models);
	}

	/**
	 * Instantiates a new Union object.
	 * @param models the models this union holds.
	 */
	public Union(List<Abstract2dModel> models) {
		this(models, Coords2d.ZERO);
	}
	
	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		List<SCAD> scads = models.stream()
				.map(m -> m.toScad(context))
				.filter(scad -> !scad.isEmpty())
				.collect(Collectors.toList());
		
		switch (scads.size()) {
		case 0:
			return SCAD.EMPTY;
		case 1:
			return scads.get(0);
		default:
			return new SCAD("union() {\n")
					.append(scads.stream().reduce(SCAD::append).get())
					.append("}\n");		}
	}

	@Override
	protected Boundaries2d getModelBoundaries() {
		return Boundaries2d.combine(models.stream()
				.map(m -> m.getBoundaries2d())
				.collect(Collectors.toList()));
	}

	@Override
	public Abstract2dModel move(Coords2d delta) {
		return new Union(models, move.move(delta));
	}

	@Override
	protected Collection<Area2d> getInnerPointCircle(FacetGenerationContext context) {
		List<Area2d> result = new ArrayList<>();
		for (Abstract2dModel m : models) {
			for (Area2d lc : m.getPointCircle(context)) {
				boolean added = false;
				for (int i=0;i<result.size();i++) {
					if (!lc.isDistinct(result.get(i))) {
						lc = lc.union(result.get(i));
						result.set(i, lc);
						added = true;
						break;
					}
				}
				
				if (!added) {
					result.add(lc);
				}
			}
		}
		return result;
	}

}
