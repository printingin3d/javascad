package eu.printingin3d.javascad.vrl;

import java.awt.Color;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.context.ITagColors;

public class FacetGenerationContext extends ColorHandlingContext {
	public final static FacetGenerationContext DEFAULT = new FacetGenerationContext(null, null, 0);
	
	public FacetGenerationContext(ITagColors tagColors, IColorGenerationContext parent, int tag) {
		super(tagColors, parent, tag);
	}

	private double fs = 0.25;
	private int fa = 6;
	
	public void setFsAndFa(double fs, int fa) {
		this.fs = fs;
		this.fa = fa;
	}
	
	public int calculateNumberOfSlices(double r) {
		return Math.min(360/fa, (int) Math.ceil(2.0*r*Math.PI/fs));
	}
	
	public FacetGenerationContext applyTag(int tag) {
		if ((tag==this.tag) || tag==0) {
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
