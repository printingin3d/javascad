package eu.printingin3d.javascad.models;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.printingin3d.javascad.basic.Angle;
import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.coords2d.LineSegment2d;
import eu.printingin3d.javascad.enums.PointRelation;
import eu.printingin3d.javascad.models2d.Abstract2dModel;
import eu.printingin3d.javascad.models2d.Area2d;
import eu.printingin3d.javascad.utils.DoubleUtils;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;
import eu.printingin3d.javascad.vrl.Polygon;

/**
 * Linear extrude the given 2D model to create a 3D object.
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class LinearExtrude extends Atomic3dModel {
	private final Abstract2dModel model;
	private final double height;
	private final Angle twist;
	private final double scale;

	/**
	 * Constructs a 3D object based on the given parameters.
	 * @param model the 2D model to be extruded
	 * @param height the length of the extrusion. That will be the height of the resulted 3D model
	 * @param twist the rotation of the 2D model during the extrusion in degrees
	 * @param scale the scaling of the 2D model during the extrusion. 1.0 means no change.
	 */
	public LinearExtrude(Abstract2dModel model, double height, Angle twist, double scale) {
		this.model = model;
		this.height = height;
		this.twist = twist;
		this.scale = scale;
	}
	
	/**
	 * Constructs a 3D object based on the given parameters. The result is exactly the same as if you
	 * call <code>new LinearExtrude(model, height, twist, 1.0)</code>.
	 * @param model the 2D model to be extruded
	 * @param height the length of the extrusion. That will be the height of the resulted 3D model
	 * @param twist the rotation of the 2D model during the extrusion in degrees
	 */
	public LinearExtrude(Abstract2dModel model, double height, Angle twist) {
		this(model, height, twist, 1.0);
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new LinearExtrude(model, height, twist, scale);
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return new SCAD("linear_extrude(height="+DoubleUtils.formatDouble(height)
					+ ", center=true, convexity=10, "
					+ "twist="+twist
					+ ",scale="+DoubleUtils.formatDouble(scale)+")")
				.append(model.toScad(context));
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		Boundaries2d boundaries2d = model.getBoundaries2d();
		Boundary boundaryX;
		Boundary boundaryY;
		if (twist.isZero()) {
			boundaryX = boundaries2d.getX();
			boundaryY = boundaries2d.getY();
		}
		else {
			boundaryX = new Boundary(
					boundaries2d.getX().getMax(), -boundaries2d.getX().getMax(), 
					boundaries2d.getX().getMin(), -boundaries2d.getX().getMin(),
					boundaries2d.getY().getMax(), -boundaries2d.getY().getMax(),
					boundaries2d.getY().getMin(), -boundaries2d.getY().getMin());
			boundaryY = boundaryX;
		}
		
		return new Boundaries3d(
					boundaryX,
					boundaryY,
					Boundary.createSymmetricBoundary(height/2.0)
				);
	}
	
	private static List<Area2d> generateCover(Area2d area) {
		List<Area2d> result = new ArrayList<>();
		Area2d coords = area;
	
		for (int i=0;i<area.size();i++) {
			if (coords.size()<=2) {
				break;
			}
/*		while (coords.size()>2) {
			if (coords.size()==3) {
				result.add(coords);
				break;
			}*/
			Coords2d p = coords.get(0);
			Coords2d prev = p;
			int count = 0;
			for (Coords2d c : coords) {
				count++;
				if (c!=p && 
						(!area.findCrossing(new LineSegment2d(c, p), false).isEmpty() || 
							area.calculatePointRelation(Coords2d.midPoint(c, p))==PointRelation.OUTSIDE)) {
					break;
				}
				prev = c;
			}
			
			if (count>3) {
				result.add(coords.subList(p, prev));
				coords = coords.subList(prev, p);
			} else {
				coords = coords.subList(1, 0);
			}
		}
		return result;
	}
	
	@Override
	protected CSG toInnerCSG(FacetGenerationContext context) {
		Color color = context.getColor();
		List<Polygon> polygons = new ArrayList<>();
		
		for (Area2d points : model.getPointCircle(context)) {
			int numOfSteps = twist.isZero() ? 1 : context.calculateNumberOfSlices(height);
	
			double y1=-height/2;
			Angle alpha1 = Angle.ZERO;
			List<Coords3d> c1 = points.rotate(alpha1).withZ(y1);
	
			for (Area2d lc : generateCover(points.rotate(alpha1).reverse())) {
				polygons.add(Polygon.fromPolygons(lc.withZ(y1), color));
			}
			
			for (int i=1;i<=numOfSteps;i++) {
				double y2 = i*height/numOfSteps-height/2;
				Angle alpha2 = twist.mul(i).divide(numOfSteps);
				
				List<Coords3d> c2 = points.rotate(alpha2).withZ(y2);
				
				for (int t=0;t<c1.size();t++) {
					int p = (t+1) % c1.size();
					
					polygons.add(Polygon.fromPolygons(Arrays.asList(
							c1.get(t),
							c2.get(p),
							c2.get(t)
						), color));
					polygons.add(Polygon.fromPolygons(Arrays.asList(
							c1.get(t),
							c1.get(p),
							c2.get(p)
							), color));
				}
				
				c1 = c2;
				y1 = y2;
				alpha1 = alpha2;
			}
			for (Area2d lc : generateCover(points.rotate(alpha1))) {
				polygons.add(Polygon.fromPolygons(lc.withZ(y1), color));
			}
		}
		
		return new CSG(polygons);
	}
}
