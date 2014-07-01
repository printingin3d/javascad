package eu.printingin3d.javascad.context;

public class ExcludedScadGenerationContext implements IScadGenerationContext {
	private static final IScadGenerationContext instance = new ExcludedScadGenerationContext();
	
	public static IScadGenerationContext getInstance() {
		return instance;
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
