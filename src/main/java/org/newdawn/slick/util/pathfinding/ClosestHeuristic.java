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
	public float getCost(TileBasedMap map, Mover mover, Point source,Point target) {
		float dx = target.getX() - source.getX();
		float dy = target.getY() - source.getY();
		float dz = target.getZ() - source.getZ();
		float result = (float) (
				Math.sqrt(Math.pow(dx, 2) +
				Math.pow(dy, 2) +
				Math.pow(dz, 2)));
		// TODO temp should traverse and count climbs
		if(target.getZ() == source.getZ() + 1) {
			result *= 100;
		}
		return result;
	}

}