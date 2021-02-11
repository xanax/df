package uk.co.gosseyn.xanax.service;

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
    //  Moves in na circular ie        10
    //                              781
    //                              6x2
    //                              543
    //

    public Path pathToNearestBlock(BlockMap map, final Point location, int block, final ForestZone zone, Set<Point> except) {

        for(Point point : zone.treeRankedByDistance(location)) {
            if (map.getBlockNumber(point) == block && !except.contains(point)) {
                Path path =pathFinderService.findPath(map, location, point);
                if (path != null) {
                    return path;
                }
            }
        }
        return null;
    }

    private Path pathToNearestBlockOld(BlockMap map, final Point location, int block, final Bounds bounds, Set<Point> except) {

        // TODO make 3d (search z-1 then z+1 then z-2 then z+2
        Point current = location.clone();
        int length = 0;
        boolean withinBounds;
        Point[] moves = {new Point(0,1,0), new Point(-1,0,0),
                new Point(0,-1,0), new Point(1,0,0)};
        do {
            withinBounds = false;
            // Move up and left by 1
            current.addx(1).addy(-1);
            // Add two to the total length of side
            length += 2;
            // Loop for each side
            for (Point move : moves) {
                // Loop for length of side
                for (int i = 0; i < length; i++) {
                    // If within bounds and is block and the path isn't blocked, return path
                    if (bounds.contains(current)) {
                        withinBounds = true;
                        if (map.getBlockNumber(current) == block && !except.contains(current)) {
                            Path path = pathFinderService.findPath(map, location, current);
                            if (path != null) {
                                return path;
                            }
                        }
                    }
                    // Increment current
                    current.addx(move.getX());
                    current.addy(move.getY());
                    //TODO current.addz(move.getZ());

                }
            }
        } while (withinBounds);
        return null;
    }
}
