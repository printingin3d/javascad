package eu.printingin3d.javascad.testutils;

import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Extendable3dModel;

public class TestExtendable3dModel extends Extendable3dModel {
	protected TestExtendable3dModel() {
		// default constructor used by cloneModel and subModel methods
	}
	
	public TestExtendable3dModel(Abstract3dModel model) {
		this.baseModel = model;
	}
}