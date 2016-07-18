package eu.printingin3d.javascad.models2d;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import eu.printingin3d.javascad.basic.Angle;
import eu.printingin3d.javascad.basic.Radius;
import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * Represents a 2D circle.
 * @author Ivan
 *
 */
public class Circle extends Abstract2dModel {
	private final Radius radius;

	private Circle(Coords2d move, Radius radius) {
		super(move);
		this.radius = radius;
	}
	
	/**
	 * Constructs the object using the given radius.
	 * @param radius the radius of the circle
	 */
	public Circle(Radius radius) {
		this(Coords2d.ZERO, radius);
	}
	
	/**
	 * Constructs the object using the given radius.
	 * @param radius the radius of the circle
	 * @deprecated use the constructor with Radius parameters instead of doubles 
	 */
	@Deprecated
	public Circle(double radius) {
		this(Radius.fromRadius(radius));
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return new SCAD("circle(r="+radius+", center=true);\n");
	}

	@Override
	protected Boundaries2d getModelBoundaries() {
		return new Boundaries2d(
				Boundary.createSymmetricBoundary(radius.getRadius()), 
				Boundary.createSymmetricBoundary(radius.getRadius()));
	}

	@Override
	public Abstract2dModel move(Coords2d delta) {
		return new Circle(this.move.move(delta), this.radius);
	}

	@Override
	protected Stream<Area2d> getInnerPointCircle(FacetGenerationContext context) {
        int numSlices = context.calculateNumberOfSlices(radius);
        Angle oneSlice = Angle.A360.divide(numSlices);
        
        List<Coords2d> points = IntStream.iterate(numSlices, x -> x-1).limit(numSlices)
        	.mapToObj(i -> radius.toCoordinate(oneSlice.mul(i)))
        	.collect(Collectors.toList());
        
        return Stream.of(new Area2d(points));
	}

}
