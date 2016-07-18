package eu.printingin3d.javascad.models2d;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.utils.AssertValue;
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
	 * @throws eu.printingin3d.javascad.exceptions.IllegalValueException in case the given list contains 
	 * 				fewer than 3 elements
	 */
	public Polygon(List<Coords2d> coords) {
		this(Coords2d.ZERO, coords);
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return new SCAD("polygon([")
				.append(coords.stream()
					.map(Coords2d::toString)
					.reduce((u, v) -> u + "," + v).get())
				.append("]);");
	}

	@Override
	protected Boundaries2d getModelBoundaries() {
		return new Boundaries2d(
			coords.stream()
				.map(c -> new Boundary(c.getX()))
				.reduce(Boundary::combine).get(),
			coords.stream()
				.map(c -> new Boundary(c.getY()))
				.reduce(Boundary::combine).get());
	}

	@Override
	public Abstract2dModel move(Coords2d delta) {
		return new Polygon(move.move(delta), coords);
	}

	@Override
	public Stream<Area2d> getInnerPointCircle(FacetGenerationContext context) {
		return Stream.of(new Area2d(coords));
	}

}
