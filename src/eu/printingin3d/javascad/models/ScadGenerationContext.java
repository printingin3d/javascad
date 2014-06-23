package eu.printingin3d.javascad.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ScadGenerationContext {
	public static final ScadGenerationContext DEFAULT = new ScadGenerationContext(null, null);

	public static ScadGenerationContext excludeThese(Collection<Integer> excluded) {
		return new ScadGenerationContext(excluded, null);
	}
	
	public static ScadGenerationContext includeThese(Collection<Integer> included) {
		return new ScadGenerationContext(null, included);
	}
	
	private final Set<Integer> excluded;
	private final Set<Integer> included;
	
	private ScadGenerationContext(Collection<Integer> excluded, Collection<Integer> included) {
		this.excluded = excluded==null ? null : new HashSet<>(excluded);
		this.included = included==null ? null : new HashSet<>(included);
	}

	public boolean isTagIncluded(int tag) {
		return excluded==null ? included==null ? true : included.contains(Integer.valueOf(tag)) 
				: !excluded.contains(Integer.valueOf(tag));
	}
}
