package eu.printingin3d.javascad.models;

public class SCAD {
	public static final SCAD EMPTY = new SCAD("");
	
	private final String scad;

	public SCAD(String scad) {
		this.scad = scad;
	}
	
	public boolean isIncluded() {
		return !scad.isEmpty();
	}
	
	public String getScad() {
		return scad;
	}
	
	public boolean isEmpty() {
		return scad.isEmpty();
	}
	
	public SCAD prepend(String text) {
		return new SCAD(text+this.scad);
	}
	
	public SCAD append(String text) {
		return new SCAD(this.scad+text);
	}
	
	public SCAD append(SCAD scad) {
		return new SCAD(this.scad+scad.scad);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
