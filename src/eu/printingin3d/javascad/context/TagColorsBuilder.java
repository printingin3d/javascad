package eu.printingin3d.javascad.context;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class TagColorsBuilder {
	private final Map<Integer, Color> tagColorMap = new HashMap<>();

	public TagColorsBuilder addTag(int tag, Color color) {
		tagColorMap.put(Integer.valueOf(tag), color);
		return this;
	}
	
	public TagColors buildTagColors() {
		return new TagColors(tagColorMap);
	}
}
