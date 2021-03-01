package org.newdawn.slick.util.pathfinding;

import uk.co.gosseyn.xanax.domain.Point;

/**
 * A heuristic that uses the tile that is closest to the target
 * as the next best tile.
 * 
 * @author Kevin Glass
 */
public class ClosestHeuristic implements AStarHeuristic {
	/**
	 */
	@Override
	public int getCost(TileBasedMap map, Mover mover, Point source,Point target) {
		int dx = target.getX() - source.getX();
		int dy = target.getY() - source.getY();
		int dz = target.getZ() - source.getZ();
		int result =
				dx * dx +
				dy * dy +
				dz * dz;
		// TODO temp should traverse and count climbs
		if(target.getZ() == source.getZ() + 1) {
			result *= 100;
		}
		return result;
	}

}