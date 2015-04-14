package eu.printingin3d.javascad.models;

public class Abstract3dModelReader {

	public static int getTag(Abstract3dModel model) {
		return model.getTag();
	}

	public static boolean isDebug(Abstract3dModel model) {
		return model.isDebug();
	}

	public static boolean isBackground(Abstract3dModel model) {
		return model.isBackground();
	}

}
