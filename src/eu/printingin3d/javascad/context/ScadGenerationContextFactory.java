package eu.printingin3d.javascad.context;

import java.util.HashSet;
import java.util.Set;

public class ScadGenerationContextFactory {
	/**
	 * The default generation context. It includes everything - no filtering or exclusion.
	 */
	public static final IScadGenerationContext DEFAULT = FullScadGenerationContext.INSTANCE; 
	
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
				return FullScadGenerationContext.INSTANCE;
			}
			return new ExcludeGenerationContext(excluded);
		}
		return new IncludeGenerationContext(excluded.isEmpty() ? null : excluded, included);
	}
}
