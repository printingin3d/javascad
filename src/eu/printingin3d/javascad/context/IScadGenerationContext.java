package eu.printingin3d.javascad.context;

public interface IScadGenerationContext {

	public abstract boolean isTagIncluded();

	public abstract IScadGenerationContext applyTag(int tag);

}