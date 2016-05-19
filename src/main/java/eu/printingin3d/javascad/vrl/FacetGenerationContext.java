package eu.printingin3d.javascad.vrl;

import java.awt.Color;

import eu.printingin3d.javascad.basic.Radius;
import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.context.ITagColors;

/**
 * Generation context for the CSG rendering. It handles the color and the resolution of circular objects 
 * (circle, cylinder etc.) 
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class FacetGenerationContext extends ColorHandlingContext {
	/**
	 * Default context.
	 */
	public static final FacetGenerationContext DEFAULT = new FacetGenerationContext(null, null, 0);

	private double fs = 0.25;
	private int fa = 6;
	private final FacetGenerationContext parentFacet;
	
	/**
	 * Creates a new context with the given tag-color pairs, parent context and tag.
	 * @param tagColors the tag-color pairs to be used
	 * @param parent the parent context
	 * @param tag the tag of the context
	 */
	public FacetGenerationContext(ITagColors tagColors, FacetGenerationContext parent, int tag) {
		super(tagColors, parent, tag);
		this.parentFacet = parent;
	}
	
	/**
	 * Sets the values of the $fs and $fa variables in the SCAD which is set in the 
	 * {@link eu.printingin3d.javascad.openscad.Consts} class.
	 * @param fs the $fs value
	 * @param fa the $fa value
	 */
	public void setFsAndFa(double fs, int fa) {
		if (parentFacet==null) {
			this.fs = fs;
			this.fa = fa;
		}
		else {
			parentFacet.setFsAndFa(fs, fa);
		}
	}
	
	/**
	 * Calculate the number of slices for the given radius based on the $fs and $fa values set.
	 * @param r the radius of the object
	 * @return the number of slices required
	 */
	public int calculateNumberOfSlices(Radius r) {
		if (parentFacet==null) {
			return Math.min(360/fa, (int) Math.ceil(r.getDiameter()*Math.PI/fs));
		}
		else {
			return parentFacet.calculateNumberOfSlices(r);
		}
	}
	
	/**
	 * Apply the given tag to this context - creates a new one if anything changed.
	 * @param tag the tag to be applied
	 * @return a context with the tag applied
	 */
	@Override
	public FacetGenerationContext applyTag(int tag) {
		if (tag==this.tag || tag==0) {
			return this;
		}
		
		return new FacetGenerationContext(tagColors, this, tag);
	}
	
	@Override
	public Color getColor() {
		Color result = super.getColor();
		return result==null ? Color.GRAY : result;
	}
}
