package eu.printingin3d.javascad.context;

public class ExcludedScadGenerationContext implements IScadGenerationContext {
	public static final IScadGenerationContext INSTANCE = new ExcludedScadGenerationContext();

	private ExcludedScadGenerationContext() {
		// prevent instantiating this class
	}

	@Override
	public boolean isTagIncluded() {
		return false;
	}

	@Override
	public IScadGenerationContext applyTag(int tag) {
		return this;
	}

}
