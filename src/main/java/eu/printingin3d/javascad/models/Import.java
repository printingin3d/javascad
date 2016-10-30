package eu.printingin3d.javascad.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import eu.printingin3d.javascad.context.IColorGenerationContext;
import eu.printingin3d.javascad.coords.Triangle3d;

/**
 * <p>Represents an import operation in OpenSCAD. Although OpenSCAD supports several formats
 * in the import operation, in JavaSCAD only binary STL files are supported as of now.</p>
 * <p>The file will be read and the boundaries of the object are determined based on the content 
 * of the file, so aligning to this object will work exactly the same way as any other object
 * in JavaSCAD.</p>
 */
public class Import extends Polyhedron {
	private final File file;
	
	private Import(File file, List<Triangle3d> triangles) {
		super(triangles);
		this.file = file;
	}
	
	/**
	 * Instantiate this import object with the given file. The file will be read to 
	 * determine the boundaries of the object.
	 * @param file the file to be read
	 * @throws IOException if there is any I/O error reading the file
	 */
	public Import(File file) throws IOException {
		this(file, readFile(file));
	}
	
	protected Import(File file, byte[] bytes) {
		this(file, parseBytes(bytes));
	}
	
	private static byte[] readFile(File file) throws IOException {
		byte[] bFile = new byte[(int) file.length()];
		try (InputStream stream = new FileInputStream(file)) {
			stream.read(bFile);
		}
		return bFile;
	}
	
	private static List<Triangle3d> parseBytes(byte[] bFile) {
		ByteBuffer buffer = ByteBuffer.wrap(bFile).order(ByteOrder.LITTLE_ENDIAN);
		buffer.position(80);
		int length = buffer.getInt();
		
		List<Triangle3d> triangles = new ArrayList<>();
		
		byte[] dst = new byte[50];
		while (triangles.size()<length) {
			buffer.get(dst, 0, 50);
			triangles.add(parseBuffer(dst));
		}
		
		return triangles;
	}

	private static Triangle3d parseBuffer(byte[] buff) {
		ByteBuffer bb = ByteBuffer.wrap(buff).order(ByteOrder.LITTLE_ENDIAN);
		bb.position(12);
		return Triangle3d.fromByteArray(bb);
	}
	
	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Import(file, triangles);
	}

	@Override
	protected SCAD innerToScad(IColorGenerationContext context) {
		return new SCAD("import(\""+file.getAbsolutePath().replace("\\", "\\\\")+"\", convexity=10);");
	}
}
