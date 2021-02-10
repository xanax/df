package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class MoveBlockChange extends Change {
    MovingObject moveable;
    Point to;

    @Override
    public void perform(Game game) {
        BlockMap map = game.getMap();
        map.removeItem(new Point(moveable.getLocation().getX(),
                moveable.getLocation().getY(),
                moveable.getLocation().getZ()),moveable);
        map.addItem(to, moveable);
        moveable.setLocation(to);

    }
}
