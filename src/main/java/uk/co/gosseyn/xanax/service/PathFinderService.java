package uk.co.gosseyn.xanax.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.example.UnitMover;
import org.springframework.stereotype.Service;
import uk.co.gosseyn.xanax.domain.BlockMap;
import uk.co.gosseyn.xanax.domain.Point;

@Service
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class PathFinderService {

    public Path findPath(BlockMap map, Point from, Point to) {
        AStarPathFinder finder = new AStarPathFinder(map, 500, true);
        return finder.findPath(new UnitMover(0),
                from, to);
    }
}
