package eu.printingin3d.javascad.models2d;

import java.util.ArrayList;
import java.util.List;

import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.exceptions.NotImplementedException;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.utils.AssertValue;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * Represents a 2D polygon.
 * @author Ivan
 *
 */
public class Polygon extends Abstract2dModel {
	private final List<Coords2d> coords;

	protected Polygon(Coords2d move, List<Coords2d> coords) {
		super(move);
		AssertValue.isTrue(coords.size()>=3, "The list of coordinates should contain at least 3 elements!");
		this.coords = new ArrayList<>(coords);
	}
	
	/**
	 * <p>Creates a polygon. The polygon consists of the given coordinates. The polygon is always closed, so
	 * it has an edge between the last and first coordinates.</p>
	 * <p>The given list should contain at least 3 coordinates, otherwise an 
	 * {@link eu.printingin3d.javascad.exceptions.IllegalValueException IllegalValueException} is thrown.</p> 
	 * @param coords the coordinates
	 * @throws eu.printingin3d.javascad.exceptions.IllegalValueException in case the given list contains fewer than 3 elements
	 */
	public Polygon(List<Coords2d> coords) {
		this(Coords2d.ZERO, coords);
	}

	@Override
	public CSG toCSG(FacetGenerationContext context) {
		throw new NotImplementedException();
	}

	@Override
	protected SCAD innerToScad(IScadGenerationContext context) {
		SCAD result = new SCAD("polygon([");
		
		boolean first = true;
		for (Coords2d c : coords) {
			if (first) {
				first = false;
			} else {
				result = result.append(",");
			}
			result = result.append(c.toString());
		}
		return result.append("]);");
	}

	@Override
	protected Boundaries2d getModelBoundaries() {
		double minX = 0.0;
		double maxX = 0.0;
		double minY = 0.0;
		double maxY = 0.0;
		
		boolean first = true;
		
		for (Coords2d c : coords) {
			if (first) {
				maxX = minX = c.getX();
				maxY = minY = c.getY();
				first = false;
			} else if (minX>c.getX()) {
				minX = c.getX();
			} else if (maxX<c.getX()) {
				maxX = c.getX();
			} else if (minY>c.getY()) {
				minY = c.getY();
			} else if (minY<c.getX()) {
				minY = c.getY();
			}
			
		}
		return new Boundaries2d(new Boundary(minX, maxX), new Boundary(minY, maxY));
	}

	@Override
	public Abstract2dModel move(Coords2d delta) {
		return new Polygon(move.move(delta), coords);
	}

}
