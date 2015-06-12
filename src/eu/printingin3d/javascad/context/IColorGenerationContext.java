package eu.printingin3d.javascad.context;

import java.awt.Color;

public interface IColorGenerationContext {
	Color getColor();
	IColorGenerationContext applyTag(int tag);
}
