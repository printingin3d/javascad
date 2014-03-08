package eu.printingin3d.javascad.models;

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
	String toScad();
}
