package uk.co.gosseyn.xanax.domain;

import lombok.Getter;
import lombok.Setter;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.newdawn.slick.util.pathfinding.example.UnitMover;

import java.util.Collection;

@Getter
@Setter
public class Map implements TileBasedMap {

    public static int ROCK = 1;
    public static int GRASS = 2;
    public static int WATER = 3;

    // For simple stuff like rock/tree trunk/grass
    private int[][][] map;

    // For complex stuff and for where multiple items exist at single point. Expect most of the elements to be null
    private Collection<Item>[][][] itemsMap;

    @Override
    public int getWidthInTiles() {
        return map.length;
    }

    @Override
    public int getHeightInTiles() {
        return map[0].length;
    }
    public int getDepthInTiles() {
        return map[0][0].length;
    }

    @Override
    public void pathFinderVisited(final int x, final int y) {

    }

    @Override
    public boolean blocked(final Mover mover, final int x, final int y) {
        int unit = ((UnitMover) mover).getType();
        if(map[x][y][4] == GRASS) { // TODO Z
            return false;
        } else {
        return true;
        }
    }

    @Override
    public float getCost(final Mover mover, final int sx, final int sy, final int tx, final int ty) {
        return 1;
    }
}
