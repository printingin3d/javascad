package eu.printingin3d.javascad.models2d;

import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.exceptions.NotImplementedException;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.utils.DoubleUtils;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

public class Circle extends Abstract2dModel {
	private final double radius;

	private Circle(Coords2d move, double radius) {
		super(move);
		this.radius = radius;
	}
	
	public Circle(double radius) {
		this(Coords2d.ZERO, radius);
	}

	@Override
	public CSG toCSG(FacetGenerationContext context) {
		throw new NotImplementedException();
	}

	@Override
	protected SCAD innerToScad(IScadGenerationContext context) {
		return new SCAD("circle(r="+DoubleUtils.formatDouble(radius)+", center=true);\n");
	}

	@Override
	protected Boundaries2d getModelBoundaries() {
		return new Boundaries2d(
				Boundary.createSymmetricBoundary(radius), 
				Boundary.createSymmetricBoundary(radius));
	}

	@Override
	public Abstract2dModel move(Coords2d delta) {
		return new Circle(this.move.move(delta), this.radius);
	}

}
