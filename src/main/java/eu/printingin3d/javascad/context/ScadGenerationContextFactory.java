package eu.printingin3d.javascad.context;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>Context generation factory to create a factory for the given includes and excludes.<p>
 * <p>A typical usage looks like this:</p>
 * <p>
 * <code>new ScadGenerationContextFactory().include(4,6,8).exclude(3,5,7).create();</code>
 * </p>
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class ScadGenerationContextFactory {
	/**
	 * The default generation context. It includes everything - no filtering or exclusion.
	 */
	public static final IScadGenerationContext DEFAULT = FullScadGenerationContext.INSTANCE; 
	
	private final Set<Integer> included = new HashSet<>(); 
	private final Set<Integer> excluded = new HashSet<>();

	/**
	 * Include the given tags to the generated output.
	 * @param values the tags should be included
	 * @return this object to make it possible to chain commands
	 */
	public ScadGenerationContextFactory include(int... values) {
		for (int i : values) {
			included.add(Integer.valueOf(i));
		}
		return this;
	}
	
	/**
	 * Exclude the given tags to the generated output.
	 * @param values the tags should be excluded
	 * @return this object to make it possible to chain commands
	 */
	public ScadGenerationContextFactory exclude(int... values) {
		for (int i : values) {
			excluded.add(Integer.valueOf(i));
		}
		return this;
	}
	
	/**
	 * Creates the context based on the includes and excludes set before.
	 * @return a SCAD generation context that represents the includes and excludes set before
	 */
	public IScadGenerationContext create() {
		if (included.isEmpty()) {
			if (excluded.isEmpty()) {
				return FullScadGenerationContext.INSTANCE;
			}
			return new ExcludeGenerationContext(excluded);
		}
		return new IncludeGenerationContext(excluded, included);
	}
}
