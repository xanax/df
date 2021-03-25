package uk.co.gosseyn.xanax.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.newdawn.slick.util.pathfinding.example.UnitMover;

import java.util.HashSet;
import java.util.Set;
@Slf4j
public class BlockMap extends Bounds implements TileBasedMap {

    final static BlockAttributes[] BLOCK_ATTRIBUTES = new BlockAttributes[] {
            new BlockAttributes("Empty", false),
            new BlockAttributes("Rock", false),
            new BlockAttributes("Grass", true),
            new BlockAttributes("Water", false),
            new BlockAttributes("Tree", false)
    };

    public static final int EMPTY = 0;
    public static final int ROCK = 1;
    public static final int GRASS = 2;
    public static final int WATER = 3;
    public static final int TREE = 4;

    private Point size;

    // For simple stuff like rock/tree trunk/grass
    private int[] map;

    // For complex stuff and for where multiple items exist at single point. Expect most of the elements to be null
    private Set<Locatable>[] itemsMap;

    private PathFinder pathFinder;

    public BlockMap(Point size) {
        super(new Point(0,0, 0), size.clone().addx(-1).addy(-1).addz(-1));
        this.size = size;
        map = new int[size.getX() * size.getY() * size.getZ()];
        itemsMap = new Set[size.getX() * size.getY() * size.getZ()];
        for(int i = 0; i < itemsMap.length; i++){
            itemsMap[i] = new HashSet<>();
        }
        pathFinder = new AStarPathFinder(this, 500, true);
    }

    public int getBlockNumber(Point location) {
        return map[(location.getZ() * size.getX() * size.getY()) + (location.getY() * size.getX()) + location.getX()];
    }
    public BlockAttributes getBlock(Point location) {
        return BLOCK_ATTRIBUTES[map[(location.getZ() * size.getX() * size.getY()) + (location.getY() * size.getX()) + location.getX()]];
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
    @Override
    public int getDepthInTiles() {
        return size.getZ();
    }

    @Override
    public void pathFinderVisited(Point point) {
//        log.trace(point.toString());
    }

    @Override
    public boolean blocked(final Mover mover, Point point) {
        //int unit = ((UnitMover) mover).getType();
        //Point below = point.clone().addz(-1);
        //TODO if before target enforce being on same Z
        if(!getBlock(point).isFloor()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public float getCost(final Mover mover, Point source, Point target) {
        if(source.getZ() != target.getZ()) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public PathFinder getPathFinder() {
        return pathFinder;
    }
}
