package eu.printingin3d.javascad.tranform;

import eu.printingin3d.javascad.coords.Coords3d;

/**
 * Represents a coordinate transformation - it can be anything: move, rotation, mirroring... 
 * @author ivivan <ivivan@printingin3d.eu>
 */
public interface ITransformation {
	/**
	 * Transforming a coordinate by this transformation.
	 * @param vec the coordinate to be transformed
	 * @return a new coordinate representing the transformed coordinate
	 */
	Coords3d transform(Coords3d vec);
	/**
	 * True if and only if this transformation is a mirror transformation.
	 * @return true if and only if this transformation is a mirror transformation.
	 */
	boolean isMirror();
}
