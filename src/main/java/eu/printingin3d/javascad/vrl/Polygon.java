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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Triangle3d;
import eu.printingin3d.javascad.coords2d.LineSegment;
import eu.printingin3d.javascad.tranform.ITransformation;
import eu.printingin3d.javascad.utils.AssertValue;

/**
 * Represents a convex polygon. A polygon is represented by its points which should be on the same plane 
 * (not tested, but guaranteed by the used algorithms), its normal (calculated from the points), 
 * its color and its distance from the origin.
 *
 */
public final class Polygon {

    /**
     * Polygon vertices.
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
		
		vertices.stream()
			.map(v -> calculateVertexPosition(v))
			.filter(p -> p!=VertexPosition.COPLANAR)
			.findFirst()
			.ifPresent(p->AssertValue.fail("Every vertex in a polygon must be coplanar, but was "+p+"!"));
	}

	/**
     * Creates a new polygon that consists of the specified vertices.
     *
     * <b>Note:</b> the vertices used to initialize a polygon must be coplanar
     * and form a convex loop.
     *
     * @param vertices polygon vertices
     * @param color the color of the polygon
     * @return a new polygon that consists of the specified vertices
     */
    public static Polygon fromPolygons(List<Coords3d> vertices, Color color) {
    	AssertValue.isTrue(vertices.size()>=3, "The coordinate list should contain at least 3 points.");
    	
    	Coords3d a = vertices.get(0);
    	Coords3d b = vertices.get(1);
    	Coords3d c = vertices.get(2);
    	Coords3d n = b.add(a.inverse()).cross(c.add(a.inverse())).unit();
        
    	return new Polygon(vertices, n, n.dot(a), color);
    }

    /**
     * Flips this polygon.
     *
     * @return a new polygon with the vertices in reversed order
     */
    public Polygon flip() {
    	List<Coords3d> newVertices = new ArrayList<>(vertices);
    	
        Collections.reverse(newVertices);

        return new Polygon(newVertices, normal.inverse(), -dist, color);
    }

    /**
     * Converts this polygon to triangles.
     * @return a list of triangles
     */
    public List<Facet> toFacets() {
        if (this.vertices.size() >= 3) {
        	Coords3d firstVertex = vertices.get(0);
        	
        	return IntStream.range(0, this.vertices.size()-2)
        		.mapToObj(i -> new Triangle3d(
	        			firstVertex, 
	        			vertices.get(i + 1), 
	        			vertices.get(i + 2)))
        		.map(tri -> new Facet(tri, normal, color))
        		.collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    protected List<Coords3d> getVertices() {
    	return vertices;
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
    	Polygon result = fromPolygons(
    		vertices.stream()
    			.map(v -> transform.transform(v))
    			.collect(Collectors.toList()), color);

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
     * @param coplanar coplanar polygons
     * @param fb front and back polygons
     */
    public void splitPolygon(Polygon polygon, List<Polygon> coplanar, FrontBack<Polygon> fb) {

        // Classify each point as well as the entire polygon into one of the four possible classes.
        VertexPosition polygonType = calculatePolygonPosition(polygon);

        // Put the polygon in the correct list, splitting it when necessary.
        switch (polygonType) {
            case COPLANAR:
                coplanar.add(polygon);
                break;
            case FRONT:
            	fb.addToFront(polygon);
                break;
            case BACK:
            	fb.addToBack(polygon);
                break;
            case SPANNING:
            	splitPolygonLowLevel(polygon, fb);
                break;
		default:
			break;
        }
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
     * @param fb front and back polygons
     */
    public void splitPolygon(Polygon polygon, FrontBack<Polygon> fb) {
    	
    	// Classify each point as well as the entire polygon into one of the four possible classes.
    	VertexPosition polygonType = calculatePolygonPosition(polygon);
    	
    	// Put the polygon in the correct list, splitting it when necessary.
    	switch (polygonType) {
    	case COPLANAR:
    		if (this.normal.dot(polygon.normal) > 0) {
				fb.addToFront(polygon);
			} else {
				fb.addToBack(polygon);
			}
    		break;
    	case FRONT:
    		fb.addToFront(polygon);
    		break;
    	case BACK:
    		fb.addToBack(polygon);
    		break;
    	case SPANNING:
    		splitPolygonLowLevel(polygon, fb);
    		break;
    	default:
    		break;
    	}
    }
    
    // Classify the entire polygon into one of the four possible classes.
    private VertexPosition calculatePolygonPosition(Polygon polygon) {
    	return polygon.vertices.stream().
    			map(v -> calculateVertexPosition(v)).
    			reduce(VertexPosition.COPLANAR, VertexPosition::add);
    }

	private void splitPolygonLowLevel(Polygon polygon, FrontBack<Polygon> polyFb) {
		FrontBack<Coords3d> fb = new FrontBack<>();
		LineSegment.lineSegmentSeries(polygon.vertices).forEach(ls -> classifyAndSplitVertex(ls, fb));
		fb.addTo(list -> fromPolygons(list, polygon.color), polyFb);
	}

	private void classifyAndSplitVertex(LineSegment<Coords3d> ls, FrontBack<Coords3d> fb) {
		VertexPosition position = calculateVertexPosition(ls.getStart());
		
		switch (position) {
		case FRONT:
			fb.addToFront(ls.getStart());
			break;
		case BACK:
			fb.addToBack(ls.getStart());
			break;
		default:
			fb.addToBoth(ls.getStart());
			break;
		}
		if (position.add(calculateVertexPosition(ls.getEnd())) == VertexPosition.SPANNING) {
		    double t = (this.dist - this.normal.dot(ls.getStart())) / 
		    				this.normal.dot(ls.getEnd().add(ls.getStart().inverse()));
		    fb.addToBoth(ls.getStart().lerp(ls.getEnd(), t));
		}
	}
    
 /*   private void addVertexToList(List<Coords3d> list, Coords3d newVertex) {
    	if (!list.isEmpty()) {
    		Coords3d lastVertex = list.get(list.size()-1);
    		
    		Coords3d prev = vertices.get(vertices.size()-1);
    		for (Coords3d c : vertices) {
    			Coords3d cross = EdgeCrossSolver.findIntersection(prev, c, lastVertex, newVertex);
    			if (cross!=null && !cross.equals(newVertex) && !cross.equals(lastVertex)) {
    				System.out.println(
    				"Added new vertex: "+cross+" between "+lastVertex+" and "+newVertex+" ("+prev+", "+c+")");
    				list.add(cross);
    				break;
    			}
    		}
    	}
    	list.add(newVertex);
    }*/
}
