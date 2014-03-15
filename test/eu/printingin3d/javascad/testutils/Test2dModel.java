package eu.printingin3d.javascad.testutils;

import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.models2d.Abstract2dModel;

public class Test2dModel extends Abstract2dModel {
	private final String model;
	private final Boundaries2d boundaries;

	public Test2dModel(String model, Boundaries2d boundaries) {
		super();
		this.model = model;
		this.boundaries = boundaries;
	}

	public Test2dModel(String model) {
		this(model, null);
	}
	
	@Override
	protected String innerToScad() {
		return model;
	}

	@Override
	protected Boundaries2d getModelBoundaries() {
		return boundaries;
	}
}
