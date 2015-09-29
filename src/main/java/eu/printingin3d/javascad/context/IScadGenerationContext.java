package eu.printingin3d.javascad.context;


public interface IScadGenerationContext {

	boolean isTagIncluded();

	IScadGenerationContext applyTag(int tag);
}