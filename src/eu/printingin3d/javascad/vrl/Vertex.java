/**
 * Vertex.java
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

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.tranform.ITransformation;

/**
 * <p>Immutable representation of a vertex.</p>
 * <p>It is used internally by the CSG sub-system, you don't really have to use it directly.</p>
 *  
 * @author ivivan <ivivan@printingin3d.eu>
 * @author mihu
 */
public class Vertex {
    private final Coords3d pos;
    private final Coords3d normal;

    /**
     * Constructor. Creates a vertex based on a position and a normal.
     *
     * @param pos position
     * @param normal normal
     */
    public Vertex(Coords3d pos, Coords3d normal) {
        this.pos = pos;
        this.normal = normal;
    }

    /**
     * Inverts all orientation-specific data - which is actually only the vertex normal.
     */
    public Vertex flip() {
        return new Vertex(pos, normal.inverse());
    }

    /**
     * Create a new vertex between this vertex and the specified vertex by
     * linearly interpolating all properties using a parameter t.
     *
     * @param other vertex
     * @param t interpolation parameter
     * @return a new vertex between this and the specified vertex
     */
    public Vertex interpolate(Vertex other, double t) {
        return new Vertex(pos.lerp(other.pos, t),
                normal.lerp(other.normal, t));
    }

    /**
     * Applies the specified transform to a copy of this vertex.
     *
     * @param transform the transform to apply
     * @return a copy of this transform
     */
    public Vertex transformed(ITransformation transform) {
        return new Vertex(transform.transform(pos), normal);
    }

    /**
     * Gets the position.
     * @return the position of this vertex
     */
	public Coords3d getPos() {
		return pos;
	}
}
