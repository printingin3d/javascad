package eu.printingin3d.javascad.context;

/**
 * <p>Used internally by the context generation framework.</p>
 * <p>Use the {@link ScadGenerationContextFactory} to generate generation context.</p>
 * @author ivivan <ivivan@printingin3d.eu>
 */
public final class FullScadGenerationContext implements IScadGenerationContext {
	/**
	 * The only instance of this immutable object.
	 */
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
