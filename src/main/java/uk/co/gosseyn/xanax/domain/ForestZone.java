package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

import static uk.co.gosseyn.xanax.domain.BlockMap.ROCK;

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

    public Point[] treeRankedByDistance(Point point) {

        return treeLocations.stream().sorted((o1, o2) -> {
            //TODO calc distance multiple times maybe?
            float distance1 = o1.distanceToPow2(point);
            float distance2 = o2.distanceToPow2(point);
            return Float.compare(distance1, distance2);
        }).toArray(Point[]::new);
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
