package eu.printingin3d.javascad.coords;


public final class Normal3d extends Abstract3d {
	public static final Normal3d X = new Normal3d(1.0, 0.0, 0.0);
	public static final Normal3d Y = new Normal3d(0.0, 1.0, 0.0);
	public static final Normal3d Z = new Normal3d(0.0, 0.0, 1.0);
	
	private Normal3d(double x, double y, double z) {
		super(x, y, z);
	}
	
	public double normalValue(Abstract3d vector) {
		return x*vector.x+y*vector.y+z*vector.z;
	}
}
