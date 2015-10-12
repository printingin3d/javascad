package eu.printingin3d.javascad.vrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * <p>Helper class for the polygon file export.</p>
 * <p>It holds a collection of vertexes and make a fixed projection between a vertex and its index.</p>
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class VertexMap {
	private final Map<Vertex, Integer> vertexMap = new HashMap<>();
	private final List<Vertex> vertexes = new ArrayList<>();
	
	/**
	 * Constructs a new object with the given collection of vertexes. The duplicate vertexes will be removed.
	 * @param vertexes the collection of vertexes to be used
	 */
	public VertexMap(Collection<Vertex> vertexes) {
		int index = 0;
		for (Vertex v : new HashSet<Vertex>(vertexes)) {
			vertexMap.put(v, Integer.valueOf(index));
			this.vertexes.add(v);
			index++;
		}
	}
	
	/**
	 * Finds the index of the given vertex.
	 * @param vertex the vertex to be found
	 * @return the index of the vertex
	 */
	public int findIndex(Vertex vertex) {
		return vertexMap.get(vertex).intValue();		
	}
	
	/**
	 * Gives the list of vertexes back which is used with the index calculation. This collection
	 * doesn't contain any duplicates.
	 * @return the list of vertexes this object holds
	 */
	public List<Vertex> getVertexList() {
		return vertexes;
	}
}
