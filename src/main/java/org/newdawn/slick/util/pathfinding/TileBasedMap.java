package org.newdawn.slick.util.pathfinding;

import uk.co.gosseyn.xanax.domain.Point;

/**
 * The description for the data we're pathfinding over. This provides the contract
 * between the data being searched (i.e. the in game map) and the path finding
 * generic tools
 * 
 * @author Kevin Glass
 */
public interface TileBasedMap {
	public int getWidthInTiles();
	public int getHeightInTiles();
	public int getDepthInTiles();
	public void pathFinderVisited(Point point);
	public boolean blocked(Mover mover, Point source, Point point);
	public int getCost(Mover mover, Point source, Point target);
}