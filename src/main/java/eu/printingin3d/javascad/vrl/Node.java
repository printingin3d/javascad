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
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import eu.printingin3d.javascad.utils.AssertValue;

/**
 * Holds a node in a BSP tree. A BSP tree is built from a collection of polygons
 * by picking a polygon to split along. That polygon (and all other coplanar
 * polygons) are added directly to that node and the other polygons are added to
 * the front and/or back subtrees. This is not a leafy BSP tree since there is
 * no distinction between internal and leaf nodes.
 */
public final class Node {

    /**
     * Polygons.
     */
    private final List<Polygon> polygons;
    /**
     * Plane used if polygons is empty.
     */
    private final Polygon basePlane;
    /**
     * Polygons in front of the plane.
     */
    private final Optional<Node> front;
    /**
     * Polygons in back of the plane.
     */
    private final Optional<Node> back;

    private Node(List<Polygon> polygons, Optional<Node> front, Optional<Node> back) {
    	AssertValue.isNotEmpty(polygons, 
    			"Polygons should be provided! If it is empty use the other constructor with the basePlane.");
    	
    	this.polygons = Collections.unmodifiableList(polygons);
    	this.basePlane = null;
		this.front = front;
		this.back = back;
	}
    
    private Node(Polygon basePlane, Optional<Node> front, Optional<Node> back) {
    	AssertValue.isNotNull(basePlane, "Baseplane should be provided if polygons is missing!");
    	
    	this.polygons = Collections.emptyList();
    	this.basePlane = basePlane;
    	this.front = front;
    	this.back = back;
    }

	/**
     * Creates a BSP node consisting of the specified polygons.
     *
     * @param polygons polygons
     * @return a new Node based on the list of polygons given
     */
    public static Optional<Node> fromPoligons(List<Polygon> polygons) {
    	if (polygons==null || polygons.isEmpty()) {
			return Optional.empty();
		}
    	
    	Polygon newPlane = polygons.get(0);

    	List<Polygon> newPolygons = new ArrayList<>();
    	FrontBack<Polygon> fb = new FrontBack<>();

    	polygons.forEach(polygon -> newPlane.splitPolygon(polygon, newPolygons, fb));
        
    	Optional<Node> newFront = fromPoligons(fb.getFront());
    	Optional<Node> newBack = fromPoligons(fb.getBack());
        return Optional.of(new Node(newPolygons, newFront, newBack));
    }

    /**
     * Inverts the node by flipping all the poligons it contains and inverting the front and 
     * back nodes as well. 
     * @return a new node which contains the reversed poligons
     */
	public Node invert() {
    	List<Polygon> newPolygons = this.polygons.stream().map(Polygon::flip).collect(Collectors.toList());
        return createNewNode(newPolygons, back.map(Node::invert), front.map(Node::invert));
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
	public List<Polygon> clipPolygons(List<Polygon> polys) {
        if (polys.isEmpty()) {
            return new ArrayList<>();
        }
        Polygon plane = getPlane();

        FrontBack<Polygon> fb = new FrontBack<>();

        polys.forEach(polygon -> plane.splitPolygon(polygon, fb));
        
        List<Polygon> result = front
        		.map(f -> f.clipPolygons(fb.getFront()))
        		.orElseGet(() -> new ArrayList<>(fb.getFront()));
        back.ifPresent(b -> result.addAll(b.clipPolygons(fb.getBack())));

        return result;
    }

    /**
     * Remove all polygons in this BSP tree that are inside the given other BSP tree.
     * @param bsp the tree to remove
     * @return a new object to contain the clipped poligons 
     */
	public Node clipTo(Node bsp) {
        List<Polygon> newPolygons = bsp.clipPolygons(this.polygons);
        return createNewNode(newPolygons, front.map(n -> n.clipTo(bsp)), back.map(n -> n.clipTo(bsp)));
    }

	/**
	 * Returns with all the polygons this node holds including those which are in the front and back nodes.
	 * @return all the polygons this node holds
	 */
	public List<Polygon> allPolygons() {
        List<Polygon> localPolygons = new ArrayList<>(polygons);
        front.ifPresent(n -> localPolygons.addAll(n.allPolygons()));
        back.ifPresent(n -> localPolygons.addAll(n.allPolygons()));
        return localPolygons;
    }

	private static Optional<Node> combinePoligons(Optional<Node> node, List<Polygon> polygons) {
		return !node.isPresent() ? fromPoligons(polygons) : Optional.of(node.get().build(polygons));
	}
	
	/**
	 * Build a new node adding the given polygons - splitting the polygons if necessary.
	 * @param polygons the polygons to be added
	 * @return a new node with the added polygons
	 */
	public Node build(List<Polygon> polygons) {
    	if (polygons.isEmpty()) {
			return this;
		}
    	
    	Polygon newPlane = this.polygons.isEmpty() ? polygons.get(0) : getPlane();

    	List<Polygon> newPolygons = new ArrayList<>(this.polygons);
    	FrontBack<Polygon> fb = new FrontBack<>();

    	polygons.forEach(polygon -> newPlane.splitPolygon(polygon, newPolygons, fb));
    	
        return new Node(newPolygons, combinePoligons(front, fb.getFront()), combinePoligons(back, fb.getBack()));
    }
	
	private Polygon getPlane() {
		return polygons.isEmpty() ? basePlane : polygons.get(0);
	}
	
	private Node createNewNode(List<Polygon> newPolygons, Optional<Node> newFront, Optional<Node> newBack) {
        return newPolygons.isEmpty() ? new Node(getPlane(), newFront, newBack) :
    		new Node(newPolygons, newFront, newBack);

	}
}
