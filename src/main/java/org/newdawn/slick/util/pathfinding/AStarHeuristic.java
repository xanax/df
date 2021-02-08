package org.newdawn.slick.util.pathfinding;

import uk.co.gosseyn.xanax.domain.Point;

/**
 * The description of a class providing a cost for a given tile based
 * on a target location and entity being moved. This heuristic controls
 * what priority is placed on different tiles during the search for a path
 * 
 * @author Kevin Glass
 */
public interface AStarHeuristic {

	/**
	 * Get the additional heuristic cost of the given tile. This controls the
	 * order in which tiles are searched while attempting to find a path to the 
	 * target location. The lower the cost the more likely the tile will
	 * be searched.

	 */
	public float getCost(TileBasedMap map, Mover mover, Point source, Point target);
}