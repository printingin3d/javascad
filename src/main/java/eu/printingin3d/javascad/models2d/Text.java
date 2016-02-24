package eu.printingin3d.javascad.models2d;

import java.util.Collection;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.coords2d.Dims2d;
import eu.printingin3d.javascad.exceptions.NotImplementedException;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.utils.DoubleUtils;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * 
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Text extends Abstract2dModel {
	private final String text;
	private final Dims2d size;

	private Text(Coords2d move, String text, Dims2d size) {
		super(move);
		
		this.text = text;
		this.size = size;
	}
	
	/**
	 * Constructs the object using the given parameters.
	 * @param text the text of the object
	 * @param size the dimensions of the object - used by the alignment functions
	 */
	public Text(String text, Dims2d size) {
		this(Coords2d.ZERO, text, size);
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return new SCAD("text(text=\""+text+"\","
				+ "size="+DoubleUtils.formatDouble(size.getY())+","
				+ "halign=\"center\","
				+ "valign=\"center\");\n");
	}

	@Override
	protected Boundaries2d getModelBoundaries() {
		double x = size.getX()/2;
		double y = size.getY()/2;
		return new Boundaries2d(new Boundary(-x, +x), new Boundary(-y, +y));
	}

	@Override
	public Abstract2dModel move(Coords2d delta) {
		return new Text(this.move.move(delta), text, size);
	}

	@Override
	protected Collection<Area2d> getInnerPointCircle(FacetGenerationContext context) {
		throw new NotImplementedException();
	}
}
