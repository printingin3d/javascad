package eu.printingin3d.javascad.tranform;

import eu.printingin3d.javascad.coords.Abstract3d;
import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.tranzitions.Direction;

public class TranformationFactory {
	
	public static ITransformation getTranlationMatrix(Abstract3d delta) {
        double elemenents[] = {
                1, 0, 0, delta.getX(), 
                0, 1, 0, delta.getY(), 
                0, 0, 1, delta.getZ() 
            };
        return new TransformationMatrix(elemenents);
	}

	private static TransformationMatrix getXRotationMatrix(double degrees) {
        double radians = -degrees * Math.PI / 180.0;
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double elemenents[] = {
            1, 0, 0, 0, 
            0, cos, sin, 0, 
            0, -sin, cos, 0
        };
        return new TransformationMatrix(elemenents);
	}
	
	private static TransformationMatrix getYRotationMatrix(double degrees) {
		double radians = -degrees * Math.PI / 180.0;
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		double elemenents[] = {
	            cos, 0, -sin, 0, 
	            0, 1, 0, 0, 
	            sin, 0, cos, 0 
		};
		return new TransformationMatrix(elemenents);
	}
	
	private static TransformationMatrix getZRotationMatrix(double degrees) {
		double radians = -degrees * Math.PI / 180.0;
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		double elemenents[] = {
	            cos, sin, 0, 0, 
	            -sin, cos, 0, 0, 
	            0, 0, 1, 0, 
		};
		return new TransformationMatrix(elemenents);
	}
	
	public static ITransformation getRotationMatrix(Angles3d angles) {
		return getXRotationMatrix(angles.getX())
				.mul(getYRotationMatrix(angles.getY()))
				.mul(getZRotationMatrix(angles.getZ()));
	}
	
    public static ITransformation getScaleMatrix(Abstract3d values) {
    	return getScaleMatrix(values.getX(), values.getY(), values.getZ());
    }
    
    public static ITransformation getScaleMatrix(double x, double y, double z) {
        double elemenents[] = {
            x, 0, 0, 0, 
            0, y, 0, 0, 
            0, 0, z, 0};
        return new TransformationMatrix(elemenents);
    }
    
    public static ITransformation getMirrorMatrix(Direction direction) {
    	switch (direction) {
    	case X:
    		return getScaleMatrix(new Coords3d(-1, 1, 1));
    	case Y:
    		return getScaleMatrix(new Coords3d(1, -1, 1));
    	case Z:
    		return getScaleMatrix(new Coords3d(1, 1, -1));
    	default:
    		return getIdentityMatrix();
    	}
    }
    
    public static ITransformation getIdentityMatrix() {
    	return getScaleMatrix(1, 1, 1);
    }

}
