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
	 * @param context the color context to be used for the generation
	 * @return the generated OpenSCAD code
	 */
	SCAD toScad(IColorGenerationContext context);
	
	/**
	 * Renders this model to its CSG interpretation.
	 * @param context the context to be used during the generation process.
	 * @return the CSG interpretation
	 */
	CSG toCSG(FacetGenerationContext context);
}
