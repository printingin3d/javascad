/**
 * Node.java
 *
 * Copyright 2014-2014 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Michael Hoffer <info@michaelhoffer.de>.
 */ 

package eu.printingin3d.javascad.vrl;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a node in a BSP tree. A BSP tree is built from a collection of polygons
 * by picking a polygon to split along. That polygon (and all other coplanar
 * polygons) are added directly to that node and the other polygons are added to
 * the front and/or back subtrees. This is not a leafy BSP tree since there is
 * no distinction between internal and leaf nodes.
 */
final class Node {

    /**
     * Polygons.
     */
    private final List<Polygon> polygons;
    /**
     * Plane used for BSP.
     */
    private final Plane plane;
    /**
     * Polygons in front of the plane.
     */
    private Node front;
    /**
     * Polygons in back of the plane.
     */
    private Node back;

    private Node(List<Polygon> polygons, Plane plane, Node front, Node back) {
		this.polygons = polygons;
		
        if (plane == null) {
        	if (polygons.isEmpty()) {
        		this.plane = null;
        	}
        	else {
        		this.plane = polygons.get(0).getPlane();
        	}
        } else {
			this.plane = plane;
		}
		this.front = front;
		this.back = back;
	}

	/**
     * Constructor.
     *
     * Creates a BSP node consisting of the specified polygons.
     *
     * @param polygons polygons
     */
    public static Node fromPoligons(List<Polygon> polygons) {
    	if (polygons == null || polygons.isEmpty()) {
			return new Node();
		}
    	
    	Plane newPlane = polygons.get(0).getPlane();

    	List<Polygon> newPolygons = new ArrayList<>();
        List<Polygon> frontP = new ArrayList<>();
        List<Polygon> backP = new ArrayList<>();
        
        Node newFront = null;
        Node newBack = null;

        for (Polygon polygon : polygons) {
        	newPlane.splitPolygon(
                    polygon, newPolygons, newPolygons, frontP, backP);
        }
        if (!frontP.isEmpty()) {
        	newFront = fromPoligons(frontP);
        }
        if (!backP.isEmpty()) {
        	newBack = fromPoligons(backP);
        }
        return new Node(newPolygons, newPlane, newFront, newBack);
    	
    }

    /**
     * Constructor. Creates a node without polygons.
     */
    private Node() {
        this(new ArrayList<Polygon>(), null, null, null);
    }

    /**
     * Converts solid space to empty space and vice verca.
     */
    public Node invert() {
    	List<Polygon> newPolygons = new ArrayList<>();

        for (Polygon polygon : this.polygons) {
        	newPolygons.add(polygon.flip());
        }

        Plane newPlane = plane == null ? null : plane.flip();
        
        Node newBack = this.front == null ? null : this.front.invert();
        Node newFront = this.back == null ? null : this.back.invert();
        
        return new Node(newPolygons, newPlane, newFront, newBack);
    }

    /**
     * Recursively removes all polygons in the {@link polygons} list that are
     * contained within this BSP tree.
     *
     * <b>Note:</b> polygons are splitted if necessary.
     *
     * @param polys the polygons to clip
     *
     * @return the cliped list of polygons
     */
    private List<Polygon> clipPolygons(List<Polygon> polys) {

        if (this.plane == null || polys.isEmpty()) {
            return new ArrayList<>(polys);
        }

        List<Polygon> frontP = new ArrayList<>();
        List<Polygon> backP = new ArrayList<>();

        for (Polygon polygon : polys) {
            this.plane.splitPolygon(polygon, frontP, backP, frontP, backP);
        }
        if (this.front != null) {
            frontP = this.front.clipPolygons(frontP);
        }
        if (this.back != null) {
            backP = this.back.clipPolygons(backP);
        } else {
            backP = new ArrayList<>(0);
        }

        frontP.addAll(backP);
        return frontP;
    }

    // Remove all polygons in this BSP tree that are inside the other BSP tree
    // `bsp`.
    /**
     * Removes all polygons in this BSP tree that are inside the specified BSP
     * tree ({@code bsp}).
     *
     * <b>Note:</b> polygons are splitted if necessary.
     *
     * @param bsp bsp that shall be used for clipping
     */
    public Node clipTo(Node bsp) {
        List<Polygon> newPolygons = bsp.clipPolygons(this.polygons);
        Node newFront = null;
        Node newBack = null;
        if (front != null) {
            newFront = front.clipTo(bsp);
        }
        if (back != null) {
            newBack = back.clipTo(bsp);
        }
        
        return new Node(newPolygons, plane, newFront, newBack);
    }

    /**
     * Returns a list of all polygons in this BSP tree.
     *
     * @return a list of all polygons in this BSP tree
     */
    public List<Polygon> allPolygons() {
        List<Polygon> localPolygons = new ArrayList<>(polygons);
        if (front != null) {
            localPolygons.addAll(front.allPolygons());
        }
        if (back != null) {
            localPolygons.addAll(back.allPolygons());
        }

        return localPolygons;
    }

    /**
     * Build a BSP tree out of {@code polygons}. When called on an existing
     * tree, the new polygons are filtered down to the bottom of the tree and
     * become new nodes there. Each set of polygons is partitioned using the
     * first polygon (no heuristic is used to pick a good split).
     *
     * @param polygons polygons used to build the BSP
     */
/*    public Node build(List<Polygon> polygons) {
    	Plane newPlane = plane;

    	if (newPlane == null) {
    		if (polygons.isEmpty()) {
    			return this;
    		}
    		newPlane = polygons.get(0).plane;
    	}

    	List<Polygon> newPolygons = new ArrayList<>(polygons);
        List<Polygon> frontP = new ArrayList<>();
        List<Polygon> backP = new ArrayList<>();
        Node newFront = front;
        Node newBack = back;

        for (Polygon polygon : polygons) {
        	newPlane.splitPolygon(
                    polygon, newPolygons, newPolygons, frontP, backP);
        }
        if (!frontP.isEmpty()) {
            if (newFront == null) {
            	newFront = new Node();
            }
            newFront = newFront.build(frontP);
        }
        if (!backP.isEmpty()) {
            if (newBack == null) {
                newBack = new Node();
            }
            newBack = newBack.build(backP);
        }
        
        return new Node(newPolygons, newPlane, newFront, newBack);
    }*/
    public Node build(List<Polygon> polygons) {
    	if (polygons==null || polygons.isEmpty()) {
			return this;
		}
    	
    	Plane newPlane = plane;
    	
        if (newPlane == null) {
        	newPlane = polygons.get(0).getPlane();
        }

        List<Polygon> frontP = new ArrayList<>();
        List<Polygon> backP = new ArrayList<>();

        for (Polygon polygon : polygons) {
        	newPlane.splitPolygon(polygon, polygons, polygons, frontP, backP);
        }
        if (!frontP.isEmpty()) {
            if (front == null) {
            	front = fromPoligons(frontP);
            } else {
            	front.build(frontP);
			}
        }
        if (!backP.isEmpty()) {
            if (back == null) {
            	back = fromPoligons(backP);
            } else {
            	back.build(backP);
			}
        }
        return new Node(this.polygons, this.plane, front, back);
    }
    
    
}
