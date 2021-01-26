package uk.co.gosseyn.xanax.domain;

import lombok.Data;

@Data
public abstract class Item extends GameObject implements Locatable {
    @Override
    public abstract int getCode();
    private Vector3d location;
    private Game game;
}
