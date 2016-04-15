package eu.printingin3d.javascad.models;

import java.util.ArrayList;
import java.util.List;

import eu.printingin3d.javascad.basic.Angle;
import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.utils.AssertValue;
import eu.printingin3d.javascad.utils.DoubleUtils;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;
import eu.printingin3d.javascad.vrl.Polygon;

/**
 * Represents a sphere. It is a descendant of {@link Abstract3dModel}, which means you
 * can use the convenient methods on spheres too.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Sphere extends Atomic3dModel {
	private final double r;

	/**
	 * Creates the sphere with the given radius.
	 * @param r the radius to be used
	 * @throws IllegalValueException if the given radius is negative
	 */
	public Sphere(double r) throws IllegalValueException {
		AssertValue.isNotNegative(r, "The radius of the Sphere should be positive, but is "+r);
		this.r = r;
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return new SCAD("sphere(r="+DoubleUtils.formatDouble(r)+");\n");
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		return new Boundaries3d(
				new Boundary(-r, r), 
				new Boundary(-r, r), 
				new Boundary(-r, r));
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Sphere(r);
	}

	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
        List<Polygon> polygons = new ArrayList<>();

        int numSlices = context.calculateNumberOfSlices(r);
        int numStacks = numSlices/2;
        
        Angle oneSlice = Angle.A360.divide(numSlices);
        for (int i = 0; i < numSlices; i++) {
            for (int j = 0; j < numStacks; j++) {
                List<Coords3d> vertices = getVertices(oneSlice, numStacks, i, j);
                polygons.add(Polygon.fromPolygons(vertices, context.getColor()));
            }
        }
        return new CSG(polygons);
	}

	private List<Coords3d> getVertices(Angle oneSlice, int numStacks, int i, int j) {
		List<Coords3d> vertices = new ArrayList<>();

		vertices.add(
		        sphereVertex(r, oneSlice.mul(i), oneSlice.mul(j))
		);
		if (j > 0) {
		    vertices.add(
		            sphereVertex(r, oneSlice.mul(i + 1), oneSlice.mul(j))
		    );
		}
		if (j < numStacks - 1) {
		    vertices.add(
		            sphereVertex(r, oneSlice.mul(i + 1), oneSlice.mul(j + 1))
		    );
		}
		vertices.add(
		        sphereVertex(r, oneSlice.mul(i), oneSlice.mul(j + 1))
		);
		return vertices;
	}

    private Coords3d sphereVertex(double r, Angle theta, Angle phi) {
        Coords3d dir = new Coords3d(
                theta.cos() * phi.sin(),
                phi.cos(),
                theta.sin() * phi.sin()
        );
        return dir.mul(r);
    }
}
