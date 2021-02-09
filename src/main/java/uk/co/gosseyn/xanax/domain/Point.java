package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public  class Point {
    int x;
    int y;
    int z;

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
    @Override
    public Point clone() {
        return new Point(this.x, this.y, this.z);
    }

    public float distanceTo(Point target) {
        float dx = target.getX() - this.getX();
        float dy = target.getY() - this.getY();
        float dz = target.getZ() - this.getZ();
        float result = (float) (
                Math.sqrt(Math.pow(dx, 2) +
                        Math.pow(dy, 2) +
                        Math.pow(dz, 2)));

        return result;
    }
}
