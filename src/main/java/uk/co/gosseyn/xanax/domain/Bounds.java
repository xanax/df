package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Bounds {
    //TODO circular
    Point min;
    Point max;
    public boolean contains(Point point) {
        return point.isGreaterOrEqual(min) && max.isGreaterOrEqual(point);
    }
    public int xLength() {
        return max.getX() - min.getX();
    }
    public int yLength() {
        return max.getY() - min.getY();
    }
    public int zLength() {
        return max.getZ() - min.getZ();
    }
    public Point center() {
        return new Point(min.getX() + xLength() / 2, min.getY() + yLength() / 2, min.getZ() + zLength() / 2 );
    }
}
