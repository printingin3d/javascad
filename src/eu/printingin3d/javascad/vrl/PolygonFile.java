package eu.printingin3d.javascad.vrl;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class PolygonFile {
	private final List<Facet> facets;
	
	public PolygonFile(List<Facet> facets) {
		this.facets = facets;
	}
	
	public void writeToFile(File file) throws IOException {
		List<Vertex> vertexes = new ArrayList<>();
		for (Facet f : facets) {
			vertexes.addAll(f.getVertexes());
		}
		
		VertexMap map = new VertexMap(vertexes);
		
		List<Vertex> sortedList = map.getVertexList();
		
		PrintStream ps = new PrintStream(file);
		
		try {
			// fix header
			ps.println("ply");
			ps.println("format ascii 1.0");
			// elements headers
			// vertexes are store the coordinates and the color
			ps.println("element vertex "+sortedList.size());
			ps.println("property float x");
			ps.println("property float y");
			ps.println("property float z");
			ps.println("property uchar red");
			ps.println("property uchar green");
			ps.println("property uchar blue");
			// faces are a list of coordinate indexes
			ps.println("element face "+facets.size());
			ps.println("property list uchar int vertex_index");
			// end_header
			ps.println("end_header");
			
			// list of vertexes
			for (Vertex v : sortedList) {
				ps.println(v);
			}
			
			for (Facet f : facets) {
				StringBuffer sb = new StringBuffer();
				List<Vertex> fvertexes = f.getVertexes();
				sb.append(fvertexes.size()).append(' ');
				for (Vertex v : fvertexes) {
					sb.append(map.findIndex(v)).append(' ');
				}
				ps.println(sb.toString());
			}
		}
		finally {
			ps.close();
		}
	}
}
