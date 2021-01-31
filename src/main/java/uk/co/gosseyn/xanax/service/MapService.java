package uk.co.gosseyn.xanax.service;

import org.springframework.stereotype.Service;
import uk.co.gosseyn.xanax.domain.Locatable;
import uk.co.gosseyn.xanax.domain.BlockMap;
import uk.co.gosseyn.xanax.domain.Point;
import uk.co.gosseyn.xanax.domain.Vector2d;
import uk.co.gosseyn.xanax.repository.GameRepository;

import javax.inject.Inject;

import static uk.co.gosseyn.xanax.domain.BlockMap.GRASS;
import static uk.co.gosseyn.xanax.domain.BlockMap.ROCK;

@Service
public class MapService {

    private GameRepository gameRepository;
    @Inject
    public MapService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public BlockMap newMap(int width, int height, int depth, double features) {
        BlockMap map = new BlockMap(new Point(width, height, depth));
        OpenSimplexNoise noise =
                new OpenSimplexNoise();
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                double value = noise.eval(x / features, y / features, 0.0);
                value = (value + 1) / 2; // make value fit in 0 to 1
                value = (int)(value * (depth + 1));
                int i;
                for (i = 0; i < (int)value - 1; i++) {
                    map.setBlock(new Point(x, y, i), ROCK); // TODO fill vertically with something
                 }
                map.setBlock(new Point(x, y, i), GRASS);
                if(value > max) {
                    max = value;
                }
                if(value < min) {
                    min = value;
                }
            }
        }
        System.out.println("min: "+min+" max: "+max);
        return map;
    }

    public void placeItem(BlockMap map, Vector2d location, Locatable item) {
        Point l = new Point(location.getX(), location.getY(), 0);
        while(map.getBlock(l) != 0) {
            l.setZ(l.getZ() + 1);
        }
        l.setZ(l.getZ() -1);
        item.setLocation(l);
        map.addItem(l, item);
    }
}
