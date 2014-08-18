package eu.printingin3d.javascad.context;

import java.awt.Color;
import java.util.Map;

public class TagColors {
	private final Map<Integer, Color> tagColorMap;
	
	public TagColors(Map<Integer, Color> tagColorMap) {
		this.tagColorMap = tagColorMap;
	}

	public Color getColor(int tag) {
		return tagColorMap.get(Integer.valueOf(tag));
	}
}
