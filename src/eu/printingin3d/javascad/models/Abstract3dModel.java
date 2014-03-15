package eu.printingin3d.javascad.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.enums.Plane;
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.tranzitions.Rotate;
import eu.printingin3d.javascad.tranzitions.Translate;
import eu.printingin3d.javascad.utils.AssertValue;
import eu.printingin3d.javascad.utils.Moves;
import eu.printingin3d.javascad.utils.RoundProperties;

/**
 * <p>Implements IModel interface and adds convenient methods to make it easier to move or rotate
 * the 3D models. Every primitive 3D object and 3D transition extend this class.</p>
 * <p>It can even represent more than one model on certain cases - see {@link #moves(Collection)}.</p>
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public abstract class Abstract3dModel implements IModel {
	/**
	 * The placeholder of the attributes for the POVRay rendering.
	 */
	public static final String ATTRIBUTES_PLACEHOLDER = "#attributes";
	
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
		moves.move(delta);
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
			moves.moves(delta);
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
		this.moves.rotate(delta);
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
		
		model.moves = moves.cloneMoves();
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
	protected abstract String innerToScad();
	
	private String getOne() {
		String item = innerToScad();
		if (item==null || item.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		if (!rotate.isZero()) {
			sb.append(Rotate.getRotate(rotate));
		}
		sb.append(item);
		
		return sb.toString();
	}
	
	private boolean addMovesScad(StringBuilder sb) {
		String oneItem = getOne();
		if (oneItem!=null && !oneItem.isEmpty()) {
			for (Coords3d coord : moves) {
				sb.append(Translate.getTranslate(coord))
				  .append(oneItem);
			}
			return true;
		}
		return false;
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
	public final String toScad() {
		StringBuilder sb = new StringBuilder(getPrefix());
		StringBuilder ending = new StringBuilder();
		if (!roundingPlane.isEmpty()) {
			sb.append("minkowski() {");
			ending.insert(0, "}");
			for (RoundProperties rp : roundingPlane.values()) {
				ending.insert(0, rp.getRounding());
			}
		}
		if (isMulti()) {
			sb.append("union(){");
			ending.insert(0, '}');
		}
		if (!addMovesScad(sb)) {
			return "";
		}
		
		return sb.toString()+ending.toString();
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
}
