package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE)
public  class Point {
    int x;
    int y;
    int z;
    public boolean isGreaterOrEqual(Point point) {
        return x >= point.x && y >= point.y && z >= point.z;
    }

    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
        this.z = point.z;
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

    public int distanceToPow2(Point target) {
        int dx = target.getX() - this.getX();
        int dy = target.getY() - this.getY();
        int dz = target.getZ() - this.getZ();
        return dx * dx +
                dy * dy +
                dz * dz;
    }

    public float distanceTo(Point target) {
        return (float)Math.sqrt(this.distanceToPow2(target));
    }
}
