package eu.printingin3d.javascad.tranzitions;

import java.awt.Color;

import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Complex3dModel;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.utils.AssertValue;
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
public class Colorize extends Complex3dModel {
	private final Color color;
	private final Abstract3dModel baseModel;

	/**
	 * Creates a Colorized object of the given object with the given color
	 * @param color the color which will be used
	 * @param model the model which will be colored
	 * @throws eu.printingin3d.javascad.exceptions.IllegalValueException if either the model or the color null
	 */
	public Colorize(Color color, Abstract3dModel model) {
		AssertValue.isNotNull(model, "The model shouldn't be null for colorize");
		AssertValue.isNotNull(color, "The color shouldn't be null for colorize");
		
		this.color = color;
		this.baseModel = model;
	}

	@Override
	protected SCAD innerToScad(IScadGenerationContext context) {
		return baseModel.toScad(context).prepend(getStringRepresentation(color));
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Colorize(color, baseModel.cloneModel());
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return baseModel.getBoundaries();
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return baseModel.toCSG(context);
	}

	public static String getStringRepresentation(Color color) {
		StringBuilder sb = new StringBuilder();
		sb.append('[').
				append(DoubleUtils.formatDouble(color.getRed()/255.0)).append(',').
				append(DoubleUtils.formatDouble(color.getGreen()/255.0)).append(',').
				append(DoubleUtils.formatDouble(color.getBlue()/255.0));

		if (color.getAlpha()<255) {
			sb.append(',').append(DoubleUtils.formatDouble(color.getAlpha()/255.0));
		}
		sb.append(']');
		
		return "color("+sb+")";
	}

	@Override
	protected Abstract3dModel innerSubModel(IScadGenerationContext context) {
		Abstract3dModel subModel = baseModel.subModel(context);
		return subModel==null ? null : new Colorize(color, subModel);
	}
}
