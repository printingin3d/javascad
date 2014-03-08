package eu.printingin3d.javascad.povray;

import eu.printingin3d.javascad.utils.DoubleUtils;

/**
 * It is used in the POVRay rendering controlling the way the out of focus
 * things will be rendered.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public enum FocalBlur {
	/**
	 * The focal blur feature is turned off. This is the default value of the camera.
	 */
	OFF(0.0, 0, 0.0, 1),
	/**
	 * It enables the focal blur, but on a fast setting.
	 */
	FAST(2.25, 7, 0.5, 64),
	/**
	 * This is the normal setting, which results a good enough quality without too
	 * much penalty on the rendering time. 
	 */
	NORMAL(2.25, 19, 0.9, 128),
	/**
	 * This is the highest setting. It is the best looking, but the slowest setting.
	 */
	HIGH(2.25, 37, 0.975, 255);
	
	private final double aperture;
	private final int blurSamples;
	private final double confidence;
	private final int variance;  // used as a 1/x value
	
	private FocalBlur(double aperture, int blurSamples, double confidence, int variance) {
		this.aperture = aperture;
		this.blurSamples = blurSamples;
		this.confidence = confidence;
		this.variance = variance;
	}
	
	@Override
	public String toString() {
		if (aperture>0) {
			return "aperture "+DoubleUtils.formatDouble(aperture)+
					" focal_point <0, 0, 0> " +
					"blur_samples "+blurSamples+
					" confidence "+DoubleUtils.formatDouble(confidence)+
					" variance 1/"+variance;
		}
		return "aperture 0";
	}
}
