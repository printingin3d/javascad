package eu.printingin3d.javascad.utils;

import java.util.ArrayList;
import java.util.Collection;
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
	private List<Coords3d> moves;
	
	/**
	 * Created the object with only a zero move - this is the starting position.
	 */
	public Moves() {
		moves = new ArrayList<>();
		moves.add(Coords3d.ZERO);
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
	public void move(Coords3d delta) {
		moves = innerMove(delta);
	}
	
	/**
	 * Add more moves, which makes this a multi-move, representing more than one move coordinates.
	 * This operation multiplies the number of moves represented by this object by the number of
	 * coordinates in the delta parameter.
	 * @param delta the collection of coordinates used by the move operation
	 */
	public void moves(Collection<Coords3d> delta) {
		if (!delta.isEmpty()) {
			List<Coords3d> temp = new ArrayList<>();
			for (Coords3d d : delta) {
				temp.addAll(innerMove(d));
			}
			moves = temp;
		}
	}
	
	/**
	 * Rotates all of the moves represented by this object.
	 * @param delta the angle it will be rotated
	 */
	public void rotate(Angles3d delta) {
		for (int i=0;i<moves.size();i++) {
			moves.set(i, moves.get(i).rotate(delta));
		}
	}
	
	/**
	 * Tells if this object represents more than one move.
	 * @return true if and only if the result of this model will be more than one model in OpenSCAD
	 */
	public boolean isMulti() {
		return moves.size()>1;
	}
	
	/**
	 * Creates a clone of this object which contains the same values, but independent from this object.
	 * @return the clone of this object
	 */
	public Moves cloneMoves() {
		Moves clone = new Moves();
		clone.moves = new ArrayList<>(moves);
		return clone;
	}

	@Override
	public Iterator<Coords3d> iterator() {
		return moves.iterator();
	}
}
