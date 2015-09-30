package eu.printingin3d.javascad.context;

/**
 * Used internally by the context generation framework.
 * @author Ivan_Suller
 *
 */
public final class ExcludedScadGenerationContext implements IScadGenerationContext {
	/**
	 * The only instance of this immutable object.
	 */
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
