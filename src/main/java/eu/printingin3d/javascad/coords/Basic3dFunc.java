package eu.printingin3d.javascad.coords;

/**
 * Basic 3D functions.
 * 
 * @param <T> just a test
 */
public abstract class Basic3dFunc <T extends Basic3dFunc<T>> extends Abstract3d {

	protected Basic3dFunc(double x, double y, double z) {
		super(x, y, z);
	}

	protected abstract T create(double x, double y, double z);
    
    /**
     * Add another value to this class and creates a new object of the same class based on that.
     * @param value the value to be added
     * @return the new value created
     */
    public T add(Basic3dFunc<?> value) {
    	return create(x+value.x, y+value.y, z+value.z);
    }

    /**
     * Returns this vector multiplied by the specified value.
     *
     * @param a the value
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return this vector multiplied by the specified value
     */
    public T mul(double a) {
        return create(x * a, y * a, z * a);
    }
    
    /**
     * Returns this vector multiplied by the given vector.
     *
     * @param a the value
     *
     * <b>Note:</b> this vector is not modified.
     *
     * @return this vector multiplied by the specified value
     */
    public T mul(Basic3dFunc<?> a) {
    	return create(x * a.x, y * a.y, z * a.z);
    }
	
	/**
	 * Return the inverse of this coordinate which means every coordinates will be negated.
	 * @return the inverse of this coordinate
	 */
	public T inverse() {
		return create(-x, -y, -z);
	}
    
    /**
     * Calculates the distance between this coordinate and the given coordinate.
     * @param d the other coordinate
     * @return the calculated distance
     */
    public double distance(Basic3dFunc<?> d) {
    	return this.add(d.inverse()).magnitude();
    }

    /**
     * Replaces the X coordinate with the given value and returns with the newly created object.
     * @param x the new X value
     * @return the newly created object.
     */
    public T withX(double x) {
    	return create(x, y, z);
    }
    
    /**
     * Replaces the Y coordinate with the given value and returns with the newly created object.
     * @param y the new Y value
     * @return the newly created object.
     */
    public T withY(double y) {
    	return create(x, y, z);
    }
    
    /**
     * Replaces the Z coordinate with the given value and returns with the newly created object.
     * @param z the new Z value
     * @return the newly created object.
     */
    public T withZ(double z) {
    	return create(x, y, z);
    }
}
