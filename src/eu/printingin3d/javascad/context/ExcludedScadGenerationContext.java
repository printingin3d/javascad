package eu.printingin3d.javascad.context;

public class ExcludedScadGenerationContext extends AbstractColorHandlingContext implements IScadGenerationContext {

	public ExcludedScadGenerationContext(TagColors tagColors, IColorGenerationContext parent, int tag) {
		super(tagColors, parent, tag);
	}

	@Override
	public boolean isTagIncluded() {
		return false;
	}

	@Override
	public IScadGenerationContext applyTag(int tag) {
		return (tag==0 || (tag==this.tag)) ? this : 
			new ExcludedScadGenerationContext(tagColors, this, tag);
	}

}
