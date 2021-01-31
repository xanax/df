package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
public abstract class Item extends GameObject implements Moveable {
    @Override
    public abstract int getCode();
    protected Point location;
    protected Game game;
}
