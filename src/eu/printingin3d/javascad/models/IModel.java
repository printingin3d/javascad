package eu.printingin3d.javascad.models;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * Represents a renderable 3D model.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public interface IModel {
	/**
	 * Renders this model and returns with the generated OpenSCAD code.
	 * @return the generated OpenSCAD code
	 */
	SCAD toScad(IColorGenerationContext context);
	
	CSG toCSG(FacetGenerationContext context);
}
