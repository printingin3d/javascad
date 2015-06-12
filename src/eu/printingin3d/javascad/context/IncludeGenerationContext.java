package eu.printingin3d.javascad.context;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class IncludeGenerationContext implements IScadGenerationContext {
	private final Set<Integer> excluded;
	private final Set<Integer> included;
	
	protected IncludeGenerationContext(Collection<Integer> excluded, Collection<Integer> included) {
		this.excluded = excluded==null ? null : new HashSet<>(excluded);
		this.included = included==null ? null : new HashSet<>(included);
	}

	@Override
	public boolean isTagIncluded() {
		return false;
	}
	
	@Override
	public IScadGenerationContext applyTag(int tag) {
		if (excluded!=null && excluded.contains(Integer.valueOf(tag))) {
			return ExcludedScadGenerationContext.INSTANCE;
		}
		if (included!=null && included.contains(Integer.valueOf(tag))) {
			if (excluded==null) {
				return FullScadGenerationContext.INSTANCE;
			}
			return new ExcludeGenerationContext(excluded);
		}
		return this;
	}
}
