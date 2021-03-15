package uk.co.gosseyn.xanax.service;

import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.util.pathfinding.Path;
import org.springframework.stereotype.Service;
import uk.co.gosseyn.xanax.domain.Bounds;
import uk.co.gosseyn.xanax.domain.ForestZone;
import uk.co.gosseyn.xanax.domain.Locatable;
import uk.co.gosseyn.xanax.domain.BlockMap;
import uk.co.gosseyn.xanax.domain.Point;
import uk.co.gosseyn.xanax.domain.Vector2d;
import uk.co.gosseyn.xanax.repository.GameRepository;

import javax.inject.Inject;

import java.util.Set;

import static uk.co.gosseyn.xanax.domain.BlockMap.EMPTY;
import static uk.co.gosseyn.xanax.domain.BlockMap.GRASS;
import static uk.co.gosseyn.xanax.domain.BlockMap.ROCK;

@Slf4j
@Service
public class MapService {

    // TODO onnce a path is calculated save and any other path where both points are on it can reuse
    private GameRepository gameRepository;
    private PathFinderService pathFinderService;

    @Inject
    public MapService(PathFinderService pathFinderService, GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.pathFinderService = pathFinderService;
    }

    public BlockMap newMap(int width, int height, int depth, double features) {
        BlockMap map = new BlockMap(new Point(width, height, depth));
        OpenSimplexNoise noise =
                new OpenSimplexNoise();
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = noise.eval(x / features, y / features, 0.0);
                value = (value + 1) / 2; // make value fit in 0 to 1
                value = (int) (value * (depth + 1));
                int i;
                for (i = 0; i < (int) value - 1; i++) {
                    map.setBlock(new Point(x, y, i), ROCK); // TODO fill vertically with something
                }
                map.setBlock(new Point(x, y, i), GRASS);
                if (value > max) {
                    max = value;
                }
                if (value < min) {
                    min = value;
                }
            }
        }
        System.out.println("min: " + min + " max: " + max);
        return map;
    }

    public void placeItem(BlockMap map, Vector2d location, Locatable item) {
        Point l = new Point(location.getX(), location.getY(), 0);
        while (map.getBlockNumber(l) != EMPTY) {
            l.addz(1);
        }
        item.setLocation(l.addz(-1));
        map.addItem(l, item);
    }

    public void placeBlock(BlockMap map, Vector2d location, int block) {
        Point l = new Point(location.getX(), location.getY(), 0);
        while (map.getBlockNumber(l) != EMPTY) {
            l.addz(1);
        }
        map.setBlock(l.addz(-1), block);
    }


    public Path pathToNearestBlock(BlockMap map, final Point location, int block, final ForestZone zone, Set<Point> except) {
        Long time = System.nanoTime();
        Path path = null;
        for(Point point : zone.treeRankedByDistance(location)) {
            if (map.getBlockNumber(point) == block && !except.contains(point)) {
                path = pathFinderService.findPath(map, location, point, true, true);
                if (path != null) {
                    break;
                }
            }
        }
        log.trace("Time to find route to nearest block: {}", ((System.nanoTime()-time)/1000000));
        return path;
    }
}
