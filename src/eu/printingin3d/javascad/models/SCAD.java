package eu.printingin3d.javascad.models;

public class SCAD {
	public static final SCAD EMPTY = new SCAD("", false);
	
	private final String scad;
	private final boolean included;
	private SCAD(String scad, boolean included) {
		this.scad = scad;
		this.included = included;
	}
	
	public SCAD(String scad) {
		this(scad, false);
	}
	

	public String getScad() {
		return scad;
	}
	
	public boolean isIncluded() {
		return included && !scad.isEmpty();
	}
	
	public boolean isEmpty() {
		return scad.isEmpty();
	}
	
	public SCAD prepend(String text) {
		return new SCAD(text+this.scad, included);
	}
	
	public SCAD append(String text) {
		return new SCAD(this.scad+text, included);
	}
	
	public SCAD append(SCAD scad) {
		return new SCAD(this.scad+scad.scad, included || scad.included);
	}

	public SCAD include() {
		return new SCAD(this.scad, true);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (included ? 1231 : 1237);
		result = prime * result + ((scad == null) ? 0 : scad.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SCAD other = (SCAD) obj;
		if (included != other.included) {
			return false;
		}
		if (scad == null) {
			if (other.scad != null) {
				return false;
			}
		} else if (!scad.equals(other.scad)) {
			return false;
		}
		return true;
	}
}
