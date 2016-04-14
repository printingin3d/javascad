package eu.printingin3d.javascad.batchtests;

import static eu.printingin3d.javascad.enums.AlignType.MAX;
import static eu.printingin3d.javascad.enums.AlignType.MIN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.printingin3d.javascad.enums.AlignType;
import eu.printingin3d.javascad.enums.Side;

public class AlignTestCase {
	private final AlignType x;
	private final AlignType y;
	private final AlignType z;
	@Override
	public String toString() {
		return x + ", " + y + ", " + z;
	}
	public AlignTestCase(AlignType x, AlignType y, AlignType z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public AlignType getX() {
		return x;
	}
	public AlignType getY() {
		return y;
	}
	public AlignType getZ() {
		return z;
	}
	public Side getSide() {
		return new Side(x, y, z);
	}

	@SuppressWarnings("deprecation")
	public static Collection<AlignTestCase> createTestSubjects() {
		List<AlignTestCase> result = new ArrayList<>();
		
		for (AlignType x : AlignType.values()) {
			if (x!=MIN && x!=MAX) {
				for (AlignType y : AlignType.values()) {
					if (y!=MIN && y!=MAX) {
						for (AlignType z : AlignType.values()) {
							if (z!=MIN && z!=MAX) {
								result.add(new AlignTestCase(x, y, z));
							}
						}
					}
				}
			}
		}
		return result;
	}
}