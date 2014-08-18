package eu.printingin3d.javascad.tranform;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.utils.AssertValue;

/**
 * A special 4x4 transformation matrix, where the 4th row is always 0,0,0,1. This is enough to represent
 * any 3D transformation and reduce the necessary calculation a lot.
 *  
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class TransformationMatrix implements ITransformation {
	private final double[] matrix;
	
	private static int getValuePosition(int row, int column) {
		AssertValue.isTrue(row>=0 && row<3, "Row number should be between 0 and 2, but was "+row);
		AssertValue.isTrue(column>=0 && column<4, "Column number should be between 0 and 3, but was "+column);
		
		return column+row*4;
	}
	
	protected TransformationMatrix(double[] matrix) {
		AssertValue.isTrue(matrix.length==12, "The representation of the matrix should contain 12 values!"); 
		
		this.matrix = matrix;
	}
	
	private double getValue(int row, int column) {
		return matrix[getValuePosition(row, column)];
	}
	
	private double getDeterminant() {
		return 
			+ getValue(0,0)*getValue(1,1)*getValue(2,2)
			+ getValue(0,1)*getValue(1,2)*getValue(2,0)
			+ getValue(0,2)*getValue(1,0)*getValue(2,1)
			- getValue(0,2)*getValue(1,1)*getValue(2,0)
			- getValue(0,1)*getValue(1,0)*getValue(2,2)
			- getValue(0,0)*getValue(1,2)*getValue(2,1)
				;
	}
	
    @Override
	public Coords3d transform(Coords3d vec) {
        double x = getValue(0,0) * vec.getX() + getValue(0,1) * vec.getY() + getValue(0,2) * vec.getZ() + getValue(0,3);
        double y = getValue(1,0) * vec.getX() + getValue(1,1) * vec.getY() + getValue(1,2) * vec.getZ() + getValue(1,3);
        double z = getValue(2,0) * vec.getX() + getValue(2,1) * vec.getY() + getValue(2,2) * vec.getZ() + getValue(2,3);

        return new Coords3d(x, y, z);
    }
    
    @Override
	public boolean isMirror() {
        return getDeterminant()<0;
    }
}
