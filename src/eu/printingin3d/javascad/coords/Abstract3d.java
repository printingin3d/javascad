package eu.printingin3d.javascad.coords;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import eu.printingin3d.javascad.enums.OutputFormat;
import eu.printingin3d.javascad.utils.DoubleUtils;

/**
 * The base class of all 3D related coordinate classes such as dimensions, angles or coordinates.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Abstract3d {
	protected final double x;
	protected final double y;
	protected final double z;
	
	protected Abstract3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Checks if the X coordinate is zero.
	 * @return return true if and only if the X coordinate is zero
	 */
	public final boolean isXZero() {
		return DoubleUtils.equalsEps(x, 0.0);
	}
	
	/**
	 * Checks if the Y coordinate is zero.
	 * @return return true if and only if the Y coordinate is zero
	 */
	public final boolean isYZero() {
		return DoubleUtils.equalsEps(y, 0.0);
	}
	
	/**
	 * Checks if the Z coordinate is zero.
	 * @return return true if and only if the Z coordinate is zero
	 */
	public final boolean isZZero() {
		return DoubleUtils.equalsEps(z, 0.0);
	}
	
	/**
	 * Checks if all three coordinates are zero, which means it is equals to 
	 * isXZero() && isYZero() && isZZero().
	 * @return return true if and only if all three coordinates are zero
	 */
	public final boolean isZero() {
		return isXZero() && isYZero() && isZZero();
	}
	
	/**
	 * Checks if all three coordinates are one.
	 * @return return true if and only if all three coordinates are one
	 */
	public final boolean isIdent() {
		return DoubleUtils.equalsEps(x, 1.0) && 
				DoubleUtils.equalsEps(y, 1.0) && 
				DoubleUtils.equalsEps(z, 1.0);
	}
	
	@Override
	public String toString() {
		return format(OutputFormat.SCAD);
	}
	
	public String format(OutputFormat outputFormat) {
		return new StringBuilder().append(outputFormat.getPreText()).
				append(DoubleUtils.formatDouble(x)).append(outputFormat.getSeparator()).
				append(DoubleUtils.formatDouble(y)).append(outputFormat.getSeparator()).
				append(DoubleUtils.formatDouble(z)).append(outputFormat.getPostText()).
				toString();
	}
    
    public byte[] toByteArray() throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN);

		byteBuffer.putFloat((float)x);
		byteBuffer.putFloat((float)y);
		byteBuffer.putFloat((float)z);
    	
    	return byteBuffer.array();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Abstract3d other = (Abstract3d) obj;
		
		return DoubleUtils.equalsEps(x, other.x) &&
				DoubleUtils.equalsEps(y, other.y) &&
				DoubleUtils.equalsEps(z, other.z);
	}

	/**
	 * @return the X coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the Y coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the Z coordinate
	 */
	public double getZ() {
		return z;
	}

    /**
     * Returns the dot product of this vector and the specified vector.
     *
     * @param a the second vector
     *
     * @return the dot product of this vector and the specified vector
     */
    public double dot(Abstract3d a) {
        return this.x * a.x + this.y * a.y + this.z * a.z;
    }

    /**
     * Returns the magnitude of this vector.
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return the magnitude of this vector
     */
    public double magnitude() {
        return Math.sqrt(this.dot(this));
    }
}
