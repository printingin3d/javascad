package eu.printingin3d.javascad.context;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ScadGenerationContextFactory {
	public static final IScadGenerationContext DEFAULT = FullScadGenerationContext.getInstance(); 
	
	private final Set<Integer> included = new HashSet<>(); 
	private final Set<Integer> excluded = new HashSet<>(); 
	
	public ScadGenerationContextFactory include(int... values) {
		for (int i : values) {
			included.add(Integer.valueOf(i));
		}
		return this;
	}
	
	public ScadGenerationContextFactory exclude(int... values) {
		for (int i : values) {
			excluded.add(Integer.valueOf(i));
		}
		return this;
	}
	
	public IScadGenerationContext create() {
		if (included.isEmpty()) {
			if (excluded.isEmpty()) {
				return DEFAULT;
			}
			return new ExcludeGenerationContext(excluded);
		}
		return new IncludeGenerationContext(excluded.isEmpty() ? null : excluded, included);
	}
	
	public static IScadGenerationContext excludeThese(Collection<Integer> excluded) {
		return new ExcludeGenerationContext(excluded);
	}
	
	public static IScadGenerationContext includeThese(Collection<Integer> included) {
		return new IncludeGenerationContext(null, included);
	}
}
