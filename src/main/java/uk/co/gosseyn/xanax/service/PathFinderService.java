package uk.co.gosseyn.xanax.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.example.UnitMover;
import org.springframework.stereotype.Service;
import uk.co.gosseyn.xanax.domain.BlockMap;
import uk.co.gosseyn.xanax.domain.Vector3d;

import java.util.Map;

@Service
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class PathFinderService {

    public Path findPath(BlockMap map, Vector3d from, Vector3d to) {
        AStarPathFinder finder = new AStarPathFinder(map, 500, true);
        return finder.findPath(new UnitMover(0),
                from.getX(), from.getY(), to.getX(), to.getY());
    }
}
