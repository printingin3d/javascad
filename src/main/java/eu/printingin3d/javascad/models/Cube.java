package eu.printingin3d.javascad.models;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Dims3d;
import eu.printingin3d.javascad.enums.AlignType;
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;
import eu.printingin3d.javascad.vrl.Polygon;

/**
 * Represents a cuboid. 
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Cube extends Atomic3dModel {
	private final Dims3d size;
	
	/**
	 * Creates a cuboid with the given corners.
	 * @param minCorner the corner with the lower coordinates
	 * @param maxCorner the corner with the higher coordinates
	 * @return a cuboid with the given corners.
	 * @throws eu.printingin3d.javascad.exceptions.IllegalValueException the minCorner has bigger value 
	 * 			then maxCorner in any coordinate (x, y or z)
	 */
	public static Abstract3dModel fromCoordinates(Coords3d minCorner, Coords3d maxCorner) {
		Abstract3dModel result = new Cube(new Dims3d(
				maxCorner.getX()-minCorner.getX(), 
				maxCorner.getY()-minCorner.getY(), 
				maxCorner.getZ()-minCorner.getZ()));
		return result.align(new Side(AlignType.MIN_IN, AlignType.MIN_IN, AlignType.MIN_IN), minCorner);
	}
	
	/**
	 * Creates a cuboid with the given size values.
	 * @param size the 3D size value used to construct the cuboid
	 */
	public Cube(Dims3d size) {
		super();
		this.size = size;
	}
	
	/**
	 * Creates a cube with the given size.
	 * @param size the size used to construct the cube
	 */
	public Cube(double size) {
		super();
		this.size = new Dims3d(size, size, size);
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return new SCAD("cube("+size+",center=true);\n");
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		double x = size.getX()/2.0;
		double y = size.getY()/2.0;
		double z = size.getZ()/2.0;
		return new Boundaries3d(
				new Boundary(-x, x),
				new Boundary(-y, y),
				new Boundary(-z, z));
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Cube(size);
	}

	private static final double P = +0.5;
	private static final double M = -0.5;
	
	private static final List<List<Coords3d>> POSITIONS = Arrays.asList(
			Arrays.asList(new Coords3d(M, M, M), new Coords3d(M, M, P), new Coords3d(M, P, P), new Coords3d(M, P, M)),
			Arrays.asList(new Coords3d(P, M, M), new Coords3d(P, P, M), new Coords3d(P, P, P), new Coords3d(P, M, P)),
			Arrays.asList(new Coords3d(M, M, M), new Coords3d(P, M, M), new Coords3d(P, M, P), new Coords3d(M, M, P)),
			Arrays.asList(new Coords3d(M, P, M), new Coords3d(M, P, P), new Coords3d(P, P, P), new Coords3d(P, P, M)),
			Arrays.asList(new Coords3d(M, M, M), new Coords3d(M, P, M), new Coords3d(P, P, M), new Coords3d(P, M, M)),
			Arrays.asList(new Coords3d(M, M, P), new Coords3d(P, M, P), new Coords3d(P, P, P), new Coords3d(M, P, P))
		);

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		return new CSG(POSITIONS.stream()
				.map(info -> info.stream().map(c -> c.mul(size)).collect(Collectors.toList()))
				.map(v -> Polygon.fromPolygons(v, context.getColor()))
				.collect(Collectors.toList())
			);
	}
}
