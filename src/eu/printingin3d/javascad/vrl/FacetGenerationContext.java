package eu.printingin3d.javascad.vrl;

public class FacetGenerationContext {
	private double fs = 0.25;
	private int fa = 6;
	
	public void setFsAndFa(double fs, int fa) {
		this.fs = fs;
		this.fa = fa;
	}
	
	public int calculateNumberOfSlices(double r) {
		return Math.min(360/fa, (int) Math.ceil(2.0*r*Math.PI/fs));
	}
}
