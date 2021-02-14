package org.newdawn.slick.util.pathfinding;

import lombok.extern.slf4j.Slf4j;
import uk.co.gosseyn.xanax.domain.Point;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A path finder implementation that uses the AStar heuristic based algorithm
 * to determine a path. 
 * 
 * @author Kevin Glass
 */
@Slf4j
public class AStarPathFinder implements PathFinder {
	/** The set of nodes that have been searched through */
	private List<Node> closed = new ArrayList<>();
	/** The set of nodes that we do not yet consider fully searched */
	private SortedList<Node> open = new SortedList();
	
	/** The map being searched */
	private TileBasedMap map;
	/** The maximum depth of search we're willing to accept before giving up */
	private int maxSearchDistance;
	
	/** The complete set of nodes across the map */
	private Node[][][] nodes;
	/** True if we allow diaganol movement */
	private boolean allowDiagMovement;
	/** The heuristic we're applying to determine which nodes to search first */
	private AStarHeuristic heuristic;
	
	/**
	 * Create a path finder with the default heuristic - closest to target.
	 * 
	 * @param map The map to be searched
	 * @param maxSearchDistance The maximum depth we'll search before giving up
	 * @param allowDiagMovement True if the search should try diaganol movement
	 */
	public AStarPathFinder(TileBasedMap map, int maxSearchDistance, boolean allowDiagMovement) {
		this(map, maxSearchDistance, allowDiagMovement, new ClosestHeuristic());
	}

	/**
	 * Create a path finder 
	 * 
	 * @param heuristic The heuristic used to determine the search order of the map
	 * @param map The map to be searched
	 * @param maxSearchDistance The maximum depth we'll search before giving up
	 * @param allowDiagMovement True if the search should try diaganol movement
	 */
	public AStarPathFinder(TileBasedMap map, int maxSearchDistance, 
						   boolean allowDiagMovement, AStarHeuristic heuristic) {
		this.heuristic = heuristic;
		this.map = map;
		this.maxSearchDistance = maxSearchDistance;
		this.allowDiagMovement = allowDiagMovement;
		
		nodes = new Node[map.getWidthInTiles()][map.getHeightInTiles()][map.getDepthInTiles()];
		for (int x=0;x<map.getWidthInTiles();x++) {
			for (int y=0;y<map.getHeightInTiles();y++) {
				for (int z=0;z<map.getDepthInTiles();z++) {
					nodes[x][y][z] = new Node(new Point(x, y, z));
				}
			}
		}
	}
	

