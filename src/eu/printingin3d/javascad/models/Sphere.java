package eu.printingin3d.javascad.models;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.exceptions.LanguageNotSupportedException;
import eu.printingin3d.javascad.utils.AssertValue;
import eu.printingin3d.javascad.utils.DoubleUtils;

/**
 * Represents a sphere. It is a descendant of {@link Abstract3dModel}, which means you
 * can use the convenient methods on spheres too.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Sphere extends Abstract3dModel {
	private final double r;

	/**
	 * Creates the sphere with the given radius.
	 * @param r the radius to be used
	 * @throws IllegalValueException if the given radius is negative
	 */
	public Sphere(double r) throws IllegalValueException {
		AssertValue.isNotNegative(r, "The radius of the Sphere should be positive, but is "+r);
		this.r = r;
	}

	@Override
	protected String innerToScad() {
		switch (Language.getCurrent()) {
		case OpenSCAD:
			return "sphere(r="+DoubleUtils.formatDouble(r)+");\n";
		case POVRay:
			return "sphere {<0,0,0> "+DoubleUtils.formatDouble(r)+Abstract3dModel.ATTRIBUTES_PLACEHOLDER+"}";
		default:
			throw new LanguageNotSupportedException();
		}
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return new Boundaries3d(
				new Boundary(-r, r), 
				new Boundary(-r, r), 
				new Boundary(-r, r));
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Sphere(r);
	}
}
