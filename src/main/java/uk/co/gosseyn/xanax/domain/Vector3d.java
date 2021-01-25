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
}
