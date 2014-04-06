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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Triangle3d;
import eu.printingin3d.javascad.tranform.ITransformation;

/**
 * Represents a convex polygon.
 *
 * Each convex polygon has a {@code shared} property, disthich is shared
 * betdisteen all polygons that are clones of each other or where split from the
 * same polygon. This can be used to define per-polygon properties (such as
 * surface color).
 */
public final class Polygon {

    /**
     * Polygon vertices
     */
    public final List<Coords3d> vertices;

    /**
     * Plane defined by this polygon.
     *
     * <b>Note:</b> uses first three vertices to define the plane.
     */
    public final Plane plane;
    
    private Polygon(List<Coords3d> vertices, Plane plane) {
		this.vertices = Collections.unmodifiableList(vertices);
		this.plane = plane;
	}

	/**
     * Constructor. Creates a new polygon that consists of the specified
     * vertices.
     *
     * <b>Note:</b> the vertices used to initialize a polygon must be coplanar
     * and form a convex loop.
     *
     * @param vertices polygon vertices
     */
    public Polygon(List<Coords3d> vertices) {
    	this(vertices, Plane.createFromPoints(
                vertices.get(0),
                vertices.get(1),
                vertices.get(2)));
    }

    /**
     * Constructor. Creates a new polygon that consists of the specified
     * vertices.
     *
     * <b>Note:</b> the vertices used to initialize a polygon must be coplanar
     * and form a convex loop.
     *
     * @param vertices polygon vertices
     *
     */
    public Polygon(Coords3d... vertices) {
        this(Arrays.asList(vertices));
    }

    /**
     * Flips this polygon.
     *
     * @return this polygon
     */
    public Polygon flip() {
    	List<Coords3d> newVertices = new ArrayList<>(vertices);
    	
        Collections.reverse(newVertices);

        return new Polygon(newVertices, plane.flip());
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
				facets.add(new Facet(triangle, plane.getNormal()));
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
    	
    	Polygon result = new Polygon(newVertices);

    	return transform.isMirror() ? result.flip() : result;
    }
}
