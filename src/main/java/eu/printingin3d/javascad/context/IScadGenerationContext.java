package eu.printingin3d.javascad.context;

/**
 * Context for the tag based filtering.
 * @author ivivan <ivivan@printingin3d.eu>
 */
public interface IScadGenerationContext {
	/**
	 * True if the current object should be included into the result.
	 * @return true if the current object should be included into the result
	 */
	boolean isTagIncluded();

	/**
	 * Apply the given tag to this context. This operation shouldn't change this object, but
	 * creates a new one if necessary.
	 * @param tag the tag to be applied
	 * @return a context representing a tag applied
	 */
	IScadGenerationContext applyTag(int tag);
}