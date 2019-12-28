package eu.printingin3d.javascad.models;

import java.util.Collections;
import java.util.List;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.utils.AssertValue;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * <p>There are cases, when we want to have an object, which boundaries differ from the calculated ones.
 * An example could be a wardrobe, when we don't want to include the handles into the boundary calculation,
 * when we align the object.</p>
 * <p>This object could be used to achieve that goal.</p>
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class BoundedModel extends Complex3dModel {
	private final Abstract3dModel baseModel;
	private final Boundaries3d boundaries3d;

	/**
	 * Creates the object.
	 * @param baseModel the object used to generate the output
	 * @param boundaries3d the boundary used by the alignment methods
	 * @throws eu.printingin3d.javascad.exceptions.IllegalValueException if either of the parameters are null
	 */
	public BoundedModel(Abstract3dModel baseModel, Boundaries3d boundaries3d) {
		AssertValue.isNotNull(baseModel, "The baseModel parameter must not be null!");
		AssertValue.isNotNull(boundaries3d, "The boundaries3d parameter must not be null!");
		
		this.baseModel = baseModel;
		this.boundaries3d = boundaries3d;
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new BoundedModel(baseModel, boundaries3d);
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return baseModel.toScad(context);
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return boundaries3d;
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return baseModel.toCSG(context);
	}

	@Override
	protected Abstract3dModel innerSubModel(IScadGenerationContext context) {
		Abstract3dModel subModel = baseModel.subModel(context);
		return subModel==null ? null : new BoundedModel(subModel, boundaries3d);
	}

    @Override
    protected List<Abstract3dModel> getChildrenModels() {
        return Collections.singletonList(baseModel);
    }
}
