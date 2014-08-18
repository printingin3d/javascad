package eu.printingin3d.javascad.tranform;

import eu.printingin3d.javascad.coords.Abstract3d;
import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.tranzitions.Direction;

public class TransformationFactory {
	
	public static ITransformation getTranlationMatrix(Abstract3d delta) {
        double elemenents[] = {
                1, 0, 0, delta.getX(), 
                0, 1, 0, delta.getY(), 
                0, 0, 1, delta.getZ() 
            };
        return new TransformationMatrix(elemenents);
	}

	public static ITransformation getRotationMatrix(Angles3d angles) {
		Coords3d x = Coords3d.xOnly(1).rotate(angles);
		Coords3d y = Coords3d.yOnly(1).rotate(angles);
		Coords3d z = Coords3d.zOnly(1).rotate(angles);
		
		double elemenents[] = {
	            x.getX(), y.getX(), z.getX(), 0, 
	            x.getY(), y.getY(), z.getY(), 0, 
	            x.getZ(), y.getZ(), z.getZ(), 0, 
		};
		return new TransformationMatrix(elemenents);
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
    		return getScaleMatrix(-1, 1, 1);
    	case Y:
    		return getScaleMatrix(1, -1, 1);
    	case Z:
    		return getScaleMatrix(1, 1, -1);
    	default:
    		return getIdentityMatrix();
    	}
    }
    
    public static ITransformation getIdentityMatrix() {
    	return getScaleMatrix(1, 1, 1);
    }

}
