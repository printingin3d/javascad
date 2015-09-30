package eu.printingin3d.javascad.context;

import java.util.Collection;

/**
 * <p>Used internally by the context generation framework.</p>
 * <p>Use the {@link ScadGenerationContextFactory} to generate generation context.</p>
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class ExcludeGenerationContext extends IncludeGenerationContext {
	
	protected ExcludeGenerationContext(Collection<Integer> excluded) {
		super(excluded, null);
	}

	@Override
	public boolean isTagIncluded() {
		return true;
	}
}
