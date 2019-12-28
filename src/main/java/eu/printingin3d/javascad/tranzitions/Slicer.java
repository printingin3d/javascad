package eu.printingin3d.javascad.tranzitions;

import java.util.Collections;
import java.util.List;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.models.Abstract3dModel;
import eu.printingin3d.javascad.models.Complex3dModel;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.tranzitions.slicer.CoverFactory;
import eu.printingin3d.javascad.utils.AssertValue;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * <p>Slice a model into pieces; the result is one of the slices. It is very useful if the model
 * is too big to be printed in one piece.</p>
 * <p>The result is based on the {@link Abstract3dModel#getBoundaries()}, so if that cannot be calculated for
 * any reason, the slice will fail. However, the result of a slice can be sliced again, so you can 
 * slice a model first in the X direction and then in the Y.</p>
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Slicer extends Complex3dModel {
	private static final double EPSILON = 10e-6;
	
	private final Abstract3dModel model;
	private final Direction direction;
	private final double lowRate;
	private final double highRate;

	/**
	 * <p>Slices the given model in the given direction. There will be piece number of equals sized slice, 
	 * and this object will represent the index of them.</p>
	 * <p>Throws IllegalValueException if the model is null; or the direction is null;
	 * or the index is negative; or the piece is negative; or the index is bigger or equals to piece.</p>
	 * @param model the model to be sliced
	 * @param direction the direction the slice should go - it can be X, Y or Z
	 * @param piece the number of pieces
	 * @param index the index of this piece - it is start from 0 to piece-1
	 */
	public Slicer(Abstract3dModel model, Direction direction, int piece, int index) {
		this(model, direction, (double)index/piece, (double)(piece-index-1)/piece);
	}

	/**
	 * <p>Slices the given model in the given direction. It will cut off lowRate portion from one side,
	 * and highRate portion from the other side. Both rates are relative to the size of the given model.</p> 
	 * <p>Throws IllegalValueException if the model is null; or the direction is null;
	 * or the rates are negative; or the sum of the two rates are bigger than one.</p>  
	 * @param model the model to be sliced
	 * @param direction the direction the slice should go - it can be X, Y or Z
	 * @param lowRate the rate of the portion which has to be cut off from the lower side
	 * @param highRate the rate of the portion which has to be cut off from the higher side
	 */
	public Slicer(Abstract3dModel model, Direction direction, double lowRate, double highRate) {
		AssertValue.isTrue(lowRate>=0.0, "The rate of the lower part should be non-negateive," +
				" but was "+lowRate);
		AssertValue.isTrue(highRate>=0.0, "The rate of the higher part should be non-negateive," +
				" but was "+highRate);
		AssertValue.isTrue(lowRate+highRate<1.0, "The two cutted off part should be less than the model," +
				" but was "+lowRate+" and "+highRate);
		AssertValue.isNotNull(model, "The model to be sliced must not be null!");
		AssertValue.isNotNull(direction, "The direction must not be null!");
		
		this.model = model;
		this.direction = direction;
		this.lowRate = lowRate;
		this.highRate = highRate;
	}
	
	private Abstract3dModel sliceModel() {
		if (lowRate<=EPSILON) {
			if (highRate<=EPSILON) {
				return null;
			}
			return CoverFactory.createCover(model, direction, highRate, false);
		}
		if (highRate<=EPSILON) {
			return CoverFactory.createCover(model, direction, lowRate, true);
		}
		return new Union(
				CoverFactory.createCover(model, direction, lowRate, true),
				CoverFactory.createCover(model, direction, highRate, false)
			);
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return new Difference(model, sliceModel()).toScad(context);
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		Boundaries3d b3d = model.getBoundaries();
		Boundary x = b3d.getX();
		Boundary y = b3d.getY();
		Boundary z = b3d.getZ();
		switch (direction) {
		case X:
			double min = b3d.getX().getMin()+b3d.getX().getSize()*lowRate;
			double max = b3d.getX().getMax()-b3d.getX().getSize()*highRate;
			x = new Boundary(min, max);
			break;
		case Y:
			min = b3d.getY().getMin()+b3d.getY().getSize()*lowRate;
			max = b3d.getY().getMax()-b3d.getY().getSize()*highRate;
			y = new Boundary(min, max);
			break;
		case Z:
		default:
			min = b3d.getZ().getMin()+b3d.getZ().getSize()*lowRate;
			max = b3d.getZ().getMax()-b3d.getZ().getSize()*highRate;
			z = new Boundary(min, max);
			break;
		}
		return new Boundaries3d(x, y, z);
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Slicer(model, direction, lowRate, highRate);
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return new Difference(model, sliceModel()).toCSG(context);
	}

	@Override
	protected Abstract3dModel innerSubModel(IScadGenerationContext context) {
		Abstract3dModel subModel = model.subModel(context);
		return subModel==null ? null : new Slicer(subModel, direction, lowRate, highRate);
	}

    @Override
    protected List<Abstract3dModel> getChildrenModels() {
        return Collections.singletonList(model);
    }
}
