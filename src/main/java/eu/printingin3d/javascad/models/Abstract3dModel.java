package eu.printingin3d.javascad.models;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import eu.printingin3d.javascad.utils.RoundProperties;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * <p>Immutable implementation of IModel interface and adds convenient methods to make it easier to move or rotate
 * the 3D models. Every primitive 3D object and 3D transition extend this class.</p>
 * <p>None of the methods changing this object rather creates a new object with the changes and gives that 
 * changed object back. This make cloning and other similar techniques unnecessary.</p>
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public abstract class Abstract3dModel implements IModel {
	private int tag = 0;
	private Coords3d move = Coords3d.ZERO;
	private Angles3d rotate = Angles3d.ZERO;
	private boolean debug = false;
	private boolean background = false;
	private final Map<Plane, RoundProperties> roundingPlane = new HashMap<>();
	private final Set<String> annotations = new HashSet<>();
	
	/**
	 * Moves this object by the given coordinates. This object won't be changed, but a new object will be created.
	 * @param delta the coordinates used by the move
	 * @return the new object created
	 */
	public Abstract3dModel move(Coords3d delta) {
		Abstract3dModel result = cloneModel();
		result.move = this.move.add(delta);
		return result;
	}

	/**
	 * <p>Add moves to this model, which converts this to an {@link Union}, representing more than one model.</p>
	 * @param delta the collection of coordinates used by the move operation
	 * @return a new object which holds the moved objects
	 */
	public Abstract3dModel moves(Collection<Coords3d> delta) {
		if (!delta.isEmpty()) {
            Abstract3dModel result = new Empty3dModel();
			for (Coords3d c : delta) {
                result = result.addModel(this.move(c));
			}
			return result;
		}
		return this;
	}
	
	/**
	 * <p>Add moves to this model, which converts this to an {@link Union}, representing more than one model.</p>
	 * <p>The moved objects is annotated with the given list of annotations respectively. There 
	 * has to be an equal number of moves and annotations given, otherwise an exception is thrown.</p>
     * <p>If any item in the annotations list is null that copy of the object won't be annotated.</p>
	 * <p>It is very convenient with the use of Coords3d.createVariances. For example:</p>
	 * <code><pre>
	 * object.moves(new Coords3d(1,1,0).createVariances(), "+1+1", "+1-1", "-1+1", "-1-1")
	 * </pre></code>
	 * @param delta the collection of coordinates used by the move operation
	 * @param annotations the list of annotations to be used
	 * @return a new object which holds the moved objects
	 * @throws IllegalValueException in case the number of moves and annotations are not equal
	 */
	public Abstract3dModel moves(List<Coords3d> delta, String... annotations) {
	    return moves(delta, Arrays.asList(annotations));
	}
	
	/**
	 * <p>Add moves to this model, which converts this to an {@link Union}, representing more than one model.</p>
	 * <p>The moved objects is annotated with the given list of annotations respectively. There 
	 * has to be an equal number of moves and annotations given, otherwise an exception is thrown.</p>
	 * <p>If any item in the annotations list is null that copy of the object won't be annotated.</p>
	 * @param delta the collection of coordinates used by the move operation
	 * @param annotations the list of annotations to be used
	 * @return a new object which holds the moved objects
	 * @throws IllegalValueException in case the number of moves and annotations are not equal
	 */
	public Abstract3dModel moves(List<Coords3d> delta, List<String> annotations) {
	    AssertValue.isTrue(delta.size()==annotations.size(), 
	            "There should be the same number of moves and annotations given, "
	                    + "but "+delta.size()+" moves and "+annotations.size()+" annotations have been given.");
	    
	    if (!delta.isEmpty()) {
	        Abstract3dModel result = new Empty3dModel();
	        int i = 0;
	        for (Coords3d c : delta) {
	            result = result.addModel(this.move(c).annotate(annotations.get(i++)));
	        }
	        return result;
	    }
	    return this;
	}
	
	/**
	 * Add moves to this model, which converts this to an {@link Union}, representing more than one model.
	 * @param delta the collection of coordinates used by the move operation
	 * @return a new object which holds the moved objects
	 */
	public Abstract3dModel moves(Coords3d... delta) {
		return moves(Arrays.asList(delta));
	}
	
	/**
	 * Creates a new object by rotating this object with the given angle.
	 * @param delta the angle it will be rotated
	 * @return the new object created
	 */
	public Abstract3dModel rotate(Angles3d delta) {
		Abstract3dModel result = cloneModel();
		result.rotate = this.rotate.rotate(delta);
		result.move = this.move.rotate(delta);
		return result;
	}
	
	/**
	 * Add rotates to this model, which converts this to an {@link Union}, representing more than one model.
	 * This object won't be changed.  
	 * @param delta the collection of angles used by the rotate operation
	 * @return a new object which holds the moved objects
	 */
	public Abstract3dModel rotates(Collection<Angles3d> delta) {
		if (!delta.isEmpty()) {
			List<Abstract3dModel> newModels = new ArrayList<>();
			for (Angles3d c : delta) {
				newModels.add(this.rotate(c));
			}
			return new Union(newModels);
		}
		return this;
	}
	
	/**
	 * Add rotates to this model, which converts this to an {@link Union}, representing more than one model.
	 * This object won't be changed.  
	 * @param delta the collection of angles used by the rotate operation
	 * @return a new object which holds the moved objects
	 */
	public Abstract3dModel rotates(Angles3d... delta) {
		return rotates(Arrays.asList(delta));
	}
	
	/**
	 * Creates a new object with the debug flag set, which means it is rendered in a different color 
	 * in preview mode (it does not affect the CGAL rendering or STL export). It is quite 
	 * useful debugging Difference. 
	 * @return the new object created
	 */
	public Abstract3dModel debug() {
		Abstract3dModel result = cloneModel();
		result.debug = true;
		return result;
	}
	
	/**
	 * Creates a new object with the background flag set, which means it will render in a transparent
	 * light gray color in preview mode and is skipped in CGAL rendering or STL export.
	 * This is mainly used as a helping object for reference during the sketch. 
	 * @return the new object created
	 */
	public Abstract3dModel background() {
		Abstract3dModel result = cloneModel();
		result.background = true;
		return result;
	}
	
	protected abstract List<Abstract3dModel> getChildrenModels();
	
	protected final List<Abstract3dModel> findAnnotatedModel(String annotation) {
	    if (annotation==null) {
            return Collections.singletonList(this);
        }
	    
        List<Abstract3dModel> result = annotations.contains(annotation) ? 
                        Collections.singletonList(this) : 
                        Collections.emptyList();
        List<Abstract3dModel> children = getChildrenModels();
        if (!children.isEmpty()) {
            result = new ArrayList<>(result);
            result.addAll(children.stream()
                    .flatMap(m -> m.findAnnotatedModel(annotation).stream())
                    .map(a -> a.move(move))
                    .map(a -> a.rotate(rotate))
                    .collect(Collectors.toList()));
        }
        return result;
	}

	/**
	 * <p>Annotate this object with the given annotation.</p>
	 * <p>An object can be annotated by several annotation at the same time.
	 * A new annotation never overrides an old one.</p>
	 * <p>If the given parameter is null this method does nothing.</p>
	 * @param annotation the annotation will be used - can be null
	 * @return the new object created
	 */
	public Abstract3dModel annotate(String annotation) {
	    if (annotation==null) {
            return this;
        }
	    Abstract3dModel result = cloneModel();
	    result.annotations.add(annotation);
	    return result;
	}
	
	/**
	 * Creates a clone of this model, so after the cloning any change on it won't affect.
	 * this model
	 * @return a copy of this model
	 */
	protected final Abstract3dModel cloneModel() {
		Abstract3dModel model = innerCloneModel();
		
		model.tag = tag;
		model.move = move;
		model.rotate = rotate;
		model.debug = debug;
		model.background = background;
		model.roundingPlane.putAll(roundingPlane);
		model.annotations.addAll(annotations);
		
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
		
		if (item==null || item.isEmpty()) {
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
		if (!oneItem.isEmpty()) {
			result = result.append(Translate.getTranslate(move))
			      .append(oneItem);
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
		
		SCAD result = addMovesScad(currentContext);
		return surroundings.surroundScad(result);
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
		Boundaries3d boundaries = getModelBoundaries().rotate(rotate).move(move);
		for (RoundProperties rp : roundingPlane.values()) {
			boundaries = boundaries.add(rp.getRoundingSize());
		}
		return boundaries;
	}
	
	/**
	 * Returns true if the result will contain rotating transformation.
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
		return !move.isZero();
	}
	
	/**
	 * <p>Moves this model to the position relative to the given model. The position is controlled by
	 * the place - see {@link Side} - and inside parameters.</p>
	 * @param place where to move this model
	 * @param model the model used as a reference point
	 * @param inside controls which side of the aligned model will be aligned 
	 * @return the new object created
	 * @deprecated Use {@link #align(Side, Abstract3dModel)} instead.
	 */
	@Deprecated
	public Abstract3dModel align(Side place, Abstract3dModel model, boolean inside) {
		return move(place.calculateCoords(getBoundaries(), model.getBoundaries(), inside));
	}
	
	/**
	 * <p>Moves this model to the position relative to the given model. The position is controlled by
	 * the place - see {@link Side} - parameter.</p>
	 * @param place where to move this model
	 * @param model the model used as a reference point
	 * @return the new object created
	 */
	public Abstract3dModel align(Side place, Abstract3dModel model) {
		return move(place.calculateCoords(getBoundaries(), model.getBoundaries()));
	}
	
    /**
     * <p>Moves this model to the position relative to the annotated part of the given model. 
     * The position is controlled by the place - see {@link Side} - parameter.</p>
     * @param innerAnnotation the annotated part of this object which will be used for the positioning.
     *          Can be null which case it does filter anything and the call is equivalent to 
     *          <code>align(place, model)</code>
     * @param place where to move this model
     * @param model the model used as a reference point
     * @return the new object created
     * @throws IllegalValueException if there are more than one pieces of this model is annotated with innerAnnotation
     */
	public Abstract3dModel align(String innerAnnotation, Side place, Abstract3dModel model) {
	    Abstract3dModel annotatedModel = findAnnotatedModel(innerAnnotation).stream()
	            .reduce((a, b) -> {
	                throw new IllegalValueException("Multiple elements has been annotated with " + innerAnnotation);
	            })
	            .get();
	    return move(place.calculateCoords(annotatedModel.getBoundaries(), model.getBoundaries()));
	}
	
	/**
	 * <p>Moves this model to the position relative to the annotated part of the given model. 
	 * The position is controlled by the place - see {@link Side} - parameter.</p>
	 * <p>In case more than one pieces of the target object is annotated with externalAnnotation this model
	 * will be aligned to all of them and the returned object will be the union of those new objects.</p>
     * @param innerAnnotation the annotated part of this object which will be used for the positioning.
     *     Can be null which case it does not filter on the source side.
	 * @param place where to move this model
	 * @param model the model used as a reference point
	 * @param externalAnnotation the annotation used to filter the target object.
	 *     Can be null which case it does not filter on the target side.
	 * @return the new object created
	 * @throws IllegalValueException if there are more than one pieces of this model is annotated with innerAnnotation 
	 *         or if there are no pieces of the target model is annotated with externalAnnotation
	 */
	public Abstract3dModel align(String innerAnnotation, Side place, Abstract3dModel model, String externalAnnotation) {
	    List<Abstract3dModel> externalAnnotatedModels = model.findAnnotatedModel(externalAnnotation);
	    AssertValue.isNotEmpty(externalAnnotatedModels,
	            "No part of the model has been annotated with "+externalAnnotation);
	    Abstract3dModel result = null;
	    for (Abstract3dModel m : externalAnnotatedModels) {
	        Abstract3dModel tmp = align(innerAnnotation, place, m);
	        result = result==null ? tmp : result.addModel(tmp);
	    }
	    return result;
	}
	
	/**
	 * <p>Moves this model to the position relative to the given coordinate. The position is controlled by
	 * the place - see {@link Side}.</p>
	 * @param place where to move this model
	 * @param coords the coordinates used as a reference point
	 * @return the new object created
	 */
	public Abstract3dModel align(Side place, Coords3d coords) {
		return move(place.calculateCoords(getBoundaries(), coords));
	}
	
	/**
	 * <p>Rounding this object with the given radius. It is possible to round only on one plane, 
	 * or rounding all around the object.</p>
	 * <p>Please pay attention that the rounding increases the objects size with the given radius.</>
	 * @param plane the rounding will happen on this plane - or all around if it set to ALL.
	 * @param radius the radius of the rounding
	 * @return the newly created object
	 * @throws IllegalValueException if the given radius is negative
	 */
	public Abstract3dModel round(Plane plane, double radius) throws IllegalValueException {
		AssertValue.isNotNegative(radius, "Radius of the rounding should not be negative!");
		
		Abstract3dModel result = cloneModel();
		result.roundingPlane.put(plane, new RoundProperties(plane, radius));
		return result;
	}
	
	protected abstract CSG toInnerCSG(FacetGenerationContext context);
	
	@Override
	public final CSG toCSG(FacetGenerationContext aContext) {
		FacetGenerationContext context = aContext.applyTag(tag);
		
		CSG csg = toInnerCSG(context);
		
		if (!rotate.isZero()) {
			csg = csg.transformed(TransformationFactory.getRotationMatrix(rotate));
		}
		
		if (!move.isZero()) {
			csg = csg.transformed(TransformationFactory.getTranlationMatrix(move));
		}
		
		return csg;
	}
	
	/**
	 * Renders this model to its CSG interpretation - convenient method which used the default
	 * generation context.
	 * @return the CSG interpretation
	 */
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
		Abstract3dModel result = cloneModel();
		result.tag = tag;
		return result;
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
	 * @param side where to move this model
	 * @param model the model to be added to this object
	 * @return a new model which contains the union of this object and the given object
	 */
	public Abstract3dModel addModelTo(Side side, Abstract3dModel model) {
		return addModel(model.align(side, this));
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
	 * <p>If the given context is the {@link eu.printingin3d.javascad.context.ScadGenerationContextFactory#DEFAULT 
	 * ScadGenerationContextFactory.DEFAULT} then this method call is logically 
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
			
			model.move = move;
			model.rotate = rotate;
			
			model.roundingPlane.putAll(roundingPlane);
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
