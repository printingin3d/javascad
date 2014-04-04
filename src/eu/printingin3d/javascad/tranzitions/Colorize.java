package eu.printingin3d.javascad.tranzitions;

import java.awt.Color;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.utils.DoubleUtils;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * Colors the object it contains. Please pay attention: colors are only supported in the 
 * CSG view in OpenSCAD - neither CGAL rendering nor STL output does not support it yet.
 * This is a limitation of the OpenSCAD rendering.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Colorize extends Abstract3dModel {
	private final Color color;
	private final Abstract3dModel baseModel;

	/**
	 * Creates a Colorized object of the given object with the given color
	 * @param color the color which will be used
	 * @param model the model which will be colored
	 */
	public Colorize(Color color, Abstract3dModel model) {
		this.color = color;
		this.baseModel = model;
	}

	@Override
	protected String innerToScad() {
		
		StringBuilder sb = new StringBuilder();
		sb.append('[').
				append(DoubleUtils.formatDouble(color.getRed()/255.0)).append(',').
				append(DoubleUtils.formatDouble(color.getGreen()/255.0)).append(',').
				append(DoubleUtils.formatDouble(color.getBlue()/255.0));

		if (color.getAlpha()<255) {
			sb.append(',').append(DoubleUtils.formatDouble(color.getAlpha()/255.0));
		}
		sb.append(']');
		
		return "color("+sb+")"+baseModel.toScad();
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Colorize(color, baseModel);
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return baseModel.getBoundaries();
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return baseModel.toCSG(context);
	}

}
