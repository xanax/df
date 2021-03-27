package org.newdawn.slick.util.pathfinding;

import uk.co.gosseyn.xanax.domain.Point;
import uk.co.gosseyn.xanax.service.PathFinderService;

/**
 * The description for the data we're pathfinding over. This provides the contract
 * between the data being searched (i.e. the in game map) and the path finding
 * generic tools
 * 
 * @author Kevin Glass
 */
public interface TileBasedMap {
	int getWidthInTiles();
	int getHeightInTiles();
	int getDepthInTiles();
	void pathFinderVisited(Point point);
	boolean blocked(Mover mover, Point point, Point target);
	int getCost(Mover mover, Point source, Point target);
    PathFinder getPathFinder();
}