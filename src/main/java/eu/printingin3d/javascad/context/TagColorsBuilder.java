package eu.printingin3d.javascad.context;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Builder of the {@link TagColors} to help to build the mapping easily.</p>
 * <p>The usual way to use this class is chaining:</p>
 * <pre><code>new TagColorsBuilder()
	.addTag(1, new Color(139, 90, 43))
	.addTag(2, Color.GRAY)
	.buildTagColors();</code></pre>
 * @author Ivan
 *
 */
public class TagColorsBuilder {
	private final Map<Integer, Color> tagColorMap = new HashMap<>();

	/**
	 * Adds a tag-color mapping to the builder which will be transferred to the TagColors later.
	 * @param tag the tag you want to assign a color to
	 * @param color the color you want to assign
	 * @return this object to make the value chainable
	 */
	public TagColorsBuilder addTag(int tag, Color color) {
		tagColorMap.put(Integer.valueOf(tag), color);
		return this;
	}
	
	/**
	 * Build the TagColors object based on the mappings created in this object.
	 * @return the TagColors object representing the mappings created in this object.
	 */
	public ITagColors buildTagColors() {
		return new TagColors(tagColorMap);
	}
}
