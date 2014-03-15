package eu.printingin3d.javascad.models;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.models2d.Abstract2dModel;
import eu.printingin3d.javascad.tranzitions.Translate;

public class Ring extends Abstract3dModel {
	private final Abstract2dModel model;
	private final double radius;
	
	public Ring(double radius, Abstract2dModel model) {
		this.model = model;
		this.radius = radius;
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Ring(radius, model);
	}

	@Override
	protected String innerToScad() {
		return "rotate_extrude() "+Translate.getTranslate(Coords3d.xOnly(radius))+model.toScad()+";";
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		Boundaries2d modelBoundaries = model.getBoundaries2d();
		Boundary z = modelBoundaries.getY();
		Boundary xy = Boundary.createSymmetricBoundary(modelBoundaries.getX().getMax()+radius);
		return new Boundaries3d(xy, xy, z);
	}

}
