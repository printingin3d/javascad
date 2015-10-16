package eu.printingin3d.javascad.tranform;

import eu.printingin3d.javascad.coords.Abstract3d;
import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.tranzitions.Direction;

/**
 * Utility class to create the standard transformation matrices. 
 * @author ivivan <ivivan@printingin3d.eu>
 */
public final class TransformationFactory {
	private TransformationFactory() {
		// prevent instantiating this class
	}
	
	/**
	 * Returns with a transformation representing a move by the given vector.
	 * @param delta the vector of the move
	 * @return the transformation representing the move
	 */
	public static ITransformation getTranlationMatrix(Abstract3d delta) {
        double[] elemenents = {
                1, 0, 0, delta.getX(), 
                0, 1, 0, delta.getY(), 
                0, 0, 1, delta.getZ() 
            };
        return new TransformationMatrix(elemenents);
	}

	/**
	 * Returns with a transformation representing a rotation by the given angle.
	 * @param angles the vector of the rotation
	 * @return the transformation representing the rotation
	 */
	public static ITransformation getRotationMatrix(Angles3d angles) {
		Coords3d x = Coords3d.X.rotate(angles);
		Coords3d y = Coords3d.Y.rotate(angles);
		Coords3d z = Coords3d.Z.rotate(angles);
		
		double[] elemenents = {
	            x.getX(), y.getX(), z.getX(), 0, 
	            x.getY(), y.getY(), z.getY(), 0, 
	            x.getZ(), y.getZ(), z.getZ(), 0, 
		};
		return new TransformationMatrix(elemenents);
	}
	
	/**
	 * Returns with a transformation representing a scaling by the given values.
	 * @param values the vector to be used for the scaling
	 * @return the transformation representing the scaling
	 */
    public static ITransformation getScaleMatrix(Abstract3d values) {
    	return getScaleMatrix(values.getX(), values.getY(), values.getZ());
    }
    
	/**
	 * Returns with a transformation representing a scaling by the given values.
	 * @param x the scale on the X axis
	 * @param y the scale on the Y axis
	 * @param z the scale on the Z axis
	 * @return the transformation representing the scaling
	 */
    public static ITransformation getScaleMatrix(double x, double y, double z) {
        double[] elemenents = {
            x, 0, 0, 0, 
            0, y, 0, 0, 
            0, 0, z, 0};
        return new TransformationMatrix(elemenents);
    }
    
    /**
	 * Returns with a transformation representing a mirror on the given direction.
     * @param direction the direction of the mirroring
     * @return the transformation representing a mirror
     */
    public static ITransformation getMirrorMatrix(Direction direction) {
    	return getScaleMatrix(direction.getCoords().mul(-2.0).move(new Coords3d(1, 1, 1)));
    }
    
    /**
     * Creates an identity transformation matrix.
     * @return an identity transformation matrix
     */
    public static ITransformation getIdentityMatrix() {
    	return getScaleMatrix(1, 1, 1);
    }

}
