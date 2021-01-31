package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public  class Vector3d {
    int x = 0;
    int y = 0;
    int z = 0;

    public boolean isGreaterOrEqual(Vector3d point) {
        return x >= point.x && y >= point.y && z >= point.z;
    }

    public Vector3d addx(int n) {
        this.x += n;
        return this;
    }
    public Vector3d addy(int n) {
        this.y += n;
        return this;
    }
    public Vector3d addz(int n) {
        this.z += n;
        return this;
    }
}
