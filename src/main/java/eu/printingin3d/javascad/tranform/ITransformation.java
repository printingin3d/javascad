package eu.printingin3d.javascad.tranform;

import eu.printingin3d.javascad.coords.Coords3d;

public interface ITransformation {
	Coords3d transform(Coords3d vec);
	boolean isMirror();
}
