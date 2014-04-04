package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.utils.AssertValue.isNotNegative;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.utils.DoubleUtils;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;
import eu.printingin3d.javascad.vrl.Polygon;
import eu.printingin3d.javascad.vrl.Vertex;

/**
 * Represents a cylinder, a truncated cone or a cone. It is a descendant of {@link Abstract3dModel}, 
 * which means you can use the convenient methods on cylinders too.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Cylinder extends Abstract3dModel {
	protected final double length;
	protected final double bottomRadius;
	protected final double topRadius;
	
	/**
	 * Creates a truncated cone. If one of the two radiuses is zero the result is a cone. 
	 * If the two radiuses are the same the result is a cylinder.
	 * @param length the length of the cylinder
	 * @param bottomRadius the bottom radius of the cylinder
	 * @param topRadius the top radius of the cylinder
	 * @throws IllegalValueException if the length or any any of the two radius parameter is negative 
	 */
	public Cylinder(double length, double bottomRadius, double topRadius) throws IllegalValueException {
		super();
		isNotNegative(length, "The length should be positive, but "+length);
		isNotNegative(bottomRadius, "Both radius should be positive, but bottom radius is "+bottomRadius);
		isNotNegative(topRadius, "Both radius should be positive, but top radius is "+topRadius);
		
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
	public Cylinder(double length, double r) throws IllegalValueException {
		super();
		isNotNegative(length, "The length should be positive, but "+length);
		isNotNegative(r, "The radius should be positive, but r1 is "+r);
		
		this.length = length;
		this.bottomRadius = r;
		this.topRadius = r;
	}

	@Override
	protected String innerToScad() {
		if (DoubleUtils.equalsEps(bottomRadius, topRadius)) {
			return "cylinder(h="+DoubleUtils.formatDouble(length)+
						", r="+DoubleUtils.formatDouble(bottomRadius)+", center=true);\n";
		}
		return "cylinder(h="+DoubleUtils.formatDouble(length)+
					", r1="+DoubleUtils.formatDouble(bottomRadius)+
					", r2="+DoubleUtils.formatDouble(topRadius)+", center=true);\n";
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		double r = Math.max(bottomRadius, topRadius);
		double z = length/2.0;
		return new Boundaries3d(
				new Boundary(-r, r), 
				new Boundary(-r, r),
				new Boundary(-z, z));
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Cylinder(length, bottomRadius, topRadius);
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		double z = length/2.0;
		
        Vertex startV = new Vertex(Coords3d.zOnly(+z), Coords3d.zOnly(+1.0));
        Vertex endV = new Vertex(Coords3d.zOnly(-z), Coords3d.zOnly(-1.0));
        List<Polygon> polygons = new ArrayList<>();

        int numSlices = context.calculateNumberOfSlices(Math.min(topRadius, bottomRadius));
        for (int i = 0; i < numSlices; i++) {
            double t0 = i / (double) numSlices;
            double t1 = (i + 1) / (double) numSlices;
            polygons.add(new Polygon(Arrays.asList(
                    startV,
                    cylPoint(+z, topRadius, t0, -1),
                    cylPoint(+z, topRadius, t1, -1))
            ));
            polygons.add(new Polygon(Arrays.asList(
                    cylPoint(+z, topRadius, t1, 0),
                    cylPoint(+z, topRadius, t0, 0),
                    cylPoint(-z, bottomRadius, t0, 0),
                    cylPoint(-z, bottomRadius, t1, 0))
            ));
            polygons.add(new Polygon(
                            endV,
                            cylPoint(-z, bottomRadius, t1, 1),
                            cylPoint(-z, bottomRadius, t0, 1)
            )
            );
        }

        return new CSG(polygons);
	}

    private Vertex cylPoint(double z, double r, double slice, double normalBlend) {
        double angle = slice * Math.PI * 2;
        Coords3d out = new Coords3d(Math.cos(angle), Math.sin(angle), 0.0);
        Coords3d pos = out.mul(r).move(Coords3d.zOnly(z));
        Coords3d normal;
        if (normalBlend==0) {
        	normal = out;
        }
        else {
        	normal = Coords3d.zOnly(normalBlend);
        }
        return new Vertex(pos, normal);
    }
}
