package uk.co.gosseyn.xanax.domain;

import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import java.util.HashSet;
import java.util.Set;
@Slf4j
public class BlockMap extends Bounds implements TileBasedMap {

    static final BlockAttributes[] BLOCK_ATTRIBUTES = new BlockAttributes[] {
            new BlockAttributes("Empty", false, true),
            new BlockAttributes("Rock", false, false),
            new BlockAttributes("Grass", true,  false),
            new BlockAttributes("Water", true, true),
            new BlockAttributes("Tree", false, false)
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

    public BlockMap(Point size) {
        super(new Point(0,0, 0), new Point(size).addx(-1).addy(-1).addz(-1));
        this.size = size;
        map = new int[size.getX() * size.getY() * size.getZ()];
        itemsMap = new Set[size.getX() * size.getY() * size.getZ()];
        for(int i = 0; i < itemsMap.length; i++){
            itemsMap[i] = new HashSet<>();
        }

    }

    public int getBlockNumber(Point location) {
        return map[blockIndex(location)];
    }

    private int blockIndex(final Point location) {
        return (location.getZ() * size.getX() * size.getY()) + (location.getY() * size.getX()) + location.getX();
    }

    public BlockAttributes getBlock(Point location) {
        return BLOCK_ATTRIBUTES[map[blockIndex(location)]];
    }
    public void setBlock(Point location, int block) {
        map[blockIndex(location)] = block;
    }
    public Set<Locatable> getItem(Point location) {
        return itemsMap[blockIndex(location)];
    }
    public void addItem(Point location, Locatable item) {
        itemsMap[blockIndex(location)].add(item);
    }
    public void removeItem(Point location, Locatable item) {
        itemsMap[blockIndex(location)].remove(item);
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
    public boolean blocked(final Mover mover, Point source, Point point) {
        //int unit = ((UnitMover) mover).getType();
        if (!getBlock(point).isFloor()
                || (source.getZ() != point.getZ()
                    && !getBlock(new Point(point.getX(), point.getY(), source.getZ())).isTraversible()
                    && !getBlock(new Point(source.getX(), source.getY(), point.getZ())).isTraversible())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getCost(final Mover mover, Point source, Point target) {
        //TODO lazyness/fitness to dictate cost
        if(source.getZ() != target.getZ()) {
            return 2;
        } else {
            return 1;
        }
    }
}
