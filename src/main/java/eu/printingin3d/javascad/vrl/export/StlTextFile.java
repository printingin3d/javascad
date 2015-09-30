package eu.printingin3d.javascad.vrl.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import eu.printingin3d.javascad.enums.OutputFormat;
import eu.printingin3d.javascad.vrl.Facet;
import eu.printingin3d.javascad.vrl.Vertex;

/**
 * 
 * @author Ivan
 *
 */
public class StlTextFile implements IFileExporter {
	private final File file;
	
	public StlTextFile(File file) {
		this.file = file;
	}

	@Override
	public void writeToFile(List<Facet> facets) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		
		try { 
	        bw.write("solid v3d.csg\n");
	        for (Facet facet : facets) {
	        	bw.write(facetToStlString(facet));
	        }
	        bw.write("endsolid v3d.csg\n");
		}
		finally {
			bw.close();
		}
	}
	
	public static String facetToStlString(Facet facet) {
		StringBuilder sb = new StringBuilder().
	        append("  facet normal ").append(facet.getNormal().format(OutputFormat.STL)).append('\n').
	        append("    outer loop\n");

		for (Vertex v : facet.getVertexes()) {
	        sb.append("    vertex ").append(v.format(OutputFormat.STL)).append('\n');
		}
		
		return sb.append("    endloop\n").append("  endfacet\n").toString();
	}
	
}
