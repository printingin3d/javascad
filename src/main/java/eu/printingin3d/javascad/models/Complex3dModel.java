package eu.printingin3d.javascad.models;

/**
 * This class represents a complex 3D model which consists of several primitive models.
 * Every transition is a descendant of this class.
 * @author Ivan
 */
public abstract class Complex3dModel extends Abstract3dModel {

	@Override
	protected final boolean isPrimitive() {
		return false;
	}
}
