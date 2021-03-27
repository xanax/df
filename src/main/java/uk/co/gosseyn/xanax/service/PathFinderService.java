package uk.co.gosseyn.xanax.service;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.example.UnitMover;
import org.springframework.stereotype.Service;
import uk.co.gosseyn.xanax.domain.BlockMap;
import uk.co.gosseyn.xanax.domain.Point;

import javax.inject.Named;

@Named
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class PathFinderService {

    public Path findPath(BlockMap map, Point from, Point to, boolean reverse, boolean adjacent) {
        return map.getPathFinder().findPath(new UnitMover(0),
                from, to, reverse, adjacent);
    }

    @Builder
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class PathFinderContext {
        UnitMover mover;
        Point from;
        Point to;
        boolean reverse;
        boolean adjacent;
    }
}
