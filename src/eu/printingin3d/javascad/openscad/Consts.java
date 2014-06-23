package eu.printingin3d.javascad.openscad;

import eu.printingin3d.javascad.models.IModel;
import eu.printingin3d.javascad.models.ScadGenerationContext;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

/**
 * Represents the constants for $fs and $fa which controls the resolution of every circle, 
 * cylinder or sphere.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Consts implements IModel {
	private final double fs;
	private final int fa;

	/**
	 * Constructs default constants with $fs = 0.25 and $fa = 6. These default values results
	 * medium detailed objects which are good for most of the cases. 
	 */
	public Consts() {
		this(0.25, 6);
	}
	
	/**
	 * Constructs a constant with the given fs and fa values.
	 * @param fs angle in mm
	 * @param fa angle in degrees
	 */
	public Consts(double fs, int fa) {
		this.fs = fs;
		this.fa = fa;
	}

	@Override
	public String toScad(ScadGenerationContext context) {
		return "$fs="+fs+";$fa="+fa+";\n";
	}

	@Override
	public CSG toCSG(FacetGenerationContext context) {
		context.setFsAndFa(fs, fa);
		
		return CSG.fromPolygons();
	}
}
