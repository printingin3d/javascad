package eu.printingin3d.javascad.context;

import java.awt.Color;

public interface ITagColors {

	/**
	 * Returns the color of the given tag or null if there is no color associated to that tag.
	 * @param tag the color to be queried 
	 * @return the color of the given tag or null if there is no color associated to that tag.
	 */
	public abstract Color getColor(int tag);

}