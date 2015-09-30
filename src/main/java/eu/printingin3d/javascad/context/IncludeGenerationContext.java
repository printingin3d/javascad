package eu.printingin3d.javascad.context;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Used internally by the context generation framework.</p>
 * <p>Use the {@link ScadGenerationContextFactory} to generate generation context.</p>
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class IncludeGenerationContext implements IScadGenerationContext {
	private final Set<Integer> excluded;
	private final Set<Integer> included;
	
	protected IncludeGenerationContext(Collection<Integer> excluded, Collection<Integer> included) {
		this.excluded = excluded==null ? Collections.<Integer>emptySet() : new HashSet<>(excluded);
		this.included = included==null ? Collections.<Integer>emptySet() : new HashSet<>(included);
	}

	@Override
	public boolean isTagIncluded() {
		return false;
	}
	
	@Override
	public IScadGenerationContext applyTag(int tag) {
		if (excluded.contains(Integer.valueOf(tag))) {
			return ExcludedScadGenerationContext.INSTANCE;
		}
		if (included.contains(Integer.valueOf(tag))) {
			if (excluded==null) {
				return FullScadGenerationContext.INSTANCE;
			}
			return new ExcludeGenerationContext(excluded);
		}
		return this;
	}
}
