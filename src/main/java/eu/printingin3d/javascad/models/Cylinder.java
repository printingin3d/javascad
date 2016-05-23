package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.utils.AssertValue.isNotNegative;
import static eu.printingin3d.javascad.utils.AssertValue.isNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.printingin3d.javascad.basic.Angle;
import eu.printingin3d.javascad.basic.Radius;
import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.utils.DoubleUtils;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;
import eu.printingin3d.javascad.vrl.Polygon;

/**
 * Represents a cylinder, a truncated cone or a cone. It is a descendant of {@link Abstract3dModel}, 
 * which means you can use the convenient methods on cylinders too.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Cylinder extends Atomic3dModel {
	protected final double length;
	protected final Radius bottomRadius;
	protected final Radius topRadius;
	
	/**
	 * Creates a truncated cone. If one of the two radiuses is zero the result is a cone. 
	 * If the two radiuses are the same the result is a cylinder.
	 * @param length the length of the cylinder
	 * @param bottomRadius the bottom radius of the cylinder
	 * @param topRadius the top radius of the cylinder
	 * @throws IllegalValueException if the length or any any of the two radius parameter is negative 
	 */
	public Cylinder(double length, Radius bottomRadius, Radius topRadius) throws IllegalValueException {
		super();
		isNotNegative(length, "The length should be positive, but "+length);
		isNotNull(bottomRadius, "Both radius should be given, but bottomRadius was null");
		isNotNull(topRadius, "Both radius should be given, but topRadius was null");

		this.length = length;
		this.bottomRadius = bottomRadius;
		this.topRadius = topRadius;
	}
	
	/**
	 * Creates a cylinder with a given length and radius.
	 * @param length the length of the cylinder
	 * @param r the radius of the cylinder
	 * @throws IllegalValueException if the length or the radius parameter is negative 
	 */
	public Cylinder(double length, Radius r) throws IllegalValueException {
		super();
		isNotNegative(length, "The length should be positive, but "+length);
		isNotNull(r, "The radius should be given, but was null");
		
		this.length = length;
		this.bottomRadius = r;
		this.topRadius = r;
	}

	/**
	 * Creates a truncated cone. If one of the two radiuses is zero the result is a cone. 
	 * If the two radiuses are the same the result is a cylinder.
	 * @param length the length of the cylinder
	 * @param bottomRadius the bottom radius of the cylinder
	 * @param topRadius the top radius of the cylinder
	 * @throws IllegalValueException if the length or any any of the two radius parameter is negative
	 * @deprecated use the constructor with Radius parameters instead of doubles 
	 */
	@Deprecated
	public Cylinder(double length, double bottomRadius, double topRadius) throws IllegalValueException {
		this(length, Radius.fromRadius(bottomRadius), Radius.fromRadius(topRadius));
	}
	
	/**
	 * Creates a cylinder with a given length and radius.
	 * @param length the length of the cylinder
	 * @param r the radius of the cylinder
	 * @throws IllegalValueException if the length or the radius parameter is negative
	 * @deprecated use the constructor with Radius parameters instead of doubles 
	 */
	@Deprecated
	public Cylinder(double length, double r) throws IllegalValueException {
		this(length, Radius.fromRadius(r));
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		if (bottomRadius.equals(topRadius)) {
			return new SCAD("cylinder(h="+DoubleUtils.formatDouble(length)+
						", r="+bottomRadius+", center=true);\n");
		}
		return new SCAD("cylinder(h="+DoubleUtils.formatDouble(length)+
					", r1="+bottomRadius+
					", r2="+topRadius+", center=true);\n");
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		double r = Math.max(bottomRadius.getRadius(), topRadius.getRadius());
		double z = length/2.0;
		return new Boundaries3d(
				Boundary.createSymmetricBoundary(r), 
				Boundary.createSymmetricBoundary(r),
				Boundary.createSymmetricBoundary(z));
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Cylinder(length, bottomRadius, topRadius);
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		double z = length/2.0;
		
		Coords3d startV = Coords3d.zOnly(+z);
		Coords3d endV = Coords3d.zOnly(-z);
        List<Polygon> polygons = new ArrayList<>();

        int numSlices = context.calculateNumberOfSlices(topRadius.min(bottomRadius));
        for (int i = 0; i < numSlices; i++) {
            double t0 = i / (double) numSlices;
            double t1 = (i + 1) / (double) numSlices;
            polygons.add(Polygon.fromPolygons(Arrays.asList(
                    startV,
                    cylPoint(+z, topRadius, t1),
                    cylPoint(+z, topRadius, t0)
                ), context.getColor()
            ));
            polygons.add(Polygon.fromPolygons(Arrays.asList(
                    cylPoint(+z, topRadius, t0),
                    cylPoint(+z, topRadius, t1),
                    cylPoint(-z, bottomRadius, t1),
                    cylPoint(-z, bottomRadius, t0)
                
            ), context.getColor()));
            polygons.add(Polygon.fromPolygons(Arrays.asList(
                            endV,
                            cylPoint(-z, bottomRadius, t0),
                            cylPoint(-z, bottomRadius, t1)
            ), context.getColor())
            
            );
        }

        return new CSG(polygons);
	}

    private Coords3d cylPoint(double z, Radius r, double slice) {
        return r.toCoordinate(Angle.A360.mul(slice)).withZ(z);
    }
}
