package eu.printingin3d.javascad.context;

import java.awt.Color;

/**
 * Base class for all classes which implement IColorGenerationContext interface.
 * Implements the logic of the getColor() method. 
 * @author Ivan
 *
 */
public abstract class AbstractColorHandlingContext implements IColorGenerationContext {
	protected final TagColors tagColors;
	protected final IColorGenerationContext parent;
	protected final int tag;
	
	public AbstractColorHandlingContext(TagColors tagColors, IColorGenerationContext parent, int tag) {
		this.tagColors = tagColors;
		this.parent = parent;
		this.tag = tag;
	}

	@Override
	public Color getColor() {
		if (tag!=0 && tagColors!=null) {
			Color result = tagColors.getColor(tag);
			if (result!=null) {
				return result;
			}
		}
		return parent==null ? null : parent.getColor();
	}

}
