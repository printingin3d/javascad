package eu.printingin3d.javascad.vrl.export;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import eu.printingin3d.javascad.vrl.Facet;

/**
 * An IFileExporter implementation for the binary STL file format. The usual extension of the file
 * is .stl. The exported file does not contain the color information of the objects.
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class StlBinaryFile implements IFileExporter {
	private final OutputStream out;
	
	/**
	 * Constructs the object with the given stream.
	 * @param out the stream to write to
	 */
	public StlBinaryFile(OutputStream out) {
		this.out = out;
	}

	@Override
	public void writeToFile(List<Facet> facets) throws IOException {
    	out.write(new byte[80]);
    	
    	out.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(facets.size()).array());
    	
    	for (Facet facet : facets) {
    		out.write(facetToBinaryStl(facet));
    	}
	}
	
	private static byte[] facetToBinaryStl(Facet facet) throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.allocate(50).order(ByteOrder.LITTLE_ENDIAN);
		
		facet.getNormal().toByteArray(byteBuffer);
		facet.getTriangle().toByteArray(byteBuffer);
		
		byteBuffer.putShort((short)0);
		
		return byteBuffer.array();
	}

	@Override
	public void close() throws IOException {
		out.close();
	}

}
