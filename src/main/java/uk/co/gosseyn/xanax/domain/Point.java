package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
}
