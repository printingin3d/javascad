package eu.printingin3d.javascad.context;

public class FullScadGenerationContext implements IScadGenerationContext {
	private static final IScadGenerationContext instance = new FullScadGenerationContext();

	public static IScadGenerationContext getInstance() {
		return instance;
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
