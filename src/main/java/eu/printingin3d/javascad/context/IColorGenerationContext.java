package eu.printingin3d.javascad.context;

import java.awt.Color;

/**
 * Provides the colors during the SCAD generation process. 
 * @author ivivan <ivivan@printingin3d.eu>
 */
public interface IColorGenerationContext {
	/**
	 * Returns with the color of the object in the current context.
	 * @return the color of the object
	 */
	Color getColor();
	/**
	 * Applying the tag and returns with a new object based on that. The current object
	 * should not be changed, but a new object should be created if necessary.
	 * @param tag the tag to be applied
	 * @return a new object with the tag applied
	 */
	IColorGenerationContext applyTag(int tag);
}
