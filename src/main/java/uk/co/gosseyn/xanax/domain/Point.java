package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE)
public  class Point {
    int x;
    int y;
    int z;

    public Point(Point point) {
        x = point.x; y = point.y; z = point.y;
    }
    public boolean isGreaterOrEqual(Point point) {
        return x >= point.x && y >= point.y && z >= point.z;
    }

    public Point addx(int n) {
        this.x += n;
        return this;
    }
    public Point addy(int n) {
        this.y += n;
        return this;
    }
    public Point addz(int n) {
        this.z += n;
        return this;
    }

    public int distanceTo(Point target) {
        int dx = target.getX() - this.getX();
        int dy = target.getY() - this.getY();
        int dz = target.getZ() - this.getZ();
        return dx * dx +dy * dy +dz * dz;
    }
}
