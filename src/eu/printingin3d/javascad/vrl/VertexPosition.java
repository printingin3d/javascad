package eu.printingin3d.javascad.vrl;

public enum VertexPosition {
    COPLANAR,
    FRONT,
    BACK,
    SPANNING;

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
    
    public static VertexPosition fromSquareDistance(double dist) {
    	return (dist < -EPSILON) ? BACK : (dist > EPSILON) ? FRONT : COPLANAR;
    }
}
