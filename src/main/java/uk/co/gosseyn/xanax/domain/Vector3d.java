package uk.co.gosseyn.xanax.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Data
@AllArgsConstructor
public  class Vector3d {
    private int x = 0;
    private int y = 0;
    private int z = 0;

    public Vector3d() {
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
