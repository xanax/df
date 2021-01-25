package uk.co.gosseyn.xanax.domain;

import lombok.Data;
import lombok.NonNull;

@Data
public  class Vector2d {
    @NonNull
    private int x;
    @NonNull
    private int y;
}
