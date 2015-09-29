package eu.printingin3d.javascad.coords;

import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.utils.DoubleUtils;


public class EdgeCrossSolver {
	
	private static class IntersectionResult {
		private final boolean allX;
		private final double x;
		private final double y;
		
		public IntersectionResult(boolean allX, double x, double y) {
			this.allX = allX;
			this.x = x;
			this.y = y;
		}

		public boolean matchX(IntersectionResult value) {
			return allX || value.allX || doubleEquals(x, value.x);
		}
		
		public boolean matchXY(IntersectionResult value) {
			return allX || doubleEquals(x, value.y);
		}
		
		public boolean matchY(IntersectionResult value) {
			return doubleEquals(y, value.y);
		}
	}
	
	
	public static Coords3d findIntersection(Coords3d e1v1, Coords3d e1v2, Coords3d e2v1, Coords3d e2v2) {
		IntersectionResult xy = findIntersection(new Coords2d(e1v1.getX(), e1v1.getY()), new Coords2d(e1v2.getX(), e1v2.getY()), 
									   new Coords2d(e2v1.getX(), e2v1.getY()), new Coords2d(e2v2.getX(), e2v2.getY()));
		IntersectionResult yz = findIntersection(new Coords2d(e1v1.getY(), e1v1.getZ()), new Coords2d(e1v2.getY(), e1v2.getZ()), 
									   new Coords2d(e2v1.getY(), e2v1.getZ()), new Coords2d(e2v2.getY(), e2v2.getZ()));
		IntersectionResult xz = findIntersection(new Coords2d(e1v1.getX(), e1v1.getZ()), new Coords2d(e1v2.getX(), e1v2.getZ()), 
									   new Coords2d(e2v1.getX(), e2v1.getZ()), new Coords2d(e2v2.getX(), e2v2.getZ()));
		
		if (xy!=null && yz!=null && xz!=null &&
				xy.matchX(xz) &&
				yz.matchXY(xy) &&
				yz.matchY(xz)) {
			Coords3d result = new Coords3d(xy.allX ? xz.x : xy.x, xy.y, xz.y);
			return result;
		}
		return null;
	}
	
	private static IntersectionResult findIntersection(Coords2d c1, Coords2d c2, Coords2d c3, Coords2d c4) {
		double x;
		double y;
		
		if (doubleEquals(c1.getY(), c2.getY())) {
			if (doubleEquals(c3.getY(), c4.getY())) {
				return doubleEquals(c1.getY(), c3.getY()) ? new IntersectionResult(true, 0.0, c1.getY()) : null;
			}
			y = c1.getY();
			double a2 = (c4.getX()-c3.getX()) / (c4.getY()-c3.getY());
			x = a2*y - a2*c3.getY()+c3.getX();
		}
		else if (doubleEquals(c3.getY(), c4.getY())) {
			double a1 = (c2.getX()-c1.getX()) / (c2.getY()-c1.getY());
			y = c3.getY();
			x = a1*y - a1*c1.getY()+c1.getX();
		}
		else {
			double a2 = (c4.getX()-c3.getX()) / (c4.getY()-c3.getY());
			double a1 = (c2.getX()-c1.getX()) / (c2.getY()-c1.getY());
			y = (a1*c1.getY()-a2*c3.getY()+c3.getX()-c1.getX()) / (a1-a2);
			x = a2*y - a2*c3.getY()+c3.getX();
		}
		
		if (DoubleUtils.between(x, c1.getX(), c2.getX()) && DoubleUtils.between(x, c3.getX(), c4.getX()) &&
			DoubleUtils.between(y, c1.getY(), c2.getY()) && DoubleUtils.between(y, c3.getY(), c4.getY())) {
			return new IntersectionResult(false, x, y);
		}
		return null;
	}	
	
	private static boolean doubleEquals(double value1, double value2) {
		return Math.abs(value1-value2)<1E-6;
	}
}
