package eu.printingin3d.javascad.models;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.enums.Plane;
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.tranform.TransformationFactory;
import eu.printingin3d.javascad.tranzitions.Colorize;
import eu.printingin3d.javascad.tranzitions.Difference;
import eu.printingin3d.javascad.tranzitions.Rotate;
import eu.printingin3d.javascad.tranzitions.Translate;
import eu.printingin3d.javascad.tranzitions.Union;
import eu.printingin3d.javascad.utils.AssertValue;
import eu.printingin3d.javascad.utils.Moves;
import eu.printingin3d.javascad.utils.RoundProperties;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * <p>Implements IModel interface and adds convenient methods to make it easier to move or rotate
 * the 3D models. Every primitive 3D object and 3D transition extend this class.</p>
 * <p>It can even represent more than one model on certain cases - see {@link #moves(Collection)}.</p>
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public abstract class Abstract3dModel implements IModel {
	private int tag = 0;
	private Moves moves = new Moves();
	private Angles3d rotate = Angles3d.ZERO;
	private boolean debug = false;
	private boolean background = false;
	private final Map<Plane, RoundProperties> roundingPlane = new HashMap<>();
	
	/**
	 * Moves this object by the given coordinates.
	 * @param delta the coordinates used by the move
	 * @return return this object to make it possible to chain more method call
	 */
	public Abstract3dModel move(Coords3d delta) {
		moves = moves.move(delta);
		return this;
	}

	/**
	 * Add moves to this model, which makes this a multi-model, representing more than one model.
	 * This operation multiplies the number of models represented by this object by the number of
	 * coordinates in the delta parameter.
	 * Please be aware that not every method works on multi-models, see {@link #isMulti()}  
	 * @param delta the collection of coordinates used by the move operation
	 * @return this object to make it possible to chain more method call
	 */
	public Abstract3dModel moves(Collection<Coords3d> delta) {
		if (!delta.isEmpty()) {
			moves = moves.moves(delta);
		}
		return this;
	}
	
	/**
	 * Rotates the current model.
	 * @param delta the angle it will be rotated
	 * @return this object to make it possible to chain more method call
	 */
	public Abstract3dModel rotate(Angles3d delta) {
		this.rotate = this.rotate.rotate(delta);
		this.moves = this.moves.rotate(delta);
		return this;
	}
	
	/**
	 * Denotes this object to be debugged, which means it is rendered in a different color 
	 * in preview mode (it does not affect the CGAL rendering or STL export). It is quite 
	 * useful debugging Difference. 
	 * @return this object to make it possible to chain more method call
	 */
	public Abstract3dModel debug() {
		this.debug = true;
		return this;
	}
	
	/**
	 * Denotes this object as a background object, which means it will render in a transparent
	 * light gray color in preview mode and is skipped in CGAL rendering or STL export.
	 * This is mainly used as a helping object for reference during the sketch. 
	 * @return this object to make it possible to chain more method call
	 */
	public Abstract3dModel background() {
		this.background = true;
		return this;
	}
	
	/**
	 * Tells if this object represents a multi-model.
	 * @return true if and only if the result of this model will be more than one model after rendering
	 */
	public final boolean isMulti() {
		return moves.isMulti();
	}
	
	/**
	 * Creates a clone of this model, so after the cloning any change on it won't affect.
	 * this model
	 * @return a copy of this model
	 */
	public final Abstract3dModel cloneModel() {
		Abstract3dModel model = innerCloneModel();
		
		model.tag = tag;
		model.moves = moves;
		model.rotate = rotate;
		model.debug = debug;
		model.background = background;
		
		return model;
	}

	protected abstract Abstract3dModel innerCloneModel();
	
	/**
	 * Generates the OpenSCAD representation of the model without.
	 * and moves or rotations 
	 * @return the representation of the model
	 */
	protected abstract SCAD innerToScad(IColorGenerationContext context);
	
	/**
	 * Returns true if and only if this object represents a primitive, atomic 3D object.
	 * This method used internally and must return true in build in primitives.
	 * @return true if and only if this object represents a primitive, atomic 3D object
	 */
	protected abstract boolean isPrimitive();
	
	private SCAD getOne(IColorGenerationContext context) {
		SCAD item = innerToScad(context);
		
		if (item==null || !item.isIncluded()) {
			return SCAD.EMPTY;
		}
		
		if (!rotate.isZero()) {
			item = item.prepend(Rotate.getRotate(rotate));
		}
		
		return item;
	}
	
	private SCAD addMovesScad(IColorGenerationContext context) {
		SCAD oneItem = getOne(context);
		SCAD result = SCAD.EMPTY;
		if (oneItem.isIncluded()) {
			for (Coords3d coord : moves) {
				result = result.append(Translate.getTranslate(coord))
				      .append(oneItem);
			}
		}
		return result;
	}
	
	private String getPrefix() {
		StringBuilder sb = new StringBuilder();
		if (debug) {
			sb.append("# ");
		}
		if (background) {
			sb.append("% ");
		}
		return sb.toString();
	}
	
	@Override
	public final SCAD toScad(IColorGenerationContext context) {
		IColorGenerationContext currentContext = context.applyTag(tag);
		
		ScadSurroundings surroundings = ScadSurroundings.EMPTY.appendPrefix(getPrefix())
				.include(getScadColor(currentContext))
				.include(getScadRounding());
		
		if (isMulti()) {
			surroundings = surroundings.appendPrefix("union(){")
					.appendPostfix("}");
		}
		SCAD result = addMovesScad(currentContext);
		if (result.isIncluded()) {
			return surroundings.surroundScad(result);
		}
		return SCAD.EMPTY;
	}
	
	private ScadSurroundings getScadColor(IColorGenerationContext currentContext) {
		ScadSurroundings surroundings = ScadSurroundings.EMPTY;
		
		if (isPrimitive()) {
			Color color = currentContext.getColor();
			if (color!=null) {
				surroundings = surroundings.appendPrefix(Colorize.getStringRepresentation(color) + "{\n")
						.appendPostfix("}\n");
			}
		}
		return surroundings;
	}
	
	private ScadSurroundings getScadRounding() {
		ScadSurroundings surroundings = ScadSurroundings.EMPTY;
		
		if (!roundingPlane.isEmpty()) {
			surroundings = surroundings.appendPrefix("minkowski() {")
					.appendPostfix("}");
			for (RoundProperties rp : roundingPlane.values()) {
				surroundings = surroundings.appendPostfix(rp.getRounding());
			}
		}
		return surroundings;
	}
	
	protected abstract Boundaries3d getModelBoundaries();
	
	/**
	 * Calculate the including cuboid for the current model. Rotation is not yet supported.
	 * @return the calculated boundaries
	 */
	public final Boundaries3d getBoundaries() {
		Boundaries3d b = getModelBoundaries().rotate(rotate);
		List<Boundaries3d> bounds = new ArrayList<>();
		for (Coords3d d : moves) {
			bounds.add(b.move(d));
		}
		Boundaries3d boundaries = Boundaries3d.combine(bounds);
		for (RoundProperties rp : roundingPlane.values()) {
			boundaries = boundaries.add(rp.getRoundingSize());
		}
		return boundaries;
	}
	
	/**
	 * Returns true if the result will contain rotating transformation
	 * @return true if the result will contain rotating transformation
	 */
	public boolean isRotated() {
		return !rotate.isZero();
	}
	
	/**
	 * Returns true if this object is moved false otherwise.
	 * @return true if this object is moved false otherwise
	 */
	public boolean isMoved() {
		return moves.isMoved();
	}
	
	/**
	 * <p>Moves this model to the position relative to the given model. The position is controlled by
	 * the place - see {@link Side} - and inside parameters.</p>
	 * @param place where to move this model
	 * @param model the model used as a reference point
	 * @param inside controls which side of the aligned model will be aligned 
	 * @return this object to make it possible to chain more method call
	 */
	public Abstract3dModel align(Side place, Abstract3dModel model, boolean inside) {
		move(place.calculateCoords(getBoundaries(), model.getBoundaries(), inside));
		return this;
	}
	
	/**
	 * <p>Moves this model to the position relative to the given coordinate. The position is controlled by
	 * the place - see {@link Side}.</p>
	 * @param place where to move this model
	 * @param coords the coordinates used as a reference point
	 * @return this object to make it possible to chain more method call
	 */
	public Abstract3dModel align(Side place, Coords3d coords) {
		move(place.calculateCoords(getBoundaries(), coords));
		return this;
	}
	
	/**
	 * <p>Rounding this object with the given radius. It is possible to round only on one plane, 
	 * or rounding all around the object.</p>
	 * <p>Please pay attention that the rounding increases the objects size with the given radius.</>
	 * @param plane the rounding will happen on this plane - or all around if it set to ALL.
	 * @param radius the radius of the rounding
	 * @return this object to make it possible to chain more method call
	 * @throws IllegalValueException if the given radius is negative
	 */
	public Abstract3dModel round(Plane plane, double radius) throws IllegalValueException {
		AssertValue.isNotNegative(radius, "Radius of the rounding should not be negative!");
		
		roundingPlane.put(plane, new RoundProperties(plane, radius));
		return this;
	}
	
	protected abstract CSG toInnerCSG(FacetGenerationContext context);
	
	@Override
	public final CSG toCSG(FacetGenerationContext aContext) {
		FacetGenerationContext context = aContext.applyTag(tag);
		
		CSG csg = toInnerCSG(context);
		
		if (!rotate.isZero()) {
			csg = csg.transformed(TransformationFactory.getRotationMatrix(rotate));
		}
		
		if (isMulti()) {
			CSG union = null;
			for (Coords3d move : moves) {
				CSG transformed = null;
				if (!move.isZero()) {
					transformed = csg.transformed(TransformationFactory.getTranlationMatrix(move));
				}
				else {
					transformed = csg;
				}
				union = (union==null) ? transformed : union.union(transformed);
			}
			csg = union;
		} else {
			for (Coords3d move : moves) {
				if (!move.isZero()) {
					csg = csg.transformed(TransformationFactory.getTranlationMatrix(move));
				}
			}
		}
		
		return csg;
	}
	
	public final CSG toCSG() {
		return toCSG(FacetGenerationContext.DEFAULT);
	}

	/**
	 * Tag the model with the given value. This value can be used to color objects 
	 * and include / exclude them from the export.
	 * @param tag the value to be used
	 * @return this object to make it possible to chain more method call 
	 */
	public Abstract3dModel withTag(int tag) {
		this.tag = tag;
		return this;
	}

	/**
	 * Convenient method to create a Union.
	 * @param model the model to be added to this object
	 * @return a new model which contains the union of this object and the given object
	 */
	public Abstract3dModel addModel(Abstract3dModel model) {
		return new Union(this, model);
	}
	
	/**
	 * Convenient method to create a Union. Adding the given model to the side of this model. Calling
	 * <code>model1.addModelTo(side, model2)</code> is always equivalent to 
	 * <code>model1.addModel(model2.align(side, model1, false))</code>.
	 * @see Abstract3dModel#addModel(Abstract3dModel)
	 * @param model the model to be added to this object
	 * @return a new model which contains the union of this object and the given object
	 */
	public Abstract3dModel addModelTo(Side side, Abstract3dModel model) {
		return addModel(model.align(side, this, false));
	}
	
	/**
	 * Convenient method to create a Difference.
	 * @param model the model to be subtracted to this object
	 * @return a new model which contains the difference of this object and the given object
	 */
	public Abstract3dModel subtractModel(Abstract3dModel model) {
		return new Difference(this, model);
	}

	protected abstract Abstract3dModel innerSubModel(IScadGenerationContext context);

	/**
	 * <p>Copies parts of the model to a new model based on the given context. It is very useful if we want
	 * to use a tagged part of the model as a separate model. Lots of things can be done to the new model:
	 * we can render it or we can use it to align to it.</p>
	 * <p>If the given context is the {@link ScadGenerationContextFactory#DEFAULT} then this method call is logically 
	 * equivalent to a {@link #cloneModel()} method call.</p>
	 * @param context the context to be used as a filter during the copy process.
	 * @return a copy of the selected parts of this model
	 */
	public Abstract3dModel subModel(IScadGenerationContext context) {
		IScadGenerationContext currentContext = context.applyTag(tag);

		Abstract3dModel model = innerSubModel(currentContext);
		if (model!=null) {
			model.tag = tag;
			model.debug = debug;
			model.background = background;
			
			model.moves = moves;
			model.rotate = rotate;
		}
		
		return model;
	}

	/**
	 * For testing purposes only.
	 * @return the tag of the model
	 */
	protected int getTag() {
		return tag;
	}

	/**
	 * For testing purposes only.
	 * @return the debug flag of the model
	 */
	protected boolean isDebug() {
		return debug;
	}

	/**
	 * For testing purposes only.
	 * @return the background flag of the model
	 */
	protected boolean isBackground() {
		return background;
	}
}
