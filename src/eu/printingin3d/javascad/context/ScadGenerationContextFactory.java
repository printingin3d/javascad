package eu.printingin3d.javascad.context;

import java.util.HashSet;
import java.util.Set;

public class ScadGenerationContextFactory {
	public static final IScadGenerationContext DEFAULT = new FullScadGenerationContext(null, null, 0); 
	
	private final Set<Integer> included = new HashSet<>(); 
	private final Set<Integer> excluded = new HashSet<>();
	private final TagColors tagColors; 
	
	public ScadGenerationContextFactory() {
		this(null);
	}
	
	public ScadGenerationContextFactory(TagColors tagColors) {
		this.tagColors = tagColors;
	}

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
				return new FullScadGenerationContext(tagColors, null, 0);
			}
			return new ExcludeGenerationContext(excluded, tagColors, null, 0);
		}
		return new IncludeGenerationContext(excluded.isEmpty() ? null : excluded, included,
				tagColors, null, 0);
	}
}
