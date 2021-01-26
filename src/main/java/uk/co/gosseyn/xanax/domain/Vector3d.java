package uk.co.gosseyn.xanax.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Data
public  class Vector3d {
    @NonNull
    private int x;
    @NonNull
    private int y;
    @NonNull
    private int z;

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
