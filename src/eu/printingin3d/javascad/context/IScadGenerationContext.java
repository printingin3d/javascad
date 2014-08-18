package eu.printingin3d.javascad.context;


public interface IScadGenerationContext extends IColorGenerationContext {

	boolean isTagIncluded();

	IScadGenerationContext applyTag(int tag);
}