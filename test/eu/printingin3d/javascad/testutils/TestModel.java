package eu.printingin3d.javascad.testutils;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.models.Abstract3dModel;

public class TestModel extends Abstract3dModel {
	public static final TestModel DEFAULT = new TestModel("(model)");
	
	private final String model;
	private final Boundaries3d boundaries;

	public TestModel(String model, Boundaries3d boundaries) {
		this.model = model;
		this.boundaries = boundaries;
	}
	
	public TestModel(String model) {
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
