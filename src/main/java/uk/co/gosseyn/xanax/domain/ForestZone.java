package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.newdawn.slick.util.pathfinding.ClosestHeuristic;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static uk.co.gosseyn.xanax.domain.BlockMap.ROCK;
import static uk.co.gosseyn.xanax.domain.BlockMap.TREE;

@Getter
@Setter
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class ForestZone extends Zone {
    BlockMap map;
    Bounds bounds;
    Set<Point> treeLocations = new HashSet<>();

    public ForestZone(BlockMap map, Bounds bounds) {
        this.map = map;
        this.bounds = bounds;
        this.scan();

    }

    public List<Point> treeRankedByDistance(Point point) {

        return treeLocations.stream().sorted((o1, o2) -> {
            //TODO calc distance multiple times maybe?
            float distance1 = o1.distanceTo(point);
            float distance2 = o2.distanceTo(point);
            if(distance1 > distance2) {
                return 1;
            } else if (distance1 < distance2) {
                return -1;
            }
            return 0;
        }).collect(Collectors.toList());
    }

    private void scan() {
        for(int z = bounds.getMin().getZ(); z <= bounds.getMax().getZ(); z++) {
            for(int y = bounds.getMin().getY(); y <= bounds.getMax().getY(); y++) {
                for (int x = bounds.getMin().getX(); x <= bounds.getMax().getX(); x++) {
                    Point point = new Point(x, y, z);
                     if(map.getBlockNumber(point)==ROCK) {
                        treeLocations.add(point);
                    }
                }
            }
        }
    }
}
