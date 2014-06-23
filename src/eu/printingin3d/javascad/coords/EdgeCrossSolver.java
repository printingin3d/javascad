package eu.printingin3d.javascad.coords;

import java.util.ArrayDeque;
import java.util.Deque;

import eu.printingin3d.javascad.utils.DoubleUtils;


public class EdgeCrossSolver {
	
	private static class State {
		private final Coords3d edge1vert1; 
		private final Coords3d edge1vert2; 
		private final Coords3d edge2vert1;
		private final Coords3d edge2vert2;
		private final int level;
		public State(Coords3d edge1vert1, Coords3d edge1vert2,
				Coords3d edge2vert1, Coords3d edge2vert2, int level) {
			this.edge1vert1 = edge1vert1;
			this.edge1vert2 = edge1vert2;
			this.edge2vert1 = edge2vert1;
			this.edge2vert2 = edge2vert2;
			this.level = level;
		}
		public Coords3d getEdge1vert1() {
			return edge1vert1;
		}
		public Coords3d getEdge1vert2() {
			return edge1vert2;
		}
		public Coords3d getEdge2vert1() {
			return edge2vert1;
		}
		public Coords3d getEdge2vert2() {
			return edge2vert2;
		}
		public int getLevel() {
			return level;
		}
	}

	private static boolean areBounariesOverlap(Coords3d edge1vert1, Coords3d edge1vert2, 
			Coords3d edge2vert1, Coords3d edge2vert2) {
		Boundaries3d b1 = new Boundaries3d(edge1vert1, edge1vert2);
		Boundaries3d b2 = new Boundaries3d(edge2vert1, edge2vert2);
		
		if (b1.getX().getMax()<b2.getX().getMin() || b1.getX().getMin()>b2.getX().getMax()) {
			return false;
		}
		
		if (b1.getY().getMax()<b2.getY().getMin() || b1.getY().getMin()>b2.getY().getMax()) {
			return false;
		}
		
		if (b1.getZ().getMax()<b2.getZ().getMin() || b1.getZ().getMin()>b2.getZ().getMax()) {
			return false;
		}
		return true;
	}
	
	private static boolean coordsAreEqual(Coords3d a, Coords3d b) {
		return DoubleUtils.equalsEps(a.getX(), b.getX()) &&
				DoubleUtils.equalsEps(a.getY(), b.getY()) &&
				DoubleUtils.equalsEps(a.getZ(), b.getZ());
/*		return DoubleUtils.isZero(a.getX()-b.getX()) &&
				DoubleUtils.isZero(a.getY()-b.getY()) &&
				DoubleUtils.isZero(a.getZ()-b.getZ());*/
	}
	
	public static Coords3d findIntersection(Coords3d e1v1, Coords3d e1v2, Coords3d e2v1, Coords3d e2v2) {
		Deque<State> stateQueue = new ArrayDeque<>(1000);

		stateQueue.add(new State(e1v1, e1v2, e2v1, e2v2, 0));
		
		State state;
		while ((state = stateQueue.pollLast()) != null) {
			Coords3d edge1vert1 = state.getEdge1vert1();
			Coords3d edge1vert2 = state.getEdge1vert2(); 
			Coords3d edge2vert1 = state.getEdge2vert1();
			Coords3d edge2vert2 = state.getEdge2vert2();
			
			Coords3d mid1 = Coords3d.midPoint(edge1vert1, edge1vert2);
			Coords3d mid2 = Coords3d.midPoint(edge2vert1, edge2vert2);
			
			if (coordsAreEqual(mid1, mid2)) {
				return Coords3d.midPoint(mid1, mid2);
			}
			
			if (areBounariesOverlap(edge1vert1, edge1vert2, edge2vert1, edge2vert2)) {
				if (!coordsAreEqual(edge1vert1, mid1)) {
					stateQueue.add(new State(mid1, edge1vert2, edge2vert1, edge2vert2, state.getLevel()+1));
				}
				if (!coordsAreEqual(edge1vert2, mid1)) {
					stateQueue.add(new State(edge1vert1, mid1, edge2vert1, edge2vert2, state.getLevel()+1));
				}
				if (!coordsAreEqual(edge2vert2, mid2)) {
					stateQueue.add(new State(edge1vert1, edge1vert2, edge2vert1, mid2, state.getLevel()+1));
				}
				if (!coordsAreEqual(edge2vert1, mid2)) {
					stateQueue.add(new State(edge1vert1, edge1vert2, mid2, edge2vert2, state.getLevel()+1));
				}
			}
		}

		return null;
	}
}
