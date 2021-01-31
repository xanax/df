package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Bounds {
    Vector3d min;
    Vector3d max;
    public boolean contains(Vector3d point) {
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
    public Vector3d center() {
        return new Vector3d(min.getX() + xLength() / 2, min.getY() + yLength() / 2, min.getZ() + zLength() / 2 );
    }
}
