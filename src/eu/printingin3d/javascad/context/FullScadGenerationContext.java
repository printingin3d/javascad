package eu.printingin3d.javascad.context;


public class FullScadGenerationContext extends AbstractColorHandlingContext implements IScadGenerationContext {

	public FullScadGenerationContext(TagColors tagColors, IColorGenerationContext parent, int tag) {
		super(tagColors, parent, tag);
	}

	@Override
	public boolean isTagIncluded() {
		return true;
	}

	@Override
	public IScadGenerationContext applyTag(int tag) {
		return (tag==0 || tag==this.tag) ? this : new FullScadGenerationContext(tagColors, this, tag);
	}

}
