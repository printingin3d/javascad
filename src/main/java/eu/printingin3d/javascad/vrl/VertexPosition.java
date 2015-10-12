package eu.printingin3d.javascad.vrl;

/**
 * Enum to mark a vertex position relative to the plane of the splitting.
 * @author ivivan <ivivan@printingin3d.eu>
 */
public enum VertexPosition {
	/**
	 * The vertex is on the plane.
	 */
    COPLANAR,
    /**
     * The vertex is in front of the plane.
     */
    FRONT,
    /**
     * The vertex is behind the plane.
     */
    BACK,
    /**
     * This is denotes the case when there are vertexes both in front and behind the plane.
     */
    SPANNING;

    /**
     * <p>This method helps to calculate the position of the polygon by adding two vertex position enum and
     * calculating the result of the two position.</p>
     * <p>This operation is commutative, so <code>a.add(b)</code> gives always the same result as 
     * <code>b.add(a)</code>.</p>
     * @param v the vertex position to add
     * @return the calculated polygon position
     */
    public VertexPosition add(VertexPosition v) {
    	if (v==this || v==COPLANAR) {
			return this;
		}
    	if (this==COPLANAR) {
			return v;
		}
    	
    	return SPANNING;
    }
    
    private static final double EPSILON = 1e-6;
    
    /**
     * Determine the vertex position based on the squared distance from the plane.
     * @param dist the square distance from the plane
     * @return the position of the vertex
     */
    public static VertexPosition fromSquareDistance(double dist) {
    	return dist < -EPSILON ? BACK : dist > EPSILON ? FRONT : COPLANAR;
    }
}
