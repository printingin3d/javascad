package eu.printingin3d.javascad.povray;

import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.enums.Language;
import eu.printingin3d.javascad.exceptions.IllegalValueException;
import eu.printingin3d.javascad.models.IModel;
import eu.printingin3d.javascad.utils.AssertValue;

/**
 * Defines the camera for the POVRay rendering. It supports only the basic functions of 
 * the camera in POVRay so far. 
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Camera implements IModel {
	private final Coords3d location;
	private final Coords3d lookAt;
	private FocalBlur focalBlur = FocalBlur.OFF;

	/**
	 * Constructs the camera which will be in the given position and look at the specified
	 * point. Neither parameters can be null. 
	 * @param location the location of the camera
	 * @param lookAt the location where the camera looks
	 * @throws IllegalValueException thrown if either of its parameters are null
	 */
	public Camera(Coords3d location, Coords3d lookAt) throws IllegalValueException {
		AssertValue.isNotNull(location, "Location must be specified for the camera!");
		AssertValue.isNotNull(lookAt, "Look at coordinates must be specified for the camera!");
		
		this.location = location;
		this.lookAt = lookAt;
	}

	@Override
	public String toScad() {
		if (Language.POVRay.isActive()) {
			return "camera {location "+location+"look_at "+lookAt+focalBlur+"} ";
		}
		return "";
	}

	/**
	 * Sets the focal blur to imitate the effect that the too close things are
	 * not sharp. The feature is turned off by default.
	 * @param focalBlur the new setting of this feature
	 */
	public void setFocalBlur(FocalBlur focalBlur) {
		this.focalBlur = focalBlur;
	}
}
