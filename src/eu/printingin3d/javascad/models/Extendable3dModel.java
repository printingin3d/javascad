package eu.printingin3d.javascad.models;

import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

public abstract class Extendable3dModel extends Abstract3dModel {
	protected Abstract3dModel baseModel;

	@Override
	protected Abstract3dModel innerCloneModel() {
		throw new UnsupportedOperationException("Cloning is not permitted for extendable 3D model unless "
				+ "it is specifically handled in the derived class!");
	}

	@Override
	protected SCAD innerToScad(IScadGenerationContext context) {
		return baseModel.toScad(context);
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return baseModel.getBoundaries();
	}


	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return baseModel.toCSG(context);
	}
}
