package eu.printingin3d.javascad.models;

import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.exceptions.NotImplementedException;
import eu.printingin3d.javascad.models2d.Abstract2dModel;
import eu.printingin3d.javascad.utils.DoubleUtils;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

public class LinearExtrude extends Complex3dModel {
	private final Abstract2dModel model;
	private final double height;
	private final double twist;
	private final double scale;

	public LinearExtrude(Abstract2dModel model, double height, double twist, double scale) {
		this.model = model;
		this.height = height;
		this.twist = twist;
		this.scale = scale;
	}
	
	public LinearExtrude(Abstract2dModel model, double height, double twist) {
		this(model, height, twist, 1.0);
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new LinearExtrude(model, height, twist);
	}

	@Override
	protected SCAD innerToScad(IScadGenerationContext context) {
		return new SCAD("linear_extrude(height="+DoubleUtils.formatDouble(height)+", center=true, convexity=10, "
					+ "twist="+DoubleUtils.formatDouble(twist)+",scale="+DoubleUtils.formatDouble(scale)+")")
				.append(model.toScad(context));
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		Boundaries2d boundaries2d = model.getBoundaries2d();
		Boundary boundaryX;
		Boundary boundaryY;
		if (DoubleUtils.isZero(twist)) {
			boundaryX = boundaries2d.getX();
			boundaryY = boundaries2d.getY();
		}
		else {
			boundaryX = new Boundary(boundaries2d.getX().getMax(), -boundaries2d.getX().getMax(), 
					boundaries2d.getY().getMax(), -boundaries2d.getY().getMax());
			boundaryY = boundaryX;
		}
		
		return new Boundaries3d(
					boundaryX,
					boundaryY,
					Boundary.createSymmetricBoundary(height/2.0)
				);
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		throw new NotImplementedException();
	}

}
