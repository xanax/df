package uk.co.gosseyn.xanax.service;

import org.springframework.stereotype.Service;
import uk.co.gosseyn.xanax.domain.Man;
import uk.co.gosseyn.xanax.domain.Map;
import uk.co.gosseyn.xanax.domain.Vector3d;
import uk.co.gosseyn.xanax.repository.GameRepository;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static uk.co.gosseyn.xanax.domain.Map.GRASS;
import static uk.co.gosseyn.xanax.domain.Map.ROCK;

@Service
public class MapService {

    private GameRepository gameRepository;
    @Inject
    public MapService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
    public Map newMap(int width, int height, int depth, double features) {
        Map map = new Map();
        map.setMap(new int[width][height][depth]);
        map.setItemsMap(new Collection[width][height][depth]);
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
                    map.getMap()[x][y][i] = ROCK; // TODO fill vertically with something
                    map.getItemsMap()[x][y][i] = new HashSet<>();
                 }
                map.getItemsMap()[x][y][i] = new HashSet<>();
                map.getMap()[x][y][i] = GRASS;
                if(x == 0 && y == 44) {
                    Man man = new Man();
                    man.setLocation(new Vector3d(x, y, i));
                    map.getItemsMap()[x][y][i].add(man);
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
}