	@Override
	public Path findPath(Mover mover, Point source, Point target) {


		// initial state for A*. The closed group is empty. Only the starting
		// tile is in the open list and it's cost is zero, i.e. we're already there
		nodes[source.getX()][source.getY()][source.getZ()].cost = 0;
		nodes[source.getX()][source.getY()][source.getZ()].depth = 0;
		closed.clear();
		open.clear();
		open.add(nodes[source.getX()][source.getY()][source.getZ()]);
		
		nodes[target.getX()][target.getY()][target.getZ()].parent = null;
		
		// while we haven't found the goal and haven't exceeded our max search depth
		int maxDepth = 0;
		while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
			// pull out the first node in our open list, this is determined to 
			// be the most likely to be the next step based on our heuristic
			Node current = getFirstInOpen();
			if (current == nodes[target.getX()][target.getY()][target.getZ()]) {
				break;
			}
			
			removeFromOpen(current);
			addToClosed(current);
			
			// search through all the neighbours of the current node evaluating
			// them as next steps
			for (int x=-1;x<2;x++) {
				for (int y=-1;y<2;y++) {
					for (int z = -1; z < 2; z++) {
						// not a neighbour, its the current tile
						if ((x == 0) && (y == 0) && (z == 0)) {
							continue;
						}

						// if we're not allowing diaganol movement then only
						// one of x or y can be set
						if (!allowDiagMovement) {
							if ((x != 0) && (y != 0) && (z != 0)) {
								continue;
							}
						}

						Point np = new Point(x+ current.point.getX(), y+ current.point.getY(),
								z + current.point.getZ());

						if (isValidLocation(mover, source, np, target, current.point)) {
							// the cost to get to this node is cost the current plus the movement
							// cost to reach this node. Note that the heursitic value is only used
							// in the sorted open list
							float nextStepCost = current.cost + getMovementCost(mover, current.point, np);
							Node neighbour = nodes[np.getX()][np.getY()][np.getZ()];

							map.pathFinderVisited(np);

							// if the new cost we've determined for this node is lower than
							// it has been previously makes sure the node hasn't been discarded. We've
							// determined that there might have been a better path to get to
							// this node so it needs to be re-evaluated
							if (nextStepCost < neighbour.cost) {
								if (inOpenList(neighbour)) {
									removeFromOpen(neighbour);
								}
								if (inClosedList(neighbour)) {
									removeFromClosed(neighbour);
								}
							}

							// if the node hasn't already been processed and discarded then
							// reset it's cost to our current cost and add it as a next possible
							// step (i.e. to the open list)
							if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
								neighbour.cost = nextStepCost;
								neighbour.heuristic = getHeuristicCost(mover, np, target);
								maxDepth = Math.max(maxDepth, neighbour.setParent(current));
								addToOpen(neighbour);
							}
						}
					}
				}
			}
		}
		if(maxDepth>=maxSearchDistance) {
			log.trace("Reached max search distance {}", maxDepth);
		}
		// since we've got an empty open list or we've run out of search 
		// there was no path. Just return null
		if (nodes[target.getX()][target.getY()][target.getZ()].parent == null) {
			return null;
		}
		
		// At this point we've definitely found a path so we can uses the parent
		// references of the nodes to find out way from the target location back
		// to the start recording the nodes on the way.
		Path path = new Path();
		Node targetn = nodes[target.getX()][target.getY()][target.getZ()];
		while (targetn != nodes[source.getX()][source.getY()][source.getZ()]) {
			path.prependStep(targetn.point);
			targetn = targetn.parent;
		}
		path.prependStep(source);
		
		// thats it, we have our path 
		return path;
	}

	/**
	 * Get the first element from the open list. This is the next
	 * one to be searched.
	 * 
	 * @return The first element in the open list
	 */
	protected Node getFirstInOpen() {
		return (Node) open.get(0);
	}
	
	/**
	 * Add a node to the open list
	 * 
	 * @param node The node to be added to the open list
	 */
	protected void addToOpen(Node node) {
		open.add(node);
	}
	
	/**
	 * Check if a node is in the open list
	 * 
	 * @param node The node to check for
	 * @return True if the node given is in the open list
	 */
	protected boolean inOpenList(Node node) {
		return open.contains(node);
	}
	
	/**
	 * Remove a node from the open list
	 * 
	 * @param node The node to remove from the open list
	 */
	protected void removeFromOpen(Node node) {
		open.remove(node);
	}
	
	/**
	 * Add a node to the closed list
	 * 
	 * @param node The node to add to the closed list
	 */
	protected void addToClosed(Node node) {
		closed.add(node);
	}
	
	/**
	 * Check if the node supplied is in the closed list
	 * 
	 * @param node The node to search for
	 * @return True if the node specified is in the closed list
	 */
	protected boolean inClosedList(Node node) {
		return closed.contains(node);
	}
	
	/**
	 * Remove a node from the closed list
	 * 
	 * @param node The node to remove from the closed list
	 */
	protected void removeFromClosed(Node node) {
		closed.remove(node);
	}
	
	/**
	 * Check if a given location is valid for the supplied mover
	 *
	 */
	protected boolean isValidLocation(Mover mover, Point source, Point neighbour, Point target, Point current) {
		boolean invalid = (neighbour.getX() < 0) || (neighbour.getY() < 0) ||(neighbour.getZ() < 0) ||
				(neighbour.getX() >= map.getWidthInTiles()) || (neighbour.getY() >= map.getHeightInTiles()
				|| (neighbour.getZ() >= map.getDepthInTiles()));

		if(!invalid && neighbour.equals(target)
				//TODO make this configurable
				&& current.getZ() == neighbour.getZ()) {
			return true;
		}
		if ((!invalid) && !source.equals(neighbour)) {
			invalid = map.blocked(mover, neighbour);
		}
		
		return !invalid;
	}

	public float getMovementCost(Mover mover, Point source, Point target) {
		return map.getCost(mover, source, target);
	}

	/**
	 * Get the heuristic cost for the given location. This determines in which 
	 * order the locations are processed.
	 *
	 */
	public float getHeuristicCost(Mover mover, Point source, Point target) {
		return heuristic.getCost(map, mover, source, target);
	}

	public static class SortedList<E> extends AbstractList<E> {

		private ArrayList<E> internalList = new ArrayList<E>();

		// Note that add(E e) in AbstractList is calling this one
		@Override
		public void add(int position, E e) {
			internalList.add(e);
			Collections.sort(internalList, null);
		}

		@Override
		public E get(int i) {
			return internalList.get(i);
		}

		@Override
		public int size() {
			return internalList.size();
		}

		@Override
		public E remove(final int index) {
			return internalList.remove(index);
		}
	}

	/**
	 * A single node in the search graph
	 */
	private class Node implements Comparable {
		private final Point point;
		/** The path cost for this node */
		private float cost;
		/** The parent of this node, how we reached it in the search */
		private Node parent;
		/** The heuristic cost of this node */
		private float heuristic;
		/** The search depth of this node */
		private int depth;

		public Node(Point point) {
			this.point = point;
		}
		
		/**
		 * Set the parent of this node
		 * 
		 * @param parent The parent node which lead us to this node
		 * @return The depth we have no reached in searching
		 */
		public int setParent(Node parent) {
			depth = parent.depth + 1;
			this.parent = parent;
			
			return depth;
		}
		
		/**
		 * @see Comparable#compareTo(Object)
		 */
		@Override
		public int compareTo(Object other) {
			Node o = (Node) other;
			
			float f = heuristic + cost;
			float of = o.heuristic + o.cost;
			
			if (f < of) {
				return -1;
			} else if (f > of) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}