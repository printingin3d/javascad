package eu.printingin3d.javascad.vrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class VertexMap {
	private final Map<Vertex, Integer> vertexMap = new HashMap<>();
	private final List<Vertex> vertexes = new ArrayList<>();
	
	public VertexMap(Collection<Vertex> vertexes) {
		int index = 0;
		for (Vertex v : new HashSet<Vertex>(vertexes)) {
			vertexMap.put(v, Integer.valueOf(index));
			this.vertexes.add(v);
			index++;
		}
	}
	
	public int findIndex(Vertex vertex) {
		return vertexMap.get(vertex).intValue();		
	}
	
	public List<Vertex> getVertexList() {
		return vertexes;
	}
}
