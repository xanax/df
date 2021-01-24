package uk.co.gosseyn.xanax;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

import static uk.co.gosseyn.xanax.MapBlockType.GRASS;
import static uk.co.gosseyn.xanax.MapBlockType.ROCK;

@Service
public class MapService {
    public Map newMap(int width, int height, int depth, double features) {
        Map map = new Map();
        map.setMap(new int[width][height][depth]);
        map.setItemsMap(new Collection[width][height][depth]);
        uk.co.gosseyn.xanax.OpenSimplexNoise noise =
                new uk.co.gosseyn.xanax.OpenSimplexNoise();
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
                    map.getMap()[x][y][i] = ROCK; // TODO fill vertically with something
                 }
                map.getMap()[x][y][i] = GRASS;
                if(x == 1 && y == 5) {
                    map.getItemsMap()[x][y][i + 1] = Collections.singleton(new Man());
                }
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
    public static void main(String[] args) {
        MapService service = new MapService();
        service.newMap(1000, 1000, 100, 1);
    }
}
