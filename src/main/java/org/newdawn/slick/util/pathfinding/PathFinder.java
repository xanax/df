package org.newdawn.slick.util.pathfinding;

import uk.co.gosseyn.xanax.domain.Point;

public interface PathFinder {

	public Path findPath(Mover mover, Point source, Point target, boolean reverse);
}