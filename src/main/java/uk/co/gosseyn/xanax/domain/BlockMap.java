package uk.co.gosseyn.xanax.domain;

import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.newdawn.slick.util.pathfinding.example.UnitMover;

import java.util.HashSet;
import java.util.Set;
@Slf4j
public class BlockMap extends Bounds implements TileBasedMap {

    public static int ROCK = 1;
    public static int GRASS = 2;
    public static int WATER = 3;
    public static int TREE = 4;

    private Point size;

    // For simple stuff like rock/tree trunk/grass
    private int[] map;

    // For complex stuff and for where multiple items exist at single point. Expect most of the elements to be null
    private Set<Locatable>[] itemsMap;

    public BlockMap(Point size) {
        super(new Point(0,0, 0), size.clone().addx(-1).addy(-1).addz(-1));
        this.size = size;
        map = new int[size.getX() * size.getY() * size.getZ()];
        itemsMap = new Set[size.getX() * size.getY() * size.getZ()];
        for(int i = 0; i < itemsMap.length; i++){
            itemsMap[i] = new HashSet<>();
        }

    }

    public int getBlock(Point location) {
        return map[(location.getZ() * size.getX() * size.getY()) + (location.getY() * size.getX()) + location.getX()];
    }
    public void setBlock(Point location, int block) {
        map[(location.getZ() * size.getX() * size.getY()) + (location.getY() * size.getX()) + location.getX()] = block;
    }
    public Set<Locatable> getItem(Point location) {
        return itemsMap[(location.getZ() * size.getX() * size.getY()) + (location.getY() * size.getX()) + location.getX()];
    }
    public void addItem(Point location, Locatable item) {
        itemsMap[(location.getZ() * size.getX() * size.getY()) + (location.getY() * size.getX()) + location.getX()].add(item);
    }
    public void removeItem(Point location, Locatable item) {
        itemsMap[(location.getZ() * size.getX() * size.getY()) + (location.getY() * size.getX()) + location.getX()].remove(item);
    }

    @Override
    public int getWidthInTiles() {
        return size.getX();
    }

    @Override
    public int getHeightInTiles() {
        return size.getY();
    }
    public int getDepthInTiles() {
        return size.getZ();
    }

    @Override
    public void pathFinderVisited(final int x, final int y) {
        log.trace("Path finder visited x %d y %d", x ,y);
    }

    @Override
    public boolean blocked(final Mover mover, final int x, final int y) {
        // TODO Z
        //int unit = ((UnitMover) mover).getType();
        if(getBlock(new Point(x, y, 4)) == GRASS && getBlock(new Point(x, y, 5)) == 0
                && getItem(new Point(x, y, 5)).isEmpty()) {
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
