package eu.printingin3d.javascad.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Coords3d;

/**
 * Represents moves which can be used to move models.
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Moves implements Iterable<Coords3d> {
	private final List<Coords3d> moves;
	
	private Moves(List<Coords3d> moves) {
		this.moves = Collections.unmodifiableList(moves);
	}
	
	/**
	 * Created the object with only a zero move - this is the starting position.
	 */
	public Moves() {
		this(Collections.singletonList(Coords3d.ZERO));
	}
	
	private List<Coords3d> innerMove(Coords3d delta) {
		List<Coords3d> result = new ArrayList<>();
		for (Coords3d c : moves) {
			result.add(c.move(delta));
		}
		return result;
	}
	
	/**
	 * Adds the delta value to all of the moves.
	 * @param delta the coordinates used by the move
	 */
	public Moves move(Coords3d delta) {
		return new Moves(innerMove(delta));
	}
	
	/**
	 * Add more moves, which makes this a multi-move, representing more than one move coordinates.
	 * This operation multiplies the number of moves represented by this object by the number of
	 * coordinates in the delta parameter.
	 * @param delta the collection of coordinates used by the move operation
	 */
	public Moves moves(Collection<Coords3d> delta) {
		if (!delta.isEmpty()) {
			List<Coords3d> temp = new ArrayList<>();
			for (Coords3d d : delta) {
				temp.addAll(innerMove(d));
			}
			return new Moves(temp);
		}
		return this;
	}
	
	/**
	 * Rotates all of the moves represented by this object.
	 * @param delta the angle it will be rotated
	 */
	public Moves rotate(Angles3d delta) {
		List<Coords3d> temp = new ArrayList<>(moves.size());
		for (Coords3d c : moves) {
			temp.add(c.rotate(delta));
		}
		return new Moves(temp);
	}
	
	/**
	 * Tells if this object represents more than one move.
	 * @return true if and only if the result of this model will be more than one model in OpenSCAD
	 */
	public boolean isMulti() {
		return moves.size()>1;
	}

	@Override
	public Iterator<Coords3d> iterator() {
		return moves.iterator();
	}
}
