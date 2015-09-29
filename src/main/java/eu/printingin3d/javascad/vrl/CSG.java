/**
 * CSG.java
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

import eu.printingin3d.javascad.tranform.ITransformation;

/**
 * Constructive Solid Geometry (CSG).
 *
 * This implementation is a Java port of
 * <a
 * href="https://github.com/evanw/csg.js/">https://github.com/evanw/csg.js/</a>
 * with some additional features like polygon extrude, transformations etc.
 * Thanks to the author for creating the CSG.js library.<br><br>
 *
 * <b>Implementation Details</b>
 *
 * All CSG operations are implemented in terms of two functions,
 * {@link Node#clipTo(Node)} and {@link Node#invert()},
 * which remove parts of a BSP tree inside another BSP tree and swap solid and
 * empty space, respectively. To find the union of {@code a} and {@code b}, we
 * want to remove everything in {@code a} inside {@code b} and everything in
 * {@code b} inside {@code a}, then combine polygons from {@code a} and
 * {@code b} into one solid:
 *
 * <blockquote><pre>
 *     a.clipTo(b);
 *     b.clipTo(a);
 *     a.build(b.allPolygons());
 * </pre></blockquote>
 *
 * The only tricky part is handling overlapping coplanar polygons in both trees.
 * The code above keeps both copies, but we need to keep them in one tree and
 * remove them in the other tree. To remove them from {@code b} we can clip the
 * inverse of {@code b} against {@code a}. The code for union now looks like
 * this:
 *
 * <blockquote><pre>
 *     a.clipTo(b);
 *     b.clipTo(a);
 *     b.invert();
 *     b.clipTo(a);
 *     b.invert();
 *     a.build(b.allPolygons());
 * </pre></blockquote>
 *
 * Subtraction and intersection naturally follow from set operations. If union
 * is {@code A | B}, differenceion is {@code A - B = ~(~A | B)} and intersection
 * is {@code A & B =
 * ~(~A | ~B)} where {@code ~} is the complement operator.
 */
public class CSG {

    private final List<Polygon> polygons;

    public CSG(List<Polygon> polygons) {
    	this.polygons = Collections.unmodifiableList(polygons);
    }

    /**
     * Constructs a CSG from the specified {@link Polygon} instances.
     *
     * @param polygons polygons
     * @return a CSG instance
     */
    public static CSG fromPolygons(Polygon... polygons) {
        return new CSG(Arrays.asList(polygons));
    }

    /**
     *
     * @return the polygons of this CSG
     */
    public List<Polygon> getPolygons() {
        return polygons;
    }

    /**
     * Return a new CSG solid representing the union of this csg and the
     * specified csg.
     *
     * <b>Note:</b> Neither this csg nor the specified csg are modified.
     *
     * <blockquote><pre>
     *    A.union(B)
     *
     *    +-------+            +-------+
     *    |       |            |       |
     *    |   A   |            |       |
     *    |    +--+----+   =   |       +----+
     *    +----+--+    |       +----+       |
     *         |   B   |            |       |
     *         |       |            |       |
     *         +-------+            +-------+
     * </pre></blockquote>
     *
     *
     * @param csg other csg
     *
     * @return union of this csg and the specified csg
     */
    public CSG union(CSG csg) {
        Node a = Node.fromPoligons(this.polygons);
        Node b = Node.fromPoligons(csg.polygons);
        a = a.clipTo(b);
        b = b.clipTo(a);
        b = b.invert();
        b = b.clipTo(a);
        b = b.invert();
        a = a.build(b.allPolygons());
        return new CSG(a.allPolygons());
    }

    /**
     * Return a new CSG solid representing the difference of this csg and the
     * specified csg.
     *
     * <b>Note:</b> Neither this csg nor the specified csg are modified.
     *
     * <blockquote><pre>
     * A.difference(B)
     *
     * +-------+            +-------+
     * |       |            |       |
     * |   A   |            |       |
     * |    +--+----+   =   |    +--+
     * +----+--+    |       +----+
     *      |   B   |
     *      |       |
     *      +-------+
     * </pre></blockquote>
     *
     * @param csg other csg
     * @return difference of this csg and the specified csg
     */
    public CSG difference(CSG csg) {
        Node a = Node.fromPoligons(this.polygons);
        Node b = Node.fromPoligons(csg.polygons);
        a = a.invert();
        a = a.clipTo(b);
        b = b.clipTo(a);
        b = b.invert();
        b = b.clipTo(a);
        b = b.invert();
        a = a.build(b.allPolygons());
        a = a.invert();
        return new CSG(a.allPolygons());
    }

    /**
     * Return a new CSG solid representing the intersection of this csg and the
     * specified csg.
     *
     * <b>Note:</b> Neither this csg nor the specified csg are modified.
     *
     * <blockquote><pre>
     *     A.intersect(B)
     *
     *     +-------+
     *     |       |
     *     |   A   |
     *     |    +--+----+   =   +--+
     *     +----+--+    |       +--+
     *          |   B   |
     *          |       |
     *          +-------+
     * }
     * </pre></blockquote>
     *
     * @param csg other csg
     * @return intersection of this csg and the specified csg
     */
    public CSG intersect(CSG csg) {
        Node a = Node.fromPoligons(this.polygons);
        Node b = Node.fromPoligons(csg.polygons);
        a = a.invert();
        b = b.clipTo(a);
        b = b.invert();
        a = a.clipTo(b);
        b = b.clipTo(a);
        a = a.build(b.allPolygons());
        a = a.invert();
        return new CSG(a.allPolygons());
    }

    public List<Facet> toFacets() {
    	List<Facet> facets = new ArrayList<>();
    	for (Polygon p : polygons) {
    		facets.addAll(p.toFacets());
    	}
    	return facets;
    }
    
    /**
     * Returns a transformed copy of this CSG.
     *
     * @param transform the transform to apply
     *
     * @return a transformed copy of this CSG
     */
    public CSG transformed(ITransformation transform) {
    	List<Polygon> newpolygons = new ArrayList<>();
    	for (Polygon p : this.polygons) {
    		newpolygons.add(p.transformed(transform));
    	}

        return new CSG(newpolygons);
    }
}
