package eu.printingin3d.javascad.vrl.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import eu.printingin3d.javascad.enums.OutputFormat;
import eu.printingin3d.javascad.vrl.Facet;
import eu.printingin3d.javascad.vrl.Vertex;
import eu.printingin3d.javascad.vrl.VertexMap;

/**
 * An IFileExporter implementation for the Polygon file format. The usual extension of the file
 * is .ply. The exported file contains the color information of the objects too.
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class PolygonFile implements IFileExporter {
	private final OutputStream stream;
	
	/**
	 * Constructs the object with the given file.
	 * @param stream the file to write to
	 */
	public PolygonFile(OutputStream stream) {
		this.stream = stream;
	}

	@Override
	public void writeToFile(List<Facet> facets) {
		List<Vertex> vertexes = new ArrayList<>();
		for (Facet f : facets) {
			vertexes.addAll(f.getVertexes());
		}
		
		VertexMap map = new VertexMap(vertexes);
		
		List<Vertex> sortedList = map.getVertexList();
		
		try (PrintStream ps = new PrintStream(stream)) {
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
				ps.println(v.format(OutputFormat.POLYGON));
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
	}

	@Override
	public void close() throws IOException {
		stream.close();
	}
}
