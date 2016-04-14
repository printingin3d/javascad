package eu.printingin3d.javascad.models;

import java.util.ArrayList;
import java.util.List;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Dims3d;
import eu.printingin3d.javascad.enums.AlignType;
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.tranzitions.Difference;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * <p>Represents support material to hold the above objects. This helps when the object cannot be printed
 * without support, but the slicer software cannot handle support material.</p>
 * <p>The resulting object is a thin continuous line which fills the given block.</p> 
 *  
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Support extends Atomic3dModel {
	private static final double SLICE_MUL = 10.0; 
	
	private final Dims3d dims;
	private final double thickness;
	
	/**
	 * Internal use only.
	 * @author ivivan <ivivan@printingin3d.eu>
	 */
	private interface IStepper {
		double nextStep(double current);
	}
	
	/**
	 * Internal use only.
	 * @author ivivan <ivivan@printingin3d.eu>
	 */
	private static class MovingStepper implements IStepper {
		private final double step;

		MovingStepper(double step) {
			this.step = step;
		}

		@Override
		public double nextStep(double current) {
			return current+step;
		}
	}
	
	/**
	 * Internal use only.
	 * @author ivivan <ivivan@printingin3d.eu>
	 */
	private static class ZigZagStepper implements IStepper {
		private final double step;

		ZigZagStepper(double step) {
			this.step = step;
		}

		@Override
		public double nextStep(double current) {
			return current>0.0 ? -step : +step;
		}
	}
	
	/**
	 * Creates a support object. The resulting object is a <i>thickness</i> thin continuous
	 * line which fills the given <i>dims</i> block. 
	 * @param dims the dimensions of the support object
	 * @param thickness the thickness of the wall to be used when creating the object
	 */
	public Support(Dims3d dims, double thickness) {
		this.dims = dims;
		this.thickness = thickness;
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Support(dims, thickness);
	}
	
	private Abstract3dModel getModel() {
		Abstract3dModel base = new Cube(dims);
		List<Abstract3dModel> slices = new ArrayList<>();
		IStepper xStepper;
		IStepper yStepper;
		Dims3d sliceSize;
		if (dims.getX()>dims.getY()) {
			long sliceNum = Math.round((dims.getX()-thickness)/thickness/SLICE_MUL);
			double dx = (dims.getX()-thickness) / sliceNum;
			sliceSize = new Dims3d(dx-thickness, dims.getY(), dims.getZ()+1);
			xStepper = new MovingStepper(dx);
			yStepper = new ZigZagStepper(thickness);
		}
		else {
			long sliceNum = Math.round((dims.getY()-thickness)/thickness/SLICE_MUL);
			double dy = (dims.getY()-thickness) / sliceNum;
			sliceSize = new Dims3d(dims.getX(), dy-thickness, dims.getZ()+1);
			xStepper = new ZigZagStepper(thickness);
			yStepper = new MovingStepper(dy);
		}
		double x = thickness;
		double y = thickness;
		while (x<dims.getX() && y<dims.getY()) {
			slices.add(new Cube(sliceSize)
					.align(new Side(AlignType.MIN_IN, AlignType.MIN_IN, AlignType.CENTER), base)
					.move(new Coords3d(x, y, 0.0))
				);
			
			x = xStepper.nextStep(x);
			y = yStepper.nextStep(y);
		}
		
		return new Difference(base, slices);
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return getModel().toScad(context);
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		double x = dims.getX()/2.0;
		double y = dims.getY()/2.0;
		double z = dims.getZ()/2.0;
		return new Boundaries3d(
				new Boundary(-x, x),
				new Boundary(-y, y),
				new Boundary(-z, z));
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return getModel().toCSG(context);
	}

}
