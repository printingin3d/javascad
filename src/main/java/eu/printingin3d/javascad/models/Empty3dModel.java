package eu.printingin3d.javascad.models;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * <p>Empty model - use this as the base class if you want to build a complex object.</p>
 * <p>Example:</p>
 * <pre>
* {@code
* Abstract3dModel result = new Empty3dModel();
* if (isCrit1()) {
*   result = result.addModel(addModel1());
* }
* if (isCrit2()) {
*   result = result.addModel(addModel2());
* }
* </pre>
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Empty3dModel extends Atomic3dModel {
	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Empty3dModel();
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return SCAD.EMPTY;
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return Boundaries3d.EMPTY;
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return CSG.fromPolygons();
	}

	@Override
	public Abstract3dModel addModel(Abstract3dModel model) {
		return model;
	}
	
	@Override
	public Abstract3dModel subtractModel(Abstract3dModel model) {
		return this;
	}
}
