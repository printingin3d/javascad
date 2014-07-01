package eu.printingin3d.javascad.models2d;

import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Dims2d;
import eu.printingin3d.javascad.exceptions.NotImplementedException;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * Represents a 2D square object.
 * 
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Square extends Abstract2dModel {
	private final Dims2d size;
	
	/**
	 * Creates a square with the given size values.
	 * @param size the 2D size value used to construct the square
	 */
	public Square(Dims2d size) {
		super();
		this.size = size;
	}
	
	
	@Override
	protected SCAD innerToScad(IScadGenerationContext context) {
		return new SCAD("square("+size+", center=true);\n");
	}


	@Override
	protected Boundaries2d getModelBoundaries() {
		return new Boundaries2d(
				Boundary.createSymmetricBoundary(size.getX()/2.0), 
				Boundary.createSymmetricBoundary(size.getY()/2.0));
	}


	@Override
	public CSG toCSG(FacetGenerationContext context) {
		throw new NotImplementedException();
	}
}
