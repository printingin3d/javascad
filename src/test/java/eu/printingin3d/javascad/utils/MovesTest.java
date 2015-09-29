package eu.printingin3d.javascad.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import eu.printingin3d.javascad.coords.Coords3d;

public class MovesTest {
	private static List<Coords3d> retrieveCoordsList(Moves moves) {
		List<Coords3d> result = new ArrayList<>();
		
		for (Coords3d coord : moves) {
			result.add(coord);
		}
		
		return result;
	}	

	@Test
	public void movesShouldContainOnlyOneZeroValueByDefault() {
		List<Coords3d> moves = retrieveCoordsList(new Moves());
		
		assertEquals(1, moves.size());
		assertTrue(moves.get(0).isZero());
	}
	
	@Test
	public void isMultiShouldBeFalseByDefault() {
		Moves moves = new Moves();
		
		assertFalse(moves.isMulti());
	}
	
	@Test
	public void isMultiShouldBeTrueAfterMovesCall() {
		Moves moves = new Moves()
					.moves(Arrays.asList(new Coords3d(1,1,1), new Coords3d(2, 3, 4)));
		
		assertTrue(moves.isMulti());
	}

	@Test
	public void moveShouldAddDeltaToMoves() {
		Moves moves = new Moves()
			.move(new Coords3d(1, 2, 3))
			.move(new Coords3d(2, 1, 3));
		
		assertEquals(new Coords3d(3, 3, 6), retrieveCoordsList(moves).get(0));
	}
	
	@Test
	public void movesShouldAddDeltaToMovesAndMakeTheListLonger() {
		Moves moves = new Moves()
			.moves(Arrays.asList(new Coords3d(1,1,1), new Coords3d(2, 3, 4)));
		
		assertEquals(2, retrieveCoordsList(moves).size());
		
		moves = moves.move(new Coords3d(1, 2, 3))
			.move(new Coords3d(2, 1, 3));
		
		assertEquals(new Coords3d(4, 4, 7), retrieveCoordsList(moves).get(0));
		assertEquals(new Coords3d(5, 6, 10), retrieveCoordsList(moves).get(1));
		
		moves = moves.moves(Arrays.asList(new Coords3d(-3,5.5,2), new Coords3d(3, 4, 5), Coords3d.ZERO));
		
		assertEquals(6, retrieveCoordsList(moves).size());
	}
	
	@Test
	public void movesWithEmptyListShouldDoNothing() {
		Moves moves = new Moves();
		moves.moves(Collections.<Coords3d>emptyList());
		
		List<Coords3d> movesList = retrieveCoordsList(moves);
		assertEquals(1, movesList.size());
		assertTrue(movesList.get(0).isZero());
	}
}
