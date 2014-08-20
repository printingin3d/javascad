package eu.printingin3d.javascad.vrl.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import eu.printingin3d.javascad.vrl.Facet;
import eu.printingin3d.javascad.vrl.Vertex;

public class StlBinaryFile implements IFileExporter {
	private final File file;
	
	public StlBinaryFile(File file) {
		this.file = file;
	}

	@Override
	public void writeToFile(List<Facet> facets) throws IOException {
    	FileOutputStream out = new FileOutputStream(file);
    	try {
	    	out.write(new byte[80]);
	    	
	    	out.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(facets.size()).array());
	    	
	    	for (Facet facet : facets) {
	    		out.write(facetToBinaryStl(facet));
	    	}
    	}
    	finally {
	    	out.close();
    	}
	}
	
	public static byte[] facetToBinaryStl(Facet facet) throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.allocate(50).order(ByteOrder.LITTLE_ENDIAN);
		
		byteBuffer.put(facet.getNormal().toByteArray());
		
		for (Vertex v : facet.getVertexes()) {
			byteBuffer.put(v.getCoords().toByteArray());
		}
		
		byteBuffer.putShort((short)0);
		
		return byteBuffer.array();
	}

}
