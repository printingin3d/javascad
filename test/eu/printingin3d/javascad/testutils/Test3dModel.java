package eu.printingin3d.javascad.testutils;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.models.Abstract3dModel;

public class Test3dModel extends Abstract3dModel {
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
	protected String innerToScad() {
		return model;
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return boundaries;
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return this;
	}
}
