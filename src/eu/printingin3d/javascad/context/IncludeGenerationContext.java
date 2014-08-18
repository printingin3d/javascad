package eu.printingin3d.javascad.context;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class IncludeGenerationContext extends AbstractColorHandlingContext implements IScadGenerationContext {
	private final Set<Integer> excluded;
	private final Set<Integer> included;
	
	protected IncludeGenerationContext(Collection<Integer> excluded, Collection<Integer> included,
			TagColors tagColors, IColorGenerationContext parent, int tag) {
		super(tagColors, parent, tag);
		
		this.excluded = excluded==null ? null : new HashSet<>(excluded);
		this.included = included==null ? null : new HashSet<>(included);
	}

	@Override
	public boolean isTagIncluded() {
		return false;
	}
	
	@Override
	public IScadGenerationContext applyTag(int tag) {
		if ((tag==this.tag) || tag==0) {
			return this;
		}
		
		if (excluded!=null && excluded.contains(Integer.valueOf(tag))) {
			return new ExcludedScadGenerationContext(tagColors, this, tag);
		}
		if (included!=null && included.contains(Integer.valueOf(tag))) {
			if (excluded==null) {
				return new FullScadGenerationContext(tagColors, this, tag);
			}
			return new ExcludeGenerationContext(excluded, tagColors, this, tag);
		}
		return new IncludeGenerationContext(excluded, included, tagColors, this, tag);
	}
}
