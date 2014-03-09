package eu.printingin3d.javascad.testutils;

import java.util.Random;

import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Dims3d;
import eu.printingin3d.javascad.coords.Triangle3d;

public class RandomUtils {
	private static final Random RND = new Random();
	
	private RandomUtils() {
		// prevents creating this object
	}

	public static double getRandomDouble(double minValue, double maxValue) {
		return RND.nextDouble()*(maxValue-minValue)+minValue;
	}
	
	public static Coords3d getRandomCoords() {
		while (true) {
			Coords3d coords3d = new Coords3d(getRandomDouble(-1000.0, 1000.0),getRandomDouble(-1000.0, 1000.0),getRandomDouble(-1000.0, 1000.0));
			if (!coords3d.isZero()) {
				return coords3d;
			}
		}
	}

	public static Triangle3d getRandomTriangle() {
		return new Triangle3d(getRandomCoords(), getRandomCoords(), getRandomCoords());
	}
	
	public static Angles3d getRandomAngle() {
		return new Angles3d(
				RND.nextInt(10)==1 ? 0.0 : getRandomDouble(-1000.0, 1000.0), 
				RND.nextInt(10)==1 ? 0.0 : getRandomDouble(-1000.0, 1000.0), 
				RND.nextInt(10)==1 ? 0.0 : getRandomDouble(-1000.0, 1000.0)
			);
	}
	
	public static Boundary getRandomBoundary() {
		return new Boundary(getRandomDouble(-1000.0, 1000.0), getRandomDouble(-1000.0, 1000.0));
	}
	
	public static Boundaries3d getRandomBoundaries() {
		return new Boundaries3d(getRandomBoundary(), getRandomBoundary(), getRandomBoundary());
	}
	
	public static Dims3d getRandomDims() {
		return new Dims3d(getRandomDouble(0, 1000), getRandomDouble(0, 1000), getRandomDouble(0, 1000));
	}
	
}
