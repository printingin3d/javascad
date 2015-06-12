package eu.printingin3d.javascad.context;


public class FullScadGenerationContext implements IScadGenerationContext {
	public static final IScadGenerationContext INSTANCE = new FullScadGenerationContext();
	
	private FullScadGenerationContext() {
		// preventing instantiating this class
	}

	@Override
	public boolean isTagIncluded() {
		return true;
	}

	@Override
	public IScadGenerationContext applyTag(int tag) {
		return this;
	}

}
