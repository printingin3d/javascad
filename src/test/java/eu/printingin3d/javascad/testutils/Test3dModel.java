package eu.printingin3d.javascad.testutils;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Atomic3dModel;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

public class Test3dModel extends Atomic3dModel {
	public static final Test3dModel DEFAULT = new Test3dModel("(model)");
	
	private final String model;
	private final Boundaries3d boundaries;

	public Test3dModel(String model, Boundaries3d boundaries) {
		this.model = model;
		this.boundaries = boundaries;
	}
	
	public Test3dModel(String model) {
		this(model, null);
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return new SCAD(model);
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return boundaries;
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Test3dModel(model, boundaries);
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return null;
	}
}
