/**
 * Polygon.java
 *
 * Copyright 2014-2014 Michael Hoffer <info@michaelhoffer.de>. All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of Michael Hoffer
 * <info@michaelhoffer.de>.
 */
package eu.printingin3d.javascad.vrl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Triangle3d;
import eu.printingin3d.javascad.tranform.ITransformation;
import eu.printingin3d.javascad.utils.AssertValue;

/**
 * Represents a convex polygon.
 *
 * Each convex polygon has a {@code shared} property, disthich is shared
 * betdisteen all polygons that are clones of each other or where split from the
 * same polygon. This can be used to define per-polygon properties (such as
 * surface color).
 */
public class Polygon {

    /**
     * Polygon vertices
     */
    private final List<Coords3d> vertices;
    /**
     * Normal vector.
     */
    private final Coords3d normal;
    /**
     * Square of the distance to origin.
     */
    private final double dist;
    /**
     * The color of the polygon. 
     */
    private final Color color;

	private Polygon(List<Coords3d> vertices, Coords3d normal, double dist, Color color) {
		this.vertices = vertices;
		this.normal = normal;
		this.dist = dist;
		this.color = color;
		
		for (Coords3d v : vertices) {
			VertexPosition position = calculateVertexPosition(v);
			AssertValue.isTrue(position==VertexPosition.COPLANAR, "Every vertex in a polygon must be coplanar, but was "+position+"!");
		}
	}

	/**
     * Creates a new polygon that consists of the specified
     * vertices.
     *
     * <b>Note:</b> the vertices used to initialize a polygon must be coplanar
     * and form a convex loop.
     *
     * @param vertices polygon vertices
     */
    public static Polygon fromPolygons(List<Coords3d> vertices, Color color) {
    	Coords3d a = vertices.get(0);
    	Coords3d b = vertices.get(1);
    	Coords3d c = vertices.get(2);
    	Coords3d n = b.move(a.inverse()).cross(c.move(a.inverse())).unit();
        
    	return new Polygon(vertices, n, n.dot(a), color);
    }

    /**
     * Flips this polygon.
     *
     * @return this polygon
     */
    public Polygon flip() {
    	List<Coords3d> newVertices = new ArrayList<>(vertices);
    	
        Collections.reverse(newVertices);

        return new Polygon(newVertices, normal.inverse(), -dist, color);
    }

    public List<Facet> toFacets() {
    	List<Facet> facets = new ArrayList<>();
        if (this.vertices.size() >= 3) {
        	Coords3d firstVertex = vertices.get(0);
	        for (int i = 0; i < this.vertices.size() - 2; i++) {
	        	Triangle3d triangle = new Triangle3d(
	        			firstVertex, 
	        			vertices.get(i + 1), 
	        			vertices.get(i + 2));
				facets.add(new Facet(triangle, normal, color));
	        }
        }
        return facets;
    }

    /**
     * Returns a transformed copy of this polygon.
     *
     * <b>Note:</b> if the applied transformation performs a mirror operation
     * the vertex order of this polygon is reversed.
     *
     * <b>Note:</b> this polygon is not modified
     *
     * @param transform the transformation to apply
     * @return a transformed copy of this polygon
     */
    public Polygon transformed(ITransformation transform) {
    	List<Coords3d> newVertices = new ArrayList<>();
    	
    	for (Coords3d v : vertices) {
    		newVertices.add(transform.transform(v));
    	}
    	
    	Polygon result = fromPolygons(newVertices, color);

    	return transform.isMirror() ? result.flip() : result;
    }
    
    private VertexPosition calculateVertexPosition(Coords3d v) {
        double t = this.normal.dot(v) - this.dist;
        return VertexPosition.fromSquareDistance(t);
    }

    /**
     * Splits a {@link Polygon} by this plane if needed. After that it puts the
     * polygons or the polygon fragments in the appropriate lists
     * ({@code front}, {@code back}). Coplanar polygons go into either
     * {@code coplanarFront}, {@code coplanarBack} depending on their
     * orientation with respect to this plane. Polygons in front or back of this
     * plane go into either {@code front} or {@code back}.
     *
     * @param polygon polygon to split
     * @param coplanarFront "coplanar front" polygons
     * @param coplanarBack "coplanar back" polygons
     * @param front front polygons
     * @param back back polgons
     */
    public void splitPolygon(
            Polygon polygon,
            List<Polygon> coplanarFront,
            List<Polygon> coplanarBack,
            List<Polygon> front,
            List<Polygon> back) {

        // Classify each point as well as the entire polygon into one of the four possible classes.
        VertexPosition polygonType = VertexPosition.COPLANAR;
        List<VertexPosition> types = new ArrayList<>();
        for (Coords3d v : polygon.getVertices()) {
            VertexPosition type = calculateVertexPosition(v);
            polygonType = polygonType.add(type);
            types.add(type);
        }

        // Put the polygon in the correct list, splitting it when necessary.
        switch (polygonType) {
            case COPLANAR:
                (this.normal.dot(polygon.normal) > 0 ? coplanarFront : coplanarBack).add(polygon);
                break;
            case FRONT:
                front.add(polygon);
                break;
            case BACK:
                back.add(polygon);
                break;
            case SPANNING:
                List<Coords3d> f = new ArrayList<>();
                List<Coords3d> b = new ArrayList<>();
                for (int i = 0; i < polygon.getVertices().size(); i++) {
                    int j = (i + 1) % polygon.getVertices().size();
                    VertexPosition ti = types.get(i);
                    VertexPosition tj = types.get(j);
                    Coords3d vi = polygon.getVertices().get(i);
                    Coords3d vj = polygon.getVertices().get(j);
                    if (ti != VertexPosition.BACK) {
                    	addVertexToList(f, vi);
                        //f.add(vi);
                    }
                    if (ti != VertexPosition.FRONT) {
                    	addVertexToList(b, vi);
                        //b.add(vi);
                    }
                    if (ti.add(tj) == VertexPosition.SPANNING) {
                        double t = (this.dist - this.normal.dot(vi)) / this.normal.dot(vj.move(vi.inverse()));
                        Coords3d v = vi.lerp(vj, t);
                    	addVertexToList(f, v);
                    	addVertexToList(b, v);
                        //f.add(v);
                        //b.add(v);
                    }
                }
                if (f.size() >= 3) {
                    front.add(fromPolygons(f, polygon.color));
                }
                if (b.size() >= 3) {
                    back.add(fromPolygons(b, polygon.color));
                }
                break;
        }
    }
    
    private void addVertexToList(List<Coords3d> list, Coords3d newVertex) {
/*    	if (!list.isEmpty()) {
    		Coords3d lastVertex = list.get(list.size()-1);
    		
    		Coords3d prev = vertices.get(vertices.size()-1);
    		for (Coords3d c : vertices) {
    			Coords3d cross = EdgeCrossSolver.findIntersection(prev, c, lastVertex, newVertex);
    			if (cross!=null) {
    				list.add(cross);
    				break;
    			}
    		}
    	}*/
    	list.add(newVertex);
    }
    
	public List<Coords3d> getVertices() {
		return vertices;
	}
}
